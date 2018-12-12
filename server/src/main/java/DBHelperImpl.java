import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelperImpl extends UnicastRemoteObject implements DBHelper{
    private Connection connection = null;
    protected DBHelperImpl() throws RemoteException {
    }
    private Connection setDBConnection(){
        if(connection==null){
            connection =  DBConnection.getConnection();
            return connection;
        }else return connection;
    }
    public int createUser(String firstName, String lastName) throws RemoteException {
        setDBConnection();

        String sql = "insert into human (firstName, lastName) values (?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,firstName);
            statement.setString(2,lastName);
            statement.execute();

            String idSql = "select max(id) from human";
            Statement idStatement = connection.createStatement();
            ResultSet resultSet = idStatement.executeQuery(idSql);
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void addContact(int id, String phoneNumber) throws RemoteException {
        setDBConnection();

        String sql = "insert into phone (id, phoneNumber) values (?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            statement.setString(2,phoneNumber);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> contact(String firstName, String lastName) throws RemoteException {
        setDBConnection();

        String sql = "SELECT phone.phoneNumber FROM human INNER JOIN phone on human.id = phone.id "+
                "WHERE human.firstName=? AND human.lastName=?;";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,firstName);
            statement.setString(2,lastName);
            ResultSet resultSet = statement.executeQuery();
            List<String> phoneNumbers = new ArrayList<>();
            while (resultSet.next()) {
                String phoneNumber = resultSet.getString("phoneNumber");
                phoneNumbers.add(phoneNumber);
            }
            return phoneNumbers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> listUsers() throws RemoteException{
        setDBConnection();

        String sql = "select * from human";

        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);

            List<User> userList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                userList.add(new User(
                        id, firstName, lastName
                ));
            }
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Contacts> listContacts() throws RemoteException{
        setDBConnection();

        String sql = "SELECT human.firstName,human.lastName,phone.phoneNumber "+
                "FROM human INNER JOIN phone on human.id = phone.id";

        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);

            List<Contacts> contactsList = new ArrayList<>();

            while (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String phoneNumber = resultSet.getString("phoneNumber");

                contactsList.add(new Contacts(
                        firstName, lastName, phoneNumber
                ));
            }
            return contactsList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
