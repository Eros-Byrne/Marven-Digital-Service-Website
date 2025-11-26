delete from answer;
delete from user_attempt;
delete from quiz_questions;
delete from quiz;
delete from capabilities;
delete from user_info;
delete from capability_skills;
delete from resources;
delete from capabilities;
delete from skills;
delete
from quiz;
delete
from quiz_questions;
delete
from user_answers;
delete
from users;

insert into user_info () values ();
insert into user_info () values ();

insert into skills (name) values ("skill1");
insert into skills (name) values ("skill2");
insert into skills (name) values ("skill3");
insert into skills (name) values ("testSkill");

# insert into capabilities (name) values ("skill1");
# insert into capabilities (name) values ("skill2");
# insert into capabilities (name) values ("skill3");
# insert into capabilities (name) values ("testSkill");

insert into quiz (name, description, time_estimate)
values ('Quiz 1 test', "A quiz of the number 1", 12345);

insert into quiz (name, description, time_estimate)
values ('Quiz 2 test', "A quiz of the number 2", 1);

insert into quiz (name, description, time_estimate)
values ('Quiz 3 test', "A quiz of the number 3", 15);

insert into capabilities (title, description)
values ("Identify the capabilities needed to deliver the service",
        "Users can:
  - show how they’ve assessed the capabilities needed to deliver the service.
  - create a core team that has the capabilities needed.
  - plan how to engage with others to bring in specialist  knowledge or information, when needed.");

insert into capabilities (title, description)
values ("Identify the capabilities needed to deliver the service",
        "Users can:
  - show how they’ve assessed the capabilities needed to deliver the service.
  - create a core team that has the capabilities needed.
  - plan how to engage with others to bring in specialist  knowledge or information, when needed.");

insert into quiz_questions (quiz_id, capability_id, text)
values (1, 1, 'how do you feel about skill 10');

insert into quiz_questions (quiz_id, capability_id, text)
values (1, 2, 'how do you feel about skill 2');

insert into quiz_questions (quiz_id, capability_id, text)
values (2, 1, 'how do you feel about skill 1 quiz 1');

insert into resources (content, difficulty, capability_id)
values ("Resource 1", "Low", 1);

insert into resources (content, difficulty, capability_id)
values ("Resource 2", "High", 1);

insert into resources (content, difficulty, capability_id)
values ("testing this resource and hopefully it works", "Medium", 1);

insert into capability_skills (capability_id, skill_id)
values (1, 1);

insert into capability_skills (capability_id, skill_id)
values (1, 4);