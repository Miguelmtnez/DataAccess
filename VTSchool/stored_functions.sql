-- Set search path
SET search_path TO _da_vtschool_2526;

-- Function to get passed subjects for a student (score >= 5)
CREATE OR REPLACE FUNCTION subjects_passed_mmr_2526(student_id_card VARCHAR)
RETURNS TABLE (
    subject_id INTEGER,
    subject_code VARCHAR,
    subject_name VARCHAR,
    score DECIMAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        sub.id,
        sub.code,
        sub.name,
        sc.score
    FROM students st
    JOIN enrollments e ON st.id_card = e.student_id
    JOIN scores sc ON e.id = sc.enrollment_id
    JOIN subjects sub ON sc.subject_id = sub.id
    WHERE st.id_card = student_id_card
      AND sc.score >= 5;
END;
$$ LANGUAGE plpgsql;

-- Function to get pending subjects for a student (not passed or not enrolled)
CREATE OR REPLACE FUNCTION subjects_pending_mmr_2526(student_id_card VARCHAR)
RETURNS TABLE (
    subject_id INTEGER,
    subject_code VARCHAR,
    subject_name VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        sub.id,
        sub.code,
        sub.name
    FROM students st
    JOIN enrollments e ON st.id_card = e.student_id
    JOIN subject_courses sc ON sc.course_id = e.course_id
    JOIN subjects sub ON sub.id = sc.subject_id
    WHERE st.id_card = student_id_card
      AND sub.id NOT IN (
          SELECT sc2.subject_id
          FROM enrollments e2
          JOIN scores sc2 ON e2.id = sc2.enrollment_id
          WHERE e2.student_id = student_id_card
            AND sc2.score >= 5
      );
END;
$$ LANGUAGE plpgsql;
