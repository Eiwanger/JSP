package forum;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.Part;
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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class MessageController {

    final static String folder = "logger";
    public static String separator = System.getProperty("file.separator");
    public static String ressourceFolder ="resources";
    private static String fileName = "forumLog.xml";
    static String logFilePath;
    static String imageFolderPath;

    private static File f = new File(ForumServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    private static File logFile;
    private static File imageFolderFile;
    private static String htmlHead = "<html><head><link rel=\"stylesheet\" " +
            "type='text/css' href=\"styles.css\"></head><body>" +
            "<table id='t01'><tbody>";

    private static ArrayList<Message> MessageList = new ArrayList<>();

    // todo change save location from file to database
    // initialize the logfile as a XML file
    public static void initLogFile() {

        logFilePath = initFilePath()+ separator + folder + separator;

        logFile = new File(logFilePath);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }

        logFilePath += fileName;

        logFile = new File(logFilePath);

        try {


            if (!logFile.exists()) {

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Node root = doc.createElement("posts");
                doc.appendChild(root);

                saveXMLFile(doc, logFilePath);
            }
        } catch (ParserConfigurationException p) {
            p.printStackTrace();
        }
    }

    public static String initFilePath()
    {
        String filePath  = ForumServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        int i = filePath.indexOf("WEB-INF/");
        filePath = filePath.substring(0, i);
        return filePath + ressourceFolder;

    }

    public static void initImageFolder() {

        imageFolderPath = initFilePath()+ separator + "Images" + separator;

        imageFolderFile = new File(imageFolderPath);
        if (!imageFolderFile.exists()) {
            imageFolderFile.mkdir();
        }
    }

    // save the document to a path as an xml file
    public static void saveXMLFile(Document doc, String path) {
        try {
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            String filePathXSL = f.toString().replaceAll("classes", "strip-space.xsl");
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(filePathXSL));
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (TransformerException e) {
        }
    }


    public static void addToList(Message msg) {
        MessageList.add(msg);
    }

    // delete the local list and the logfile
    public static void deletePostList() {
        MessageList.clear();
        logFile.delete();
        deleteDir(imageFolderFile);
        initImageFolder();
        initLogFile();

    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    // setting the current date
    public static String setCurrentDate() {

        String DATE_FORMAT_NOW = "yyyy-MM-dd_HH:mm:ss";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }


    static void logMessageXML(Message m) {
        Node post = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(logFilePath);
            Node root = doc.getDocumentElement();
            post = doc.createElement("Post");

            ((Element) post).setAttribute("name", m.getName());
            ((Element) post).setAttribute("date", m.getDate());
            ((Element) post).setAttribute("msg", m.getMessage());


            if (!m.getFavViews().isEmpty()) {
                Node favViews = doc.createElement("favViews");

                for (String s : m.favView) {
                    Node view = doc.createElement("View");
                    view.appendChild(doc.createTextNode(s));
                    favViews.appendChild(view);
                }
                post.appendChild(favViews);
            }


            if (!m.getFavSports().isEmpty()) {
                Node favSports = doc.createElement("favSports");
                for (String s : m.favSport) {
                    Node sport = doc.createElement("Sport");
                    sport.appendChild(doc.createTextNode(s));
                    favSports.appendChild(sport);
                    post.appendChild(favSports);
                }
            }
            // todo :done images written to file
            if (!m.getImageLinks().isEmpty()) {
                Node images = doc.createElement("images");
                for (String s : m.getImageLinks()) {
                    Node image = doc.createElement("Image");
                    image.appendChild(doc.createTextNode(s));
                    images.appendChild(image);
                    post.appendChild(images);
                }
            }


            root.appendChild(post);
            saveXMLFile(doc, logFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void readMsgLogFile() {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(logFilePath);
            Node root = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("Post");

            for (int i = 0; i < nodeList.getLength(); i++) {
                MessageList.add(getMessage(nodeList.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Message getMessage(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        Message msg = new Message();
        LinkedList<String> viewsList = new LinkedList<>();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            msg.setName(element.getAttribute("name"));
            msg.setDate(element.getAttribute("date"));
            msg.setMessage(element.getAttribute("msg"));
            if (element.hasChildNodes()) {

                msg.favView = getChildNodes(element, "favViews");
                msg.favSport = getChildNodes(element, "favSports");
                // todo :done get imagelinks from file
                msg.imageLinks = getChildNodes(element, "images");
            }
        }
        return msg;
    }

    private static LinkedList<String> getChildNodes(Element element, String tagname) {
        LinkedList<String> taglist = new LinkedList<>();
        try {

            if (element.getElementsByTagName(tagname).getLength() == 0) {
                return taglist;
            }

            NodeList tags = element.getElementsByTagName(tagname).item(0).getChildNodes();


            if (tags == null) {
                return taglist;
            }
            for (int i = 0; i < tags.getLength(); i++) {
                Node view = tags.item(i);
                if (view.getNodeType() == Node.ELEMENT_NODE) {
                    taglist.add(((Element) view).getTextContent());

                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            return taglist;
        }
        return taglist;
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

    // print functions
    // print a single message
    private static String singleOutput(Message m) {
        String returnString = "";
        returnString += "<tr><td class=\"user\" >";
        returnString += "<h3>" + m.getName() + "</h3></td>" +
                "<td class=\"poststyle\" rowspan='2'  width='50%'text-align='left'>" + m.getMessage() + "</td>";

        try {
            if (!m.getImageLinks().isEmpty()) {
                for (String s : m.getImageLinks()) {

                    int i = s.indexOf("resources/Images/");
                    String newPath = s.substring(i);
                    i = s.indexOf("resources/Images/");
                    String imageName = s.substring(i);

                    // todo :done link image to picture
                    returnString += "<td class=\"poststyle\" rowspan='2'>" +
                            "<img src='" + newPath + "'alt='" + imageName + "' width='200' height='200'>";
                }


                returnString += "</td";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        returnString += "</tr>";

        returnString += "<tr><td class=\"date\" >" + m.getDate() + "</td></tr>";


        if (!m.getFavViews().isEmpty()) {
            if (m.getFavViews().length() == 1) {
                returnString += "<tr><td></td><td class=\"poststyle\"> Favourite View: ";
            } else {
                returnString += "<tr><td></td><td class=\"poststyle\"> Favourite Views: ";
            }
            returnString += m.getFavViews() + "</td></tr>";
        }
        if (!m.getFavSports().isEmpty()) {
            if (m.getFavSports().length() == 1) {
                returnString += "<tr><td></td><td class=\"poststyle\">Favourite Sport: ";
            } else {
                returnString += "<tr><td></td><td class=\"poststyle\">Favourite Sports: ";
            }
            returnString += m.getFavSports() + "</td></tr>";
        }

        returnString += "\n\n";
        return returnString;
    }

    // print the result for a search
    public static String printSearchedMessages(ArrayList<Message> msgList, String input) {
        String returnString = "";
        returnString += htmlHead;

        if (msgList.isEmpty()) {
            returnString += "<p> no results for your search </p>";
        } else {
            returnString += "<p>Results for " + "\"" + input + "\"" + "</p>";
            for (Message m : msgList) {

                returnString += singleOutput(m);

            }
            returnString += "</tbody></table>";


        }
        returnString += "<form action='forum.html'>" +
                "            <input class='stickyBack' type=\"submit\" value=\"Back\"/>" +
                "        </form>";
        returnString += "</body>";
        returnString += "</html>";

        return returnString;
    }

    // print every message in the list
    public static String printAllMessages() {
        String returnString = "";
        returnString += htmlHead;

        if (MessageList.isEmpty()) {
            returnString += "<p>no posts available</p>";
        } else {
            for (Message msg : MessageList) {
                returnString += singleOutput(msg);
            }
        }

        returnString += "</tbody></table><br>";

        returnString += "<form action='forum.html'>" +
                "            <input class='stickyBack' type=\"submit\" value=\"Back\"/>" +
                "        </form>";
        returnString += "</body>";
        returnString += "</html>";

        return returnString;
    }

    // file operations
    public static String getFilename(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            String[] tokens = contentDisp.split(";");

            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    return new File(token.split("=")[1].replace('\\', '/')).getName().replace("\"", "");
                }
            }
        }
        return "";
    }


    public static String saveFileToDisk(Part part, String filename) {
        File newImage = null;
        try {
            newImage = new File(imageFolderPath + filename);
            part.write(newImage.getAbsolutePath());
        } catch (IOException e) {
            return "";
        }
        return newImage.getAbsolutePath();
    }

}
