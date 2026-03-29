package com.acnecare.api.message.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.acnecare.api.message.entity.Message;
import com.acnecare.api.message.entity.MessageImage;


public interface MessageImageRepository extends JpaRepository<MessageImage, String> {
    List<MessageImage> findByMessage(Message message); 
}