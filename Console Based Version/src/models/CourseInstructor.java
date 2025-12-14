package models;
import java.io.Serializable;

public class CourseInstructor implements Serializable{
    // Attributes
    private String name;
    private String qualification;

    // Additional Attributes
    private String instructorId;
    private String password;

    // Constructors
    public CourseInstructor(){
        this("None","None","None","");
    }


    public CourseInstructor(String name, String qualification, String instructorId, String password){
        this.name = name;
        this.qualification = qualification;
        this.instructorId = instructorId;
        this.password = password;
    }

    // Setters & Getters
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setQualification(String qualification){
        this.qualification = qualification;
    }
    public String getQualification(){
        return this.qualification;
    }

    public void setInstructorId(String instructorId){
        this.instructorId = instructorId;
    }
    public String getInstructorId(){
        return this.instructorId;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

    @Override
    public String toString(){
        String x = "Name: "+this.name+"\nQualification: "+this.qualification;
        return x;
    }
}