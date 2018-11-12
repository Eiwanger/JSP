package forum;

import java.util.LinkedList;
import java.util.List;

public class Message {

    private String name;
    private String date;
    private String message;

    LinkedList<String> imageLinks;
    LinkedList<String> favView;
    LinkedList<String> favSport;


    // Constructors
    public Message() {

    }

    public Message(String name, String date, String message, LinkedList<String> favouriteView, LinkedList<String> favouriteSports, LinkedList<String> imageLinks) {
        if (name.isEmpty()) {
            this.name = "No User";
        } else {
            this.name = name;
        }
        this.date = date;

        this.message = message;

        this.favView = favouriteView;

        this.favSport = favouriteSports;

        this.imageLinks = imageLinks;
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFavViewAtIndex(int i) {
        return favView.get(i);
    }

    public String getFavSportAtIndex(int i) {
        return favSport.get(i);
    }


    public String getFavViews() {
        String views = "";
        if (!(favView == null) && !favView.isEmpty()) {
            for (String s : favView) {
                views += s + " | ";
            }
            return views.substring(0, views.length() - 3);
        }
        return views;

    }

    public String getFavSports() {

        String sports = "";
        if (!(favSport == null) && !favSport.isEmpty()) {
            for (String s : favSport) {
                sports += s + " | ";
            }
            return sports.substring(0, sports.length() - 3);
        }


        return sports;
    }

    public LinkedList<String> getImageLinks()
    {
        return imageLinks;
    }
}
