package login;

import forum.ForumServlet;
import forum.MessageController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginController {

    private static String fileName = "userData.xml";
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
                + separator + folder + separator+ fileName;
        userDataFile = new File(userDataPath);
        try {

            if (!userDataFile.exists()) {

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Node root = doc.createElement("Users");
                doc.appendChild(root);

                MessageController.saveXMLFile(doc, userDataPath);
            }
        } catch (ParserConfigurationException p) {
            p.printStackTrace();
        }

    }

    private static void addUserToFile(String username, String passwordHash) {
/*        String log = username + "   " + passwordHash + "\n";

        try {
            fileWriter = new FileWriter(userDataPath, true);
            fileWriter.write(log);
            fileWriter.close();
        } catch (IOException e) {

        }
*/
        Node user = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(userDataPath);
            Node root = doc.getDocumentElement();
            user = doc.createElement("user");

            ((Element) user).setAttribute("username", username);
            ((Element) user).setAttribute("password", passwordHash);




            root.appendChild(user);
            MessageController.saveXMLFile(doc, userDataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean registUser(String nUserName, String nPassword) throws UserInputException {

        if (!checkIfEmpty(nUserName) && !checkIfEmpty(nPassword)) {
            throw new UserInputException("Input empty");
        }
        if (UserLogin.containsKey(nUserName)) {
            throw new UserInputException("User already exists");
        }
        if(nUserName.length() < 3)
        {
            throw new UserInputException("Username to short! Write at least 3 letters!");
        }
        if(!nUserName.matches("^[a-zA-Z0-9]*$")){
            throw new UserInputException("No special letters allowed in Username");
        }
        if(nPassword.length() < 4)
        {
            throw new UserInputException(("Unsafe password! Use at least 4 letters"));
        }

        addUser(nUserName, nPassword);
        return true;
    }

    private static void addUser(String user, String password) {

        UserLogin.put(user, encryptPassword(password));
        addUserToFile(user, encryptPassword(password));
    }

    protected static void collectData() {
/*        String tmp;
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
*/
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(userDataPath);
            Node root = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tmp = nodeList.item(i);
                if (tmp.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element)tmp;
                    UserLogin.put(element.getAttribute("username"), element.getAttribute("password"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

// encrypt the password (test this function)
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

    // create the servlet, because I need it more than once
    public static String createServlet(String action, String username, String password) {
        String body = "";
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
            body += ("<td >Password is empty</td></tr>");
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
