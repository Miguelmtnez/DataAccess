# VTSchool - Enrollment Functionality

## ✅ Implemented for Evaluation: 23/11/2025

### 📋 `--enroll` Option

Enrolls an existing student in a course for a specific year.

#### Syntax
```bash
java -jar vtschool.jar --enroll <idcard> <course_code> <year>
```

#### Parameters
- `<idcard>`: Student's ID card (8 digits)
- `<course_code>`: Course code (1 = DAM, 2 = DAW)
- `<year>`: Enrollment year (e.g., 2023, 2024, 2025)

#### Examples
```bash
# Enroll student 12332001 in DAM (course 1) for year 2025
java -jar vtschool.jar --enroll 12332001 1 2025

# Enroll student 12332005 in DAW (course 2) for year 2025
java -jar vtschool.jar --enroll 12332005 2 2025
```

### 🔄 Business Logic

#### First Time Enrollment (Year 1)
When a student enrolls in a course for the first time:
- All **year 1 subjects** are registered
- All scores are set to `NULL` (not graded yet)
- Creates one enrollment record
- Creates score records for all year 1 subjects

#### Subsequent Enrollments (Year 2+)
When a student enrolls in subsequent years:
- All **year 2 subjects** are added
- All **failed year 1 subjects** (score < 5 or NULL) are added
- Only subjects not yet passed are registered
- Cannot enroll if course is already completed

### ✅ Validations

The system validates:
1. ✅ Student exists in database
2. ✅ Course exists in database
3. ✅ Student not already enrolled for that specific year
4. ✅ Student hasn't completed the course (all subjects passed)
5. ✅ Valid course code and year format

### 🚫 Error Messages

| Error | Message |
|-------|---------|
| Student not found | `Error: Student with ID card X not found` |
| Course not found | `Error: Course with code X not found` |
| Already enrolled | `Error: Student is already enrolled in this course for year X` |
| Course completed | `Error: Student has already completed this course and cannot enroll again` |
| Missing arguments | `Error: Missing arguments` |
| Invalid format | `Error: Course code and year must be valid integers` |

### 📊 Database Schema

#### Affected Tables
- `students` - Validates student exists
- `courses` - Validates course exists
- `enrollments` - Creates new enrollment record
- `scores` - Creates score records for subjects
- `subjects` - Gets subjects for the course
- `subject_courses` - Links subjects to courses

### 🔧 Stored Functions (MANDATORY)

Two stored functions are **required** and must be created in PostgreSQL:

#### 1. `asignaturas_aprobadas_[INITIALS]_2526`
Returns all subjects a student has passed (score ≥ 5) in a course.

#### 2. `asignaturas_pendientes_[INITIALS]_2526`
Returns all subjects a student still needs to pass in a course.

**Location:** See `stored_functions.txt` in project root for SQL code.

**Important:** 
- Replace `xx` in function names with YOUR INITIALS
- Example: `asignaturas_aprobadas_jrgs_2526`
- Both functions are MANDATORY for evaluation

### 📝 Testing Scenarios

#### Test Case 1: First Enrollment
```bash
# Student 12332007 enrolls in DAM for first time
java -jar vtschool.jar --enroll 12332007 1 2025

Expected: 5 year 1 subjects registered with NULL scores
```

#### Test Case 2: Second Enrollment (All Year 1 Passed)
```bash
# Student who passed all year 1 subjects enrolls for year 2
java -jar vtschool.jar --enroll 12332001 1 2025

Expected: Year 2 subjects only (Data Access, Services and Processes)
```

#### Test Case 3: Second Enrollment (Some Year 1 Failed)
```bash
# Student who failed some year 1 subjects enrolls for year 2
java -jar vtschool.jar --enroll 12332003 1 2025

Expected: Year 2 subjects + failed year 1 subjects
```

#### Test Case 4: Already Enrolled
```bash
# Try to enroll student twice in same course/year
java -jar vtschool.jar --enroll 12332001 1 2024

Expected: Error message - already enrolled
```

#### Test Case 5: Course Completed
```bash
# Student who passed ALL subjects tries to enroll again
# (After completing both year 1 and year 2 with passing grades)

Expected: Error message - course already completed
```

### 🏗️ Architecture

#### MVC Pattern
```
Main.java (Controller)
    ↓
EnrollmentService.java (Service/Business Logic)
    ↓
EnrollmentDAO, CourseDAO, SubjectDAO, ScoreDAO (Data Access)
    ↓
Student, Course, Subject, Enrollment, Score (Models/Entities)
```

#### Transaction Management
- All enrollment operations (enrollment + scores) are executed in a **single transaction**
- **All-or-nothing**: If any part fails, entire operation rolls back
- Ensures data consistency

### 🎯 Evaluation Criteria (23/11/2025)

- ✅ Correctly identifies first vs subsequent enrollments
- ✅ Registers appropriate subjects for each case
- ✅ Validates student and course existence
- ✅ Prevents duplicate enrollments
- ✅ Prevents enrollment in completed courses
- ✅ Creates scores with NULL values
- ✅ Transaction management (all-or-nothing)
- ✅ Proper error messages
- ✅ Stored functions included in `stored_functions.txt`
- ✅ Stored functions use correct naming convention with initials

### 📚 Next Steps (30/11/2025)

After enrollment is working, the following features will be implemented:
- `--qualify` - Enter scores for enrolled subjects
- `--print` - Display student results and transcript
