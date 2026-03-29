package com.acnecare.api.chatroom.mapper;

import com.acnecare.api.chatroom.dto.request.ChatRoomCreationRequest;
import com.acnecare.api.chatroom.dto.response.ChatRoomResponse;
import com.acnecare.api.chatroom.entity.ChatRoom;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    
    // ChatRoom toChatRoom(ChatRoomCreationRequest request);

    ChatRoomResponse toChatRoomResponse(ChatRoom chatrooom);

    List<ChatRoomResponse> toChatRoomResponses(List<ChatRoom> entities);
}