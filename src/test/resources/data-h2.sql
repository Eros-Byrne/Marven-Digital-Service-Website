INSERT INTO users (user_id, name, email, phone, password)
VALUES (1, 'Test user', 'test@email.com', '07123456789', 'password');

-- Add test user for QuizDetailSummaryRepositoryTest
INSERT INTO users (user_id, name, email, phone, password)
VALUES (100, 'Test User 100', 'testuser100@email.com', '07987654321', 'password');

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

INSERT INTO teams (team_id, team_name, team_description)
VALUES (1, 'Test team 1', 'Test description 1');

INSERT INTO team_members (team_id, user_id, is_manager)
VALUES (1, 1, false);

INSERT INTO teams (team_id, team_name, team_description)
VALUES (2, 'Test team 2', 'Test description 2');

INSERT INTO team_members (team_id, user_id, is_manager)
VALUES (2, 1, true);

-- Insert quizzes for test
-- INSERT INTO quiz (quiz_id, name, description, time_estimate, outcome_id) VALUES
-- (1, 'Building a team', 'Reflect on how you build and support your team.', 15, 1),
-- (2, 'Designing a user journey', 'Reflect on how confident you feel about understanding, designing and improving the user journey.', 20, 2),
-- (3, 'Designing content', 'Reflect on how confident you feel about designing, testing and improving accessible bilingual content.', 20, 3),
-- (4, 'Managing a service', 'Reflect on how confident you feel about managing and improving a service.', 20, 4),
-- (5, 'Managing technology for a service', 'Reflect on how confident you feel about selecting, managing and assuring technology for a service.', 20, 5),
-- (6, 'Managing data for a service', 'Reflect on how confident you feel about managing data legally, ethically and effectively.', 20, 6);
--
-- -- Insert quiz questions so test dont fail
-- INSERT INTO quiz_questions (question_id, quiz_id, capability_id, text) VALUES
-- (1, 1, 1, 'Test question 1 for quiz 1'),
-- (2, 1, 1, 'Test question 2 for quiz 1'),
-- (3, 1, 1, 'Test question 3 for quiz 1'),
-- (4, 2, 1, 'Test question 1 for quiz 2');

INSERT INTO outcomes (title)
VALUES ('Test Outcome');

INSERT INTO outcomes (title)
VALUES ('Test Outcome 2')
