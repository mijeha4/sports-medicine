-- Create tables for sports medicine database

-- Athletes table
CREATE TABLE athletes (
    athlete_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE,
    sport_type VARCHAR(255),
    phone VARCHAR(255),
    registration_date DATE
);

-- Doctors table
CREATE TABLE doctors (
    doctor_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    specialization VARCHAR(255),
    license_number VARCHAR(255)
);

-- Examination types table
CREATE TABLE examinationtypes (
    type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(255),
    description TEXT
);

-- Medical examinations table
CREATE TABLE medicalexaminations (
    examination_id SERIAL PRIMARY KEY,
    athlete_id BIGINT REFERENCES athletes(athlete_id),
    doctor_id BIGINT REFERENCES doctors(doctor_id),
    type_id BIGINT REFERENCES examinationtypes(type_id),
    examination_date DATE,
    next_examination_date DATE,
    conclusion TEXT
);

-- Physio indicators table
CREATE TABLE physioindicators (
    indicator_id SERIAL PRIMARY KEY,
    examination_id BIGINT REFERENCES medicalexaminations(examination_id),
    indicator_name VARCHAR(255),
    measured_value DOUBLE PRECISION,
    unit VARCHAR(50),
    normal_min DOUBLE PRECISION,
    normal_max DOUBLE PRECISION
);

-- Recommendations table
CREATE TABLE recommendations (
    recommendation_id SERIAL PRIMARY KEY,
    examination_id BIGINT REFERENCES medicalexaminations(examination_id),
    recommendation_text TEXT,
    priority VARCHAR(50),
    status VARCHAR(50)
);
