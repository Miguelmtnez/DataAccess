import java.util.ArrayList;

class Student {
    private String name;
    private int grade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        if (grade >= 0 && grade <= 10) {
            this.grade = grade;
        }
    }

    public boolean isPassed() {
        return grade >= 5;
    }
}

class Students {
    private ArrayList<Student> students = new ArrayList<>();

    // Adds a new student to the list
    public void add(Student student) {
        students.add(student);
    }

    // Returns the student at position num
    public Student get(int num) {
        if (num >= 0 && num < students.size()) {
            return students.get(num);
        }
        return null;
    }

    // Returns the average grade of the students
    public float getAverage() {
        if (students.isEmpty()) {
            return 0;
        } else {
            float average = 0;
            for (Student student : students) {
                average += student.getGrade();
            }
            return average / students.size();
        }
    }
}
