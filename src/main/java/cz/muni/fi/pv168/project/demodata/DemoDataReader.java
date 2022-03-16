package cz.muni.fi.pv168.project.demodata;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.TaskState;
import cz.muni.fi.pv168.project.model.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Same as TestGenerator in employee-evidence.
 * Reads data from txt file for the first time.
 * Is used only in *Dao classes.
 */
public final class DemoDataReader {

    private static InputStream createInputStream(){
        InputStream inputStream = DemoDataReader.class.getResourceAsStream("tasks.txt");
        if (inputStream == null) {
            throw new IllegalArgumentException("tasks.txt resource not found on classpath: " + DemoDataReader.class);
        }
        return inputStream;
    }

    public static List<Category> getCategories() throws IOException {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(createInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                Category category = new Category(line.split("; ")[5]);
                if (!categories.contains(category))
                    categories.add(category);
            }
        }
        return categories;
    }

    public static List<Task> getTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        List<Category> categories = getCategories();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(createInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split("; ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                Category category = categories.stream().filter(c -> c.getName().equals(attributes[5])).findFirst().orElse(null);
                Task task = new Task(attributes[0],
                        attributes[1],
                        LocalDateTime.parse(attributes[2], formatter),
                        LocalDateTime.parse(attributes[3], formatter),
                        Integer.parseInt(attributes[4]),
                        category);
                task.setProgress(Integer.parseInt(attributes[6]));
                task.setDoneTime(LocalDateTime.parse(attributes[7], formatter));
                task.setStateOfTask(TaskState.valueOf(attributes[8]));
                task.setSpentTime(Integer.parseInt(attributes[9]));
                tasks.add(task);
            }
        }
        return tasks;
    }


}
