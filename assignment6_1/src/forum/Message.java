package forum;

import java.util.LinkedList;

public class Message {

    private String name;
    private String date;
    private String message;
    private int msgId;

    LinkedList<String> imageLinks;


    // Constructors
    public Message() {

    }

    public Message(String name, String date, String message, LinkedList<String> imageLinks, int id) {
        if (name.isEmpty()) {
            this.name = "No User";
        } else {
            this.name = name;
        }
        this.date = date;

        this.message = message;

        this.imageLinks = imageLinks;

        this.msgId = id;
    }

    public Message(String name, String date, String message, LinkedList<String> imageLinks) {
        if (name.isEmpty()) {
            this.name = "No User";
        } else {
            this.name = name;
        }
        this.date = date;

        this.message = message;

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



    public LinkedList<String> getImageLinks()
    {
        return imageLinks;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgId() {
        return msgId;
    }
}
