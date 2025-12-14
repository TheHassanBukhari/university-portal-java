package util;
import models.*;
import java.io.Serializable;
import java.util.ArrayList;

public class RecordList<T> implements Serializable{
    // Attributes
    ArrayList<T> items = new ArrayList<>();

    // Methods
    public void add(T item){
        items.add(item);
        System.out.println("Added successfully!");
    }

    public void remove(String id){
        for(int i = items.size() - 1; i >= 0; i--){
            T item = items.get(i);
            if(item instanceof Student && id.equals(((Student)item).getStudentId())){
                items.remove(i);
            }
            else if(item instanceof Course && id.equals(((Course)item).getCourseCode())){
                items.remove(i);
            }
        }
    }

    public ArrayList<T> getAll(){
        return items;
    }

    public int size(){
        return (int) items.size();
    }
}

// 6. RecordList<T> (Generic Class)
// A ributes: List<T> items
// Methods: add(T item), remove(String id), getAll()
// Usage: stores Students, Courses, or Transcripts