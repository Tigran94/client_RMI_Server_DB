import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DBHelper extends Remote {


    int createUser(String firstName, String lastName) throws RemoteException;
    void addContact(int id, String phoneNumber) throws RemoteException;

    List<String> contact(String firstName, String lastName) throws RemoteException;
    List<User> listUsers() throws RemoteException;
    List<Contacts> listContacts() throws RemoteException;
}
