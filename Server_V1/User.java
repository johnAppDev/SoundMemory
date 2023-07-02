import java.util.ArrayList;

public class User {
    private String name;
    private int height;
    private int weight;
    private String birthdate;
    private String caretakername;
    private String illnesses;
    private String allergies;
    private String additionalnotes;
    private String emergencycontacts;
    private int roomnumber;
    private String currentmedication;
    private int born;

    private ArrayList<Integer> songs;

    public User(String name, int born, int height, int weight, String birthdate, String caretakername, String illnesses,
            String allergies, String additionalnotes, String emergencycontacts, int roomnumber,
            String currentmedication) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.birthdate = birthdate;
        this.caretakername = caretakername;
        this.illnesses = illnesses;
        this.allergies = allergies;
        this.additionalnotes = additionalnotes;
        this.emergencycontacts = emergencycontacts;
        this.roomnumber = roomnumber;
        this.currentmedication = currentmedication;
        this.born = born;

        songs = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public int getheight() {
        return height;
    }

    public int getweight() {
        return weight;
    }

    public String getBirthDate() {
        return birthdate;
    }

    public String getcaretakername() {
        return caretakername;
    }

    public String getillnesses() {
        return illnesses;
    }

    public String getallergies() {
        return allergies;
    }

    public String getadditionalnotes() {
        return additionalnotes;
    }

    public String getemergencycontacts() {
        return emergencycontacts;
    }

    public int getroomnumber() {
        return roomnumber;
    }

    public String getcurrentmedication() {
        return currentmedication;
    }

    public int getBirthYear() {
        return born;
    }

}
