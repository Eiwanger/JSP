package forum;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class Message {

    private String name;
    private String date;
    private String message;


    String favView[];
    String favSport[];


    // Constructors
    public Message(){

   }

    public Message(String name, String date, String message, String favouriteView[], String favSports[])
    {
        if(name.isEmpty())
        {
            this.name = "No User";
        }else{
            this.name = name;
        }
        this.date = date;

        this.message = message;

        this.favView = favouriteView;
        this.favSport= favSports;
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

    public String getFavViews()
    {
        String views = "";
if(!(favView == null)) {
    for (int i = 0; i < favView.length; i++) {
        views += favView[i] + " | ";
    }
    return views.substring(0, views.length()-3);
}
    return views;

    }

    public String getFavSports()
    {

        String sports = "";
if(!(favSport == null)) {
    for (int i = 0; i < favSport.length; i++) {
        sports += favSport[i] + " | ";
    }
    return sports.substring(0, sports.length()-3);
}


        return sports;
    }

}
