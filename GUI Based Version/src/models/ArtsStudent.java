package models;

public class ArtsStudent extends Student {

    public ArtsStudent() {
        super();
        setProgram("Arts");
    }

    public ArtsStudent(String studentId, String name, String password) {
        super(studentId, name, "Arts", password);
    }
}
