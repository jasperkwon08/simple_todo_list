import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TaskManager {
    private String fileName;
    private ArrayList<Task> tasks = new ArrayList<>();

    public TaskManager() {
        this("default_tasks.txt");
    }

    public TaskManager(String fileName) {
        this.fileName = "task_files/" + fileName;
        loadTasks();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void addTask(String name) {
        tasks.add(new Task(name, false));
        saveTasks();
    }

    public void toggleTask(Task task) {
        task.setDone(!task.isDone());
        saveTasks();
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        saveTasks();
    }

    // each line structure: 1|Buy milk  (1 = done, 0 = not done, basically boolean logic)
    private void loadTasks() {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    boolean done = parts[0].equals("1");
                    tasks.add(new Task(parts[1], done));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        File file = new File(fileName);
        if (tasks.isEmpty()) {
            file.delete();
            new File("task_files").delete(); // folder deletion should fail if not empty
            return;
        }
        new File("task_files").mkdir();
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                String flag = task.isDone() ? "1" : "0";
                writer.println(flag + "|" + task.getName());
            }
        } catch (IOException e) {
            System.out.println("Could not save tasks: " + e.getMessage());
        }
    }
}