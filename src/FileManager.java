import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String USERS_FILE = "users.csv";
    private static final String APPOINTMENTS_FILE = "appointments.csv";
    private static final String AVAILABILITY_FILE = "availability.csv";

    // ─── USERS ───────────────────────────────────────────

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[3].equals("therapist")) {
                    users.add(new Therapist(Integer.parseInt(parts[0]), parts[1], parts[2], parts[4]));
                } else {
                    users.add(new Client(Integer.parseInt(parts[0]), parts[1], parts[2], parts[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    public static void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(user.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // ─── APPOINTMENTS ─────────────────────────────────────

    public static List<Appointment> loadAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Appointment a = new Appointment(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        LocalDate.parse(parts[3]),
                        LocalTime.parse(parts[4]),
                        LocalTime.parse(parts[5])
                );
                a.setStatus(parts[6]);
                appointments.add(a);
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
        return appointments;
    }

    public static void saveAppointment(Appointment appointment) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE, true))) {
            bw.write(appointment.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving appointment: " + e.getMessage());
        }
    }

    public static void saveAllAppointments(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            bw.write("appointmentId,therapistId,clientId,date,startTime,endTime,status");
            bw.newLine();
            for (Appointment a : appointments) {
                bw.write(a.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }

    // ─── AVAILABILITY ─────────────────────────────────────

    public static void saveAvailability(int therapistId, String day,
                                        String startTime, String endTime) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AVAILABILITY_FILE, true))) {
            bw.write(therapistId + "," + day + "," + startTime + "," + endTime);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving availability: " + e.getMessage());
        }
    }

    public static List<String[]> loadAvailability(int therapistId) {
        List<String[]> slots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == therapistId) {
                    slots.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading availability: " + e.getMessage());
        }
        return slots;
    }
}