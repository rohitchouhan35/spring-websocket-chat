package com.rohitchouhan35.springwebsocket.controller;

import com.rohitchouhan35.springwebsocket.model.GroupNames;
import com.rohitchouhan35.springwebsocket.model.Message;
import com.rohitchouhan35.springwebsocket.model.UserGroup;
import com.rohitchouhan35.springwebsocket.repository.GroupRepository;
import com.rohitchouhan35.springwebsocket.repository.MessageRepository;
import com.rohitchouhan35.springwebsocket.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;
import java.time.LocalDateTime;

@CrossOrigin("*")
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserGroup userGroup;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        return message;
    }

    @MessageMapping("/join-group")
    public void joinGroup(@Payload String groupName, Principal principal) {
        GroupNames groupNmes = groupRepository.findByName(groupName);
        if (groupNmes != null) {
            if (!isUserMemberOfGroup(principal.getName(), groupName)) {
                userGroup.setUsername(principal.getName());
                userGroup.setGroupName(groupName);
                userGroupRepository.save(userGroup);

                // Notify the user and group members about the user joining the group
                simpMessagingTemplate.convertAndSend("/topic/" + groupName, "User " + principal.getName() + " joined the group.");
            }
        }
    }

    @MessageMapping("/leave-group")
    public void leaveGroup(@Payload String groupName, Principal principal) {
        GroupNames groupNames = groupRepository.findByName(groupName);
        if (groupNames != null) {
            if (isUserMemberOfGroup(principal.getName(), groupName)) {
                userGroupRepository.deleteByUsernameAndGroupName(principal.getName(), groupName);

                // Notify the user and group members about the user leaving the group
                simpMessagingTemplate.convertAndSend("/topic/" + groupName, "User " + principal.getName() + " left the group.");
            }
        }
    }

    private boolean isUserMemberOfGroup(String username, String groupName) {
        return userGroupRepository.existsByUsernameAndGroupName(username, groupName);
    }

}
