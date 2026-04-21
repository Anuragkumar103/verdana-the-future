package com.verdana.contact.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a contact form submission stored in the database.
 *
 * Table: contact_messages
 * @author Anurag Kumar
 */
@Entity
@Table(name = "contact_messages")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "sender_name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "sender_email", nullable = false, length = 150)
    private String email;

    @Size(max = 200, message = "Subject cannot exceed 200 characters")
    @Column(name = "subject", length = 200)
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 3000, message = "Message must be between 10 and 3000 characters")
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false, nullable = false)
    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private MessageStatus status = MessageStatus.NEW;

    public enum MessageStatus {
        NEW, READ, REPLIED, ARCHIVED
    }

    public ContactMessage(String name, String email, String subject, String message) {
        this.name    = name;
        this.email   = email;
        this.subject = subject;
        this.message = message;
    }
}
