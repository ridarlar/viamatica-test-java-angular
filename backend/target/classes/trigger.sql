CREATE TABLE history (
     id INT AUTO_INCREMENT PRIMARY KEY,
     user_name VARCHAR(255) NOT NULL,
     check_in DATETIME NOT NULL,
     check_out DATETIME NOT NULL,
     user_id INT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES user(id)
);

DELIMITER //
CREATE TRIGGER after_attendanceRecord_insert
    AFTER INSERT ON attendanceRecord
    FOR EACH ROW
BEGIN
    DECLARE username VARCHAR(255);
    SELECT userName INTO username FROM user WHERE id = NEW.userId;
    INSERT INTO logTable (user_id, user_name, check_in, check_out)
    VALUES (NEW.userId, username, NEW.checkIn, NEW.checkOut);
END;
DELIMITER ;
