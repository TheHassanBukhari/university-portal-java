package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore<T> {

    public void saveToFile(String fileName, List<T> items) {
        // Create parent directory if it doesn't exist
        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();  // Create folder(s) like "data/"
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            oos.writeObject(items);
            System.out.println("Data saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    public List<T> loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fileName))) {
            @SuppressWarnings("unchecked")
            List<T> items = (List<T>) ois.readObject();
            System.out.println("Data loaded from " + fileName);
            return items;
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file found. Starting fresh.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}