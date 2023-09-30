import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ContactManager {
    private String filePath;

    public ContactManager(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Contacts> getContactsFromFile() {
        ArrayList<Contacts> contacts = new ArrayList<>();
        StringBuilder xmlContent = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                xmlContent.append(line).append("\n");
            }
            reader.close();
            String xmlData = xmlContent.toString();

            String contactRegex = "<contact>([\\s\\S]*?)</contact>";
            Pattern contactPattern = Pattern.compile(contactRegex);
            Matcher contactMatcher = contactPattern.matcher(xmlData);

            while (contactMatcher.find()) {
                String contact = contactMatcher.group(1);

                String nameRegex = "<name>([\\w\\s]+)</name>";
                String phoneRegex = "<phone_number>(\\d{10})</phone_number>";
                String emailRegex = "<email>([A-Za-z_]([\\.A-Za-z0-9\\+-_]+)*@([A-Za-z_]([A-Za-z0-9-])*)(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z0-9]{2,}))</email>";
                String dobRegex = "<dob>(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-\\d{4}</dob>";
                String genderRegex = "<gender>([\\w\\s]+)</gender>";
                String addressRegex = "<address>(.+?)</address>";
                Pattern namePattern = Pattern.compile(nameRegex);
                Pattern phonePattern = Pattern.compile(phoneRegex);
                Pattern emailPattern = Pattern.compile(emailRegex);
                Pattern dobPattern = Pattern.compile(dobRegex);
                Pattern genderPattern = Pattern.compile(genderRegex);
                Pattern addressPattern = Pattern.compile(addressRegex);
                Matcher nameMatcher = namePattern.matcher(contact);
                Matcher phoneMatcher = phonePattern.matcher(contact);
                Matcher emailMatcher = emailPattern.matcher(contact);
                Matcher dobMatcher = dobPattern.matcher(contact);
                Matcher genderMatcher = genderPattern.matcher(contact);
                Matcher addressMatcher = addressPattern.matcher(contact);

                if (nameMatcher.find() && phoneMatcher.find() && emailMatcher.find() &&
                        dobMatcher.find() && genderMatcher.find() && addressMatcher.find()) {
                    String name = nameMatcher.group(1);
                    String phone = phoneMatcher.group(1);
                    String email = emailMatcher.group(1);
                    String dob = dobMatcher.group(1);
                    String gender = genderMatcher.group(1);
                    String address = addressMatcher.group(1);
                    Contacts contactObj = new Contacts(name, phone, email, dob, gender, address);
                    contacts.add(contactObj);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return contacts;
    }

    public void saveContactsToFile(ArrayList<Contacts> contacts) {
        try {
            File inputFile = new File(filePath);
            File tempFile = new File("temp.xml");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
                if (line.trim().equals("</contact_list>")) {
                    for (Contacts contact : contacts) {
                        writer.write(" <contact>\n");
                        writer.write("    <name>" + contact.getName() + "</name>\n");
                        writer.write("    <phone_number>" + contact.getPhoneNumber() + "</phone_number>\n");
                        writer.write("    <email>" + contact.getEmail() + "</email>\n");
                        writer.write("    <dob>" + contact.getDOb() + "</dob>\n");
                        writer.write("    <gender>" + contact.getGender() + "</gender>\n");
                        writer.write("    <address>" + contact.getAddress() + "</address>\n");
                        writer.write("  </contact>\n");
                    }
                }
            }

            reader.close();
            writer.close();

            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                System.out.println("Contact list saved successfully!");
            } else {
                System.out.println("Failed to update contact list.");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean isEmailUnique(String email) {
        ArrayList<Contacts> contacts = getContactsFromFile();
        for (Contacts contact : contacts) {
            if (contact.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPhoneNumberUnique(String phoneNumber) {
        ArrayList<Contacts> contacts = getContactsFromFile();
        for (Contacts contact : contacts) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidName(String name) {
        return name.matches("^[A-Za-z\\s]+$");
    }

    public boolean isValidField(String field) {
        return field.matches("^(?!.*\\s{2,}).*$");
    }

    public void addContact(Contacts contact) {
        ArrayList<Contacts> contacts = getContactsFromFile();
        contacts.add(contact);
        saveContactsToFile(contacts);
    }

    public void deleteContact(int selectedRow) {
        ArrayList<Contacts> contacts = getContactsFromFile();
        if (selectedRow >= 0 && selectedRow < contacts.size()) {
            contacts.remove(selectedRow);
            saveContactsToFile(contacts);
        }
    }

    public void updateContact(int selectedRow, Contacts updatedContact) {
        ArrayList<Contacts> contacts = getContactsFromFile();
        if (selectedRow >= 0 && selectedRow < contacts.size()) {
            contacts.set(selectedRow, updatedContact);
            saveContactsToFile(contacts);
        }
    }
    public ArrayList<Contacts> searchContacts(String query) {
        ArrayList<Contacts> searchResults = new ArrayList<>();
        ArrayList<Contacts> contacts = getContactsFromFile(); 
        for (Contacts contact : contacts) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase()) ||
                contact.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                contact.getPhoneNumber().contains(query)) {
                searchResults.add(contact);
            }
        }
        return searchResults;
    }
    
    

}
