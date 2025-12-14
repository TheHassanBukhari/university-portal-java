package models;
import java.io.Serializable;

public class ResultEntry implements Serializable{
    // Attributes
    private Course course;
    private double marksObtained;
    private double courseGPA;

    // Additional attributes
    public static final int totalAttendance = 30;
    private int attendance = 0;
    private double attendancePercentage = 0.0;

    // Constructors
    public ResultEntry() {
        this(null, 0.0);
    }

    public ResultEntry(Course course, double marksObtained) {
        this.course = course;
        this.marksObtained = marksObtained;
        evaluateGPA();
        updateAttendancePercentage();
    }

    // Setters & Getters
    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
        evaluateGPA();
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public double getCourseGPA() {
        return this.courseGPA;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
        updateAttendancePercentage();
    }

    public int getAttendance() {
        return this.attendance;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void addAttendance(int x) {
        this.attendance += x;
        updateAttendancePercentage();
    }

    private void updateAttendancePercentage() {
        this.attendancePercentage = (attendance / (double) totalAttendance) * 100.0;
    }

    // Methods
    public void evaluateGPA() {
        if (this.marksObtained >= 85)
            this.courseGPA = 4.0;
        else if (this.marksObtained >= 80)
            this.courseGPA = 3.66;
        else if (this.marksObtained >= 75)
            this.courseGPA = 3.33;
        else if (this.marksObtained >= 70)
            this.courseGPA = 3.0;
        else if (this.marksObtained >= 65)
            this.courseGPA = 2.66;
        else if (this.marksObtained >= 61)
            this.courseGPA = 2.33;
        else if (this.marksObtained >= 58)
            this.courseGPA = 2.0;
        else if (this.marksObtained >= 55)
            this.courseGPA = 1.66;
        else if (this.marksObtained >= 50)
            this.courseGPA = 1.0;
        else
            this.courseGPA = 0;
    }

    @Override
    public String toString() {
        String result = "";
        result += "Course Code: " + course.getCourseCode() + "\n";
        result += "Course Title: " + course.getTitle() + "\n";
        result += "Credit Hours: " + course.getCreditHours() + "\n";
        result += "Instructor:\n" + course.getCourseInstructor() + "\n";
        result += "Marks Obtained: " + marksObtained + "/100\n";
        result += "GPA: " + courseGPA + "\n";
        result += "Attendance: " + attendance + "/" + totalAttendance +
                " (" + String.format("%.2f", attendancePercentage) + "%)\n";
        result += "----------------------------\n";
        return result;
    }
}
