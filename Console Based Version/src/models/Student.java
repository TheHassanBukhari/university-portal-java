package models;
import java.util.ArrayList;
import java.util.Scanner;
import interfaces.ResultCalculator;
public abstract class Student implements ResultCalculator{
    // Attributes
    public static int totalStudents;
    private String studentId;
    private String name;
    private String program;
    private Transcript transcript;

    // Additional attributes
    private String password;
    private ArrayList<String> complaints = new ArrayList<>();


    // Constructors
    public Student() {
        this("None", "None", "None", ""); // default password empty
    }

    public Student(String studentId, String name, String program, String password) {
        this.studentId = studentId;
        this.name = name;
        this.program = program;
        this.password = password; // initialize password
        this.transcript = new Transcript();
        totalStudents++;
        // Complaints is already initialized inline
    }



    // Setters & Getters

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getProgram() {
        return program;
    }
    public void setProgram(String program) {
        this.program = program;
    }


    public Transcript getTranscript() {
        return transcript;
    }
    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }


    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public ArrayList<String> getComplaints() {
        return complaints;
    }
    public void addComplaints(String complaint) {
        this.complaints.add(complaint);
    }



    // Methods
    public double calculateGPA(){
        return this.transcript.getGPA();
    }

    public void displayResults(){
        System.out.println("GPA: "+this.transcript.getGPA());
        System.out.println("Obtained Marks: "+this.transcript.getTotalMarksObtained());
        System.out.println("Total Marks: "+this.transcript.getTotalMarks());
        System.out.println("Marks percentage: "+this.calculatePercentage());
    }

    @Override
    public double calculateGrade(){
        return this.calculateGPA();
    }

    @Override
    public double calculateTotal(){
        return this.transcript.getTotalMarksObtained();
    }

    @Override
    public double calculatePercentage(){
        if(transcript.getTotalMarks() == 0) return 0;
        double percentage = (this.transcript.getTotalMarksObtained()/this.transcript.getTotalMarks())*100.0;
        return percentage;
    }
    // Methods: addCourse(), calculateGPA(), displayResults()
}