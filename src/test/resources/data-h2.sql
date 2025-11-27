INSERT INTO capabilities (capability_id, title, description)
VALUES (1, 'Normal Capability1', 'Description');

INSERT INTO resources (resource_id, content, difficulty, capability_id)
VALUES (1, 'Resource 1', 'Low', 1);

INSERT INTO skills (skill_id, name)
VALUES (1, 'Skill 1');

INSERT INTO skills (skill_id, name)
VALUES (2, 'Skill 2');

INSERT INTO capabilities (capability_id, title, description)
VALUES (2, 'Capability with no resources', 'Description');

INSERT INTO capabilities (capability_id, title, description)
VALUES (3, 'Capability with no skills', 'Description');

INSERT INTO capability_skills (capability_id, skill_id)
VALUES (1, 1);

INSERT INTO capability_skills (capability_id, skill_id)
VALUES (1, 2);

INSERT INTO capability_skills (capability_id, skill_id)
VALUES (2, 1);

INSERT INTO resources (resource_id, content, difficulty, capability_id)
VALUES (2, 'Resource 2', 'High', 3);
