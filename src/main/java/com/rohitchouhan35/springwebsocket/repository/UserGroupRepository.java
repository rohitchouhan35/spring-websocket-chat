package com.rohitchouhan35.springwebsocket.repository;

import com.rohitchouhan35.springwebsocket.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    boolean existsByUsernameAndGroupName(String username, String groupName);
    void deleteByUsernameAndGroupName(String username, String groupName);

}
