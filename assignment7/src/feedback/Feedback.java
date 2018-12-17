package feedback;

public class Feedback {

    private String firstname;
    private String surname;
    private String email;

    private String phonenumber;
    private String feedback;
    private String username;

    public Feedback(){}

    public Feedback(String firstname, String surname, String email, String phonenumber, String feedback, String username) {
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.feedback = feedback;
        this.username = username;

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
