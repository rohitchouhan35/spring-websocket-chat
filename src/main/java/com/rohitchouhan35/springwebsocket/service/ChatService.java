package com.rohitchouhan35.springwebsocket.service;

import com.rohitchouhan35.springwebsocket.model.GroupNames;
import com.rohitchouhan35.springwebsocket.model.Message;
import com.rohitchouhan35.springwebsocket.model.UserGroup;
import com.rohitchouhan35.springwebsocket.repository.GroupRepository;
import com.rohitchouhan35.springwebsocket.repository.MessageRepository;
import com.rohitchouhan35.springwebsocket.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class ChatService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    public Message forwardMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        return message;
    }

    public void joinGroup(String groupName, Principal principal) {
        GroupNames groupNames = groupRepository.findByName(groupName);
        if (groupNames != null && !isUserMemberOfGroup(principal.getName(), groupName)) {
            UserGroup userGroup = new UserGroup();
            userGroup.setUsername(principal.getName());
            userGroup.setGroupName(groupName);
            userGroupRepository.save(userGroup);
            simpMessagingTemplate.convertAndSend("/topic/" + groupName, "User " + principal.getName() + " joined the group.");
        }
    }

    public void leaveGroup(String groupName, Principal principal) {
        GroupNames groupNames = groupRepository.findByName(groupName);
        if (groupNames != null && isUserMemberOfGroup(principal.getName(), groupName)) {
            userGroupRepository.deleteByUsernameAndGroupName(principal.getName(), groupName);
            simpMessagingTemplate.convertAndSend("/topic/" + groupName, "User " + principal.getName() + " left the group.");
        }
    }

    private boolean isUserMemberOfGroup(String username, String groupName) {
        return userGroupRepository.existsByUsernameAndGroupName(username, groupName);
    }
}
