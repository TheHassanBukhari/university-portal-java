package models;

public class EngineeringStudent extends Student {

    public EngineeringStudent() {
        super();
        setProgram("Engineering");
    }

    public EngineeringStudent(String studentId, String name, String password) {
        super(studentId, name, "Engineering", password);
    }
}
