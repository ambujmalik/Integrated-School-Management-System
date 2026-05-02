package com.ismp.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @Column(name = "sender_id")
    private Integer senderId; // The User ID of the person sending it

    @Column(name = "recipient_id")
    private Integer recipientId; // Target User ID (null if sent to a group)

    @Column(name = "target_type", length = 30)
    private String targetType; // e.g., 'ALL', 'CLASS', 'SCHOOL', 'INDIVIDUAL'

    @Column(name = "target_id")
    private Integer targetId; // e.g., the ClassID if targetType is 'CLASS'

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "title_odia", length = 400)
    private String titleOdia;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "message_odia", columnDefinition = "TEXT")
    private String messageOdia;

    @Column(name = "notification_type", length = 30)
    private String notificationType = "General"; // e.g., 'Fee Reminder', 'Exam', 'Holiday'

    @CreationTimestamp
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "is_read")
    private Boolean isRead = false;

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public Integer getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Integer recipientId) {
		this.recipientId = recipientId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleOdia() {
		return titleOdia;
	}

	public void setTitleOdia(String titleOdia) {
		this.titleOdia = titleOdia;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageOdia() {
		return messageOdia;
	}

	public void setMessageOdia(String messageOdia) {
		this.messageOdia = messageOdia;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
}
