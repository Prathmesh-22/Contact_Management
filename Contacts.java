import java.util.Comparator;

public class Contacts implements Comparable<Contacts> {
     private String name;
    private String phoneNumber;
    private String email;
    private String dob;
    private String gender;
    private String address;
    // private String 

    Contacts(String name,String phoneNumber,String email,String dob,String gender,String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
    }

    public String getName() { return this.name;}
    public String getPhoneNumber() { return this.phoneNumber;}
    public String getEmail() { return this.email;}
    public String getDOb() { return this.dob;}
    public String getGender() { return this.gender;}
    public String getAddress() { return this.address;}

    @Override
    public int compareTo(Contacts other) {
        return this.name.compareTo(other.name);
    }
    @Override
    public String toString() {
        return "Contacts: [" + this.name + ", " + this.phoneNumber + "]";
    }
    public static final Comparator<Contacts> BY_NAME = Comparator.comparing(Contacts::getName);
}
