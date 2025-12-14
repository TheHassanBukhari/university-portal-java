package models;
import java.io.Serializable;

public class Course implements Serializable{
    // Attributes
    public static int totalCourses;
    private String courseCode;
    private String title;
    private int creditHours;
    private CourseInstructor courseInstructor;

    // Constructors
    public Course() {
        this("None", "None", 0, "None", "None", "None", "");
    }

    public Course(String courseCode, String title, int creditHours,
                  String instructorName, String instructorQualification,
                  String instructorId, String instructorPassword) {

        this.courseCode = courseCode;
        this.title = title;
        this.creditHours = creditHours;

        this.courseInstructor = new CourseInstructor(
            instructorName,
            instructorQualification,
            instructorId,
            instructorPassword
        );

        totalCourses++;
    }

    // Setters & Getters
    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }
    public String getCourseCode(){
        return this.courseCode;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public void setCreditHours(int creditHours){
        this.creditHours = creditHours;
    }
    public int getCreditHours(){
        return this.creditHours;
    }

    public void setCourseInstructor(CourseInstructor courseInstructor){
        this.courseInstructor = courseInstructor;
    }
    public CourseInstructor getCourseInstructor(){
        return this.courseInstructor;
    }

    // Methods
    public void displayCourseDetails(){
        System.out.println("Course code: "+this.courseCode);
        System.out.println("Course Title: "+this.title);
        System.out.println("Credit Hours: "+this.creditHours);
        System.out.println("INSTRUCTOR DETAILS:");
        System.out.println(courseInstructor);
    }
}
