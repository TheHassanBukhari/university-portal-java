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
        for(int i = items.size() - 1; i >= 0; i--){ // iterate in back if 2 had same id, then an element will shift and skip
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