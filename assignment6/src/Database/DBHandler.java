package Database;

import forum.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class DBHandler {

    static String dbUserName = "e1800821";
    static String dbPassword = "dislike-defect";

    static String dbName = "e1800821_forum1";
    static String dbPicTableName = "pictures";
    static String dbPostTableName = "posts";


    public static void AddMessageToDataBase(Message msg) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            // Here we load the database driver for Oracle database
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            // For mySQL database the above code would look like this:

            Class.forName("com.mysql.jdbc.Driver");
            // Here we set the JDBC URL for the Oracle database
            //String url = "jdbc:oracle:thin:@db.cc.puv.fi:1521:ora817";
            // For mySQL database the above code would look like
            // something this.
            // Notice that here we are accessing mg_db database

            String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;

            // Here we create a connection to the database
            //conn = DriverManager.getConnection(url, "scott", "tiger");
            // For mg_db mySQL database the above code would look like
            // the following:

            conn = DriverManager.getConnection(url, dbUserName, dbPassword);

            PreparedStatement pres = conn.prepareStatement("INSERT INTO " + dbPostTableName + " (name, date, body) " +
                    "VALUES ( ?,?,?);", Statement.RETURN_GENERATED_KEYS);

            pres.setString(1, msg.getName());
            pres.setString(2, msg.getDate()); // fixme change string to Date
            pres.setString(3, msg.getMessage());

            // Here we create the statement object for executing SQL commands.
            stmt = conn.createStatement();

            // Here we execute SQL query and save the results to a ResultSet
            // object
            //resultSet = stmt.executeQuery(query);
            if (pres.executeUpdate() == 0) {
                throw new Exception("Bad Update");
            }
            ResultSet resSet = pres.getGeneratedKeys();
            resSet.next();

            int postID = resSet.getInt(1);

            PreparedStatement presPic = conn.prepareStatement("INSERT INTO " + dbPicTableName + " (postID, path)" + " VALUES (" + postID + ",?)");
            for (String s : msg.getImageLinks()) {
                presPic.setString(1, s);
                if (presPic.executeUpdate() == 0) {
                    throw new Exception("Bad Update");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);

        }
    }

    public static ArrayList<Message> getMessagesFromDB() {
        ArrayList<Message> messageList = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);

            String query = "SELECT * FROM " + dbPostTableName;

            // Here we create the statement object for executing SQL commands.
            stmt = conn.createStatement();

            ResultSet resSet = stmt.executeQuery(query);

            String picQuery = "SELECT path FROM " + dbPicTableName + " WHERE postID" + " LIKE ?";
            PreparedStatement prePicList = conn.prepareStatement(picQuery);

            while (resSet.next()) {
                LinkedList<String> picList = new LinkedList<>();
                int postID = resSet.getInt(1);

                prePicList.setInt(1, postID);

                ResultSet picSet = prePicList.executeQuery();

                while (picSet.next()) {
                    picList.add(picSet.getString("path"));
                }
                messageList.add(new Message(resSet.getString("name"), resSet.getDate("date").toString(),
                        resSet.getString("body"), picList , postID));
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);

        }

        return messageList;
    }

    private static void closeConnection(Connection conn, Statement stmt) {
        try {
            // Here we close all open streams
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
        }
    }

    public static void deleteDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMeatData = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();


            // Here we create the statement object for executing SQL commands.

            // Here we execute SQL query and save the results to a ResultSet
            // object
            //resultSet = stmt.executeQuery(query);
            PreparedStatement presDelPosts = conn.prepareStatement("TRUNCATE TABLE " + dbPostTableName + ";");
            PreparedStatement presDelPics = conn.prepareStatement("TRUNCATE TABLE " + dbPicTableName + ";");
            presDelPics.executeUpdate();
            presDelPosts.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);

        }
    }

}
