package com.acnecare.api.chatroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acnecare.api.chatroom.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    /**
     * Tìm ChatRoom giữa 2 user (1-1 chat).
     * Trả về Optional<ChatRoom> vì có thể chưa tồn tại room giữa 2 người này.
     */
    @Query("""
                SELECT cr FROM ChatRoom cr
                WHERE (cr.user.id = :userId1 AND cr.otherUser.id = :userId2)
                   OR (cr.user.id = :userId2 AND cr.otherUser.id = :userId1)
            """)
    Optional<ChatRoom> findByTwoUsers(@Param("userId1") String userId1,
            @Param("userId2") String userId2);

    /**
     * Tìm tất cả ChatRoom mà user đang tham gia (làm user hoặc otherUser).
     * Trả về List<ChatRoom>, có thể rỗng nếu user chưa có phòng chat nào.
     */
    @Query("""
                SELECT cr FROM ChatRoom cr
                WHERE cr.user.id = :userId OR cr.otherUser.id = :userId
                ORDER BY cr.lastMessageAt DESC NULLS LAST
            """)
    List<ChatRoom> findAllByUserId(@Param("userId") String userId);

    /**
     * Kiểm tra xem user có nằm trong phòng chat này hay không.
     * Trả về true nếu user là user hoặc otherUser của room.
     */
    @Query("""
                SELECT CASE WHEN COUNT(cr) > 0 THEN true ELSE false END
                FROM ChatRoom cr
                WHERE cr.roomId = :roomId
                  AND (cr.user.id = :userId OR cr.otherUser.id = :userId)
            """)
    boolean isUserParticipant(@Param("roomId") String roomId, @Param("userId") String userId);
}
