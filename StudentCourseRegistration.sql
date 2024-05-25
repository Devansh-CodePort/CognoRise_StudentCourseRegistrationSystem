use student;
create table course(
course_id int auto_increment primary key,
course_code char(20) unique not null,
course_title char(80) not null,
course_description text,
course_capacity int not null,
course_schedule char(80)
);
create table stud
(
stud_id int auto_increment primary key,
stud_name char(30) not null,
course_id int,
foreign key (course_id) references course(course_id) 
);













