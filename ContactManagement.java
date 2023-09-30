import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class ContactManagement {
    protected static final AbstractButton searchField = null;
    DefaultTableModel table;
    JFrame frame;
    static String filePath = "C:\\Users\\Prathmesh\\OneDrive\\Desktop\\Advance Java\\Contact Management\\Contact.xml";

    ContactManager contactManager = new ContactManager(filePath);

    public void createAndShowUI() {
        frame = new JFrame("Contact Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        table = new DefaultTableModel();
        table.addColumn("Name");
        table.addColumn("Phone_Number");
        table.addColumn("Email");
        table.addColumn("DOB");
        table.addColumn("Gender");
        table.addColumn("Address");

        ArrayList<Contacts> contacts = contactManager.getContactsFromFile();
        for (Contacts contact : contacts) {
            String[] contactDetails = {
                contact.getName(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                contact.getDOb(),
                contact.getGender(),
                contact.getAddress()
            };
            table.addRow(contactDetails);
        }

        JTable Table = new JTable(table);
        JScrollPane scrollPane = new JScrollPane(Table);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                showAddContactDialog();
            }
        }); 
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        JButton searchButton = new JButton("Search");
        buttonPanel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                showSearchDialog();
            }
        });

        JButton clearButton = new JButton("Clear Search");
        buttonPanel.add(clearButton);

        JButton updateButton = new JButton("Update Contact");
        buttonPanel.add(updateButton);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selectedRow = Table.getSelectedRow();
                if (selectedRow >= 0) {
                    showUpdateDialog(selectedRow);
                }
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // AbstractButton searchField;
                searchField.setText("");
                table.setRowCount(0);
                ArrayList<Contacts> allContacts = contactManager.getContactsFromFile();
                for (Contacts contact : allContacts) {
                    String[] contactDetails = {
                        contact.getName(),
                        contact.getPhoneNumber(),
                        contact.getEmail(),
                        contact.getDOb(),
                        contact.getGender(),
                        contact.getAddress()
                    };
                    table.addRow(contactDetails);
                }
            }
        });
        JButton deleteButton = new JButton("Delete Contact"); 
        buttonPanel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selectedRow = Table.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirmDelete = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this contact?", "Delete Contact", JOptionPane.YES_NO_OPTION);
                    if (confirmDelete == JOptionPane.YES_OPTION) {
                        contactManager.deleteContact(selectedRow);
                        saveContactsToFile(contactManager.getContactsFromFile());
                        table.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a contact to delete.");
                }
            }
        });


        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }


    private void showAddContactDialog() {
        JTextField nameField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField addressField = new JTextField();

        JPanel addPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        addPanel.add(new JLabel("Name"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Phone Number"));
        addPanel.add(phoneNumberField);
        addPanel.add(new JLabel("Email"));
        addPanel.add(emailField);
        addPanel.add(new JLabel("Date of Birth"));
        addPanel.add(dobField);
        addPanel.add(new JLabel("Gender"));
        addPanel.add(genderField);
        addPanel.add(new JLabel("Address"));
        addPanel.add(addressField);

        int result = JOptionPane.showConfirmDialog(frame, addPanel, "Add Contact", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();
            String dob = dobField.getText();
            String gender = genderField.getText();
            String address = addressField.getText();

            if (!contactManager.isValidName(name) || !contactManager.isValidField(address) ||
                    !contactManager.isEmailUnique(email) || !contactManager.isPhoneNumberUnique(phoneNumber)) {
                JOptionPane.showMessageDialog(frame, "Invalid or duplicate data. Please check and try again.");
                return;
            }

            Contacts contactObj = new Contacts(name, phoneNumber, email, dob, gender, address);
            contactManager.addContact(contactObj);
            saveContactsToFile(contactManager.getContactsFromFile());

            String[] contactDetails = {name, phoneNumber, email, dob, gender, address};
            table.addRow(contactDetails);
        }
    }
    private void showSearchDialog() {
        JTextField searchField = new JTextField();

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.add(new JLabel("Enter search query:"));
        searchPanel.add(searchField);

        int result = JOptionPane.showConfirmDialog(frame, searchPanel, "Search Contacts", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                ArrayList<Contacts> searchResults = contactManager.searchContacts(query);

                table.setRowCount(0);

                for (Contacts contact : searchResults) {
                    String[] contactDetails = {
                        contact.getName(),
                        contact.getPhoneNumber(),
                        contact.getEmail(),
                        contact.getDOb(),
                        contact.getGender(),
                        contact.getAddress()
                    };
                    table.addRow(contactDetails);
                }
            }
        }
    }

    private void saveContactsToFile(ArrayList<Contacts> contacts) {
        contactManager.saveContactsToFile(contacts);
    }
    private void showUpdateDialog(int selectedRow) {
        JTextField nameField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField addressField = new JTextField();

        nameField.setText(table.getValueAt(selectedRow, 0).toString());
        phoneNumberField.setText(table.getValueAt(selectedRow, 1).toString());
        emailField.setText(table.getValueAt(selectedRow, 2).toString());
        dobField.setText(table.getValueAt(selectedRow, 3).toString());
        genderField.setText(table.getValueAt(selectedRow, 4).toString());
        addressField.setText(table.getValueAt(selectedRow, 5).toString());
    
        JPanel updatePanel = new JPanel(new GridLayout(8, 2, 10, 10));
        updatePanel.add(new JLabel("Name"));
        updatePanel.add(nameField);
        updatePanel.add(new JLabel("Phone Number"));
        updatePanel.add(phoneNumberField);
        updatePanel.add(new JLabel("Email"));
        updatePanel.add(emailField);
        updatePanel.add(new JLabel("Date of Birth"));
        updatePanel.add(dobField);
        updatePanel.add(new JLabel("Gender"));
        updatePanel.add(genderField);
        updatePanel.add(new JLabel("Address"));
        updatePanel.add(addressField);
    
        int result = JOptionPane.showConfirmDialog(frame, updatePanel, "Update Contact", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();
            String dob = dobField.getText();
            String gender = genderField.getText();
            String address = addressField.getText();
    
            if (!contactManager.isValidName(name) || !contactManager.isValidField(address)) {
                JOptionPane.showMessageDialog(frame, "Invalid data. Please check and try again.");
                return;
            }
    
            Contacts updatedContact = new Contacts(name, phoneNumber, email, dob, gender, address);
            contactManager.updateContact(selectedRow, updatedContact);
            saveContactsToFile(contactManager.getContactsFromFile());
            table.setValueAt(name, selectedRow, 0);
            table.setValueAt(phoneNumber, selectedRow, 1);
            table.setValueAt(email, selectedRow, 2);
            table.setValueAt(dob, selectedRow, 3);
            table.setValueAt(gender, selectedRow, 4);
            table.setValueAt(address, selectedRow, 5);
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ContactManagement manager = new ContactManagement();
                manager.createAndShowUI();
            }
        });
    }
}