package models;

public class ScienceStudent extends Student {

    public ScienceStudent() {
        super();
        setProgram("Science");
    }

    public ScienceStudent(String studentId, String name, String password) {
        super(studentId, name, "Science", password);
    }
}
