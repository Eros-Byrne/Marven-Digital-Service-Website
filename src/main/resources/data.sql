delete from answer;
delete from user_attempt;
delete from quiz_questions;
delete from quiz;
delete from capabilities;
delete from user_info;

insert into user_info () values ();
insert into user_info () values ();

insert into capabilities (name) values ("skill1");
insert into capabilities (name) values ("skill2");
insert into capabilities (name) values ("skill3");
insert into capabilities (name) values ("testSkill");

insert into quiz (name, description, time_estimate)
values ('Quiz 1 test', "A quiz of the number 1", 12345);

insert into quiz (name, description, time_estimate)
values ('Quiz 2 test', "A quiz of the number 2", 1);

insert into quiz (name, description, time_estimate)
values ('Quiz 3 test', "A quiz of the number 3", 15);

insert into quiz_questions (quiz_id, capability_id, text)
values (1, 1, 'how do you feel about skill 10');

insert into quiz_questions (quiz_id, capability_id, text)
values (1, 2, 'how do you feel about skill 2');

insert into quiz_questions (quiz_id, capability_id, text)
values (2, 1, 'how do you feel about skill 1 quiz 1');
