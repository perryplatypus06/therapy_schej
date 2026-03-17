import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Test saving an appointment
        Appointment a = new Appointment(
                1, 1, 2,
                java.time.LocalDate.of(2026, 3, 20),
                java.time.LocalTime.of(10, 0),
                java.time.LocalTime.of(11, 0)
        );
        FileManager.saveAppointment(a);
        System.out.println("Saved appointment: " + a);

        // Test loading it back
        List<Appointment> loaded = FileManager.loadAppointments();
        System.out.println("Loaded " + loaded.size() + " appointment(s)");
        for (Appointment appt : loaded) {
            System.out.println(appt);
        }
    }
}