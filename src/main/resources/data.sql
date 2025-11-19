delete
from quiz;
delete
from quiz_questions;
delete
from user_answers;
delete
from users;
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

insert into users ()
values ();
insert into users ()
values ();
