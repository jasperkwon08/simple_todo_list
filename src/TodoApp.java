import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class TodoApp extends JFrame {

    private TaskManager manager;
    private String username;
    private JPanel listPanel = new JPanel(new GridLayout(0, 1));

    public TodoApp() {
        username = getUsername("default");
        manager = new TaskManager(username + "_tasks.txt");

        setTitle("Todo List - " + username);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());

        JButton switchButton = new JButton("Switch User");
        switchButton.addActionListener(e -> switchUser());

        JPanel topPanel = new JPanel();
        topPanel.add(addButton);
        topPanel.add(switchButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(listPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        showTasks();
    }

    // asks for username, fallback is used if Cancel is clicked
    private String getUsername(String fallback) {
        while (true) {
            String input = JOptionPane.showInputDialog("Enter your username:");
            if (input == null) {
                return fallback;
            }
            String name = input.trim().toLowerCase();
            if (name.isEmpty()) {
                return "default";
            }
            // regex to check if username contains numbers of symbols
            if (name.matches("[a-z]+")) {
                return name;
            }
            JOptionPane.showMessageDialog(null,
                    "Usernames can only contain letters (no numbers, symbols or spaces). Please try again.",
                    "Invalid Username", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchUser() {
        String newName = getUsername(username);
        if (newName.equals(username)) {
            return;
        }
        username = newName;
        manager = new TaskManager(username + "_tasks.txt");
        setTitle("Todo List - " + username);
        showTasks();
    }

    private void showTasks() {
        listPanel.removeAll();

        for (Task task : manager.getTasks()) {
            JCheckBox checkBox = new JCheckBox(task.getName(), task.isDone());
            checkBox.addActionListener(e -> manager.toggleTask(task));

            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(e -> removeTask(task));

            JPanel row = new JPanel(new BorderLayout());
            row.add(checkBox, BorderLayout.CENTER);
            row.add(removeButton, BorderLayout.EAST);
            listPanel.add(row);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addTask() {
        String name = JOptionPane.showInputDialog(this, "Enter a new task:");
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Add \"" + name.trim() + "\"?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            manager.addTask(name.trim());
            showTasks();
        }
    }

    private void removeTask(Task task) {
        int answer = JOptionPane.showConfirmDialog(this,
                "Remove \"" + task.getName() + "\"?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            manager.removeTask(task);
            showTasks();
        }
    }

    public static void main(String[] args) {
        new TodoApp().setVisible(true);
    }
}