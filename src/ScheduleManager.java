import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleManager {

    private List<Appointment> appointments;
    private List<User> users;

    public ScheduleManager() {
        this.appointments = FileManager.loadAppointments();
        this.users = FileManager.loadUsers();
    }

    // ─── BOOK ─────────────────────────────────────────────

    public boolean bookAppointment(int therapistId, int clientId,
                                   LocalDate date, LocalTime startTime, LocalTime endTime) {
        Appointment newAppt = new Appointment(
                generateId(), therapistId, clientId, date, startTime, endTime
        );

        if (hasConflict(newAppt)) {
            System.out.println("Conflict detected. Slot unavailable.");
            return false;
        }

        appointments.add(newAppt);
        FileManager.saveAppointment(newAppt);
        System.out.println("Appointment booked successfully.");
        return true;
    }

    // ─── CANCEL ───────────────────────────────────────────

    public boolean cancelAppointment(int appointmentId) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId() == appointmentId) {
                a.setStatus("cancelled");
                FileManager.saveAllAppointments(appointments);
                System.out.println("Appointment cancelled.");
                return true;
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }

    // ─── RESCHEDULE ───────────────────────────────────────

    public boolean rescheduleAppointment(int appointmentId, LocalDate newDate,
                                         LocalTime newStart, LocalTime newEnd) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId() == appointmentId) {
                Appointment temp = new Appointment(
                        appointmentId, a.getTherapistId(), a.getClientId(),
                        newDate, newStart, newEnd
                );
                if (hasConflict(temp)) {
                    System.out.println("Conflict detected. Cannot reschedule.");
                    return false;
                }
                appointments.remove(a);
                appointments.add(temp);
                FileManager.saveAllAppointments(appointments);
                System.out.println("Appointment rescheduled.");
                return true;
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }

    // ─── GET APPOINTMENTS ─────────────────────────────────

    public List<Appointment> getAppointmentsForClient(int clientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getClientId() == clientId && a.getStatus().equals("booked")) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Appointment> getAppointmentsForTherapist(int therapistId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getTherapistId() == therapistId) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }

    // ─── LOGIN ────────────────────────────────────────────

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // ─── HELPERS ──────────────────────────────────────────

    private boolean hasConflict(Appointment newAppt) {
        for (Appointment existing : appointments) {
            if (existing.getStatus().equals("cancelled")) continue;
            if (existing.overlapsWith(newAppt)) return true;
        }
        return false;
    }

    private int generateId() {
        if (appointments.isEmpty()) return 1;
        int maxId = 0;
        for (Appointment a : appointments) {
            if (a.getAppointmentId() > maxId) maxId = a.getAppointmentId();
        }
        return maxId + 1;
    }
}