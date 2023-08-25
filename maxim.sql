CREATE TABLE t_user (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id VARCHAR(20) NOT NULL,
                        username VARCHAR(50) NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        authorities VARCHAR(100) NOT NULL,
                        create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);