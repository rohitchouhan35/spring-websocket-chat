package com.rohitchouhan35.springwebsocket.repository;

import com.rohitchouhan35.springwebsocket.model.GroupNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupNames, Long> {

    GroupNames findByName(String name);

}
