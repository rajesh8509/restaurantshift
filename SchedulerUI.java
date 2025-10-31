package ui;

import client.SchedulerClient;
import model.Shift;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SchedulerUI extends JFrame {
    private SchedulerClient client;
    private DefaultListModel<String> shiftListModel;
    private JList<String> shiftList;
    private JTextField nameField, timeField, roleField;

    public SchedulerUI() {
        client = new SchedulerClient("localhost", 5000);
        setupUI();
        loadShifts();
    }

    private void setupUI() {
        setTitle("Restaurant Shift Scheduler");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("Employee Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Shift Time:"));
        timeField = new JTextField();
        inputPanel.add(timeField);

        inputPanel.add(new JLabel("Role:"));
        roleField = new JTextField();
        inputPanel.add(roleField);

        JButton addButton = new JButton("Add Shift");
        addButton.addActionListener(this::addShift);
        inputPanel.add(addButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadShifts());
        inputPanel.add(refreshButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel
        shiftListModel = new DefaultListModel<>();
        shiftList = new JList<>(shiftListModel);
        add(new JScrollPane(shiftList), BorderLayout.CENTER);
    }

    private void addShift(ActionEvent e) {
        String name = nameField.getText();
        String time = timeField.getText();
        String role = roleField.getText();

        if (name.isEmpty() || time.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        Shift shift = new Shift(name, time, role);
        String response = client.addShift(shift);
        JOptionPane.showMessageDialog(this, response);
        loadShifts();
    }

    private void loadShifts() {
        shiftListModel.clear();
        List<Shift> shifts = client.getAllShifts();
        if (shifts != null) {
            for (Shift s : shifts) {
                shiftListModel.addElement(s.toString());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SchedulerUI().setVisible(true));
    }
}
