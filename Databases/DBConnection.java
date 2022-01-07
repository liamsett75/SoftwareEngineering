package Databases;

import java.sql.*;

public class DBConnection{

    private static DBConnection dbInstance;
    private static Connection connection; //connection object
    //with singleton make this global
    //doesnt need to be abstract
   // best method
       //     singleton helper
    //regular class not abstract
    //only want to close the connection at the end of the application

    //Docs for connection

    //make undo button
    //ask user if they want to save changes
    //so auto commit would be off
    //click delete node and it deleted it from table
    //an then save changes would actually delter it from the database


    private DBConnection()
    {}


    /**
     *
     * @return Connection object
     */
    public static Connection getConnection() {

        try {
            // connects to the database
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
        }
        catch (SQLException e) {
//            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }

        return connection;
    }

    /**
     *
     * @return dbInstance
     */
    public static DBConnection getInstance() {
        if (dbInstance == null) {
            dbInstance = new DBConnection();
//            System.out.println("CREATED NEW CONNECTION");
        }
        else
        {
//            System.out.println("existing connection");
        }
        return dbInstance;
    }


    /**
     * {}
     * void connection() - connects to the database, class stores the connection object
     */
//    static void openConnection(){
//        try {
//            // connects to the database
//            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
//            connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
//        }
//        catch (SQLException e) {
//            System.out.println("Connection failed. Check output console.");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * void closeConnection() - closes connection to database
//     */
//    static void closeConnection(){
//        try {
//            connection.close();
//        }
//        catch (SQLException e){
//            System.out.println("Connection failed. Check output console.");
//            e.printStackTrace();
//        }
//    }
}

