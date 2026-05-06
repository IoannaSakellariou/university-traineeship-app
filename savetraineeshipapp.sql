
CREATE DATABASE savetraineeshipapp;
USE savetraineeshipapp;



CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    role ENUM('USER', 'STUDENT', 'PROFESSOR', 'COMPANY', 'COMMITTEE_MEMBER'),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE committee_member (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT UNIQUE,
    username VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE companies (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT UNIQUE,
    username VARCHAR(255),
    companyname VARCHAR(255),
    companylocation VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- Πίνακας καθηγητών
CREATE TABLE professors (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT UNIQUE,
    username VARCHAR(255),
    professor_name VARCHAR(255),
    interests VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE student (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT UNIQUE,
    assigned_traineeship_id INT UNIQUE,
    studentname VARCHAR(255),
    username VARCHAR(255),
    am VARCHAR(255),
    avg_grade FLOAT,
    interests VARCHAR(255),
    preferred_location VARCHAR(255),
    skills VARCHAR(255),
    looking_for_traineeship BIT,
    role ENUM('USER', 'STUDENT', 'PROFESSOR', 'COMPANY', 'COMMITTEE_MEMBER'),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE traineeship_position (
    id INT NOT NULL AUTO_INCREMENT,
    company_id INT,
    committee_member_id INT,
    supervisor_id INT,
    student_id INT,
    from_date DATE,
    to_date DATE,
    is_assigned BIT NOT NULL,
    completed BIT NOT NULL,
    pass_fail_grade BIT NOT NULL,
    assigned_student_am VARCHAR(20),
    title VARCHAR(255),
    description VARCHAR(255),
    skills VARCHAR(255),
    topics VARCHAR(255),
    student_log_book VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES companies(id),
    FOREIGN KEY (committee_member_id) REFERENCES committee_member(id),
    FOREIGN KEY (supervisor_id) REFERENCES professors(id),
    FOREIGN KEY (student_id) REFERENCES student(id)
) ENGINE=InnoDB;

CREATE TABLE evaluation (
    id INT NOT NULL AUTO_INCREMENT,
    traineeship_position_id INT,
    effectiveness INT NOT NULL,
    efficiency INT NOT NULL,
    facilities INT NOT NULL,
    guidance INT NOT NULL,
    motivation INT NOT NULL,
    evaluation_type ENUM('PROFESSOR', 'COMPANY', 'COMMITTEE_MEMBER'),
    PRIMARY KEY (id),
    FOREIGN KEY (traineeship_position_id) REFERENCES traineeship_position(id)
) ENGINE=InnoDB;


CREATE TABLE position_applications (
    position_id INT NOT NULL,
    student_id INT NOT NULL,
    FOREIGN KEY (position_id) REFERENCES traineeship_position(id),
    FOREIGN KEY (student_id) REFERENCES student(id)
) ENGINE=InnoDB;

ALTER TABLE student
ADD CONSTRAINT fk_assigned_traineeship
FOREIGN KEY (assigned_traineeship_id) REFERENCES traineeship_position(id);

alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
alter table traineeship_position add constraint FK4btgtp1sd36rby2vgb71b281t foreign key (student_id) references student (id);
