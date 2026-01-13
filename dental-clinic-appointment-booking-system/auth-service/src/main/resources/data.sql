INSERT INTO users (name, email, password, role, enabled) VALUES
('Dr. John Doe', 'doctor1@clinic.com', '$2a$10$Dow1pQw1Qw1Qw1Qw1Qw1QeQw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1', 'DOCTOR', true),
('Dr. Jane Smith', 'doctor2@clinic.com', '$2a$10$Dow1pQw1Qw1Qw1Qw1Qw1QeQw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1Qw1', 'DOCTOR', true);
-- Passwords are bcrypt hashes for 'password123'
