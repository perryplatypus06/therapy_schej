import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private String contactNumber;
    private List<Integer> appointmentIds;

    public Client(int userId, String username, String password, String contactNumber) {
        super(userId, username, password, "client");
        this.contactNumber = contactNumber;
        this.appointmentIds = new ArrayList<>();
    }

    public String getContactNumber() { return contactNumber; }
    public List<Integer> getAppointmentIds() { return appointmentIds; }

    public void addAppointmentId(int id) { appointmentIds.add(id); }
    public void removeAppointmentId(int id) { appointmentIds.remove(Integer.valueOf(id)); }

    @Override
    public String toString() {
        return getUserId() + "," + getUsername() + "," + getPassword() + ",client," + contactNumber;
    }
}