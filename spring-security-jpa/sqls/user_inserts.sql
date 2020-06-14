drop user eApp@localhost;
CREATE USER eApp@localhost IDENTIFIED BY 'odroid';
GRANT ALL PRIVILEGES ON entities.database_name to eApp@localhost;
GRANT ALL PRIVILEGES ON *.* TO 'root'@'192.168.15.%' IDENTIFIED BY 'odroid' WITH GRANT OPTION;

FLUSH PRIVILEGES;

INSERT INTO `user` (`id`, `active`, `password`, `roles`, `user_name`) VALUES (0, b'1', 'odroid', 'ROLE_USER,ROLE_ADMIN', 'admin');
INSERT INTO `user` (`id`, `active`, `password`, `roles`, `user_name`) VALUES (1, b'1', 'odroid', 'ROLE_USER', 'user');


