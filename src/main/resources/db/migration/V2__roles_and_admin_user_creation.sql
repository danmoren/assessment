-- Create the role table to store different user roles
CREATE TABLE tb_role
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

COMMENT ON TABLE tb_role IS 'Role name (e.g., ADMIN, USER)';

CREATE SEQUENCE role_id_seq START 3 INCREMENT 1 ;
ALTER TABLE tb_role ALTER COLUMN id SET NOT NULL;
ALTER TABLE tb_role ALTER COLUMN id SET DEFAULT nextval('role_id_seq');

-- Create a mapping table for users and roles (Many-to-Many relationship)
CREATE TABLE tb_user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES tb_role(id) ON DELETE CASCADE
);

-- Insert default roles into the tb_role table
INSERT INTO tb_role (id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

-- Insert admin user
INSERT INTO tb_user (id, email, password, name) VALUES
    (1, 'admin@test.com', '$2a$10$RPJHXaN50K12WYY1Xygfr.4HyqlflmAgIzIC6s.7BEbi3SjDdqvVG', 'Admin User');

-- Assign the ADMIN role to the admin user
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);