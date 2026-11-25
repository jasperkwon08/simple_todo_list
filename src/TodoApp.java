import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class TodoApp extends JFrame {

    private TaskManager manager = new TaskManager();
    private JPanel listPanel = new JPanel(new GridLayout(0, 1));

    public TodoApp() {
        setTitle("To-Do List");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());

        JPanel topPanel = new JPanel();
        topPanel.add(addButton);

        // keeps the rows from stretching to fill the window
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(listPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(centerPanel), BorderLayout.CENTER);

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