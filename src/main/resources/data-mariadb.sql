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


insert into users (email, password, name, phone, jobrole) values
    ('test@example.com', 'password', 'Test User', '07123456789', 'Tester');


insert into skills (name) values ('skill1');
insert into skills (name) values ('skill2');
insert into skills (name) values ('skill3');
insert into skills (name) values ('testSkill');

insert into outcomes (title) values
    ('Building a team'),
    ('Designing a user journey'),
    ('Managing data for service'),
    ('Designing content');

-- Create quizzes
insert into quiz (name, description, time_estimate)
values ('Building a team', 'Reflect on how you build and support your team.', 15);

insert into quiz (name, description, time_estimate)
values ('Designing a user journey', 'Reflect on how confident you feel about understanding, designing and improving the user journey.', 20);

insert into quiz (name, description, time_estimate)
values ('Quiz 3 test', "A quiz of the number 3", 15);

-- Create capabilities
insert into capabilities (title, description, outcome_id)
values ('Identify the capabilities needed to deliver the service',
        'Users can:
  - show how they’ve assessed the capabilities needed to deliver the service.
  - create a core team that has the capabilities needed.
  - plan how to engage with others to bring in specialist  knowledge or information, when needed.',
        1),
(
    'Establish clear roles and responsibilities',
    'Users can:
    - demonstrate how the team has clear roles and responsibilities.
    - align the team around their purpose and goals.
    - show awareness of the roles and capabilities that are in the team.
    - understand how each person will help them work towards their goals.',
    1
),
(
  'Agree ways of working',
  'Users can:
  - demonstrate how the team has agreed their ways of working.
  - define shared expectations, working arrangements and ways to collaborate effectively.
  - establish routines to help keep the team aligned and on track.',
  1
),
(
  'Create the environment for teams to thrive',
  'Users can:
  - demonstrate how they encourage diverse perspectives within the team.
  - allow team members to contribute and feel safe to speak up.
  - ensure teams have the right support to develop their skills as they work.',
  1
),
(
  'Adapt ways of working based on the needs of the team',
  'Users can:
  - show how they monitor and improve ways of working.
  - reflect on their collective performance.
  - suggest actions to improve.',
  1
);

insert into capabilities (title, description, outcome_id) values
 (
 'Understand how users currently interact with the service',
 'Users can use quantitative and qualitative data from diverse users to understand the current state of the service, create a user journey map, and identify needs, pain points and opportunities.',
2
),
(
'Ideate potential solutions',
'Users can prioritise pain points and consider potential solutions for the highest priority problems.',
2
),
(
'Test potential solutions',
'Users can test potential solutions with users, gather feedback, and judge whether solutions address the problem before implementation.',
2
),
(
'Implement solutions',
'Users can implement tested solutions, update the user journey map, and use data to measure improvement.',
2
),
(
'Share improvements',
'Users can share information about changes and demonstrate impact using data.',
2
),
(
'Improve a service regularly',
'Users can continually monitor and improve the service using data to inform priorities, design/test/implement solutions, and show impact.',
2
);


-- Create questions for Quiz 1
-- Capability 1: Identify the capabilities needed to deliver the service
insert into quiz_questions (quiz_id, capability_id, text) values
(1, 1, 'How confident are you that you can assess the capabilities needed to deliver this service?'),
(1, 1, 'How confident are you that you can create a core team with the capabilities needed?'),
(1, 1, 'How confident are you that you can plan when to involve specialists for additional knowledge or information?');

-- Capability 2: Establish clear roles and responsibilities
insert into quiz_questions (quiz_id, capability_id, text) values
(1, 2, 'How confident are you that your team has clear roles and responsibilities?'),
(1, 2, 'How confident are you that you can align your team around a shared purpose and goals?'),
(1, 2, 'How confident are you that you understand the roles and capabilities of each team member?'),
(1, 2, 'How confident are you that you understand how each person in the team contributes towards your goals?');

-- Capability 3: Agree ways of working
insert into quiz_questions (quiz_id, capability_id, text) values
(1, 3, 'How confident are you that your team has agreed clear ways of working?'),
(1, 3, 'How confident are you that you can define shared expectations and working arrangements with your team?'),
(1, 3, 'How confident are you that you can establish effective ways for your team to collaborate?'),
(1, 3, 'How confident are you that you can set up routines to keep your team aligned and on track?');

-- Capability 4: Create the environment for teams to thrive
insert into quiz_questions (quiz_id, capability_id, text) values
(1, 4, 'How confident are you that you can encourage diverse perspectives within your team?'),
(1, 4, 'How confident are you that you can create an environment where team members feel safe to speak up?'),
(1, 4, 'How confident are you that you can ensure your team has support to develop their skills as they work?');

-- Capability 5: Adapt ways of working based on the needs of the team
insert into quiz_questions (quiz_id, capability_id, text) values
(1, 5, 'How confident are you that you can regularly review and improve your team’s ways of working?'),
(1, 5, 'How confident are you that you can reflect on your team’s performance?'),
(1, 5, 'How confident are you that you can suggest and act on improvements based on what your team learns?');

-- Questions for quiz 2
-- Capability 1: Understand how users currently interact with the service (capability_id = 6)
insert into quiz_questions (quiz_id, capability_id, text) values
(2, 6, 'How confident are you that you can use quantitative data (for example web analytics, call logs or performance metrics) to understand how users interact with the service?'),
(2, 6, 'How confident are you that you can use qualitative data (for example interviews or surveys) to understand how users interact with the service?'),
(2, 6, 'How confident are you that you can gather data from a diverse range of users?'),
(2, 6, 'How confident are you that you can create a user journey map using available data?'),
(2, 6, 'How confident are you that you can identify user needs, pain points and opportunities to improve the service from a user journey map?');

-- Capability 2: Ideate potential solutions (capability_id = 7)
insert into quiz_questions (quiz_id, capability_id, text) values
(2, 7, 'How confident are you that you can prioritise the most important pain points in the user journey?'),
(2, 7, 'How confident are you that you can generate potential solutions to address the highest priority problems?');

-- Capability 3: Test potential solutions (capability_id = 8)
insert into quiz_questions (quiz_id, capability_id, text) values
(2, 8, 'How confident are you that you can test potential solutions with users?'),
(2, 8, 'How confident are you that you can gather useful feedback from users about potential solutions?'),
(2, 8, 'How confident are you that you can judge whether a potential solution addresses the problem before it is implemented?');

-- Capability 4: Implement solutions (capability_id = 9)
insert into quiz_questions (quiz_id, capability_id, text) values
(2, 9, 'How confident are you that you can implement solutions that have been successfully tested with users?'),
(2, 9, 'How confident are you that you can update the user journey map to reflect changes made to the service?'),
(2, 9, 'How confident are you that you can use data to measure whether an implemented solution has improved the service?');

-- Capability 5: Share improvements (capability_id = 10)
insert into quiz_questions (quiz_id, capability_id, text) values
(2, 10, 'How confident are you that you can clearly explain the changes that have been made to the service?'),
(2, 10, 'How confident are you that you can use data to show how a change has improved the service for users?');

-- Capability 6: Improve a service regularly (capability_id = 11)
insert into quiz_questions (quiz_id, capability_id, text) values
(2, 11, 'How confident are you that you can continually monitor the service and identify when it needs improvement?'),
(2, 11, 'How confident are you that you can use data to monitor the current state of the service?'),
(2, 11, 'How confident are you that you can use data to decide which problems to prioritise?'),
(2, 11, 'How confident are you that you can design, test and implement solutions on an ongoing basis?'),
(2, 11, 'How confident are you that you can regularly share information about changes made to the service?'),
(2, 11, 'How confident are you that you can use data to demonstrate how changes have improved the service for users?');
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