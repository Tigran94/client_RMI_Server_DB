import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to contacts");
        System.out.println("Enter command");

        while (true) {
            System.out.println("(C) -> Create user");
            System.out.println("(A) -> Add contact");
            System.out.println("(L) -> List users");
            System.out.println("(S) -> Search contact by name");
            System.out.println("(LC) -> List of Contacts");
            System.out.println("(Q) -> Quit");

            String command = scanner.nextLine();
            switch (command) {
                case "C":
                    createUser();
                    break;
                case "A":
                    addContact();
                    break;
                case "L":
                    listUsers();
                    break;
                case "S":
                    contacts();
                    break;
                case "LC":
                    listContacts();
                    break;
                case "Q":
                    return;
            }
        }
    }

    private static void addContact() {
        System.out.print("UserID: ");
        int userID = Integer.parseInt(scanner.nextLine());

        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();

        DBHelper dbHelper = null;
        try {
            dbHelper = (DBHelper) Naming.lookup("rmi://localhost/unknown");
            dbHelper.addContact( userID, phoneNumber);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(
                MessageFormat.format(
                            "User {0} created successfully",
                        phoneNumber
                )
        );
    }

    private static void contacts() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        DBHelper dbHelper = null;
        List<String> contacts = null;

        try {
            dbHelper = (DBHelper) Naming.lookup("rmi://localhost/unknown");
            contacts = dbHelper.contact( firstName, lastName);
            for (String contact : contacts) {
                System.out.println(contact);
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void createUser() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        DBHelper dbHelper = null;
        int userId = 0;

        try {
            dbHelper = (DBHelper) Naming.lookup("rmi://localhost/unknown");
            userId = dbHelper.createUser( firstName, lastName);

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (userId != -1) {
            System.out.println(
                    MessageFormat.format(
                            "Phone Number: {0} created successfully",
                            userId
                    )
            );
        } else {
            System.out.println(
                    "Error while creating the user"
            );
        }
    }
    private static void listContacts() {
        DBHelper dbHelper = null;
        try {
            dbHelper = (DBHelper) Naming.lookup("rmi://localhost/unknown");
            List<Contacts> contactsList = dbHelper.listContacts();
            if (contactsList == null) return;

            for (Contacts contact : contactsList) {
                System.out.println(contact);
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void listUsers() {
        DBHelper dbHelper = null;
        try {
            dbHelper = (DBHelper) Naming.lookup("rmi://localhost/unknown");
            List<User> users = dbHelper.listUsers();
            if (users == null) return;

            for (User user : users) {
                System.out.println(user);
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
