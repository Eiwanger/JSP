package Database;

import forum.ForumServlet;
import forum.Message;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;

public class DBHandler {

    static String dbUserName = "e1800821";
    static String dbPassword = "dislike-defect";

    static String dbName = "e1800821_forum2";
    static String dbPicTableName = "pictures";
    static String dbPostTableName = "posts";
    static String ressourcesLocation= "resources/Images/";

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
            //GZ2cPTsEvFC3
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



            PreparedStatement presPic = conn.prepareStatement("INSERT INTO " + dbPicTableName
                    + " (postID, name, image)" + " VALUES (" + postID + " ,?,?)");
            for (String s : msg.getImageLinks()) {
                String name = s.substring(s.indexOf(ressourcesLocation) + ressourcesLocation.length());
                presPic.setString(1, name);
                File file = new File(s);
                FileInputStream fis = new FileInputStream(file);
                presPic.setBlob(2,fis,file.length());
                if (presPic.executeUpdate() == 0)
                    throw new Exception("Bad Update");
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



            String picQuery = "SELECT name, image FROM " + dbPicTableName + " WHERE postID" + " LIKE ?";
            String nameQuery = "SELECT name FROM " + dbPicTableName + " WHERE postID" + " LIKE ?";

            PreparedStatement prePicList = conn.prepareStatement(picQuery);
            PreparedStatement preNameList = conn.prepareStatement(nameQuery);


            String filePath  = ForumServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            int i = filePath.indexOf("WEB-INF/");
            filePath = filePath.substring(0, i);

            while (resSet.next()) {
                LinkedList<String> picList = new LinkedList<>();
                int postID = resSet.getInt(1);

                // precheck

                preNameList.setInt(1, postID);
                ResultSet nameSet = preNameList.executeQuery();

                while(nameSet.next())
                {
                    File nameCheck = new File(filePath +ressourcesLocation + nameSet.getString("name"));
                    if(!nameCheck.exists())
                    {
                        prePicList.setInt(1, postID);
                        ResultSet picSet = prePicList.executeQuery();

                        FileOutputStream fos = null;
                        while (picSet.next()) {
                            String webfolder = "out/artifacts/web_war_exploded/";
                            File file = new File(filePath +ressourcesLocation + picSet.getString("name"));
                            fos = new FileOutputStream(file);

                            InputStream is = picSet.getBinaryStream("image");
                            byte[] imageBuffer = new byte[is.available()];
                            is.read(imageBuffer);
                            fos.write(imageBuffer);
                            fos.close();
                            is.close();
                            String path = file.getAbsolutePath();
                            picList.add(path);
                        }

                    }else
                    {
                        picList.add(nameCheck.getAbsolutePath());
                    }

                }
                messageList.add(new Message(resSet.getString("name"), resSet.getDate("date").toString(),
                        resSet.getString("body"), picList , postID));
/*
                prePicList.setInt(1, postID);
                ResultSet picSet = prePicList.executeQuery();

                FileOutputStream fos = null;
                while (picSet.next()) {
                    String webfolder = "out/artifacts/web_war_exploded/";
                    File file = new File(filePath +ressourcesLocation + picSet.getString("name"));
                    fos = new FileOutputStream(file);

                    InputStream is = picSet.getBinaryStream("image");
                    // Here we reserve memory area to read the image
                    // content.
                    byte[] imageBuffer = new byte[is.available()];
                    // Here we read the image data from the database to
                    // the memory area.
                    is.read(imageBuffer);
                    // Here write the image data from memory to the
                    // file.
                    fos.write(imageBuffer);
                    // Here we close the output and input streams.
                    fos.close();
                    is.close();

                    //int i = file.getAbsolutePath().indexOf("resources/Images/");
                    String path = file.getAbsolutePath();
                    picList.add(path);
                }
                messageList.add(new Message(resSet.getString("name"), resSet.getDate("date").toString(),
                        resSet.getString("body"), picList , postID));
                        */
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
