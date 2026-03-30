package com.acnecare.api.chatroom.mapper;

import com.acnecare.api.chatroom.dto.response.ChatRoomResponse;
import com.acnecare.api.chatroom.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // 🚨 NHỚ IMPORT CÁI NÀY
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {

    // Chỉ đường cho MapStruct: Lấy id của user map vào userId, lấy id của otherUser
    // map vào otherUserId
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "otherUser.id", target = "otherUserId")
    ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom);

    List<ChatRoomResponse> toChatRoomResponses(List<ChatRoom> entities);
}