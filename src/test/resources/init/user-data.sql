DELETE from user_role;
DELETE from users;
DELETE from roles;
DELETE from TODO;

INSERT INTO users (id, user_name, password ) VALUES (1,'test-user','test@123');

INSERT INTO roles (id, name ) VALUES (1,'ROLE_USER');

INSERT INTO user_role (role_id, user_id ) VALUES (1,1);
INSERT INTO TODO (id, title, status, description ) VALUES (100000,'DEVELOPMENT 1','NEW','TEST DESCRIPTION 1');
INSERT INTO TODO (id, title, status, description ) VALUES (200000,'DEVELOPMENT 2','IN_PROGRESS','TEST DESCRIPTION 2');
INSERT INTO TODO (id, title, status, description ) VALUES (300000,'DEVELOPMENT 3','DONE','TEST DESCRIPTION 3');
INSERT INTO TODO (id, title, status, description ) VALUES (400000,'DEVELOPMENT 4','NEW','TEST DESCRIPTION 4');



