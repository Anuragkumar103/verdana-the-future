package com.verdana.contact.service;

import com.verdana.contact.dto.ContactRequest;
import com.verdana.contact.model.ContactMessage;
import com.verdana.contact.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Business service layer for contact form operations.
 * Handles persistence, email dispatch, and retrieval logic.
 *
 * @author Anurag Kumar
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository  contactRepository;
    private final EmailService        emailService;

    /**
     * Processes a new contact form submission:
     *  1. Persists the message to the database
     *  2. Sends owner notification email (async)
     *  3. Sends auto-reply confirmation to submitter (async)
     */
    @Transactional
    public ContactMessage submitMessage(ContactRequest req) {
        ContactMessage msg = new ContactMessage(
                req.getName().trim(),
                req.getEmail().trim().toLowerCase(),
                req.getSubject() != null ? req.getSubject().trim() : null,
                req.getMessage().trim()
        );

        ContactMessage saved = contactRepository.save(msg);
        log.info("Saved contact message id={} from email={}", saved.getId(), saved.getEmail());

        // Fire-and-forget async emails
        emailService.sendOwnerNotification(saved);
        emailService.sendAutoReply(saved);

        return saved;
    }

    /** Returns all messages, newest first. */
    @Transactional(readOnly = true)
    public List<ContactMessage> getAllMessages() {
        return contactRepository.findAllByOrderBySubmittedAtDesc();
    }

    /** Returns a single message by id. */
    @Transactional(readOnly = true)
    public Optional<ContactMessage> getById(Long id) {
        return contactRepository.findById(id);
    }

    /** Returns counts per status for admin dashboard. */
    @Transactional(readOnly = true)
    public long countNewMessages() {
        return contactRepository.countByStatus(ContactMessage.MessageStatus.NEW);
    }

    /** Marks a message as READ. */
    @Transactional
    public Optional<ContactMessage> markAsRead(Long id) {
        return contactRepository.findById(id).map(m -> {
            m.setStatus(ContactMessage.MessageStatus.READ);
            return contactRepository.save(m);
        });
    }
}
