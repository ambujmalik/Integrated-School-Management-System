package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.Notification;
import com.ismp.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

	@Autowired
    private NotificationRepository notificationRepository;

    @PostMapping
    public Notification sendNotification(@RequestBody Notification notification) {
        return notificationRepository.save(notification);
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getMyNotifications(@PathVariable Integer userId) {
        return notificationRepository.findByRecipientIdOrderBySentAtDesc(userId);
    }
}
