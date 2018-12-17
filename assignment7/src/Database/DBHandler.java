package Database;

import feedback.Feedback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBHandler {

    static String dbUserName = "e1800820";
    static String dbPassword = "GZ2cPTsEvFC3";
    static String dbName = "e1800820_feedback";
    static String dbFeedbackTableName = "formdata";

    public static void AddFeedbackToDataBase(Feedback fb) {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);

            PreparedStatement pres = conn.prepareStatement("INSERT INTO " + dbFeedbackTableName +
                    " (username, firstname, surname, email, phonenumber, feedback) " +
                    " VALUES ( ?,?,?,?,?,?);");

            pres.setString(1, fb.getUsername());
            pres.setString(2, fb.getFirstname());
            pres.setString(3, fb.getSurname());
            pres.setString(4, fb.getEmail());
            pres.setString(5, fb.getPhonenumber());
            pres.setString(6, fb.getFeedback());

            stmt = conn.createStatement();

            if (pres.executeUpdate() == 0) {
                throw new Exception("Bad Update");
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            closeConnection(conn, stmt);

        }
    }


    public static List<Feedback> getFeedbackFromDB(String userName) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        Feedback feed = null;
        List<Feedback> feedList = new LinkedList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);

            String query = "SELECT * FROM " + dbFeedbackTableName + " WHERE username LIKE '" + userName + "';";

            // Here we create the statement object for executing SQL commands.
            stmt = conn.createStatement();

            ResultSet resSet = stmt.executeQuery(query);
            while(resSet.next()) {
                feed = new Feedback(resSet.getString("firstname"), resSet.getString("surname"),
                        resSet.getString("email"), resSet.getString("phonenumber"),
                        resSet.getString("feedback"), resSet.getString("username"));
                feedList.add(feed);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);

        }

        return feedList;
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
            PreparedStatement presDelFeedback = conn.prepareStatement("TRUNCATE TABLE " + dbFeedbackTableName + ";");
            presDelFeedback.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stmt);

        }
    }

}
