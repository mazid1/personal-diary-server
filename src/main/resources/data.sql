insert into user (id, username, password) values (10001, 'user1', 'p1'),
                                                 (10002, 'user2', 'p2'),
                                                 (10003, 'user3', 'p3'),
                                                 (10004, 'user4', 'p4');

insert into category (id, title, is_default) values (20001, 'Home', true),
                                                    (20002, 'Work', true),
                                                    (20003, 'Budget', true),
                                                    (20004, 'Other', true);
insert into category (id, title, is_default, user_id) values (20021, 'Custom', false, 10002);

insert into note (id, title, description, date, category_id) values (30001, 'Coding', 'Writing java codes after a long break!', '2019-07-08T09:10:56Z', 20001),
                                                                    (30002, 'Song', 'Hearing some Japanese song.', '2019-07-08T09:10:56Z', 20001),
                                                                    (30003, 'Dinner Cost', 'OMG! I have expend too much money today for dinner!', '2019-07-08T09:10:56Z', 20003),
                                                                    (30004, 'Random', 'This is some random text. It should be in other category.', '2019-07-08T09:10:56Z', 20004);

