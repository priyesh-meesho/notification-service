package com.meesho.notificationservice.repositories.JPArepositories;

import com.meesho.notificationservice.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
}