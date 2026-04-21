package com.verdana.contact.controller;

import com.verdana.contact.dto.ApiResponse;
import com.verdana.contact.dto.ContactRequest;
import com.verdana.contact.model.ContactMessage;
import com.verdana.contact.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for all contact-form related API operations.
 *
 * Public endpoints:
 *   POST /api/contact         — Submit a new contact message
 *
 * Admin endpoints (prefix /api/admin/contact):
 *   GET  /api/admin/contact            — List all messages
 *   GET  /api/admin/contact/{id}       — Get single message
 *   PUT  /api/admin/contact/{id}/read  — Mark as read
 *   GET  /api/admin/contact/stats      — Basic stats
 *
 * @author Anurag Kumar
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5500",
        "http://127.0.0.1:5500",
        "https://verdana.eco"
}, maxAge = 3600)
public class ContactController {

    private final ContactService contactService;

    /* ═══════════════════════════════════════
       PUBLIC — Submit contact message
       ═══════════════════════════════════════ */

    @PostMapping("/contact")
    public ResponseEntity<ApiResponse> submitContact(
            @Valid @RequestBody ContactRequest request) {

        log.info("Incoming contact submission from: {}", request.getEmail());

        ContactMessage saved = contactService.submitMessage(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Thank you " + saved.getName() + "! Your message has been received. We'll get back to you within 24 hours. 🌿",
                        Map.of("id", saved.getId(), "submittedAt", saved.getSubmittedAt())
                ));
    }

    /* ═══════════════════════════════════════
       ADMIN — Message management
       ═══════════════════════════════════════ */

    @GetMapping("/admin/contact")
    public ResponseEntity<ApiResponse> getAllMessages() {
        List<ContactMessage> messages = contactService.getAllMessages();
        return ResponseEntity.ok(
                ApiResponse.ok("Retrieved " + messages.size() + " messages", messages)
        );
    }

    @GetMapping("/admin/contact/{id}")
    public ResponseEntity<ApiResponse> getMessage(@PathVariable Long id) {
        return contactService.getById(id)
                .map(m -> ResponseEntity.ok(ApiResponse.ok("Message found", m)))
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Message not found with id: " + id)));
    }

    @PutMapping("/admin/contact/{id}/read")
    public ResponseEntity<ApiResponse> markRead(@PathVariable Long id) {
        return contactService.markAsRead(id)
                .map(m -> ResponseEntity.ok(ApiResponse.ok("Marked as read", m)))
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Message not found with id: " + id)));
    }

    @GetMapping("/admin/contact/stats")
    public ResponseEntity<ApiResponse> getStats() {
        long totalNew  = contactService.countNewMessages();
        long totalAll  = contactService.getAllMessages().size();
        return ResponseEntity.ok(
                ApiResponse.ok("Stats retrieved", Map.of(
                        "total",    totalAll,
                        "unread",   totalNew,
                        "read",     totalAll - totalNew
                ))
        );
    }

    /* Health check */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status",  "UP",
                "service", "Verdana Backend",
                "author",  "Anurag Kumar"
        ));
    }
}
