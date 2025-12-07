delete from answer;
delete from user_attempt;
delete from quiz_questions;
delete from quiz;
delete from capabilities;
delete from capability_skills;
delete from resources;
delete from skills;
delete from users;
delete from outcomes;

-- password for this user is password
insert into users (user_id, email, password, name, phone, jobrole) values
    (1, 'test@example.com', '$2a$10$G9GYd3lS9lAHXKrUpiYsmO6M1FsK8LO.HuZPd6mfFVcvQfyWMzuzS', 'Test Admin', '07123456789', 'admin');


insert into skills (name) values ('skill1');
insert into skills (name) values ('skill2');
insert into skills (name) values ('skill3');
insert into skills (name) values ('testSkill');

insert into outcomes (title) values
    ('Career Exploration'),
    ('Skill Development'),
    ('Professional Network'),
    ('Job Readiness');

-- Create quizzes
insert into quiz (name, description, time_estimate)
values ('Quiz 1 test', "A quiz of the number 1", 12345);

insert into quiz (name, description, time_estimate)
values ('Quiz 2 test', "A quiz of the number 2", 1);

insert into quiz (name, description, time_estimate)
values ('Quiz 3 test', "A quiz of the number 3", 15);

-- Create capabilities
insert into capabilities (title, description, outcome_id)
values ("Identify the capabilities needed to deliver the service",
        "Users can:
  - show how they’ve assessed the capabilities needed to deliver the service.
  - create a core team that has the capabilities needed.
  - plan how to engage with others to bring in specialist  knowledge or information, when needed.",
        1);

insert into capabilities (title, description, outcome_id)
values ("Identify the capabilities needed to deliver the service 2",
        "Users can:
  - show how they’ve assessed the capabilities needed to deliver the service.
  - create a core team that has the capabilities needed.
  - plan how to engage with others to bring in specialist  knowledge or information, when needed.",
        2);

-- Create questions for Quiz 1
insert into quiz_questions (quiz_id, capability_id, text)
values (1, 1, 'how do you feel about skill 10');

insert into quiz_questions (quiz_id, capability_id, text)
values (1, 2, 'how do you feel about skill 2');

insert into quiz_questions (quiz_id, capability_id, text)
values (1, 1, 'how do you feel about skill 1 quiz 1');

-- Create resources
insert into resources (content, difficulty, capability_id)
values ("Resource 1", "Low", 1);

insert into resources (content, difficulty, capability_id)
values ("Resource 2", "High", 1);

insert into resources (content, difficulty, capability_id)
values ("testing this resource and hopefully it works", "Medium", 1);

-- Link capabilities to skills
insert into capability_skills (capability_id, skill_id)
values (1, 1);

insert into capability_skills (capability_id, skill_id)
values (1, 4);

-- ========================================
-- TEST DATA: User 1 completes Quiz 1
-- ========================================

-- IMPORTANT: quiz_id must match the quiz in user_attempt
# insert into user_attempt (user_attempt_id, quiz_id, user_id, attempt, complete)
# values (1, 1, 1, 1, 1);
#
# -- Answers for Quiz 1 questions (questions 1, 2, 3)
# insert into answer (question_id, user_attempt_id, score)
# values (1, 1, 4); -- Capability 1, score 4
#
# insert into answer (question_id, user_attempt_id, score)
# values (2, 1, 2); -- Capability 2, score 2
#
# insert into answer (question_id, user_attempt_id, score)
# values (3, 1, 5); -- Capability 1, score 5