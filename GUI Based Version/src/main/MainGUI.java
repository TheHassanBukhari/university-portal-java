package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import models.*;
import util.RecordList;
import util.DataStore;

public class MainGUI extends JFrame {
    // Data storage
    private RecordList<Student> students;
    private RecordList<Course> courses;
    private DataStore<Student> studentStore;
    private DataStore<Course> courseStore;
    
    // GUI Components
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Panels
    private JPanel loginPanel;
    private JPanel adminPanel;
    private JPanel studentPanel;
    private JPanel instructorPanel;
    
    // Current user
    private Student currentStudent;
    private Course currentInstructorCourse;
    
    public MainGUI() {
        // Initialize data
        students = new RecordList<>();
        courses = new RecordList<>();
        studentStore = new DataStore<>();
        courseStore = new DataStore<>();
        
        loadData();
        
        // Setup frame
        setTitle("University Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create card layout for switching panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create all panels
        createLoginPanel();
        createAdminPanel();
        createStudentPanel();
        createInstructorPanel();
        
        // Add panels to card layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(adminPanel, "ADMIN");
        mainPanel.add(studentPanel, "STUDENT");
        mainPanel.add(instructorPanel, "INSTRUCTOR");
        
        add(mainPanel);
        
        // Show login panel first
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    private void loadData() {
        // Load students
        ArrayList<Student> loadedStudents = (ArrayList<Student>) studentStore.loadFromFile("data/students.dat");
        if (loadedStudents != null) {
            for (Student s : loadedStudents) {
                students.add(s);
            }
            // UPDATE THE STATIC COUNTER
            Student.totalStudents = students.getAll().size();
        }
        
        // Load courses 
        ArrayList<Course> loadedCourses = (ArrayList<Course>) courseStore.loadFromFile("data/courses.dat");
        if (loadedCourses != null) {
            for (Course c : loadedCourses) {
                courses.add(c);
            }
            // UPDATE THE STATIC COUNTER
            Course.totalCourses = courses.getAll().size();
        }
    }
    
    private void saveData() {
        studentStore.saveToFile("data/students.dat", students.getAll());
        courseStore.saveToFile("data/courses.dat", courses.getAll());
    }
    
    // LOGIN PANEL
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("UNIVERSITY PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);
        
        JButton studentBtn = new JButton("Student Login");
        studentBtn.setPreferredSize(new Dimension(200, 50));
        studentBtn.addActionListener(e -> showStudentLoginDialog());
        
        JButton instructorBtn = new JButton("Instructor Login");
        instructorBtn.setPreferredSize(new Dimension(200, 50));
        instructorBtn.addActionListener(e -> showInstructorLoginDialog());
        
        JButton adminBtn = new JButton("Admin Login");
        adminBtn.setPreferredSize(new Dimension(200, 50));
        adminBtn.addActionListener(e -> showAdminLoginDialog());
        
        JButton exitBtn = new JButton("Exit & Save");
        exitBtn.setPreferredSize(new Dimension(200, 50));
        exitBtn.addActionListener(e -> {
            saveData();
            System.exit(0);
        });
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        loginPanel.add(studentBtn, gbc);
        
        gbc.gridy = 2;
        loginPanel.add(instructorBtn, gbc);
        
        gbc.gridy = 3;
        loginPanel.add(adminBtn, gbc);
        
        gbc.gridy = 4;
        loginPanel.add(exitBtn, gbc);
    }
    
    private void showStudentLoginDialog() {
        JTextField idField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Student Login", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String password = new String(passField.getPassword());
            
            // Find student
            Student student = null;
            for (Student s : students.getAll()) {
                if (s.getStudentId().equalsIgnoreCase(id) && s.getPassword().equals(password)) {
                    student = s;
                    break;
                }
            }
            
            if (student != null) {
                currentStudent = student;
                refreshStudentPanel();
                cardLayout.show(mainPanel, "STUDENT");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showInstructorLoginDialog() {
        JTextField idField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Instructor ID:"));
        panel.add(idField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Instructor Login", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String password = new String(passField.getPassword());
            
            // Find instructor course
            Course course = null;
            for (Course c : courses.getAll()) {
                if (c.getCourseInstructor().getInstructorId().equalsIgnoreCase(id) && 
                    c.getCourseInstructor().getPassword().equals(password)) {
                    course = c;
                    break;
                }
            }
            
            if (course != null) {
                currentInstructorCourse = course;
                refreshInstructorPanel();
                cardLayout.show(mainPanel, "INSTRUCTOR");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showAdminLoginDialog() {
        JPasswordField passField = new JPasswordField(15);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Admin Password:"));
        panel.add(passField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Admin Login", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passField.getPassword());
            
            if (password.equals("admin123")) {
                refreshAdminPanel();
                cardLayout.show(mainPanel, "ADMIN");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ADMIN PANEL
    private void createAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("ADMIN PANEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        adminPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel with buttons - Updated to include all removal functionalities
        JPanel buttonPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] buttonLabels = {
            "Add Student", "Remove Student",
            "Add Course", "Remove Course",
            "View All Students", "View All Courses",
            "Assign Course", "Remove Course from Student",
            "View Complaints", "System Stats",
            "Save Data", "Logout"
        };
        
        for (String label : buttonLabels) {
            JButton btn = new JButton(label);
            btn.addActionListener(e -> handleAdminButton(label));
            buttonPanel.add(btn);
        }
        
        adminPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void handleAdminButton(String action) {
        switch (action) {
            case "Add Student":
                addStudentGUI();
                break;
            case "Remove Student":
                removeStudentGUI();
                break;
            case "Add Course":
                addCourseGUI();
                break;
            case "Remove Course":
                removeCourseGUI();
                break;
            case "View All Students":
                viewAllStudentsGUI();
                break;
            case "View All Courses":
                viewAllCoursesGUI();
                break;
            case "Assign Course":
                assignCourseGUI();
                break;
            case "Remove Course from Student":
                removeCourseFromStudentGUI();
                break;
            case "View Complaints":
                viewComplaintsGUI();
                break;
            case "System Stats":
                showStatsGUI();
                break;
            case "Save Data":
                saveData();
                JOptionPane.showMessageDialog(this, "Data saved successfully!");
                break;
            case "Logout":
                cardLayout.show(mainPanel, "LOGIN");
                break;
        }
    }
    
    private void addStudentGUI() {
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JPasswordField passField = new JPasswordField(10);
        JComboBox<String> programCombo = new JComboBox<>(new String[]{"Science", "Arts", "Engineering"});
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(new JLabel("Program:"));
        panel.add(programCombo);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Student", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String password = new String(passField.getPassword());
            String program = (String) programCombo.getSelectedItem();
            
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check duplicate ID
            for (Student s : students.getAll()) {
                if (s.getStudentId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(this, "Student ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            Student newStudent;
            switch (program) {
                case "Science":
                    newStudent = new ScienceStudent(id, name, password);
                    break;
                case "Arts":
                    newStudent = new ArtsStudent(id, name, password);
                    break;
                case "Engineering":
                    newStudent = new EngineeringStudent(id, name, password);
                    break;
                default:
                    newStudent = new ScienceStudent(id, name, password);
            }
            
            students.add(newStudent);
            // Update static counter
            Student.totalStudents = students.getAll().size();
            JOptionPane.showMessageDialog(this, "Student added successfully!\nTotal Students: " + Student.totalStudents);
        }
    }
    
    private void removeStudentGUI() {
        if (students.getAll().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students to remove.", "Remove Student", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create an array of student options
        String[] studentOptions = new String[students.getAll().size()];
        for (int i = 0; i < students.getAll().size(); i++) {
            Student s = students.getAll().get(i);
            studentOptions[i] = s.getStudentId() + " - " + s.getName() + " (" + s.getProgram() + ")";
        }
        
        // Show dialog to select student to remove
        String selectedStudent = (String) JOptionPane.showInputDialog(this, 
            "Select Student to Remove:", "Remove Student", 
            JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);
        
        if (selectedStudent == null) return; // User cancelled
        
        // Extract student ID from selection
        String studentId = selectedStudent.split(" - ")[0];
        
        // Confirm removal
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove student: " + selectedStudent + "?\n\n" +
            "Warning: This will also remove all their course registrations, results, and complaints!",
            "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Find the student
            Student studentToRemove = null;
            for (Student s : students.getAll()) {
                if (s.getStudentId().equals(studentId)) {
                    studentToRemove = s;
                    break;
                }
            }
            
            if (studentToRemove != null) {
                // Remove the student
                students.getAll().remove(studentToRemove);
                
                // Update static counter
                Student.totalStudents = students.getAll().size();
                
                // Check if current logged in student is being removed
                if (currentStudent != null && currentStudent.getStudentId().equals(studentId)) {
                    currentStudent = null;
                    JOptionPane.showMessageDialog(this, 
                        "Student removed successfully!\n\n" +
                        "Note: This student was logged in and has been logged out.\n" +
                        "Total Students: " + Student.totalStudents,
                        "Student Removed", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Student removed successfully!\nTotal Students: " + Student.totalStudents,
                        "Student Removed", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Student not found in the system.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addCourseGUI() {
        JTextField codeField = new JTextField(10);
        JTextField titleField = new JTextField(10);
        JTextField creditsField = new JTextField(5);
        JTextField insNameField = new JTextField(10);
        JTextField insQualField = new JTextField(10);
        JTextField insIdField = new JTextField(10);
        JPasswordField insPassField = new JPasswordField(10);
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.add(new JLabel("Course Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Course Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Credit Hours:"));
        panel.add(creditsField);
        panel.add(new JLabel("Instructor Name:"));
        panel.add(insNameField);
        panel.add(new JLabel("Instructor Qualification:"));
        panel.add(insQualField);
        panel.add(new JLabel("Instructor ID:"));
        panel.add(insIdField);
        panel.add(new JLabel("Instructor Password:"));
        panel.add(insPassField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Course", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String code = codeField.getText().trim();
                String title = titleField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                String insName = insNameField.getText().trim();
                String insQual = insQualField.getText().trim();
                String insId = insIdField.getText().trim();
                String insPass = new String(insPassField.getPassword());
                
                // Check duplicate course code
                for (Course c : courses.getAll()) {
                    if (c.getCourseCode().equalsIgnoreCase(code)) {
                        JOptionPane.showMessageDialog(this, "Course code already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                Course newCourse = new Course(code, title, credits, insName, insQual, insId, insPass);
                courses.add(newCourse);
                // Update static counter
                Course.totalCourses = courses.getAll().size();
                JOptionPane.showMessageDialog(this, "Course added successfully!\nTotal Courses: " + Course.totalCourses);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid credit hours! Enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void removeCourseGUI() {
        if (courses.getAll().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses to remove.", "Remove Course", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create an array of course options
        String[] courseOptions = new String[courses.getAll().size()];
        for (int i = 0; i < courses.getAll().size(); i++) {
            Course c = courses.getAll().get(i);
            courseOptions[i] = c.getCourseCode() + " - " + c.getTitle() + " (" + c.getCourseInstructor().getName() + ")";
        }
        
        // Show dialog to select course to remove
        String selectedCourse = (String) JOptionPane.showInputDialog(this, 
            "Select Course to Remove:", "Remove Course", 
            JOptionPane.QUESTION_MESSAGE, null, courseOptions, courseOptions[0]);
        
        if (selectedCourse == null) return; // User cancelled
        
        // Extract course code from selection
        String courseCode = selectedCourse.split(" - ")[0];
        
        // Find the course
        Course courseToRemove = null;
        for (Course c : courses.getAll()) {
            if (c.getCourseCode().equals(courseCode)) {
                courseToRemove = c;
                break;
            }
        }
        
        if (courseToRemove == null) {
            JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if any students are enrolled in this course
        ArrayList<Student> enrolledStudents = getStudentsInCourse(courseToRemove);
        if (!enrolledStudents.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Warning: " + enrolledStudents.size() + " student(s) are enrolled in this course.\n" +
                "Removing it will also remove their results for this course.\n\n" +
                "Do you want to proceed?", 
                "Confirm Removal", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Remove course from all enrolled students' transcripts
            for (Student student : enrolledStudents) {
                removeCourseFromStudent(student, courseCode);
            }
        }
        
        // Check if the current instructor is logged in for this course
        if (currentInstructorCourse != null && currentInstructorCourse.getCourseCode().equals(courseCode)) {
            int confirmLogout = JOptionPane.showConfirmDialog(this,
                "Warning: The instructor for this course is currently logged in.\n" +
                "Removing the course will log them out.\n\n" +
                "Do you want to proceed?",
                "Instructor Logout Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmLogout != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Confirm removal
        int confirmFinal = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove course: " + selectedCourse + "?\n",
            "Final Confirmation", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmFinal == JOptionPane.YES_OPTION) {
            // Remove the course
            courses.getAll().remove(courseToRemove);
            
            // Update static counter
            Course.totalCourses = courses.getAll().size();
            
            // Logout instructor if they were using this course
            if (currentInstructorCourse != null && currentInstructorCourse.getCourseCode().equals(courseCode)) {
                currentInstructorCourse = null;
                cardLayout.show(mainPanel, "LOGIN");
                JOptionPane.showMessageDialog(this, 
                    "Course removed successfully!\n\n" +
                    "Note: The instructor was logged in and has been logged out.\n" +
                    "Total Courses: " + Course.totalCourses,
                    "Course Removed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Course removed successfully!\nTotal Courses: " + Course.totalCourses,
                    "Course Removed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void viewAllStudentsGUI() {
        if (students.getAll().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students registered.", "Students", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columns = {"ID", "Name", "Program"};
        Object[][] data = new Object[students.getAll().size()][3];
        
        for (int i = 0; i < students.getAll().size(); i++) {
            Student s = students.getAll().get(i);
            data[i][0] = s.getStudentId();
            data[i][1] = s.getName();
            data[i][2] = s.getProgram();
        }
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "All Students", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void viewAllCoursesGUI() {
        if (courses.getAll().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses available.", "Courses", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columns = {"Code", "Title", "Credits", "Instructor"};
        Object[][] data = new Object[courses.getAll().size()][4];
        
        for (int i = 0; i < courses.getAll().size(); i++) {
            Course c = courses.getAll().get(i);
            data[i][0] = c.getCourseCode();
            data[i][1] = c.getTitle();
            data[i][2] = c.getCreditHours();
            data[i][3] = c.getCourseInstructor().getName();
        }
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "All Courses", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void assignCourseGUI() {
        if (students.getAll().isEmpty() || courses.getAll().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Need students and courses first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Select student
        String[] studentOptions = new String[students.getAll().size()];
        for (int i = 0; i < students.getAll().size(); i++) {
            Student s = students.getAll().get(i);
            studentOptions[i] = s.getStudentId() + " - " + s.getName();
        }
        
        String selectedStudent = (String) JOptionPane.showInputDialog(this, 
            "Select Student:", "Assign Course", 
            JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);
        
        if (selectedStudent == null) return;
        
        String studentId = selectedStudent.split(" - ")[0];
        Student student = null;
        for (Student s : students.getAll()) {
            if (s.getStudentId().equals(studentId)) {
                student = s;
                break;
            }
        }
        
        // Select course
        String[] courseOptions = new String[courses.getAll().size()];
        for (int i = 0; i < courses.getAll().size(); i++) {
            Course c = courses.getAll().get(i);
            courseOptions[i] = c.getCourseCode() + " - " + c.getTitle();
        }
        
        String selectedCourse = (String) JOptionPane.showInputDialog(this, 
            "Select Course:", "Assign Course", 
            JOptionPane.QUESTION_MESSAGE, null, courseOptions, courseOptions[0]);
        
        if (selectedCourse == null) return;
        
        String courseCode = selectedCourse.split(" - ")[0];
        Course course = null;
        for (Course c : courses.getAll()) {
            if (c.getCourseCode().equals(courseCode)) {
                course = c;
                break;
            }
        }
        
        // Check if already enrolled
        for (ResultEntry r : student.getTranscript().getResults()) {
            if (r.getCourse().getCourseCode().equals(courseCode)) {
                JOptionPane.showMessageDialog(this, "Student already enrolled in this course!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Get marks and attendance
        JTextField marksField = new JTextField(5);
        JTextField attendanceField = new JTextField(5);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Initial Marks (0-100):"));
        panel.add(marksField);
        panel.add(new JLabel("Initial Attendance (0-30):"));
        panel.add(attendanceField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Initial Data", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double marks = Double.parseDouble(marksField.getText());
                int attendance = Integer.parseInt(attendanceField.getText());
                
                ResultEntry resultEntry = new ResultEntry(course, marks);
                resultEntry.setAttendance(attendance);
                student.getTranscript().addResultEntry(resultEntry);
                
                JOptionPane.showMessageDialog(this, "Course assigned successfully!");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid numbers entered!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // NEW FUNCTION: Remove Course from Student
    private void removeCourseFromStudentGUI() {
        if (students.getAll().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students registered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Select student
        String[] studentOptions = new String[students.getAll().size()];
        for (int i = 0; i < students.getAll().size(); i++) {
            Student s = students.getAll().get(i);
            studentOptions[i] = s.getStudentId() + " - " + s.getName();
        }
        
        String selectedStudent = (String) JOptionPane.showInputDialog(this, 
            "Select Student:", "Remove Course", 
            JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);
        
        if (selectedStudent == null) return;
        
        String studentId = selectedStudent.split(" - ")[0];
        Student student = null;
        for (Student s : students.getAll()) {
            if (s.getStudentId().equals(studentId)) {
                student = s;
                break;
            }
        }
        
        if (student == null) return;
        
        // Get student's enrolled courses
        if (student.getTranscript().getResults().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This student is not enrolled in any courses.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create list of enrolled courses
        ArrayList<ResultEntry> enrolledCourses = student.getTranscript().getResults();
        String[] courseOptions = new String[enrolledCourses.size()];
        for (int i = 0; i < enrolledCourses.size(); i++) {
            ResultEntry r = enrolledCourses.get(i);
            courseOptions[i] = r.getCourse().getCourseCode() + " - " + r.getCourse().getTitle() + 
                              " (Marks: " + String.format("%.2f", r.getMarksObtained()) + ")";
        }
        
        String selectedCourse = (String) JOptionPane.showInputDialog(this, 
            "Select Course to Remove:", "Remove Course", 
            JOptionPane.QUESTION_MESSAGE, null, courseOptions, courseOptions[0]);
        
        if (selectedCourse == null) return;
        
        String courseCode = selectedCourse.split(" - ")[0];
        
        // Confirm removal
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove course '" + courseCode + "' from student '" + student.getName() + "'?\n" +
            "This action cannot be undone.", 
            "Confirm Removal", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Find and remove the course
            boolean removed = false;
            ArrayList<ResultEntry> results = student.getTranscript().getResults();
            for (int i = 0; i < results.size(); i++) {
                ResultEntry r = results.get(i);
                if (r.getCourse().getCourseCode().equals(courseCode)) {
                    results.remove(i);
                    removed = true;
                    break;
                }
            }
            
            if (removed) {
                JOptionPane.showMessageDialog(this, 
                    "Course '" + courseCode + "' has been successfully removed from student '" + student.getName() + "'.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to remove course. Course not found.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewComplaintsGUI() {
        StringBuilder sb = new StringBuilder();
        boolean hasComplaints = false;
        
        for (Student s : students.getAll()) {
            if (!s.getComplaints().isEmpty()) {
                hasComplaints = true;
                sb.append("Student: ").append(s.getName()).append(" (").append(s.getStudentId()).append(")\n");
                for (String comp : s.getComplaints()) {
                    sb.append("  • ").append(comp).append("\n");
                }
                sb.append("\n");
            }
        }
        
        if (!hasComplaints) {
            sb.append("No complaints submitted.");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "All Complaints", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void showStatsGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("SYSTEM STATISTICS\n\n");
        sb.append("Total Students: ").append(Student.totalStudents).append("\n");
        sb.append("Total Courses: ").append(Course.totalCourses).append("\n");
        sb.append("Pass Marks: ").append(interfaces.ResultCalculator.passMarks).append("\n");
        
        if (!students.getAll().isEmpty()) {
            sb.append("\nPROGRAM DISTRIBUTION\n");
            int science = 0, arts = 0, engineering = 0;
            for (Student s : students.getAll()) {
                switch (s.getProgram()) {
                    case "Science": science++; break;
                    case "Arts": arts++; break;
                    case "Engineering": engineering++; break;
                }
            }
            sb.append("Science: ").append(science).append(" students\n");
            sb.append("Arts: ").append(arts).append(" students\n");
            sb.append("Engineering: ").append(engineering).append(" students\n");
        }
        
        JOptionPane.showMessageDialog(this, sb.toString(), "System Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void refreshAdminPanel() {
        // Refresh any admin data if needed
    }
    
    // STUDENT PANEL
    private void createStudentPanel() {
        studentPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("STUDENT PANEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        studentPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] buttonLabels = {
            "View Profile", "View Results", 
            "View Course Details", "Submit Complaint",
            "View My Complaints", "Logout"
        };
        
        for (String label : buttonLabels) {
            JButton btn = new JButton(label);
            btn.addActionListener(e -> handleStudentButton(label));
            buttonPanel.add(btn);
        }
        
        studentPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void handleStudentButton(String action) {
        if (currentStudent == null) return;
        
        switch (action) {
            case "View Profile":
                showStudentProfile();
                break;
            case "View Results":
                showStudentResults();
                break;
            case "View Course Details":
                showStudentCourseDetails();
                break;
            case "Submit Complaint":
                submitComplaint();
                break;
            case "View My Complaints":
                viewStudentComplaints();
                break;
            case "Logout":
                currentStudent = null;
                cardLayout.show(mainPanel, "LOGIN");
                break;
        }
    }
    
    private void showStudentProfile() {
        StringBuilder sb = new StringBuilder();
        sb.append("STUDENT PROFILE\n\n");
        sb.append("Name: ").append(currentStudent.getName()).append("\n");
        sb.append("ID: ").append(currentStudent.getStudentId()).append("\n");
        sb.append("Program: ").append(currentStudent.getProgram()).append("\n");
        sb.append(String.format("Overall Attendance: %.2f%%\n", currentStudent.getTranscript().getAttendancePercentage()));
        sb.append(String.format("Overall GPA: %.2f\n", currentStudent.calculateGPA()));
        sb.append(String.format("Overall Percentage: %.2f%%\n", currentStudent.calculatePercentage()));
        
        JOptionPane.showMessageDialog(this, sb.toString(), "My Profile", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showStudentResults() {
        if (currentStudent.getTranscript().getResults().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No course results available.", "My Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("MY RESULTS\n\n");
        sb.append(String.format("Overall GPA: %.2f\n", currentStudent.calculateGPA()));
        sb.append(String.format("Overall Percentage: %.2f%%\n\n", currentStudent.calculatePercentage()));
        
        for (ResultEntry r : currentStudent.getTranscript().getResults()) {
            sb.append("Course: ").append(r.getCourse().getTitle()).append("\n");
            sb.append(String.format("  Marks: %.2f/100\n", r.getMarksObtained()));
            sb.append(String.format("  GPA: %.2f\n", r.getCourseGPA()));
            sb.append(String.format("  Attendance: %d/%d (%.2f%%)\n", 
                r.getAttendance(), ResultEntry.totalAttendance, r.getAttendancePercentage()));
            sb.append("  Instructor: ").append(r.getCourse().getCourseInstructor().getName()).append("\n");
            sb.append("-------------------\n");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "My Results", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void showStudentCourseDetails() {
        if (currentStudent.getTranscript().getResults().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No enrolled courses.", "My Courses", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columns = {"Course", "Code", "Credits", "Instructor"};
        Object[][] data = new Object[currentStudent.getTranscript().getResults().size()][4];
        
        int i = 0;
        for (ResultEntry r : currentStudent.getTranscript().getResults()) {
            data[i][0] = r.getCourse().getTitle();
            data[i][1] = r.getCourse().getCourseCode();
            data[i][2] = r.getCourse().getCreditHours();
            data[i][3] = r.getCourse().getCourseInstructor().getName();
            i++;
        }
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "My Courses", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void submitComplaint() {
        JTextArea complaintArea = new JTextArea(5, 30);
        complaintArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(complaintArea);
        
        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Submit Complaint", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String complaint = complaintArea.getText().trim();
            if (!complaint.isEmpty()) {
                currentStudent.addComplaints(complaint);
                JOptionPane.showMessageDialog(this, "Complaint submitted successfully!");
            }
        }
    }
    
    private void viewStudentComplaints() {
        if (currentStudent.getComplaints().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No complaints submitted yet.", "My Complaints", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("MY COMPLAINTS\n\n");
        int i = 1;
        for (String comp : currentStudent.getComplaints()) {
            sb.append(i).append(". ").append(comp).append("\n\n");
            i++;
        }
        
        JOptionPane.showMessageDialog(this, sb.toString(), "My Complaints", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void refreshStudentPanel() {
        // Refresh student data if needed
    }
    
    // INSTRUCTOR PANEL
    private void createInstructorPanel() {
        instructorPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("INSTRUCTOR PANEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        instructorPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] buttonLabels = {
            "View Profile", "View Course", 
            "View All Students", "Update Marks/Attendance",
            "Logout"
        };
        
        for (String label : buttonLabels) {
            JButton btn = new JButton(label);
            btn.addActionListener(e -> handleInstructorButton(label));
            buttonPanel.add(btn);
        }
        
        instructorPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void handleInstructorButton(String action) {
        if (currentInstructorCourse == null) return;
        
        switch (action) {
            case "View Profile":
                showInstructorProfile();
                break;
            case "View Course":
                showInstructorCourse();
                break;
            case "View All Students":
                viewCourseStudents();
                break;
            case "Update Marks/Attendance":
                updateStudentData();
                break;
            case "Logout":
                currentInstructorCourse = null;
                cardLayout.show(mainPanel, "LOGIN");
                break;
        }
    }
    
    private void showInstructorProfile() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSTRUCTOR PROFILE\n\n");
        sb.append("Name: ").append(currentInstructorCourse.getCourseInstructor().getName()).append("\n");
        sb.append("ID: ").append(currentInstructorCourse.getCourseInstructor().getInstructorId()).append("\n");
        sb.append("Qualification: ").append(currentInstructorCourse.getCourseInstructor().getQualification()).append("\n");
        sb.append("Course: ").append(currentInstructorCourse.getTitle()).append("\n");
        
        JOptionPane.showMessageDialog(this, sb.toString(), "My Profile", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showInstructorCourse() {
        StringBuilder sb = new StringBuilder();
        sb.append("COURSE DETAILS\n\n");
        sb.append("Code: ").append(currentInstructorCourse.getCourseCode()).append("\n");
        sb.append("Title: ").append(currentInstructorCourse.getTitle()).append("\n");
        sb.append("Credit Hours: ").append(currentInstructorCourse.getCreditHours()).append("\n");
        sb.append("Enrolled Students: ").append(getStudentsInCourse(currentInstructorCourse).size()).append("\n");
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Course Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void viewCourseStudents() {
        ArrayList<Student> courseStudents = getStudentsInCourse(currentInstructorCourse);
        
        if (courseStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students enrolled in this course.", "Course Students", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columns = {"ID", "Name", "Program", "Marks", "Attendance %"};
        Object[][] data = new Object[courseStudents.size()][5];
        
        for (int i = 0; i < courseStudents.size(); i++) {
            Student s = courseStudents.get(i);
            ResultEntry r = getResultEntryForCourse(s, currentInstructorCourse);
            data[i][0] = s.getStudentId();
            data[i][1] = s.getName();
            data[i][2] = s.getProgram();
            data[i][3] = r != null ? String.format("%.2f", r.getMarksObtained()) : "N/A";
            data[i][4] = r != null ? String.format("%.2f", r.getAttendancePercentage()) : "N/A";
        }
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Course Students", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void updateStudentData() {
        ArrayList<Student> courseStudents = getStudentsInCourse(currentInstructorCourse);
        
        if (courseStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students to update.", "Update", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Select student
        String[] studentOptions = new String[courseStudents.size()];
        for (int i = 0; i < courseStudents.size(); i++) {
            Student s = courseStudents.get(i);
            studentOptions[i] = s.getStudentId() + " - " + s.getName();
        }
        
        String selectedStudent = (String) JOptionPane.showInputDialog(this, 
            "Select Student to Update:", "Update Student", 
            JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);
        
        if (selectedStudent == null) return;
        
        String studentId = selectedStudent.split(" - ")[0];
        Student student = null;
        for (Student s : courseStudents) {
            if (s.getStudentId().equals(studentId)) {
                student = s;
                break;
            }
        }
        
        if (student == null) return;
        
        ResultEntry result = getResultEntryForCourse(student, currentInstructorCourse);
        if (result == null) return;
        
        // Update choice
        String[] options = {"Update Marks", "Update Attendance"};
        String choice = (String) JOptionPane.showInputDialog(this, 
            "What to update?", "Update", 
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice == null) return;
        
        if (choice.equals("Update Marks")) {
            String marksStr = JOptionPane.showInputDialog(this, 
                "Enter new marks (0-100):", String.valueOf(result.getMarksObtained()));
            if (marksStr != null) {
                try {
                    double newMarks = Double.parseDouble(marksStr);
                    result.setMarksObtained(newMarks);
                    JOptionPane.showMessageDialog(this, "Marks updated!");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid marks!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            String attStr = JOptionPane.showInputDialog(this, 
                "Enter new attendance (0-30):", String.valueOf(result.getAttendance()));
            if (attStr != null) {
                try {
                    int newAtt = Integer.parseInt(attStr);
                    result.setAttendance(newAtt);
                    JOptionPane.showMessageDialog(this, "Attendance updated!");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid attendance!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private ArrayList<Student> getStudentsInCourse(Course course) {
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
    
    private void removeCourseFromStudent(Student student, String courseCode) {
        ArrayList<ResultEntry> resultsToRemove = new ArrayList<>();
        
        for (ResultEntry r : student.getTranscript().getResults()) {
            if (r.getCourse().getCourseCode().equals(courseCode)) {
                resultsToRemove.add(r);
            }
        }
        
        student.getTranscript().getResults().removeAll(resultsToRemove);
    }
    
    private ResultEntry getResultEntryForCourse(Student student, Course course) {
        for (ResultEntry r : student.getTranscript().getResults()) {
            if (r.getCourse().getCourseCode().equals(course.getCourseCode())) {
                return r;
            }
        }
        return null;
    }
    
    private void refreshInstructorPanel() {
        // Refresh instructor data if needed
    }
}