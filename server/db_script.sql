DROP DATABASE studybuddies;
CREATE DATABASE studybuddies;
USE studybuddies;

CREATE TABLE StudyProgram (SPID int not null auto_increment PRIMARY KEY,
                           Name varchar(50) not null);

CREATE TABLE Student (Fullname varchar(75) not null PRIMARY KEY,
                      Semester int not null,
                      StudyProgramID int not null,
                      Description varchar(75),
                      Phone varchar(20),
                      FOREIGN KEY (StudyProgramID) REFERENCES StudyProgram(SPID));

CREATE TABLE Campus (CPID int not null auto_increment PRIMARY KEY,
                           Name varchar(50) not null);

CREATE TABLE MeetingPoint (MPID int not null auto_increment PRIMARY KEY,
                           CPID int not null,
                           Name varchar(50) not null,
                           FOREIGN KEY (CPID) REFERENCES Campus(CPID));

CREATE TABLE Course (CID int not null auto_increment PRIMARY KEY,
                     Name varchar(50) not null,
                     Shortcut varchar(5) not null);

CREATE TABLE Learngroup (LID int not null auto_increment PRIMARY KEY,
                         Creator varchar(75) not null,
                         CID int not null,
                         MeetingtimeFrom TIMESTAMP,
                         MeetingtimeTo TIMESTAMP,
                         MeetingPointID int not null,
                         Title varchar(50),
                         Description varchar(150),
                         MaxStudentCount int,
                         FOREIGN KEY (MeetingpointID) REFERENCES MeetingPoint(MPID),
                         FOREIGN KEY (Creator) REFERENCES Student(Fullname),
                         FOREIGN KEY (CID) REFERENCES Course(CID));

CREATE TABLE StudentLearngroup (Fullname varchar(75) not null,
                           LID int not null,
                           FOREIGN KEY (Fullname) REFERENCES Student(Fullname),
                           FOREIGN KEY (LID) REFERENCES Learngroup(LID),
                           PRIMARY KEY (Fullname,LID));

# There are courses which are in multiple StudyPrograms
CREATE TABLE StudyProgramCourse (SPID int not null,
                           CID int not null,
                           FOREIGN KEY (CID) REFERENCES Course(CID),
                           FOREIGN KEY (SPID) REFERENCES StudyProgram(SPID),
                           PRIMARY KEY (SPID,CID));

DELIMITER //
CREATE TRIGGER trig_MaxStudentCount BEFORE INSERT ON StudentLearngroup
FOR EACH ROW
BEGIN
  DECLARE maxCount,currentCount INT;

  SELECT MaxStudentCount FROM Learngroup WHERE LID = NEW.LID INTO maxCount;
  SELECT Count(*) FROM StudentLearngroup WHERE LID = NEW.LID INTO currentCount;

  IF (currentCount = maxCount)
  THEN
    signal sqlstate '45000' set message_text = 'Max Student limit reached';
  END IF;

END //
DELIMITER ;


INSERT INTO Campus VALUES (1,"Campus Gummersbach");
INSERT INTO Campus VALUES (2,"Campus Deutz");

#Meetingpoints for Campus Gummersbach
INSERT INTO MeetingPoint VALUES ("",1,"Container");
INSERT INTO MeetingPoint VALUES ("",1,"Bibliothek");
INSERT INTO MeetingPoint VALUES ("",1,"Eingang Mensa");
INSERT INTO MeetingPoint VALUES ("",1,"Eingang Ferchau Gebäude");
INSERT INTO MeetingPoint VALUES ("",1,"Lernraum 2105");

#Meetingpoints for Campus Deutz
INSERT INTO MeetingPoint VALUES ("",2,"Bibliothek");
INSERT INTO MeetingPoint VALUES ("",2,"Eingang Mensa");
INSERT INTO MeetingPoint VALUES ("",2,"Lernraum 1303");

INSERT INTO StudyProgram VALUES (1,"Informatik");
INSERT INTO StudyProgram VALUES (2,"Medieninformatik");
INSERT INTO StudyProgram VALUES (3,"Wirtschaftsinformatik");

INSERT INTO Course VALUES (1,"Algorithmen der Programmierung I", "AP1");
INSERT INTO Course VALUES (2,"Algorithmik", "ALG");
INSERT INTO Course VALUES (3,"Computergrafik und Animation", "CGA");
INSERT INTO Course VALUES (4,"Produktion und Logistik", "PuL");

INSERT INTO StudyProgramCourse VALUES (1,1); # Informatik -> AP1
INSERT INTO StudyProgramCourse VALUES (2,1); # Medieninformatik -> AP1
INSERT INTO StudyProgramCourse VALUES (3,1); # Wirtschaftsinformatik -> AP1

INSERT INTO StudyProgramCourse VALUES (1,2); # Informatik -> ALG
INSERT INTO StudyProgramCourse VALUES (2,3); # Medieninformatik -> CGA
INSERT INTO StudyProgramCourse VALUES (3,4); # Wirtschaftsinformatik -> PuL

INSERT INTO Student VALUES ("Kalaman",5,1,"Bald haben wir es geschafft. Nur noch 2 Wochen :)","0173123456789");
INSERT INTO Student VALUES ("Maclachlan",5,1,"Hat jemand noch Probeklausuren?","0172987654321");

INSERT INTO Learngroup VALUES (1,"Maclachlan",2,STR_TO_DATE('2018-04-12 11:30:00','%Y-%m-%d %H:%i:%s'),STR_TO_DATE('2018-04-12 14:30:00','%Y-%m-%d %H:%i:%s'),2,"Lust auf Algorithmik ?","Hey Leute, ich suche noch 3 fleißige Studenten die Lust haben mit mir Algorithmik zu lernen. Wäre gut wenn ihr mindestens im 3. Semester seid",4);
INSERT INTO Learngroup VALUES (2,"Kalaman",1,STR_TO_DATE('2018-04-12 09:30:00','%Y-%m-%d %H:%i:%s'),STR_TO_DATE('2018-04-12 12:00:00','%Y-%m-%d %H:%i:%s'),6,"AP1 durchlernen","Ihr müsst auch für AP1 lernen ? Dann seid ihr in dieser Gruppe richtig. Wir haben vor mind 6 Stunden am Tag zu lernen! Ich freue mich auf euch",6);

INSERT INTO StudentLearngroup VALUES ("Maclachlan",1);
INSERT INTO StudentLearngroup VALUES ("Kalaman",2);

SELECT * FROM StudyProgram;
SELECT * FROM Course;
SELECT * FROM Campus;
SELECT * FROM MeetingPoint;
SELECT * FROM StudyProgramCourse;
SELECT * FROM Student;
