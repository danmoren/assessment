-- Create tb_user table
CREATE TABLE tb_user
(
    id BIGINT NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(129) NOT NULL,
    name VARCHAR(120),
    PRIMARY KEY (id)
);

-- Add comments to tb_user table and its columns
COMMENT ON TABLE tb_user IS 'All users';
COMMENT ON COLUMN tb_user.id IS 'unique identifier of the user';
COMMENT ON COLUMN tb_user.email IS 'email for user';
COMMENT ON COLUMN tb_user.password IS 'password';
COMMENT ON COLUMN tb_user.name IS 'name of the user';

CREATE SEQUENCE user_id_seq START 2 INCREMENT 1 ;
ALTER TABLE tb_user ALTER COLUMN id SET NOT NULL;
ALTER TABLE tb_user ALTER COLUMN id SET DEFAULT nextval('user_id_seq');

-- Create tb_user_external_project table
CREATE TABLE tb_user_external_project
(
    id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    PRIMARY KEY (id, user_id),
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE
);

-- Add comments to tb_user_external_project table and its columns
COMMENT ON TABLE tb_user_external_project IS 'External Project identifier for users';
COMMENT ON COLUMN tb_user_external_project.id IS 'identifier of external project';
COMMENT ON COLUMN tb_user_external_project.user_id IS 'unique identifier of the user';
COMMENT ON COLUMN tb_user_external_project.name IS 'Name of external project';

CREATE SEQUENCE user_project_id_seq START 1 INCREMENT 1 ;
ALTER TABLE tb_user_external_project ALTER COLUMN id SET NOT NULL;
ALTER TABLE tb_user_external_project ALTER COLUMN id SET DEFAULT nextval('user_project_id_seq');