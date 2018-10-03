package forum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Message {

    private String name;
    private String date;
    private String message;


    private static ArrayList<Message> MessageList = new ArrayList<>();

    // Constructors
    public Message(){

   }

    public Message(String name, String date, String message)
    {
        this.name = name;
        this.date = date;
        this.message = message;
    }

    public static void addToList(Message msg){
        MessageList.add(msg);
    }

    // Print all messages of the list
    public static String printAllMessages() {
        String returnString = "";
        returnString += "<html>";
        returnString += "<head><link rel=\"stylesheet\" href=\"styles.css\"></head>";
        returnString += "<body>";
        returnString += "<table>";
        if (MessageList.isEmpty()) {
            returnString = "<p> no posts available </p>";
        } else {
            for (Message m : MessageList) {
                returnString += "<tr><td>";
                returnString += "<h3>" + m.getName() + "</h3></td></tr>";
                returnString += "<tr><td>" + m.getDate() + "</td></tr>";
                returnString += "<tr><td>" + m.getMessage() + "</td></tr>";
            }
            returnString += "</table><br>";


        }
        returnString += "<br><a href='forum.html'>Back</a>";
        returnString += "</body>";
        returnString += "</html>";

        return returnString;
    }

    // Print only the messages which were found in a search
    public static String printSearchedMessages(ArrayList<Message> msgList, String input) {
        String returnString = "";
        returnString += "<html>";
        returnString += "<head><link rel=\"stylesheet\" href=\"styles.css\"></head>";
        returnString += "<body>";
        returnString += "<table>";
        if (msgList.isEmpty()) {
            returnString = "<p> no results for your search </p>";
        } else {
            returnString = "<p>Results for " + "\"" + input + "\""+ "</p>";
            for (Message m : msgList) {
                returnString += "<tr><td>";
                returnString += "<h3>" + m.getName() + "</h3></td></tr>";
                returnString += "<tr><td>" + m.getDate() + "</td></tr><br>";
                returnString += "<tr><td>" + m.getMessage() + "</td></tr>";
            }
            returnString += "</table><br>";


        }
        returnString += "<br><a href='forum.html'>Back</a>";
        returnString += "</body>";
        returnString += "</html>";

        return returnString;
    }

    public static void deletePostList(){
        MessageList.clear();
    }

    // setting the current date
    public static String setCurrentDate() {

        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    // Search methods
    public static ArrayList<Message> searchName(String input){
        ArrayList<Message> searchResults= new ArrayList<>();
        if(!input.isEmpty()) {
            for (Message m : Message.MessageList
            ) {
                if (m.getName().contains(input)) {
                    searchResults.add(m);
                }
            }
        }
        return searchResults;
    }

    public static ArrayList<Message> searchDate(String input){
        ArrayList<Message> searchResults = new ArrayList<>();
        for (Message m: Message.MessageList
        ) {
            if(m.getDate().contains(input)){
                searchResults.add(m);
            }
        }
        return searchResults;
    }


    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {


        return date;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
