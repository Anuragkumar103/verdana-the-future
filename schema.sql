-- ════════════════════════════════════════
--  Verdana Database Schema
--  Author: Anurag Kumar
-- ════════════════════════════════════════

-- Create database (run once as root)
CREATE DATABASE IF NOT EXISTS verdana_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Create dedicated user
CREATE USER IF NOT EXISTS 'verdana_user'@'localhost'
    IDENTIFIED BY 'verdana_pass';

GRANT ALL PRIVILEGES ON verdana_db.* TO 'verdana_user'@'localhost';
FLUSH PRIVILEGES;

USE verdana_db;

-- ── Contact Messages ──────────────────
CREATE TABLE IF NOT EXISTS contact_messages (
    id             BIGINT          NOT NULL AUTO_INCREMENT,
    sender_name    VARCHAR(100)    NOT NULL,
    sender_email   VARCHAR(150)    NOT NULL,
    subject        VARCHAR(200),
    message        TEXT            NOT NULL,
    submitted_at   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status         ENUM('NEW','READ','REPLIED','ARCHIVED') NOT NULL DEFAULT 'NEW',

    PRIMARY KEY (id),
    INDEX idx_email      (sender_email),
    INDEX idx_status     (status),
    INDEX idx_submitted  (submitted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── Sample query: view unread messages ──
-- SELECT id, sender_name, sender_email, subject, submitted_at
-- FROM contact_messages
-- WHERE status = 'NEW'
-- ORDER BY submitted_at DESC;
