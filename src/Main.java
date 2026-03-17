import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        ScheduleManager sm = new ScheduleManager();

        // Test login
        User user = sm.login("admin", "admin123");
        System.out.println("Logged in as: " + (user != null ? user.getUsername() : "failed"));

        // Test booking
        boolean booked = sm.bookAppointment(
                1, 2,
                LocalDate.of(2026, 3, 25),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );
        System.out.println("Booking 1: " + booked);

        // Test conflict detection — same therapist, same slot
        boolean blocked = sm.bookAppointment(
                1, 2,
                LocalDate.of(2026, 3, 25),
                LocalTime.of(10, 30),
                LocalTime.of(11, 30)
        );
        System.out.println("Booking 2 (should be blocked): " + blocked);
    }
}