SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';
SET default_table_access_method = heap;

CREATE SCHEMA IF NOT EXISTS _da_vtschool_2526;

SET search_path=_da_vtschool_2526;

CREATE TABLE subjects (
    code integer NOT NULL,
    name character varying(50),
    year integer,
    hours integer
);

ALTER TABLE subjects OWNER TO postgres;

CREATE SEQUENCE subjects_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

ALTER TABLE subjects_code_seq OWNER TO postgres;
ALTER SEQUENCE subjects_code_seq OWNED BY subjects.code;

CREATE TABLE courses (
    code integer NOT NULL,
    name character varying(90) NOT NULL
);

ALTER TABLE courses OWNER TO postgres;

CREATE SEQUENCE courses_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

ALTER TABLE courses_code_seq OWNER TO postgres;
ALTER SEQUENCE courses_code_seq OWNED BY courses.code;

CREATE SEQUENCE subject_courses_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE subject_courses (
    code integer NOT NULL DEFAULT nextval('subject_courses_code_seq'::regclass),
    subject_id integer NOT NULL,
    course_id integer NOT NULL
);

CREATE TABLE enrollments (
    student character varying(12) NOT NULL,
    course integer NOT NULL,
    year integer NOT NULL, 
    code integer NOT NULL
);

ALTER TABLE enrollments OWNER TO postgres;

CREATE SEQUENCE inscriptions_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE inscriptions_code_seq OWNER TO postgres;
ALTER SEQUENCE inscriptions_code_seq OWNED BY enrollments.code;

CREATE TABLE scores (
    enrollment_id integer NOT NULL,
    subject_id integer NOT NULL,
    score integer,
    code integer NOT NULL
);

ALTER TABLE scores OWNER TO postgres;

CREATE SEQUENCE scores_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE scores_code_seq OWNER TO postgres;
ALTER SEQUENCE scores_code_seq OWNED BY scores.code;

CREATE TABLE students (
    firstname character varying(50) NOT NULL,
    lastname character varying(100) NOT NULL,
    idcard character varying(8) NOT NULL,
    phone character varying(12),
    email character varying(100)
);

ALTER TABLE students OWNER TO postgres;

ALTER TABLE ONLY courses ALTER COLUMN code SET DEFAULT nextval('courses_code_seq'::regclass);
ALTER TABLE ONLY enrollments ALTER COLUMN code SET DEFAULT nextval('inscriptions_code_seq'::regclass);
ALTER TABLE ONLY subjects ALTER COLUMN code SET DEFAULT nextval('subjects_code_seq'::regclass);
ALTER TABLE ONLY scores ALTER COLUMN code SET DEFAULT nextval('scores_code_seq'::regclass);

INSERT INTO courses (name) VALUES ('Multiplatform app development');
INSERT INTO courses (name) VALUES ('Web development');

INSERT INTO subjects (name, year, hours) VALUES ('Data Access', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Database Management Systems', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Services and Processes', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Technical English', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Development Environments', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Markup Languages', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Programming', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Client-side development', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Server-side development', 2, NULL);

INSERT INTO subject_courses (subject_id, course_id) VALUES (2, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (4, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (5, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (6, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (7, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (1, 1);
INSERT INTO subject_courses (subject_id, course_id) VALUES (3, 1);

INSERT INTO subject_courses (subject_id, course_id) VALUES (2, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (5, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (6, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (7, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (4, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (8, 2);
INSERT INTO subject_courses (subject_id, course_id) VALUES (9, 2);

INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Aitana', 'Garcia', '12332003', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('John', 'Spencer', '12332004', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('John', 'Smith', '12332005', '654654654', 'johnsmith@email.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Marcos', 'Andreu', '12332006', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Student', 'X', '12332007', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Mark', 'Ross', '12332008', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Estrella', 'Garcia', '12332002', '', 'estrella@email.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Robe', 'Iniesta', '12332111', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Jose', 'Garcia', '12332001', '655565566', 'jrgarcia@mail.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Ken', 'Brockman', '12332321', '123456789', 'ken@e.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Kevin', 'Smith', '12332444', '123456789', '');

INSERT INTO enrollments (student, course, year) VALUES ('12332001', 1, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332003', 1, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332005', 2, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332004', 2, 2023);
INSERT INTO enrollments (student, course, year) VALUES ('12332001', 1, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332003', 2, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332005', 2, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332004', 1, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332006', 1, 2024);
INSERT INTO enrollments (student, course, year) VALUES ('12332111', 2, 2024);

INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 6, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 2, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 5, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 7, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 6, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 2, 10);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 5, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 7, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 2, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 5, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 6, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 7, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 4, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 6, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 7, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 2, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 5, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 3, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 5, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 7, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 1, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 8, 10);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 9, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 5, 10);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 7, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (7, 8, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (7, 9, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 4, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 6, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 1, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 3, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 6, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 2, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 5, 3);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 7, 4);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 4, 7);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 6, 9);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 2, 8);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 5, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 7, 6);
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 4, 7);

SELECT pg_catalog.setval('courses_code_seq', 2, true);
SELECT pg_catalog.setval('inscriptions_code_seq', 12, true);

ALTER TABLE ONLY subjects ADD CONSTRAINT pk_subjects PRIMARY KEY (code);
ALTER TABLE ONLY courses ADD CONSTRAINT pk_courses PRIMARY KEY (code);
ALTER TABLE ONLY enrollments ADD CONSTRAINT pk_inscriptions PRIMARY KEY (code);
ALTER TABLE ONLY scores ADD CONSTRAINT pk_scores PRIMARY KEY (code);
ALTER TABLE ONLY scores ADD CONSTRAINT ur_scores UNIQUE (enrollment_id, subject_id);
ALTER TABLE ONLY students ADD CONSTRAINT pk_students PRIMARY KEY (idcard);
ALTER TABLE ONLY subject_courses ADD CONSTRAINT ur_subject_courses UNIQUE (subject_id, course_id);
ALTER TABLE ONLY subject_courses ADD CONSTRAINT pk_subject_courses PRIMARY KEY (code);

ALTER TABLE ONLY enrollments ADD CONSTRAINT fk_course FOREIGN KEY (course) REFERENCES courses(code) NOT VALID;
ALTER TABLE ONLY scores ADD CONSTRAINT fk_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(code);
ALTER TABLE ONLY enrollments ADD CONSTRAINT fk_student FOREIGN KEY (student) REFERENCES students(idcard) NOT VALID;
ALTER TABLE ONLY scores ADD CONSTRAINT fk_subjects FOREIGN KEY (subject_id) REFERENCES subjects(code);
ALTER TABLE ONLY subject_courses ADD CONSTRAINT fk_subject_courses_course FOREIGN KEY (course_id) REFERENCES courses(code) NOT VALID;
ALTER TABLE ONLY subject_courses ADD CONSTRAINT fk_subject_courses_subject FOREIGN KEY (subject_id) REFERENCES subjects(code) NOT VALID;
