package com.verdana.contact.service;

import com.verdana.contact.model.ContactMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

/**
 * Service responsible for sending email notifications when a
 * contact form submission is received.
 *
 * Sends two emails:
 *  1. Notification to site owner (luckyanurag9045@gmail.com)
 *  2. Auto-reply confirmation to the submitter
 *
 * @author Anurag Kumar
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${verdana.mail.to:luckyanurag9045@gmail.com}")
    private String ownerEmail;

    @Value("${verdana.mail.from:noreply@verdana.eco}")
    private String fromEmail;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    /* ── Notification to site owner ── */
    @Async
    public void sendOwnerNotification(ContactMessage msg) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(ownerEmail);
            helper.setSubject("🌿 New Message — Verdana | From: " + msg.getName());
            helper.setText(buildOwnerHtml(msg), true);
            mailSender.send(mime);
            log.info("Owner notification sent for message id={}", msg.getId());
        } catch (Exception ex) {
            log.error("Failed to send owner notification: {}", ex.getMessage(), ex);
        }
    }

    /* ── Auto-reply to submitter ── */
    @Async
    public void sendAutoReply(ContactMessage msg) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(msg.getEmail());
            helper.setSubject("🌿 We received your message — Verdana");
            helper.setText(buildAutoReplyHtml(msg), true);
            mailSender.send(mime);
            log.info("Auto-reply sent to {}", msg.getEmail());
        } catch (Exception ex) {
            log.error("Failed to send auto-reply to {}: {}", msg.getEmail(), ex.getMessage(), ex);
        }
    }

    /* ── HTML templates ── */
    private String buildOwnerHtml(ContactMessage msg) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"/></head>
            <body style="margin:0;padding:0;background:#f4ede0;font-family:'Jost',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr><td align="center" style="padding:40px 20px;">
                  <table width="600" cellpadding="0" cellspacing="0" style="background:#fff;border-radius:8px;overflow:hidden;box-shadow:0 4px 20px rgba(13,40,24,0.08)">
                    <!-- Header -->
                    <tr><td style="background:linear-gradient(135deg,#0d2818,#2d6a4f);padding:40px;text-align:center;">
                      <h1 style="color:#d8f3dc;font-size:28px;margin:0;font-weight:300;letter-spacing:4px;">🌿 VERDANA</h1>
                      <p style="color:#95d5b2;font-size:12px;letter-spacing:3px;margin:8px 0 0;text-transform:uppercase;">New Contact Message</p>
                    </td></tr>
                    <!-- Body -->
                    <tr><td style="padding:40px;">
                      <table width="100%%">
                        <tr><td style="padding:12px 0;border-bottom:1px solid #e8d5b5;">
                          <span style="font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#40916c;font-weight:600;">From</span><br/>
                          <span style="font-size:16px;color:#0d2818;margin-top:4px;display:block;">%s</span>
                        </td></tr>
                        <tr><td style="padding:12px 0;border-bottom:1px solid #e8d5b5;">
                          <span style="font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#40916c;font-weight:600;">Email</span><br/>
                          <a href="mailto:%s" style="font-size:16px;color:#2d6a4f;margin-top:4px;display:block;">%s</a>
                        </td></tr>
                        <tr><td style="padding:12px 0;border-bottom:1px solid #e8d5b5;">
                          <span style="font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#40916c;font-weight:600;">Subject</span><br/>
                          <span style="font-size:16px;color:#0d2818;margin-top:4px;display:block;">%s</span>
                        </td></tr>
                        <tr><td style="padding:12px 0;border-bottom:1px solid #e8d5b5;">
                          <span style="font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#40916c;font-weight:600;">Received</span><br/>
                          <span style="font-size:16px;color:#0d2818;margin-top:4px;display:block;">%s</span>
                        </td></tr>
                        <tr><td style="padding:24px 0 0;">
                          <span style="font-size:11px;letter-spacing:2px;text-transform:uppercase;color:#40916c;font-weight:600;">Message</span>
                          <div style="font-size:15px;color:#4a5a50;line-height:1.8;margin-top:12px;padding:20px;background:#f4ede0;border-radius:4px;border-left:3px solid #52b788;">%s</div>
                        </td></tr>
                      </table>
                    </td></tr>
                    <!-- Footer -->
                    <tr><td style="background:#0d2818;padding:24px;text-align:center;">
                      <p style="color:rgba(212,243,220,0.4);font-size:12px;margin:0;">Made with 💚 by Anurag Kumar · Verdana Eco Initiative</p>
                    </td></tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(
                escHtml(msg.getName()),
                escHtml(msg.getEmail()), escHtml(msg.getEmail()),
                escHtml(msg.getSubject() != null ? msg.getSubject() : "—"),
                msg.getSubmittedAt() != null ? msg.getSubmittedAt().format(FMT) : "—",
                escHtml(msg.getMessage()).replace("\n", "<br/>")
        );
    }

    private String buildAutoReplyHtml(ContactMessage msg) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"/></head>
            <body style="margin:0;padding:0;background:#f4ede0;font-family:'Jost',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr><td align="center" style="padding:40px 20px;">
                  <table width="600" cellpadding="0" cellspacing="0" style="background:#fff;border-radius:8px;overflow:hidden;box-shadow:0 4px 20px rgba(13,40,24,0.08)">
                    <tr><td style="background:linear-gradient(135deg,#0d2818,#2d6a4f);padding:50px 40px;text-align:center;">
                      <h1 style="color:#d8f3dc;font-size:28px;margin:0;font-weight:300;letter-spacing:4px;">🌿 VERDANA</h1>
                      <p style="color:#95d5b2;font-size:28px;margin:16px 0 0;">Thank you, %s!</p>
                    </td></tr>
                    <tr><td style="padding:40px;">
                      <p style="font-size:16px;color:#4a5a50;line-height:1.8;">
                        We've received your message and are grateful you reached out. A member of our team will respond within <strong style="color:#2d6a4f;">24 hours</strong>.
                      </p>
                      <div style="margin:28px 0;padding:20px;background:#d8f3dc;border-radius:4px;">
                        <p style="font-size:13px;letter-spacing:2px;text-transform:uppercase;color:#2d6a4f;font-weight:600;margin:0 0 8px;">Your message</p>
                        <p style="font-size:14px;color:#0d2818;line-height:1.7;margin:0;font-style:italic;">"%s"</p>
                      </div>
                      <p style="font-size:15px;color:#4a5a50;line-height:1.8;">
                        In the meantime, explore our initiatives at <a href="https://verdana.eco" style="color:#2d6a4f;">verdana.eco</a> and connect on social media.
                      </p>
                      <p style="font-size:15px;color:#4a5a50;">Warmly,<br/><strong style="color:#0d2818;">Anurag Kumar</strong><br/><span style="color:#40916c;font-size:13px;">Founder, Verdana</span></p>
                    </td></tr>
                    <tr><td style="background:#0d2818;padding:24px;text-align:center;">
                      <p style="color:rgba(212,243,220,0.4);font-size:12px;margin:0;">Made with 💚 by Anurag Kumar · Verdana Eco Initiative</p>
                    </td></tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(
                escHtml(msg.getName()),
                escHtml(msg.getMessage().length() > 200
                        ? msg.getMessage().substring(0, 200) + "…"
                        : msg.getMessage())
        );
    }

    private String escHtml(String s) {
        if (s == null) return ""; 
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
