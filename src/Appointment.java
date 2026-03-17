import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int appointmentId;
    private int therapistId;
    private int clientId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

    public Appointment(int appointmentId, int therapistId, int clientId,
                       LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.appointmentId = appointmentId;
        this.therapistId = therapistId;
        this.clientId = clientId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "booked";
    }

    public int getAppointmentId() { return appointmentId; }
    public int getTherapistId() { return therapistId; }
    public int getClientId() { return clientId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public boolean overlapsWith(Appointment other) {
        if (this.therapistId != other.therapistId) return false;
        if (!this.date.equals(other.date)) return false;
        return this.startTime.isBefore(other.endTime) &&
                other.startTime.isBefore(this.endTime);
    }

    @Override
    public String toString() {
        return appointmentId + "," + therapistId + "," + clientId + "," +
                date + "," + startTime + "," + endTime + "," + status;
    }
}