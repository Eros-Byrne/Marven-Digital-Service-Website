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
insert into users (user_id, email, password, name, phone) values
    (1, 'test@example.com', '$2a$10$G9GYd3lS9lAHXKrUpiYsmO6M1FsK8LO.HuZPd6mfFVcvQfyWMzuzS', 'Test User', '07123456789');


insert into skills (name) values
                              ('Data Analysis'),
                              ('Empathy'),
                              ('Active Listening'),
                              ('Critical Thinking'),
                              ('Communication'),
                              ('User Engagement'),
                              ('Curiosity'),
                              ('Open-Mindedness'),
                              ('Adaptability'),
                              ('Collaboration'),
                              ('Problem Solving'),
                              ('Prioritisation'),
                              ('Creativity'),
                              ('Persuasion'),
                              ('Attention to Detail'),
                              ('Analytical Thinking'),
                              ('Stakeholder Engagement'),
                              ('Storytelling'),
                              ('Influencing'),
                              ('Presenting'),
                              ('Transparency'),
                            ('Organisational Awareness'),
                            ('Inclusivity'),
                            ('Accountability'),
                            ('Organisation'),
                              ('Decision-making'),
                              ('Leadership'),
                              ('Strategic thinking'),
                              ('Data Literacy'),
                              ('Goal-setting'),
                              ('Negotiation'),
                              ('Time management'),
                              ('Financial management');


insert into outcomes (title) values
    ('Building a team'),
    ('Designing a user journey'),
    ('Designing content'),
    ('Managing a service'),
    ('Managing technology for a service'),
    ('Managing data for a service');


-- Create quizzes
insert into quiz (name, description, time_estimate)
values ('Building a team', 'Reflect on how you build and support your team.', 15);

insert into quiz (name, description, time_estimate)
values ('Designing a user journey', 'Reflect on how confident you feel about understanding, designing and improving the user journey.', 20);

insert into quiz (name, description, time_estimate)
values ('Designing content', 'Reflect on how confident you feel about designing, testing and improving accessible bilingual content.', 20);

insert into quiz (name, description, time_estimate)
values ('Managing a service','Reflect on how confident you feel about managing and improving a service.',20);

insert into quiz (name, description, time_estimate)
values ('Managing technology for a service','Reflect on how confident you feel about selecting, managing and assuring technology for a service.',20);

insert into quiz (name, description, time_estimate)
values ('Managing data for a service','Reflect on how confident you feel about managing data legally, ethically and effectively.',20);
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
 'Users can:
-  use quantitative data like web analytics, call logs, and performance metrics to understand the current state of the service.
- use qualitative data like user interviews and surveys to understand the current state of the service.
- use data from a diverse range of users.
- use data to create a user journey map.
- identify user needs, pain points and opportunities to improve the service from a user journey map.',
2
),
(
'Ideate potential solutions',
'Users can:
- show how they have prioritised identified pain points.
- consider potential solutions to address the highest priority problem/s.',
2
),
(
'Test potential solutions',
'Users can:
- test potential solutions with users.
- gather feedback.
- identify whether the solution addresses the problem, before it is implemented.',
2
),
(
'Implement solutions',
'Users can:
- implement solutions that have been successfully tested.
- update the user journey map to show the changes made.
- continue to use quantitative and/or qualitative data to measure whether the solution has improved the service.',
2
),
(
'Share improvements',
'Users can:
- share information about the change that has been made.
- use data to demonstrate how the change has improved the service for users.',
2
),
(
'Improve a service regularly',
'Users can:
- continually monitor and improve the service.
- use data to continually monitor the current state of the service.
- use data to inform their priorities.
- design, test and implement solutions, using data to measure whether the solution has improved the service.
- share information regularly about changes made.
- demonstrate how the change has improved the service for users, by using data.',
2
);
insert into capabilities (title, description, outcome_id) values
(
'Understand how users interact with your content',
'Users can:
- use quantitative data to understand how a diverse range of users interact with their content.
- use qualitative data to understand how a diverse range of users interact with their content.
- use data to determine user needs.
- identify difficulties and opportunities to improve content.
- plan how to avoid duplication or conflicting messaging of existing content.',
3
),
(
'Plan accessible and bilingual content',
'Users can:
- prioritise identified difficulties.
- consider structure and format of content.
- develop content in plain Welsh and plain English.
- treat both languages equally.
- ensure content is accessible to a diverse range of users.',
3
),
(
'Test your content',
'Users can:
- test content with users.
- review or peer review content.
- gather feedback.
- identify whether solutions address the problem before publishing.',
3
),
(
'Publish your content',
'Users can:
- publish content that has been successfully tested.
- publish content in an appropriate format.
- use data to measure whether content has improved the user experience.',
3
),
(
'Share improvements',
'Users can:
- share information about changes made.
- demonstrate improvements to user experience using data.',
3
),
(
'Improve your content regularly',
'Users can:
- continually monitor and improve content.
- monitor performance using data.
- use data to inform priorities.
- design, test and publish improvements.
- demonstrate improvement using data.',
3
);
insert into capabilities (title, description, outcome_id) values
(
'Identify users and their needs',
'Users can:
- identify the users of the product or service.
- use quantitative data like web analytics, call logs, and performance metrics to understand their users and their needs.
- use qualitative data like user interviews and surveys to understand their users and their needs.
- plan to test any assumptions about the needs of users.
- explain the outcome they are looking to achieve, rather than the output.',
4
),
(
'Create a vision',
'Users can:
- describe the service.
- describe the aims of the service users and their identified needs and who it is for.
- share the vision, within the team, organisation, other relevant organisations and publicly , if appropriate.
- use the vision to create alignment, both within and beyond the team.',
4
),
(
'Consider the Well-being of Future Generations (Wales) Act 2015',
'Users can:
- clearly identify how the product or service aligns with the organisational wellbeing objectives.
- demonstrate how they can track progress towards the objectives.',
4
),
(
'Measure progress',
'Users can:
- identify suitable outcomes.
- demonstrate how these align with the vision.
- measure progress against outcomes.
- determine their next steps.',
4
),
(
'Plan improvements to the service',
'Users can:
- create a plan that is simple, and easily understood by others.
- show how the plan aligns with the vision.
- adapt their plans as they learn.
- shape the plan using identified priorities.
- review and update the plan regularly.',
4
),
(
'Prioritise and manage work',
'Users can:
- track planned improvements.
- prioritise their work.
- define and understand the standard of work that needs to be delivered.',
4
),
(
'Communicate progress and decisions',
'Users can:
- provide regular progress updates.
- keep stakeholders informed about how the service is progressing towards its goals.
- share updates regularly.
- highlight achievements, challenges and next steps.
- explain decisions which are supported using evidence from users and data.',
4
);
insert into capabilities (title, description, outcome_id) values
(
'Identify what technology is needed',
'Users can:
- clearly identify the technology needed to run the service, based on the needs of users.
- identify the essential capabilities of a technology solution.
- identify the most appropriate solution based on user needs and capabilities.
- identify a scalable, cloud-based and widely supported technology.',
5
),
(
'Decide whether to buy or build a solution',
'Users can:
- evaluate the benefits and drawbacks of procuring or developing a system.
- make use of repeatable patterns.
When procuring users can:
- make choices that avoid being locked into lengthy contracts.
- change suppliers when they need to so they are not dependent on a single supplier.
When building users can:
- follow best practices when building a solution.
- use common programming languages and develop solutions which are easy to maintain and iterate.',
5
),
(
'Ensure technology meets relevant standards',
'Users can:
- ensure technology choices comply with relevant standards, including the Digital Service Standard for Wales and the Welsh Language Standards.',
5
),
(
'Understand security risks',
'Users can:
- identify common security threats.
- mitigate risks through security testing.
- implement practical security measures.
- access support to ensure systems are secure.',
5
),
(
'Ensure compliance',
'Users can:
- identify legislation and policies relating to their service.
- follow relevant processes to ensure compliance with legislation and policy.',
5
);
insert into capabilities (title, description, outcome_id) values
(
'Use data legally and ethically',
'Users can:
- demonstrate an awareness of legal responsibilities such as GDPR and the Data Protection Act.
- differentiate between different categories of data.
- explain safeguards for different categories of data.
- apply principles from the Data Ethics Framework.',
6
),
(
'Identify the data the service needs',
'Users can:
- identify the types of data a service collects.
- explain why the data is needed to deliver the service.',
6
),
(
'Store data appropriately',
'Users can:
- explain how data is stored, processed and retained.
- identify potential risks and ways to mitigate them.',
6
),
(
'Ensure data is usable',
'Users can:
- explain the importance of data quality, consistency and structure.
- identify how and when to seek specialist support.',
6
),
(
'Share and reuse data responsibly',
'Users can:
- identify opportunities to share or reuse data where it benefits users.
- ensure data is shared securely, ethically and legally.',
6
),
(
'Identify where to get advice and assurance',
'Users can:
- demonstrate awareness of when to involve data specialists such as DPOs or Information Governance leads.',
6
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

-- Capability 1: Understand how users interact with your content (capability_id = 12)
insert into quiz_questions (quiz_id, capability_id, text) values
(3, 12, 'How confident are you that you can use quantitative data to understand how users interact with content?'),
(3, 12, 'How confident are you that you can use qualitative research to understand user needs?'),
(3, 12, 'How confident are you that you can identify difficulties users face when understanding or finding content?'),
(3, 12, 'How confident are you that you can identify opportunities to improve existing content?'),
(3, 12, 'How confident are you that you can avoid duplication or conflicting messaging across content?');

-- Capability 2: Plan accessible and bilingual content (capability_id = 13)
insert into quiz_questions (quiz_id, capability_id, text) values
(3, 13, 'How confident are you that you can prioritise content issues based on user needs?'),
(3, 13, 'How confident are you that you can design accessible content for a diverse range of users?'),
(3, 13, 'How confident are you that you can develop content in plain Welsh and plain English equally?'),
(3, 13, 'How confident are you that you can consider structure and format to improve content clarity?');

-- Capability 3: Test your content (capability_id = 14)
insert into quiz_questions (quiz_id, capability_id, text) values
(3, 14, 'How confident are you that you can test content with users before publishing?'),
(3, 14, 'How confident are you that you can gather useful feedback through reviews or peer reviews?'),
(3, 14, 'How confident are you that you can judge whether content addresses user needs before publishing?');

-- Capability 4: Publish your content (capability_id = 15)
insert into quiz_questions (quiz_id, capability_id, text) values
(3, 15, 'How confident are you that you can publish content in an appropriate format?'),
(3, 15, 'How confident are you that you can use data to assess whether published content improved the user experience?');

-- Capability 5: Share improvements (capability_id = 16)
insert into quiz_questions (quiz_id, capability_id, text) values
(3, 16, 'How confident are you that you can share content improvements with stakeholders?'),
(3, 16, 'How confident are you that you can demonstrate improvements using data?');

-- Capability 6: Improve your content regularly (capability_id = 17)
insert into quiz_questions (quiz_id, capability_id, text) values
(3, 17, 'How confident are you that you can continually monitor content performance using data?'),
(3, 17, 'How confident are you that you can use data to prioritise future content improvements?'),
(3, 17, 'How confident are you that you can regularly design, test and publish content improvements?'),
(3, 17, 'How confident are you that you can demonstrate ongoing improvements to user experience using data?');

-- Identify users and their needs (capability_id = 18)
insert into quiz_questions (quiz_id, capability_id, text) values
(4, 18, 'How confident are you that you can identify the users of a service?'),
(4, 18, 'How confident are you that you can use quantitative data to understand user needs?'),
(4, 18, 'How confident are you that you can use qualitative research to understand user needs?'),
(4, 18, 'How confident are you that you can plan to test assumptions about user needs?'),
(4, 18, 'How confident are you that you can focus on outcomes rather than outputs?');

-- Create a vision (capability_id = 19)
insert into quiz_questions (quiz_id, capability_id, text) values
(4, 19, 'How confident are you that you can clearly describe the service and its purpose?'),
(4, 19, 'How confident are you that you can describe the needs of service users?'),
(4, 19, 'How confident are you that you can share a clear service vision with stakeholders?'),
(4, 19, 'How confident are you that you can use a vision to create alignment across teams?');

-- Well-being of Future Generations Act (capability_id = 20)
insert into quiz_questions (quiz_id, capability_id, text) values
(4, 20, 'How confident are you that you can identify how the service aligns with organisational wellbeing objectives?'),
(4, 20, 'How confident are you that you can track progress against wellbeing objectives?');

-- Measure progress (capability_id = 21)
insert into quiz_questions (quiz_id, capability_id, text) values
(4, 21, 'How confident are you that you can identify meaningful outcomes for the service?'),
(4, 21, 'How confident are you that outcomes align with the service vision?'),
(4, 21, 'How confident are you that you can measure progress against outcomes?'),
(4, 21, 'How confident are you that you can determine appropriate next steps based on progress?');

-- Plan improvements to the service (capability_id = 22)
insert into quiz_questions (quiz_id, capability_id, text) values
(4, 22, 'How confident are you that you can create a simple improvement plan?'),
(4, 22, 'How confident are you that improvement plans align with the service vision?'),
(4, 22, 'How confident are you that you can adapt plans as you learn?'),
(4, 22, 'How confident are you that you can review and update plans regularly?');

-- Prioritise and manage work (capability_id = 23)
insert into quiz_questions (quiz_id, capability_id, text) values
(4, 23, 'How confident are you that you can prioritise work effectively?'),
(4, 23, 'How confident are you that you can track planned improvements?'),
(4, 23, 'How confident are you that you can define and meet required delivery standards?');

-- Communicate progress and decisions (capability_id = 24)
insert into quiz_questions (quiz_id, capability_id, text) values
(4, 24, 'How confident are you that you can provide regular progress updates?'),
(4, 24, 'How confident are you that you can keep stakeholders informed using evidence and data?'),
(4, 24, 'How confident are you that you can clearly explain decisions that have been made?');

-- Capability 25: Identify what technology is needed
insert into quiz_questions (quiz_id, capability_id, text) values
(5, 25, 'How confident are you that you can identify the technology needed to run the service based on user needs?'),
(5, 25, 'How confident are you that you can identify the essential capabilities of a technology solution?'),
(5, 25, 'How confident are you that you can select the most appropriate technology solution?'),
(5, 25, 'How confident are you that you can identify scalable, cloud-based and widely supported technology?');

-- Capability 26: Decide whether to buy or build a solution
insert into quiz_questions (quiz_id, capability_id, text) values
(5, 26, 'How confident are you that you can evaluate whether to buy or build a technology solution?'),
(5, 26, 'How confident are you that you can avoid supplier lock-in when procuring technology?'),
(5, 26, 'How confident are you that you can follow best practices when building a technology solution?');

-- Capability 27: Ensure technology meets relevant standards
insert into quiz_questions (quiz_id, capability_id, text) values
(5, 27, 'How confident are you that technology choices comply with relevant digital and language standards?');

-- Capability 28: Understand security risks
insert into quiz_questions (quiz_id, capability_id, text) values
(5, 28, 'How confident are you that you can identify common security threats?'),
(5, 28, 'How confident are you that you can mitigate security risks through testing and controls?'),
(5, 28, 'How confident are you that you know where to get support to ensure systems are secure?');

-- Capability 29: Ensure compliance
insert into quiz_questions (quiz_id, capability_id, text) values
(5, 29, 'How confident are you that you can identify legislation and policies relevant to your service?'),
(5, 29, 'How confident are you that you can follow processes to ensure compliance with legislation and policy?');
-- Capability 30: Use data legally and ethically
insert into quiz_questions (quiz_id, capability_id, text) values
(6, 30, 'How confident are you that you understand your legal responsibilities when managing data?'),
(6, 30, 'How confident are you that you can apply ethical principles to data-related decisions?');

-- Capability 31: Identify the data the service needs
insert into quiz_questions (quiz_id, capability_id, text) values
(6, 31, 'How confident are you that you can identify the data a service needs to operate effectively?'),
(6, 31, 'How confident are you that you can explain why specific data is required?');

-- Capability 32: Store data appropriately
insert into quiz_questions (quiz_id, capability_id, text) values
(6, 32, 'How confident are you that data is stored, processed and retained appropriately?'),
(6, 32, 'How confident are you that you can identify and mitigate data storage risks?');

-- Capability 33: Ensure data is usable
insert into quiz_questions (quiz_id, capability_id, text) values
(6, 33, 'How confident are you that you can ensure data quality and consistency?'),
(6, 33, 'How confident are you that you know when to seek specialist data support?');

-- Capability 34: Share and reuse data responsibly
insert into quiz_questions (quiz_id, capability_id, text) values
(6, 34, 'How confident are you that you can identify opportunities to reuse or share data responsibly?'),
(6, 34, 'How confident are you that data sharing is secure and compliant?');

-- Capability 35: Identify where to get advice and assurance
insert into quiz_questions (quiz_id, capability_id, text) values
(6, 35, 'How confident are you that you know when and where to seek data governance advice?');
-- Create resources
insert into resources (content, difficulty, capability_id)
values ("Resource 1", "Low", 1);

insert into resources (content, difficulty, capability_id)
values ("Resource 2", "High", 1);

insert into resources (content, difficulty, capability_id)
values ("testing this resource and hopefully it works", "Medium", 1);

-- Link capabilities to skills
insert into capability_skills values
(1, 17),
(1, 2),
(1, 11),
(1, 9);

-- Establish clear roles and responsibilities
insert into capability_skills values
(2, 5),
(2, 4),
(2, 3),
(2, 8),
(2, 11),
(2, 10);

-- Agree ways of working
insert into capability_skills values
(3, 10),
(3, 3),
(3, 11);

-- Create the environment for teams to thrive
insert into capability_skills values
(4, 10),
(4, 8),
(4, 2),
(4, 5),
(4, 18),
(4, 3);

-- Adapt ways of working based on the needs of the team
insert into capability_skills values
(5, 1),
(5, 11),
(5, 9);
insert into capability_skills values
(6, 1),
(6, 2),
(6, 3),
(6, 4),
(6, 5),
(6, 6),
(6, 7),
(6, 8),
(6, 9),
(6, 10);

-- Ideate potential solutions
insert into capability_skills values
(7, 11),
(7, 12),
(7, 13),
(7, 10),
(7, 8),
(7, 5),
(7, 14),
(7, 9);
-- Test potential solutions
insert into capability_skills values
(8, 3),
(8, 8),
(8, 4),
(8, 5),
(8, 9),
(8, 10),
(8, 15);

-- Implement solutions
insert into capability_skills values
(9, 11),
(9, 9),
(9, 5),
(9, 10),
(9, 15),
(9, 16),
(9, 6),
(9, 17);

-- Share improvements
insert into capability_skills values
(10, 5),
(10, 18),
(10, 17),
(10, 9),
(10, 10),
(10, 19),
(10, 20),
(10, 21);

-- Improve a service regularly
insert into capability_skills values
(11, 1),
(11, 9),
(11, 11),
(11, 5),
(11, 10),
(11, 17),
(11, 6);

insert into capability_skills values
(12, 3),
(12, 1),
(12, 2),
(12, 11),
(12, 5),
(12, 15),
(12, 10),
(12, 9),
(12, 22);

-- Plan accessible and bilingual content
insert into capability_skills values
(13, 9),
(13, 15),
(13, 10),
(13, 5),
(13, 13),
(13, 23),
(13, 22),
(13, 12),
(13, 11);

-- Test your content
insert into capability_skills values
(14, 3),
(14, 9),
 (14, 16),
(14, 15),
(14, 10),
(14, 5),
(14, 2),
(14, 8),
(14, 11);

-- Publish your content
insert into capability_skills values
(15, 24),
(15, 9),
(15, 15),
 (15, 10),
(15, 5),
(15, 1),
 (15, 25);

-- Share improvements
insert into capability_skills values
(16, 9),
(16, 10),
(16, 5),
(16, 19),
(16, 20),
(16, 17),
(16, 18),
(16, 21);

-- Improve your content regularly
insert into capability_skills values
(17, 9),
(17, 10),
(17, 5),
(17, 1),
(17, 11),
(17, 17),
(17, 6);

-- Identify users and their needs
insert into capability_skills values
(18, 3),
(18, 16),
(18, 5),
(18, 7),
(18, 2);

-- Create a vision
insert into capability_skills values
(19, 24),
(19, 9),
(19, 10),
(19, 26),
(19, 27),
(19, 28);

-- Well-being of Future Generations Act
insert into capability_skills values
(20, 24),
(20, 10),
(20, 27),
(20, 28);

-- Measure progress
insert into capability_skills values
(21, 15),
(21, 4),
(21, 1),
(21, 29),
(21, 11);

-- Plan improvements to the service
insert into capability_skills values
(22, 24),
(22, 13),
(22, 25),
(22, 11),
(22, 17),
(22, 28);

-- Prioritise and manage work
insert into capability_skills values
(23, 24),
(23, 26),
(23, 30),
(23, 12),
(23, 31);

-- Communicate progress and decisions
insert into capability_skills values
(24, 5),
(24, 29),
(24, 26),
(24, 17),
(24, 18),
(24, 21);

-- Identify what technology is needed (capability_id = 25)
insert into capability_skills values
(25, 16),
(25, 5),
(25, 11);

-- Decide whether to buy or build a solution (capability_id = 26)
insert into capability_skills values
(26, 26),
(26, 33),
(26, 31);

-- Ensure technology meets relevant standards (capability_id = 27)
insert into capability_skills values
(27, 15),
(27, 17);

-- Understand security risks (capability_id = 28)
insert into capability_skills values
(28, 10),
(28, 4);

-- Ensure compliance (capability_id = 29)
insert into capability_skills values
(29, 24),
(29, 17);
