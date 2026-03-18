import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TherapistDashboard extends JFrame {

    private ScheduleManager scheduleManager;
    private Therapist therapist;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public TherapistDashboard(ScheduleManager scheduleManager, Therapist therapist) {
        this.scheduleManager = scheduleManager;
        this.therapist = therapist;

        setTitle("Therapist Dashboard - " + therapist.getUsername());
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel welcomeLabel = new JLabel("Welcome, Dr. " + therapist.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Client ID", "Date", "Start Time", "End Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        appointmentTable = new JTable(tableModel);
        appointmentTable.setRowHeight(25);
        appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel availabilityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        availabilityPanel.setBorder(BorderFactory.createTitledBorder("Set Availability"));
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        JComboBox<String> dayCombo = new JComboBox<>(days);
        JTextField startField = new JTextField(5);
        JTextField endField = new JTextField(5);
        JButton saveAvailButton = new JButton("Save");
        styleButton(saveAvailButton, new Color(70, 130, 180));

        availabilityPanel.add(new JLabel("Day:"));
        availabilityPanel.add(dayCombo);
        availabilityPanel.add(new JLabel("Start (HH:MM):"));
        availabilityPanel.add(startField);
        availabilityPanel.add(new JLabel("End (HH:MM):"));
        availabilityPanel.add(endField);
        availabilityPanel.add(saveAvailButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");
        styleButton(refreshButton, new Color(46, 139, 87));
        styleButton(logoutButton, new Color(105, 105, 105));
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        bottomPanel.add(availabilityPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadAppointments();

        saveAvailButton.addActionListener(e -> {
            String day = (String) dayCombo.getSelectedItem();
            String start = startField.getText().trim();
            String end = endField.getText().trim();
            if (start.isEmpty() || end.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter start and end times.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FileManager.saveAvailability(therapist.getUserId(), day, start, end);
            JOptionPane.showMessageDialog(this, "Availability saved successfully!");
            startField.setText("");
            endField.setText("");
        });

        refreshButton.addActionListener(e -> loadAppointments());

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = scheduleManager
                .getAppointmentsForTherapist(therapist.getUserId());
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getClientId(),
                    a.getDate(),
                    a.getStartTime(),
                    a.getEndTime(),
                    a.getStatus()
            });
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
}