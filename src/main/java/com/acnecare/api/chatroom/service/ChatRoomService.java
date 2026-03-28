package com.acnecare.api.chatroom.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.acnecare.api.user.entity.User;
import com.acnecare.api.chatroom.dto.request.ChatRoomCheckRequest;
import com.acnecare.api.chatroom.dto.request.ChatRoomCreationRequest;
import com.acnecare.api.chatroom.dto.request.ChatRoomGetRequest;
import com.acnecare.api.chatroom.dto.response.ChatRoomResponse;
import com.acnecare.api.chatroom.entity.ChatRoom;
import com.acnecare.api.chatroom.mapper.ChatRoomMapper;
import com.acnecare.api.chatroom.repository.ChatRoomRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.user.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomService {
    UserRepository userRepository;
    ChatRoomRepository chatRoomRepository;
    ChatRoomMapper chatRoomMapper;

    /**
     * Tạo phòng chat 1-1 giữa hai người dùng hoặc trả về phòng đã tồn tại
     * <p>
     * Sử dụng cặp userId được sắp xếp (idA < idB) để đảm bảo không tạo trùng.
     * </p>
     *
     * @param request chứa senderId và receiverId
     * @return ChatRoomResponse thông tin phòng chat
     * @throws AppException nếu user không tồn tại
     */
    public ChatRoomResponse createChatRoom(ChatRoomCreationRequest request) {

        String userId1 = request.getReceiverId();
        String userId2 = request.getSenderId();

        String idA = userId1.compareTo(userId2) < 0 ? userId1 : userId2;
        String idB = userId1.compareTo(userId2) < 0 ? userId2 : userId1;

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByTwoUsers(idA, idB);

        // Nếu đã có room giữa 2 user thì trả về chatroom
        if (existingRoom.isPresent()) {
            return chatRoomMapper.toChatRoomResponse(existingRoom.get());
        }

        // Tạo mới nếu chưa có
        User userA = userRepository.findById(idA)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User userB = userRepository.findById(idB)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ChatRoom newRoom = ChatRoom.builder()
                .user(userA)
                .otherUser(userB)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastMessageAt(null) // chưa có tin nhắn
                .build();

        ChatRoom savedRoom = chatRoomRepository.save(newRoom);
        return chatRoomMapper.toChatRoomResponse(savedRoom);
    }

    /**
     * Lấy danh sách tất cả phòng chat mà user đang tham gia
     * (user là người khởi tạo hoặc là người kia trong phòng)
     *
     * @param request chứa senderId (userId của người đang đăng nhập)
     * @return List<ChatRoomResponse> - danh sách phòng chat
     */
    public List<ChatRoomResponse> getChatRoomsByUserId(ChatRoomGetRequest request) {
        String userId = request.getSenderId();

        // Kiểm tra user tồn tại
        userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId);

        // Map entity → DTO
        return chatRoomMapper.toChatRoomResponses(chatRooms);
    }

    /**
     * Lấy thông tin phòng chat theo ID
     *
     * @param roomId ID của phòng chat
     * @return ChatRoom thông tin phòng chat
     * @throws AppException nếu phòng chat không tồn tại (CHAT_ROOM_NOT_FOUND)
     */
    public ChatRoom getChatRoomsByUserId(String roomId) {
        ChatRoom existingRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        return existingRoom;
    }

    /**
     * Kiểm tra xem user có nằm trong phòng chat này hay không
     * (là user hoặc otherUser của phòng).
     *
     * @param request chứa userid và roomid cần kiểm tra
     * @return true nếu user có quyền truy cập phòng chat, false nếu không
     */
    public boolean checkUserInRoom(ChatRoomCheckRequest request) {
        return chatRoomRepository.isUserParticipant(
                request.getRoomid(),
                request.getUserid());
    }

    public void saveChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }
}
