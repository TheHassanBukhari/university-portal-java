package main;

import java.util.ArrayList;
import java.util.Scanner;
import models.*;
import interfaces.*;
import util.RecordList;
import util.DataStore;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static RecordList<Student> students = new RecordList<>();
    static RecordList<Course> courses = new RecordList<>();
    static DataStore<Student> studentStore = new DataStore<>();
    static DataStore<Course> courseStore = new DataStore<>();

    public static void main(String[] args) {
        loadData(); // Load existing data at line 54

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("-------------------------");
            System.out.println("||  UNIVERSITY PORTAL  ||");
            System.out.println("-------------------------");
            System.out.println("1. Student Login");
            System.out.println("2. Instructor Login");
            System.out.println("3. Admin Panel");
            System.out.println("4. View System Stats");
            System.out.println("0. Exit & Save");
            System.out.print("Select: ");
            int a = sc.nextInt();
            sc.nextLine();

            switch (a) {
                case 0 -> {
                    saveData(); // save data at line 70
                    running = false;
                }
                case 1 -> studentLogin(); // line 390
                case 2 -> instructorLogin(); // line 492
                case 3 -> adminPanel(); // line 78
                case 4 -> viewSystemStats(); // line 360
                default -> System.out.println("Invalid input! Try again.");
            }
        }

        System.out.println("Thank you for using our service. Goodbye!");
    }

    // DATA HANDLING SECTION =========================================================================

    public static void loadData() {
        System.out.println("Loading data from files...");
        ArrayList<Student> loadedStudents = (ArrayList<Student>) studentStore.loadFromFile("data//students.dat");
        if (loadedStudents != null) {
            for (Student s : loadedStudents) {
                students.add(s);
            }
        }

        ArrayList<Course> loadedCourses = (ArrayList<Course>) courseStore.loadFromFile("data//courses.dat");
        if (loadedCourses != null) {
            for (Course c : loadedCourses) {
                courses.add(c);
            }
        }
    }

    public static void saveData() {
        System.out.println("Saving data to files...");
        studentStore.saveToFile("data//students.dat", students.getAll());
        courseStore.saveToFile("data//courses.dat", courses.getAll());
    }

    // ADMIN PANEL =========================================================================


    public static void adminPanel() {
        System.out.print("Enter admin password: ");
        String pwd = sc.nextLine();
        if (!pwd.equals("admin123")) {
            System.out.println("Access denied!");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("-------------------");
            System.out.println("||  ADMIN PANEL  ||");
            System.out.println("--------------------");
            System.out.println("1. Add New Student");
            System.out.println("2. Add New Course");
            System.out.println("3. View All Students");
            System.out.println("4. View All Courses");
            System.out.println("5. Assign Course to Student");
            System.out.println("6. Remove Student");
            System.out.println("7. Remove Course");
            System.out.println("8. View Student Complaints");
            System.out.println("9. Save Data Now");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addNewStudent(); // Line 122
                case 2 -> addNewCourse(); // Line 157
                case 3 -> viewAllStudents(); // Line 195
                case 4 -> viewAllCourses(); // Line 208
                case 5 -> assignCourseToStudent(); // Line 222
                case 6 -> removeStudent(); // Line 271
                case 7 -> removeCourse(); // Line 293
                case 8 -> viewAllComplaints(); // Line 334
                case 9 -> saveData(); // Line 70
                case 0 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public static void addNewStudent() {
        System.out.print("Enter student ID: ");
        String id = sc.nextLine();
        
        // Check if ID already exists
        if (findStudentById(id) != null) { // Line 483
            System.out.println("Student ID already exists!");
            return;
        }

        System.out.print("Enter student name: ");
        String name = sc.nextLine();
        
        System.out.print("Enter student password: ");
        String password = sc.nextLine();
        
        System.out.print("Select program (1-Science, 2-Arts, 3-Engineering): ");
        int prog = sc.nextInt();
        sc.nextLine();

        Student newStudent;
        switch (prog) {
            case 1 -> newStudent = new ScienceStudent(id, name, password);
            case 2 -> newStudent = new ArtsStudent(id, name, password);
            case 3 -> newStudent = new EngineeringStudent(id, name, password);
            default -> {
                System.out.println("Invalid program!");
                return;
            }
        }

        students.add(newStudent);
        System.out.println("Student added successfully! Total students: " + Student.totalStudents);
    }

    public static void addNewCourse() {
        System.out.print("Enter course code: ");
        String code = sc.nextLine();
        
        // Check if course already exists
        for (Course c : courses.getAll()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                System.out.println("Course code already exists!");
                return;
            }
        }

        System.out.print("Enter course title: ");
        String title = sc.nextLine();
        
        System.out.print("Enter credit hours: ");
        int hours = sc.nextInt();
        sc.nextLine();

        System.out.println("\nINSTRUCTOR DETAILS.");

        System.out.print("Enter instructor name: ");
        String insName = sc.nextLine();
        
        System.out.print("Enter instructor qualification: ");
        String qual = sc.nextLine();
        
        System.out.print("Enter instructor ID: ");
        String insId = sc.nextLine();
        
        System.out.print("Enter instructor password: ");
        String insPwd = sc.nextLine();

        Course newCourse = new Course(code, title, hours, insName, qual, insId, insPwd);
        courses.add(newCourse);
        System.out.println("Course added successfully! Total courses: " + Course.totalCourses);
    }

    public static void viewAllStudents() {
        System.out.println("\nALL STUDENTS.");
        if (students.getAll().isEmpty()) {
            System.out.println("No students registered.");
            return;
        }

        for (Student s : students.getAll()) {
            System.out.println("ID: " + s.getStudentId() + " | Name: " + s.getName() + 
                             " | Program: " + s.getProgram());
        }
    }

    public static void viewAllCourses() {
        System.out.println("\nALL COURSES.");
        if (courses.getAll().isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        for (Course c : courses.getAll()) {
            System.out.println("Code: " + c.getCourseCode() + " | Title: " + c.getTitle() + 
                             " | Credits: " + c.getCreditHours() + 
                             " | Instructor: " + c.getCourseInstructor().getName());
        }
    }

    public static void assignCourseToStudent() {
        System.out.print("Enter student ID: ");
        String stuId = sc.nextLine();
        Student student = findStudentById(stuId); // Line 483  
        
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.print("Enter course code: ");
        String courseCode = sc.nextLine();
        Course course = null;
        
        for (Course c : courses.getAll()) {
            if (c.getCourseCode().equalsIgnoreCase(courseCode)) {
                course = c;
                break;
            }
        }

        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        // Check if already enrolled
        for (ResultEntry r : student.getTranscript().getResults()) {
            if (r.getCourse().getCourseCode().equalsIgnoreCase(courseCode)) {
                System.out.println("Student already enrolled in this course!");
                return;
            }
        }

        System.out.print("Enter initial marks (0-100): ");
        double marks = sc.nextDouble();
        sc.nextLine();
        
        System.out.print("Enter initial attendance (0-30): ");
        int attendance = sc.nextInt();
        sc.nextLine();

        ResultEntry result = new ResultEntry(course, marks);
        result.setAttendance(attendance);
        student.getTranscript().addResultEntry(result);
        
        System.out.println("Course assigned successfully!");
    }

    public static void removeStudent() {
        System.out.print("Enter student ID to remove: ");
        String id = sc.nextLine();
        
        Student student = findStudentById(id); // Line 483
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.print("Are you sure? (Y/N): ");
        String confirm = sc.nextLine();
        
        if (confirm.equalsIgnoreCase("Y")) {
            students.remove(id);
            Student.totalStudents--;
            System.out.println("Student removed successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    public static void removeCourse() {
        System.out.print("Enter course code to remove: ");
        String code = sc.nextLine();
        
        boolean found = false;
        for (Course c : courses.getAll()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Course not found!");
            return;
        }

        System.out.print("Are you sure? This will remove course from all students. (Y/N): ");
        String confirm = sc.nextLine();
        
        if (confirm.equalsIgnoreCase("Y")) {
            courses.remove(code);
            Course.totalCourses--;
            
            // Remove course from all students' transcripts
            for (Student s : students.getAll()) {
                ArrayList<ResultEntry> toRemove = new ArrayList<>();
                for (ResultEntry r : s.getTranscript().getResults()) {
                    if (r.getCourse().getCourseCode().equalsIgnoreCase(code)) {
                        toRemove.add(r);
                    }
                }
                s.getTranscript().getResults().removeAll(toRemove); // method from Collection interface inherited by Arraylist
            }
            
            System.out.println("Course removed successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    public static void viewAllComplaints() {
        System.out.println();
        System.out.println("--------------------------------");
        System.out.println("||  ALL STUDENTS COMPLAINTS  ||");
        System.out.println("--------------------------------");
        boolean hasComplaints = false;
        
        for (Student s : students.getAll()) {
            if (!s.getComplaints().isEmpty()) {
                hasComplaints = true;
                System.out.println("\nStudent: " + s.getName() + " (" + s.getStudentId() + ")");
                int i = 1;
                for (String comp : s.getComplaints()) {
                    System.out.println("  " + i + ". " + comp);
                    i++;
                }
            }
        }
        
        if (!hasComplaints) {
            System.out.println("No complaints found.");
        }
    }

    // SYSTEM STATISTICS =========================================================================


    public static void viewSystemStats() {
        System.out.println();
        System.out.println("-------------------");
        System.out.println("||  SYSTEM STATISTICS  ||");
        System.out.println("--------------------");
        System.out.println("Total Students: " + Student.totalStudents);
        System.out.println("Total Courses: " + Course.totalCourses);
        System.out.println("Pass Marks: " + ResultCalculator.passMarks);
        
        if (!students.getAll().isEmpty()) {
            System.out.println();
            System.out.println("-------------------");
            System.out.println("||  STUDENT DISTRIBUTION  ||");
            System.out.println("--------------------");
            int science = 0;
            int arts = 0;
            int engineering = 0;

            for (Student s : students.getAll()) {
                switch (s.getProgram()) {
                    case "Science" -> science++;
                    case "Arts" -> arts++;
                    case "Engineering" -> engineering++;
                }
            }
            System.out.println("Science: " + science + " students");
            System.out.println("Arts: " + arts + " students");
            System.out.println("Engineering: " + engineering + " students");
        }
    }

    // STUDENT SECTION =========================================================================


    public static void studentLogin() {
        System.out.print("Enter your student ID: ");
        String id = sc.nextLine().trim();
        Student selectedStudent = findStudentById(id); // Line 483

        if (selectedStudent == null) {
            System.out.println("Could not find student with ID '" + id + "'");
            System.out.println("Ask admin to register this student.");
            return;
        }

        System.out.print("Enter password: ");
        String pwd = sc.nextLine();
        if (!selectedStudent.getPassword().equals(pwd)) {
            System.out.println("Password incorrect!");
            return;
        }

        studentDisplay(selectedStudent);
    }

    public static void studentDisplay(Student student) {
        System.out.println("Welcome " + student.getName());

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("---------------------");
            System.out.println("||  STUDENT PANEL  ||");
            System.out.println("----------------------");
            System.out.println("1. View profile");
            System.out.println("2. View results");
            System.out.println("3. Submit complaint");
            System.out.println("4. View past complaints");
            System.out.println("5. View enrolled courses");
            System.out.println("0. Logout");
            System.out.print("Select: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("Student name: " + student.getName());
                    System.out.println("Student ID: " + student.getStudentId());
                    System.out.println("Student program: " + student.getProgram());
                    System.out.printf("Student overall attendance: %.2f%%\n",
                            student.getTranscript().getAttendancePercentage());
                }
                case 2 -> {
                    student.displayResults();
                    System.out.print("View each course separately (Y/N): ");
                    String ans = sc.nextLine();
                    System.out.println("----------------------------");
                    if (ans.equalsIgnoreCase("y")) {
                        for (ResultEntry result : student.getTranscript().getResults()) {
                            System.out.println(result);
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Enter your complaint: ");
                    String complaint = sc.nextLine();
                    student.addComplaints(complaint);
                    System.out.println("Complaint submitted!");
                }
                case 4 -> {
                    if (student.getComplaints().isEmpty()) {
                        System.out.println("No complaints submitted yet.");
                    } else {
                        int i = 0;
                        for (String comp : student.getComplaints()) {
                            System.out.println((i + 1) + ". " + comp);
                            i++;
                        }
                    }
                }
                case 5 -> {
                    System.out.println("ENROLLED COURSES.");
                    for (ResultEntry r : student.getTranscript().getResults()) {
                        System.out.println("Course: " + r.getCourse().getTitle() + 
                                         " (" + r.getCourse().getCourseCode() + ")");
                    }
                }
                case 0 -> {
                    System.out.println("Logged out.");
                    running = false;
                }
                default -> {
                    System.out.println("Invalid input! Try again.");
                }
            }
        }
    }

    public static Student findStudentById(String id) {
        for (Student student : students.getAll()) {
            if (student.getStudentId().equalsIgnoreCase(id)){
                return student;
            }
        }
        return null;
    }

    // INSTRUCTOR SECTION =========================================================================


    public static void instructorLogin() {
        System.out.print("Enter your instructor ID: ");
        String id = sc.nextLine().trim();
        Course selectedCourse = findCourseByInstructorId(id);

        if (selectedCourse == null) {
            System.out.println("Could not find instructor with ID '" + id + "'");
            System.out.println("Ask admin to register this instructor.");
            return;
        }

        System.out.print("Enter password: ");
        String pwd = sc.nextLine();
        if (!selectedCourse.getCourseInstructor().getPassword().equals(pwd)) {
            System.out.println("Password incorrect!");
            return;
        }

        instructorDisplay(selectedCourse);
    }

    public static void instructorDisplay(Course course) {
        System.out.println("Welcome " + course.getCourseInstructor().getName());

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("||  INSTRUCTOR PANEL  ||");
            System.out.println("------------------------");
            System.out.println("1. View profile");
            System.out.println("2. View Course Details");
            System.out.println("3. Address Students");
            System.out.println("0. Logout");
            System.out.print("Select: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("Instructor name: " + course.getCourseInstructor().getName());
                    System.out.println("Instructor ID: " + course.getCourseInstructor().getInstructorId());
                    System.out.println("Instructor qualification: " + course.getCourseInstructor().getQualification());
                }
                case 2 -> course.displayCourseDetails();
                case 3 -> addressStudents(course);
                case 0 -> {
                    System.out.println("Logged out.");
                    running = false;
                }
                default -> System.out.println("Invalid input! Try again.");
            }
        }
    }

    public static Course findCourseByInstructorId(String id) {
        for (Course course : courses.getAll()) {
            if (course.getCourseInstructor().getInstructorId().equalsIgnoreCase(id)){
                return course;
            }
        }
        return null;
    }



    public static void addressStudents(Course course) {
        ArrayList<Student> myStudents = getStudentsInCourse(course);

        if (myStudents.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("||  MANAGE STUDENTS  ||");
            System.out.println("------------------------");
            System.out.println("1. View low attendance students");
            System.out.println("2. View failed students");
            System.out.println("3. View passed students");
            System.out.println("4. View all students");
            System.out.println("0. Return");
            System.out.print("Select: ");
            int choice = sc.nextInt();
            sc.nextLine();

            ArrayList<Student> filteredStudents = new ArrayList<>();

            switch (choice) {
                case 0 -> running = false;
                case 1 -> {
                    filteredStudents = getLowAttendanceStudents(myStudents, course);
                    System.out.println("Low attendance students (<70%):");
                }
                case 2 -> {
                    filteredStudents = getFailedStudents(myStudents, course);
                    System.out.println("Failed students (<50 marks):");
                }
                case 3 -> {
                    filteredStudents = getPassedStudents(myStudents, course);
                    System.out.println("Passed students:");
                }
                case 4 -> {
                    filteredStudents.addAll(myStudents);
                    System.out.println("All students:");
                }
                default -> {
                    System.out.println("Invalid input! Try again.");
                    continue;
                }
            }

            displayStudents(filteredStudents, course);
            updateStudentMarksOrAttendance(filteredStudents, course);
            System.out.println();
        }
    }


    // HELPER METHODS =========================================================================

    public static ArrayList<Student> getStudentsInCourse(Course course) {
        ArrayList<Student> list = new ArrayList<>();
        for (Student student : students.getAll()) {
            for (ResultEntry r : student.getTranscript().getResults()) {
                if (r.getCourse().getCourseCode().equals(course.getCourseCode())) {
                    list.add(student);
                    break;
                }
            }
        }
        return list;
    }

    public static ArrayList<Student> getLowAttendanceStudents(ArrayList<Student> students, Course course) {
        ArrayList<Student> list = new ArrayList<>();
        for (Student student : students) {
            ResultEntry r = getResultEntryForCourse(student, course);
            if (r != null && r.getAttendancePercentage() < 70) list.add(student);
        }
        return list;
    }

    public static ArrayList<Student> getFailedStudents(ArrayList<Student> students, Course course) {
        ArrayList<Student> list = new ArrayList<>();
        for (Student student : students) {
            ResultEntry r = getResultEntryForCourse(student, course);
            if (r != null && (r.getMarksObtained() < 50 || r.getCourseGPA() == 0)) list.add(student);
        }
        return list;
    }

    public static ArrayList<Student> getPassedStudents(ArrayList<Student> students, Course course) {
        ArrayList<Student> list = new ArrayList<>();
        for (Student student : students) {
            ResultEntry r = getResultEntryForCourse(student, course);
            if (r != null && r.getMarksObtained() >= 50 && r.getAttendancePercentage() >= 70) list.add(student);
        }
        return list;
    }

    public static ResultEntry getResultEntryForCourse(Student student, Course course) {
        for (ResultEntry r : student.getTranscript().getResults()) {
            if (r.getCourse().getCourseCode().equals(course.getCourseCode())) return r;
        }
        return null;
    }

    public static void displayStudents(ArrayList<Student> students, Course course) {
        for (Student student : students) {
            System.out.println("---------------------------------");
            System.out.println("ID: " + student.getStudentId());
            System.out.println("Name: " + student.getName());
            System.out.println("Program: " + student.getProgram());
            ResultEntry r = getResultEntryForCourse(student, course);
            if (r != null) {
                System.out.printf("Marks: %.2f/100, GPA: %.2f\n", r.getMarksObtained(), r.getCourseGPA());
                System.out.printf("Attendance: %d/%d (%.2f%%)\n",
                        r.getAttendance(), ResultEntry.totalAttendance, r.getAttendancePercentage());
            }
        }
    }

    public static void updateStudentMarksOrAttendance(ArrayList<Student> students, Course course) {
        System.out.print("\nEnter student ID to update (or press Enter to skip): ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) return;

        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(id)) {
                ResultEntry r = getResultEntryForCourse(student, course);
                if (r == null) break;

                System.out.println("1. Update Marks");
                System.out.println("2. Update Attendance");
                System.out.print("Select: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter new marks (0-100): ");
                        double newMarks = sc.nextDouble();
                        sc.nextLine();
                        r.setMarksObtained(newMarks);
                        System.out.println("Marks updated.");
                    }
                    case 2 -> {
                        System.out.print("Enter new attendance (0-30): ");
                        int newAttendance = sc.nextInt();
                        sc.nextLine();
                        r.setAttendance(newAttendance);
                        System.out.println("Attendance updated.");
                    }
                    default -> System.out.println("Invalid selection.");
                }
                return;
            }
        }
        System.out.println("Student not found in this course.");
    }
}