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

insert into users ()
values ();
insert into users ()
values ();

insert into skills (name) values ("skill1");
insert into skills (name) values ("skill2");
insert into skills (name) values ("skill3");
insert into skills (name) values ("testSkill");

insert into quiz (name, description, time_estimate)
values ('Quiz 1 test', "A quiz of the number 1", 12345);
insert into quiz (name, description, time_estimate)
values ('Quiz 2 test', "A quiz of the number 2", 1);
insert into quiz (name, description, time_estimate)
values ('Quiz 3 test', "A quiz of the number 3", 15);


insert into quiz_questions (quiz_id, title, text, skill_id)
values (1,'test question 1 quiz 1', 'how do you feel about skill 1', 1);
insert into quiz_questions (quiz_id, title, text, skill_id)
values (1,'test question 2 quiz 1', 'how do you feel about skill 2', 2);
insert into quiz_questions (quiz_id, title, text, skill_id)
values (2,'test question 1 quiz 2', 'how do you feel about skill 1 quiz 2', 1);

insert into capabilities (title, description)
values ("Identify the capabilities needed to deliver the service",
      "Users can:
- show how they’ve assessed the capabilities needed to deliver the service.
- create a core team that has the capabilities needed.
- plan how to engage with others to bring in specialist  knowledge or information, when needed.");

insert into resources (content, difficulty, capability_id)
values ("Resource 1", "Low", 1);

insert into resources (content, difficulty, capability_id)
values ("Resource 2", "High", 1);

insert into resources (content, difficulty, capability_id)
values ("testing this resource and hopefully it works", "Medium", 1);

