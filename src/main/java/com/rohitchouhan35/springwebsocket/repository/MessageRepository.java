package com.rohitchouhan35.springwebsocket.repository;

import com.rohitchouhan35.springwebsocket.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
