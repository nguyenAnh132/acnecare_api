package com.acnecare.api.message.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.acnecare.api.message.entity.MessageImage;
import com.acnecare.api.chatroom.entity.ChatRoom;
import com.acnecare.api.chatroom.repository.ChatRoomRepository;
import com.acnecare.api.chatroom.service.ChatRoomService;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.message.dto.request.MessageLatestRequest;
import com.acnecare.api.message.dto.request.MessageRequest;
import com.acnecare.api.message.dto.request.MessgeGetRequest;
import com.acnecare.api.message.dto.response.MessageResponse;
import com.acnecare.api.message.dto.response.PagedResponse;
import com.acnecare.api.message.entity.Message;
import com.acnecare.api.message.enums.MessageType;
import com.acnecare.api.message.mapper.MessageMapper;
import com.acnecare.api.message.repository.MessageRepository;
import com.acnecare.api.message.repository.MessageImageRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    MessageRepository messageRepository;
    ChatRoomService chatRoomService; // nếu bạn muốn gọi service khác
    MessageMapper messageMapper;
    @Autowired
    MessageImageRepository messageImageRepository;

    /**
     * Lấy danh sách tin nhắn của một phòng chat theo phân trang
     * - Tin nhắn mới nhất sẽ nằm ở đầu (page 0)
     * - Sắp xếp theo createAt giảm dần (mới → cũ)
     */
    public PagedResponse<MessageResponse> getMessagesByRoomId(MessgeGetRequest req) {

        Sort sort = Sort.by(
                Sort.Order.desc("createAt"),
                Sort.Order.asc("senderId"));

        int requestedPage = req.getPage();
        PageRequest pageable = PageRequest.of(requestedPage, req.getSize(), sort);

        Page<Message> messagePage = messageRepository.findByRoomIdOrderByCreateAtDesc(
                req.getRoomId(), pageable);

        if (req.getPage() > messagePage.getTotalPages() - 1 && messagePage.getTotalPages() > 0) {
            PageRequest lastPage = PageRequest.of(messagePage.getTotalPages() - 1, req.getSize(), sort);
            messagePage = messageRepository.findByRoomIdOrderByCreateAtDesc(req.getRoomId(), lastPage);
        }

        return messageMapper.toPagedResponse(messagePage);
    }

    /**
     * Lưu một tin nhắn mới vào phòng chat và cập nhật thông tin phòng
     * <p>
     * - Validate request cơ bản<br>
     * - Kiểm tra phòng chat tồn tại<br>
     * - Chuyển đổi type tin nhắn (mặc định TEXT nếu không hợp lệ)<br>
     * - Lưu tin nhắn và cập nhật lastMessageAt + updatedAt của phòng chat
     * </p>
     *
     * @param request thông tin tin nhắn cần gửi (roomId, senderId, content, type,
     *                ...)
     * @return MessageResponse thông tin tin nhắn đã lưu
     * @throws AppException
     *                      - INVALID_REQUEST nếu thiếu roomId hoặc senderId<br>
     *                      - CHAT_ROOM_NOT_FOUND nếu phòng chat không tồn tại
     */
    @Transactional
    public MessageResponse saveMessage(MessageRequest request) {
        // Validate cơ bản
        if (request.getRoomId() == null || request.getSenderId() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        ChatRoom chatRoom = chatRoomService.getChatRoomsByUserId(request.getRoomId());

        Message message = messageMapper.toMessageEntity(request);

        // Xử lý type
        try {
            message.setType(MessageType.valueOf(request.getType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            message.setType(MessageType.TEXT); // default
        }

        message.setMessageContent(request.getContent());
        message.setCreateAt(LocalDateTime.now());
        message.setIsReadAt(null); // chưa đọc

        if (message.getType() != MessageType.IMAGES) {
            message.setImages(new ArrayList<>());
        }

        Message savedMessage = messageRepository.save(message);

        // Cập nhật lastMessageAt của phòng chat
        chatRoom.setLastMessageAt(savedMessage.getCreateAt());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatRoomService.saveChatRoom(chatRoom);

        return messageMapper.toMessageResponse(savedMessage);
    }

    /**
     * Lấy tin nhắn mới nhất của từng phòng chat mà user đang tham gia
     * <p>
     * Trả về danh sách các tin nhắn cuối cùng (latest message) từ tất cả các phòng
     * chat
     * mà user là thành viên (làm người tạo hoặc người kia), sắp xếp theo thời gian
     * giảm dần.
     * Thường dùng để hiển thị preview tin nhắn cuối trong danh sách chat.
     * </p>
     *
     * @param mRequest chứa userId của người dùng hiện tại
     * @return List<MessageResponse> danh sách tin nhắn mới nhất của từng phòng
     */
    public List<MessageResponse> getLatestMessage(MessageLatestRequest mRequest) {
        String userId = mRequest.getUserId();

        List<Message> listMessage = messageRepository.findLatestMessagePerRoomForUser(userId);

        return messageMapper.toMessageResponseList(listMessage);
    }

    @Transactional
    public MessageResponse saveImageMessage(String roomId, String senderId,
            org.springframework.web.multipart.MultipartFile file) {
        try {
            // 1. LƯU FILE VÀO THƯ MỤC TRÊN MÁY TÍNH (uploads/messages)
            String UPLOAD_DIR = "uploads/messages/";
            java.io.File dir = new java.io.File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = java.util.UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            java.nio.file.Path filePath = java.nio.file.Paths.get(UPLOAD_DIR + fileName);
            java.nio.file.Files.copy(file.getInputStream(), filePath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            // 🚨 SỬA Ở ĐÂY: Chỉ lưu đường dẫn tương đối vào Database
            String dbImageUrl = "/files/messages/" + fileName;

            // 2. LƯU VÀO BẢNG messages
            ChatRoom chatRoom = chatRoomService.getChatRoomsByUserId(roomId);

            Message message = new Message();
            message.setRoomId(roomId);
            message.setSenderId(senderId);
            message.setType(MessageType.IMAGES);
            message.setMessageContent(dbImageUrl); // Lưu "/files/messages/xxx.jpg"
            message.setCreateAt(LocalDateTime.now());

            Message savedMessage = messageRepository.save(message);

            // 3. LƯU VÀO BẢNG message_images
            MessageImage messageImage = new MessageImage();
            messageImage.setMessage(savedMessage);
            messageImage.setImageUrl(dbImageUrl);
            messageImage.setFileName(file.getOriginalFilename());
            messageImage.setFileSize(file.getSize());
            messageImage.setMimeType(file.getContentType());
            messageImage.setUploadAt(LocalDateTime.now());
            messageImageRepository.save(messageImage);

            // 4. CẬP NHẬT THỜI GIAN PHÒNG CHAT
            chatRoom.setLastMessageAt(savedMessage.getCreateAt());
            chatRoom.setUpdatedAt(LocalDateTime.now());
            chatRoomService.saveChatRoom(chatRoom);

            return messageMapper.toMessageResponse(savedMessage);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
    }
}
