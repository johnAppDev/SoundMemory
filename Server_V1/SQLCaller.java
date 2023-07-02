import java.sql.*;

//import com.mysql.cj.PerConnectionLRUFactory;
//import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

public class SQLCaller {

    private static String connectionStr;
    private static Connection connection;
    private static Statement statement;

    // initializes common varialbles
    public SQLCaller() throws SQLException {
        connectionStr = "jdbc:mysql://localhost:3306/main";

        connection = DriverManager.getConnection(connectionStr, "root", "root");

        statement = connection.createStatement();

    }

    // checks to see if the name is in the database
    public static boolean checkName(String name) throws SQLException {

        ResultSet resultSet = statement.executeQuery("select * from t");

        while (resultSet.next()) {
            if (name.equals(resultSet.getString(1))) {
                return true;
            }
            System.out.println(resultSet.getString(1));
        }
        return false;

    }

    // checks to see if the name and pass exist, and if they match;
    public static boolean checkPass(String pass, String name) throws SQLException {

        boolean passBool = false;
        boolean nameBool = false;
        int count = 0;
        int nameCount = 0;
        ResultSet resultSet = statement.executeQuery("select * from t");
        System.out.println(pass + " " + name);
        // loops through the results given by resultset to find the password that
        // matches pass, finds the index of the password that matches
        while (resultSet.next()) {
            if (pass.equals(resultSet.getString(2))) {
                passBool = true;
            } else if (passBool != true) {
                count++;
                System.out.println(resultSet.getString(2));
            }

        }
        resultSet = statement.executeQuery("select * from t");
        // checks if the name equals a name in the database, and that it matches with
        // the password given
        while (resultSet.next()) {
            if (name.equals(resultSet.getString(1)) && nameCount == count) {
                nameBool = true;
                // System.out.println(resultSet.getString(1));
            } else {
                nameCount++;
                System.out.println(resultSet.getString(1));
            }

        }
        // checks if name and pass are both true
        if (nameBool && passBool) {
            return true;
        } else {
            return false;
        }

    }

    // creates user
    public static User createUser(String username) throws SQLException {

        ResultSet resultSet = statement.executeQuery("select * from t");
        // default settings
        String name = "tony";
        int height = 0;
        int weight = 0;
        String birthdate = "03/03/2007";
        String caretakername = "somebody";
        String illnesses = "example1";
        String allergies = "example2";
        String additionalnotes = "example3";
        String emergencycontacts = "example4";
        int roomnumber = 0;
        String currentmedication = "example5";

        int birthyear = 0;
        //get birthyear of user
        while (resultSet.next()) {
            if (resultSet.getString(1).equals(username)) {
                birthyear = resultSet.getInt(3);
                System.out.println(resultSet.getInt(3));
            }
        }
        //get all other information of the user
        resultSet = statement.executeQuery("select * from patientinformation");
        while (resultSet.next()) {
            if (resultSet.getString(1).equals(username)) {
                name = resultSet.getString(2);
                height = resultSet.getInt(3);
                weight = resultSet.getInt(4);
                birthdate = resultSet.getString(5);
                caretakername = resultSet.getString(6);
                illnesses = resultSet.getString(7);
                allergies = resultSet.getString(8);
                additionalnotes = resultSet.getString(9);
                emergencycontacts = resultSet.getString(10);
                roomnumber = resultSet.getInt(11);
                currentmedication = resultSet.getString(12);
            }
        }
        System.out.println(name + height + weight + birthdate + caretakername + illnesses + allergies + additionalnotes
                + emergencycontacts + roomnumber + currentmedication);
        //returns the User with all of the new data.
        return new User(name, birthyear, height, weight, birthdate, caretakername, illnesses, allergies,
                additionalnotes, emergencycontacts, roomnumber, currentmedication);

    }
    //gets the song Names
    public static String getSongsNames(int birthyear) throws SQLException {
        String result = "";
        ResultSet resultSet = statement.executeQuery("select * from songs");
        //finds songs in the correct age range
        while (resultSet.next()) {
            if (resultSet.getInt(2) <= birthyear + 15 && resultSet.getInt(2) >= birthyear + 10) {
                result += resultSet.getString(1) + ",";
            }

        }
        return result.substring(0, result.length() - 1);
    }
//adds a user to the database
    public static void addUser(String name, String pass, int yearOfBirth) throws SQLException {
        System.out.println("something new");
        String statement = " insert into t (name,pass,birthyear)" + " values (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, pass);
        preparedStatement.setInt(3, yearOfBirth);
        System.out.println("this is new");
        preparedStatement.execute();
        System.out.println("soensohosoeshoesho");
        statement = " insert into patientinformation (username,name,height,weight,birthdate,caretakername,illnesses,allergies,additionalnotes,emergencycontacts,roomnumber,currentmedication)"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, "");
        preparedStatement.setFloat(3, 0);
        preparedStatement.setFloat(4, 0);
        preparedStatement.setString(5, "");
        preparedStatement.setString(6, "");
        preparedStatement.setString(7, "");
        preparedStatement.setString(8, "");
        preparedStatement.setString(9, "");
        preparedStatement.setString(10, "");
        preparedStatement.setInt(11, 0);
        preparedStatement.setString(12, "");

        preparedStatement.execute();
    }
    //updates patient information
    public static void addpatientinformation(String[] patientinformation, String username) throws SQLException {
        try {
            for (String patient : patientinformation) {
                System.out.println(patient + "");
            }
            int currentprofile = 0;
            ResultSet resultSet1 = statement.executeQuery("select * from t");
            while (resultSet1.next()) {
                if (username.equals(resultSet1.getString(1))) {
                    currentprofile = resultSet1.getInt(4);
                    // System.out.println(resultSet.getString(2));
                }
            }
            String statement1 = "UPDATE t SET birthyear = ? WHERE id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(statement1);
            preparedStatement1.setInt(1, Integer.parseInt(patientinformation[3].substring(6)));
            preparedStatement1.setInt(2, currentprofile);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
            ResultSet resultSet = statement.executeQuery("select * from patientinformation");

            int currentId = 0;
            while (resultSet.next()) {
                if (username.equals(resultSet.getString(1))) {
                    currentId = resultSet.getInt(13);
                    System.out.println(Integer.toString(currentId));
                    // System.out.println(resultSet.getString(2));
                }
            }

            String statement = "UPDATE patientinformation SET name = ?, height = ?, weight = ?, birthdate = ?, caretakername = ?, illnesses = ?, allergies = ?, additionalnotes = ?, emergencycontacts = ?, roomnumber = ?, currentmedication = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, patientinformation[0]);
            preparedStatement.setDouble(2, Double.parseDouble(patientinformation[1]));
            preparedStatement.setDouble(3, Double.parseDouble(patientinformation[2]));
            preparedStatement.setString(4, patientinformation[3]);
            preparedStatement.setString(5, patientinformation[4]);
            preparedStatement.setString(6, patientinformation[5]);
            preparedStatement.setString(7, patientinformation[6]);
            preparedStatement.setString(8, patientinformation[7]);

            preparedStatement.setString(9, patientinformation[8]);

            preparedStatement.setInt(10, Integer.parseInt(patientinformation[9]));

            preparedStatement.setString(11, patientinformation[10]);
            preparedStatement.setInt(12, currentId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            resultSet.close();
            /*
             * String statement =
             * " update patientinformation set height = 69 where roomnumber = 0";
             * PreparedStatement preparedStatement = connection.prepareStatement(statement);
             * preparedStatement.executeUpdate();
             * System.out.println(Integer.parseInt(patientinformation[3].substring(6)));
             * 
             * 
             * // preparedStatement.setInt(1,
             * // Integer.parseInt(patientinformation[3].substring(6)));
             * // preparedStatement.setString(2, username);
             * // preparedStatement.executeUpdate();
             * /*
             * shtshtashtasht
             * statement = " update patientinformation set name = ? ";
             * System.out.println("heoseosh");
             * PreparedStatement preparedStatement1 =
             * connection.prepareStatement(statement);
             * preparedStatement1.setString(1, patientinformation[0]);
             * // preparedStatement1.setString(2, username);
             * preparedStatement1.executeUpdate();
             * statement = "update patientinformation set height = ?";
             * PreparedStatement preparedStatement2 =
             * connection.prepareStatement(statement);
             * preparedStatement2.setFloat(1, Float.parseFloat(patientinformation[1]));
             * // preparedStatement2.setString(2, username);
             * preparedStatement2.executeUpdate();
             * /*
             * preparedStatement1.setFloat(2, Float.parseFloat(patientinformation[1]));
             * preparedStatement1.setFloat(3, Float.parseFloat(patientinformation[2]));
             * preparedStatement1.setString(4, patientinformation[3]);
             * preparedStatement1.setString(5, patientinformation[4]);
             * preparedStatement1.setString(6, patientinformation[5]);
             * preparedStatement1.setString(7, patientinformation[6]);
             * preparedStatement1.setString(8, patientinformation[7]);
             * preparedStatement1.setString(9, patientinformation[8]);
             * preparedStatement1.setInt(10, Integer.parseInt(patientinformation[9]));
             * preparedStatement1.setString(11, patientinformation[10]);
             * preparedStatement1.setString(12, username);
             * preparedStatement1.executeUpdate();
             * System.out.println("somethmore");
             */
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}