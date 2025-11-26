SET MODE MYSQL;
SET IGNORECASE TRUE;

CREATE TABLE capabilities (
    capability_id BIGINT PRIMARY KEY,
    title VARCHAR(128),
    description TEXT
);

CREATE TABLE resources (
    resource_id BIGINT PRIMARY KEY,
    content VARCHAR(255),
    difficulty VARCHAR(10),
    capability_id BIGINT,
    FOREIGN KEY (capability_id) REFERENCES capabilities(capability_id)
);

CREATE TABLE skills (
    skill_id BIGINT PRIMARY KEY,
    name VARCHAR(128)
);

CREATE TABLE capability_skills (
    capability_id BIGINT,
    skill_id BIGINT,
    PRIMARY KEY (capability_id, skill_id),
    FOREIGN KEY (capability_id) REFERENCES capabilities(capability_id),
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
);
