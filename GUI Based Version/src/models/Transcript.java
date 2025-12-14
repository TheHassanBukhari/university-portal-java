package models;
import java.util.ArrayList;
import java.io.Serializable;

public class Transcript implements Serializable{
    // Attributes
    private ArrayList<ResultEntry> results;

    // New attribute: attendance percentage
    private double attendancePercentage;

    // Constructors
    public Transcript() {
        this.results = new ArrayList<>();
    }

    // Getters
    public ArrayList<ResultEntry> getResults() {
        return results;
    }

    public double getAttendancePercentage() {
        calculateAttendancePercentage();
        return attendancePercentage;
    }

    // Methods
    public void addResultEntry(ResultEntry r) {
        results.add(r);
    }

    public double getTotalMarks() {
        return results.size() * 100;
    }

    public double getTotalMarksObtained() {
        double x = 0;
        for (ResultEntry r : results) {
            x += r.getMarksObtained();
        }
        return x;
    }

    public double getGPA() {
        double qualityPoints = 0;
        int totalCreditHours = 0;

        for (ResultEntry r : results) {
            qualityPoints += r.getCourseGPA() * r.getCourse().getCreditHours();
            totalCreditHours += r.getCourse().getCreditHours();
        }

        if (totalCreditHours == 0) return 0;
        return qualityPoints / totalCreditHours;
    }

    // New Method: calculate attendance percentage
    private void calculateAttendancePercentage() {
        if (results.isEmpty()) {
            attendancePercentage = 0;
            return;
        }

        double totalAttendance = 0;
        for (ResultEntry r : results) {
            totalAttendance += r.getAttendance();
        }

        double maxPossible = results.size() * ResultEntry.totalAttendance;
        attendancePercentage = (totalAttendance / maxPossible) * 100.0;
    }
}
