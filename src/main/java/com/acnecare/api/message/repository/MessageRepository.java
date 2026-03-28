package com.acnecare.api.message.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acnecare.api.message.entity.Message;

public interface MessageRepository extends JpaRepository<Message, String> {

    /**
     * Lấy danh sách tin nhắn của một phòng chat, sắp xếp theo thời gian tạo giảm
     * dần
     * (phân trang)
     *
     * @param roomId   ID của phòng chat
     * @param pageable thông tin phân trang (page, size, sort)
     * @return Page<Message> trang tin nhắn theo yêu cầu
     */
    Page<Message> findByRoomIdOrderByCreateAtDesc(String roomId, Pageable pageable);

    /**
     * Lấy tin nhắn mới nhất (gần đây nhất) trong một phòng chat
     *
     * @param roomId ID của phòng chat
     * @return Optional<Message> tin nhắn mới nhất hoặc rỗng nếu phòng chưa có tin nhắn
     */
    Optional<Message> findFirstByRoomIdOrderByCreateAtDesc(String roomId);

    /**
     * Lấy tin nhắn mới nhất của từng phòng chat mà user đang tham gia
     * <p>
     * Trả về danh sách các tin nhắn cuối cùng (latest message) của tất cả các phòng
     * mà user là người tạo (user) hoặc là người kia (otherUser), sắp xếp theo thời gian giảm dần.
     * Dùng để hiển thị danh sách chat rooms kèm preview tin nhắn cuối.
     * </p>
     *
     * @param userId ID của người dùng
     * @return List<Message> danh sách tin nhắn mới nhất của từng phòng
     */
    @Query("""
                SELECT m FROM Message m
                WHERE m.roomId IN (
                    SELECT cr.roomId
                    FROM ChatRoom cr
                    WHERE cr.user.id = :userId
                       OR cr.otherUser.id = :userId
                )
                AND m.createAt = (
                    SELECT MAX(m2.createAt)
                    FROM Message m2
                    WHERE m2.roomId = m.roomId
                )
                ORDER BY m.createAt DESC
            """)
    List<Message> findLatestMessagePerRoomForUser(@Param("userId") String userId);
}