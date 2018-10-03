package forum;

import javafx.geometry.Pos;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class PostList {
/*
   private static ArrayList<Message> MessageList = new ArrayList<>();

    public PostList()
   {

   };

   public static void addToList(Message msg){
       MessageList.add(msg);
   }

public static String printAllMessages() {
        String returnString = "";
        returnString += "<html>";
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

     public static String printSearchedMessages(ArrayList<Message> msgList, String input) {
        String returnString = "";
        returnString += "<html>";
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

         public static ArrayList<Message> searchName(String input){
        ArrayList<Message> searchResults= new ArrayList<>();
        for (Message m: Message.MessageList
        ) {
            if(m.getName().contains(input)){
                searchResults.add(m);
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

public static void deletePostList(){
MessageList.clear();
}
    }



*/
}
