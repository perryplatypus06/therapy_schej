import java.util.ArrayList;
import java.util.List;

public class Therapist extends User {
    private String specialization;
    private List<String> availableSlots;

    public Therapist(int userId, String username, String password, String specialization) {
        super(userId, username, password, "therapist");
        this.specialization = specialization;
        this.availableSlots = new ArrayList<>();
    }

    public String getSpecialization() { return specialization; }
    public List<String> getAvailableSlots() { return availableSlots; }

    public void addSlot(String slot) { availableSlots.add(slot); }
    public void removeSlot(String slot) { availableSlots.remove(slot); }

    @Override
    public String toString() {
        return getUserId() + "," + getUsername() + "," + getPassword() + ",therapist," + specialization;
    }
}