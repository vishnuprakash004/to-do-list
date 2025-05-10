import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class ToDoListApp {
    private JFrame frame;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInputField;

    public ToDoListApp() {
        initializeFrame();
        initializeInputPanel();
        initializeTaskList();
        initializeButtonPanel();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 500);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new BorderLayout(10, 10));
    }

    private void initializeInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(new Color(30, 30, 30));
        inputPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        taskInputField = new JTextField();
        taskInputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taskInputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add");
        styleButton(addButton);
        addButton.addActionListener(e -> addTaskAnimated());

        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.NORTH);
    }

    private void initializeTaskList() {
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taskList.setBackground(new Color(50, 50, 50));
        taskList.setForeground(Color.WHITE);
        taskList.setSelectionBackground(new Color(70, 130, 180));
        taskList.setSelectionForeground(Color.WHITE);
        taskList.setFixedCellHeight(40);
        taskList.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Toggle "done" on double-click
        taskList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    toggleTaskComplete();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton deleteButton = new JButton("Delete Task");
        JButton clearButton = new JButton("Clear All");

        deleteButton.addActionListener(e -> deleteTaskAnimated());
        clearButton.addActionListener(e -> clearAllTasksAnimated());

        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Footer Label
        JLabel footerLabel = new JLabel("Created by Vishnu Prakash");
        footerLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));


        // Panel to hold both buttons and footer
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(footerLabel, BorderLayout.SOUTH);

        frame.add(southPanel, BorderLayout.SOUTH);
    }


    private void addTaskAnimated() {
        String task = taskInputField.getText().trim();
        if (!task.isEmpty()) {
            taskInputField.setText("");

            taskListModel.addElement(task);

            // Animate by flashing the selection color
            int index = taskListModel.size() - 1;
            taskList.setSelectedIndex(index);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                int count = 0;
                boolean toggle = false;

                @Override
                public void run() {
                    if (count >= 6) { // Number of flashes
                        taskList.clearSelection();
                        timer.cancel();
                    } else {
                        toggle = !toggle;
                        taskList.setSelectionBackground(toggle ? new Color(100, 149, 237) : new Color(70, 130, 180));
                        count++;
                    }
                }
            }, 0, 100);
        } else {
            showError("Task cannot be empty!");
        }
    }


    private void deleteTaskAnimated() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                int count = 0;
                boolean toggle = false;

                @Override
                public void run() {
                    if (count >= 6) {
                        taskListModel.remove(selectedIndex);
                        timer.cancel();
                    } else {
                        toggle = !toggle;
                        taskList.setSelectionBackground(toggle ? new Color(255, 69, 58) : new Color(70, 130, 180));
                        count++;
                    }
                }
            }, 0, 100);
        } else {
            showError("No task selected!");
        }
    }


    private void clearAllTasksAnimated() {
        if (taskListModel.isEmpty()) {
            showError("No tasks to clear!");
            return;
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = 0;
            boolean toggle = false;

            @Override
            public void run() {
                if (count >= 6) {
                    taskListModel.clear();
                    taskList.setBackground(new Color(50, 50, 50));
                    timer.cancel();
                } else {
                    toggle = !toggle;
                    taskList.setBackground(toggle ? new Color(255, 69, 58) : new Color(50, 50, 50));
                    count++;
                }
            }
        }, 0, 100);
    }


    private void toggleTaskComplete() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String task = taskListModel.getElementAt(selectedIndex);
            if (task.startsWith("✔ ")) {
                taskListModel.setElementAt(task.substring(2), selectedIndex);
            } else {
                taskListModel.setElementAt("✔ " + task, selectedIndex);
            }
        }
    }


    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
