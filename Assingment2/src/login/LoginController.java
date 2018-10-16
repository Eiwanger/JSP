package login;

import forum.ForumServlet;

import java.io.*;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginController {

    private static String fileName = "userData.txt";
    private static String userDataPath;
    private static File userDataFile;
    private static String folder = "UserData";
    private static String separator;

    private static FileWriter fileWriter;
    private static FileReader fileReader;

    // private static UserLogin userLogin = new UserLogin();
    private static HashMap<String, String> UserLogin = new HashMap<String, String>();

    public static void initUserDataFile() {
        separator = System.getProperty("file.separator");
        userDataPath = LoginController.class.getProtectionDomain().getCodeSource().getLocation().getPath()
                + separator + folder + separator;
        userDataFile = new File(userDataPath);
        if (!userDataFile.exists()) {
            userDataFile.mkdir();
        }
        userDataPath = userDataFile + separator + fileName;
    }

    private static void addUserToFile(String username, String passwordHash) {
        String log = username + "   " + passwordHash + "\n";

        try {
            fileWriter = new FileWriter(userDataPath, true);
            fileWriter.write(log);
            fileWriter.close();
        } catch (IOException e) {

        }

    }

    public static boolean registUser(String nUserName, String nPassword) throws UserInputException {

        if (!checkIfEmpty(nUserName) && !checkIfEmpty(nPassword)) {
            throw new UserInputException("Input empty");
        }
        if (UserLogin.containsKey(nUserName)) {
            throw new UserInputException("User already exists");
        }

        addUser(nUserName, nPassword);
        return true;
    }

    private static void addUser(String user, String password) {

        UserLogin.put(user, encryptPassword(password));
        addUserToFile(user, encryptPassword(password));
    }

    protected static void collectData() {
        String tmp;
        int border;
        try {
            fileReader = new FileReader(userDataPath);
            BufferedReader buffR = new BufferedReader(fileReader);
            while ((tmp = buffR.readLine()) != null) {
                border = tmp.indexOf("   ");
                UserLogin.put(tmp.substring(0, border), tmp.substring(border + 3));
            }
        } catch (IOException e) {

        }
    }

    public static boolean checkUsername(String username) {
        return UserLogin.containsKey(username);
    }

    public static boolean checkIfEmpty(String parameter) {
        if (parameter != null && !parameter.equals("") && !parameter.equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean checkLogin(String username, String password) {
        if (!checkIfEmpty(password) || !checkIfEmpty(username)) {
            return false;
        }
        if (UserLogin.containsKey(username) && UserLogin.containsValue(encryptPassword(password))) {
            return UserLogin.get(username).equals(encryptPassword(password));
        } else {
            return false;
        }
    }


    public static String encryptPassword(String passwordToHash) {

        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }

    public static String createServlet(String action, String username, String password) {
        String body = "";
        // out.println("<div class='user_form'>");
        body += ("<h2>Login</h2>");
        // Here we set the value for method to post, so that
        // the servlet service method calls doPost in the
        // response to this form submit

        body += ("<form method='POST' + action='login.html'>");
        body += ("<table style='width=100%'><tr><td>");
        body += ("User Name: </td><td ");

        if (action != null && !LoginController.checkIfEmpty(username)) {
            body += (" style='background-color:red;'");
            body += ("><input type='text' name='userName' value='" + (username == null ? "" : username) + "' size='40'></td>");
            body += ("<td >Username is empty</td></tr>");
        } else {
            body += ("><input type='text' name='userName' value='" + (username == null ? "" : username) + "' size='40'></td></tr>");
        }
        body += ("<tr><td >Password: </td><td ");

        if (action != null && !LoginController.checkIfEmpty(password)) {
            body += (" style='background-color:red;'");
            body += ("><input type='password' name='password' value='" + (password == null ? "" : password) + "' size='40'></td>");
            body += ("<td >Password was empty</td></tr>");
        } else {
            body += ("><input type='password' name='password' value='" + (password == null ? "" : password) + "' size='40'></td></tr>");
        }
        body += ("<tr><td></td><td><input type='submit' name='action' value='Login'></td></tr>");
        body += ("</table>");
        body += ("<h3>Register here for a new account</h3>" +
                "    <table>" +
                "        <tr><td></td><td><input type='submit' name='action' VALUE='Register'> </td></tr>" +
                "    </table>");
        body += ("</form>");

        return body;
    }
}
