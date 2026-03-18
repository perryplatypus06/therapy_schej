import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClientDashboard extends JFrame {

    private ScheduleManager scheduleManager;
    private Client client;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public ClientDashboard(ScheduleManager scheduleManager, Client client) {
        this.scheduleManager = scheduleManager;
        this.client = client;

        setTitle("Client Dashboard - " + client.getUsername());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel welcomeLabel = new JLabel("Welcome, " + client.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Therapist ID", "Date", "Start Time", "End Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        appointmentTable = new JTable(tableModel);
        appointmentTable.setRowHeight(25);
        appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton bookButton = new JButton("Book Appointment");
        JButton cancelButton = new JButton("Cancel Appointment");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");

        styleButton(bookButton, new Color(46, 139, 87));
        styleButton(cancelButton, new Color(178, 34, 34));
        styleButton(refreshButton, new Color(70, 130, 180));
        styleButton(logoutButton, new Color(105, 105, 105));

        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadAppointments();

        bookButton.addActionListener(e -> showBookingDialog());
        cancelButton.addActionListener(e -> cancelSelectedAppointment());
        refreshButton.addActionListener(e -> loadAppointments());
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = scheduleManager.getAppointmentsForClient(client.getClientId());
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getTherapistId(),
                    a.getDate(),
                    a.getStartTime(),
                    a.getEndTime(),
                    a.getStatus()
            });
        }
    }

    private void showBookingDialog() {
        JDialog dialog = new JDialog(this, "Book Appointment", true);
        dialog.setSize(350, 280);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Therapist ID:"));
        JTextField therapistField = new JTextField();
        panel.add(therapistField);

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        JTextField dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Start Time (HH:MM):"));
        JTextField startField = new JTextField();
        panel.add(startField);

        panel.add(new JLabel("End Time (HH:MM):"));
        JTextField endField = new JTextField();
        panel.add(endField);

        JButton confirmButton = new JButton("Book");
        styleButton(confirmButton, new Color(46, 139, 87));
        JButton cancelBtn = new JButton("Cancel");
        panel.add(confirmButton);
        panel.add(cancelBtn);

        dialog.add(panel);

        confirmButton.addActionListener(e -> {
            try {
                int therapistId = Integer.parseInt(therapistField.getText().trim());
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                LocalTime start = LocalTime.parse(startField.getText().trim());
                LocalTime end = LocalTime.parse(endField.getText().trim());

                if (end.isBefore(start) || end.equals(start)) {
                    JOptionPane.showMessageDialog(dialog,
                            "End time must be after start time.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = scheduleManager.bookAppointment(
                        therapistId, client.getClientId(), date, start, end
                );

                if (success) {
                    JOptionPane.showMessageDialog(dialog,
                            "Appointment booked successfully!");
                    dialog.dispose();
                    loadAppointments();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Time slot unavailable. Please choose another slot.",
                            "Conflict", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Invalid input. Please check your entries.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void cancelSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an appointment to cancel.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this appointment?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            scheduleManager.cancelAppointment(appointmentId);
            loadAppointments();
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
}