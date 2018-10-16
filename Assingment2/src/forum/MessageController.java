package forum;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MessageController {

    private static FileWriter fileWriter;
    private static FileReader fileReader;

    final static String folder = "logger";
    public static String separator;

    static String path;
    private static File logFile;

    private static String fileName = "forumLog.txt";

    private static String filepath;

    private static ArrayList<Message> MessageList = new ArrayList<>();

    public static void initLogFile() {
        separator = System.getProperty("file.separator");
        path = ForumServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath() + separator + folder + separator;
        //logFile = new File(path);

        logFile = new File(path);
        try {
            if (!logFile.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Node root = doc.createElement("posts");
                doc.appendChild(root);
                filepath = logFile + separator + fileName;
                saveXMLFile(doc);
            }
        } catch (ParserConfigurationException p) {
            p.printStackTrace();
        }

    }

    public static void saveXMLFile(Document doc) {
        try {
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            /*String filePathXSL = f.toString().replaceAll("classes", "strip-space.xsl");*/
            Transformer transformer = transformerFactory.newTransformer(/*new StreamSource(filePathXSL)*/);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (TransformerException e) {
        }
    }


    public static String printSearchedMessages(ArrayList<Message> msgList, String input) {
        String returnString = "";
        returnString += "<html>";
        returnString += "<head><link rel=\"stylesheet\" href=\"styles.css\"></head>";
        returnString += "<body>";
        returnString += "<table><tbody>";
        if (msgList.isEmpty()) {
            returnString = "<p> no results for your search </p>";
        } else {
            returnString = "<p>Results for " + "\"" + input + "\"" + "</p>";
            for (Message m : msgList) {

                returnString += singleOutput(m);
            }
            returnString += "</tbody></table><br>";


        }
        returnString += "<br><a href='forum.html'>Back</a>";
        returnString += "</body>";
        returnString += "</html>";

        return returnString;
    }

    private static String singleOutput(Message m) {
        String returnString = "";
        returnString += "<tr><td class=\"poststyle\">";
        returnString += "<h3>" + m.getName() + "</h3></td><td class=\"poststyle\">" + m.getMessage() + "</td></tr>\n";

        returnString += "<tr><td class=\"poststyle\">" + m.getDate() + "</td></tr>\n";

        if (!m.getFavViews().isEmpty()) {
            returnString += "<tr><td></td><td class=\"poststyle\"> Favourite Views: ";
            returnString += m.getFavViews() + "</td></tr>";
        }
        if (!m.getFavSports().isEmpty()) {
            returnString += "<tr><td></td><td class=\"poststyle\">Favourite Sports: ";
            returnString += m.getFavSports() + "</td></tr>";
        }
        returnString += "\n\n";
        return returnString;
    }

    public static String printAllMessages() {
        String returnString = "";
        returnString += "<html>";
        returnString += "<head><link rel=\"stylesheet\" href=\"styles.css\"></head>";
        returnString += "<body>";
        returnString += "<table><tbody>";

        if(MessageList.isEmpty())
        {
            returnString += "<p>no posts available</p>";
        }
        else
        {
            for(Message msg : MessageList)
            {
               returnString += singleOutput(msg);
            }
        }

        /*
        if (!readLogFile().isEmpty()) {
            returnString += readLogFile();
        } else {
            returnString += "<p>no posts available</p>";
        }
        */
        returnString += "</tbody></table><br>";

        returnString += "<br><a href='forum.html'>Back</a>";
        returnString += "</body>";
        returnString += "</html>";

        return returnString;
    }

    public static void addToList(Message msg) {
        MessageList.add(msg);
    }

    public static void deletePostList() {
        MessageList.clear();

        try {
            java.lang.Runtime.getRuntime().exec("rm -f " + logFile.getAbsolutePath() + separator + fileName);
            System.out.println(logFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // setting the current date
    public static String setCurrentDate() {

        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    // Search methods
    public static ArrayList<Message> searchName(String input) {
        ArrayList<Message> searchResults = new ArrayList<>();
        if (!input.isEmpty()) {
            for (Message m : MessageController.MessageList
            ) {
                if (m.getName().contains(input)) {
                    searchResults.add(m);
                }
            }
        }
        return searchResults;
    }

    public static ArrayList<Message> searchDate(String input) {
        ArrayList<Message> searchResults = new ArrayList<>();
        for (Message m : MessageController.MessageList
        ) {
            if (m.getDate().contains(input)) {
                searchResults.add(m);
            }
        }
        return searchResults;
    }
/*
// for .txt file
    static void logMessage(Message m) {

        String logMsg = "";
        logMsg += "<table><tbody>";
        logMsg += singleOutput(m);
        logMsg += "</tbody></table><br>";

        try {
            fileWriter = new FileWriter(filepath, true);
            fileWriter.write(logMsg);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    static Node logMessageXML(Message m) {
        Node post = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);
            post = doc.createElement("Post");
            Node Name = doc.createElement("Name");

            Node name = doc.createAttribute("username");
            ((Attr) name).setValue(m.getName());
            Name.appendChild(name);
            post.appendChild(Name);

            Node Date = doc.createElement("Date");
            Node date = doc.createAttribute("date");
            ((Attr) date).setValue(m.getDate());
            Date.appendChild(date);
            post.appendChild(Date);

            Node Message = doc.createElement("Message");
            Node msg = doc.createAttribute("msg");
            ((Attr) msg).setValue(m.getName());
            Message.appendChild(msg);
            post.appendChild(Message);


            if(!m.getFavViews().isEmpty()) {
                Node FavViews = doc.createElement("FavViews");
                for(int i=0; i<m.favView.length; i++) {
                    Node favViews = doc.createAttribute("favView"+ i);
                    ((Attr) favViews).setValue(m.favView[i]);
                    Message.appendChild(favViews);
                }
                post.appendChild(FavViews);
            }

            if(!m.getFavSports().isEmpty()) {
                Node FavSports = doc.createElement("FavSports");
                for(int i=0; i<m.favSport.length; i++) {
                    Node favViews = doc.createAttribute("favSports"+ i);
                    ((Attr) favViews).setValue(m.favSport[i]);
                    Message.appendChild(favViews);
                }
                post.appendChild(FavSports);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    /*
    // old version with .txt
    public static String readLogFile() {
        String readValue = "";
        String tmp;
        try {
            fileReader = new FileReader(filepath);
            BufferedReader buffRead = new BufferedReader(fileReader);
            while ((tmp = buffRead.readLine()) != null) {
                readValue += tmp;
            }
            buffRead.close();
            fileReader.close();
        } catch (IOException e) {

        }

        return readValue;
    }
*/

    public static void readMsgLogFile()
    {

    }
}
