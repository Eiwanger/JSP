package feedback;

import Database.DBHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {

    //static String schoolUrl = "app3.cc.puv.fi";
    //String localUrl = "localhost:8080";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession hs = request.getSession();
        hs.setMaxInactiveInterval(120);
        boolean redirect = false;
        String user = "";
        int ageOfCookie = 60*60;
        try {
            user = request.getUserPrincipal().getName();
        }catch (NullPointerException e)
        {
            response.sendRedirect("index.html");
        }
/*
        if (request.getParameter("action") == null) {
            response.sendRedirect("index.html");
        }
*/
        String firstname = request.getParameter("firstname");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phonenumber = request.getParameter("phonenumber");
        String feedback = request.getParameter("feedback");


        if (firstname.length() != 0) {
            Cookie c = new Cookie("firstname", firstname.trim());
           // c.setDomain(schoolUrl);
            c.setMaxAge(ageOfCookie);
            response.addCookie(c);
        } else {
            redirect = true;
        }
        if (surname.length() != 0) {
            Cookie c = new Cookie("surname", surname.trim());
           // c.setDomain(schoolUrl);
            c.setMaxAge(ageOfCookie);
            response.addCookie(c);
        } else {
            redirect = true;
        }
        if (email.length() != 0) {
            Cookie c = new Cookie("email", email.trim());
            //c.setDomain(schoolUrl);
            c.setMaxAge(ageOfCookie);
            response.addCookie(c);
        } else {
            redirect = true;
        }
        if (phonenumber.length() != 0) {
            Cookie c = new Cookie("phonenumber", phonenumber.trim());
            //c.setDomain(schoolUrl);
            c.setMaxAge(ageOfCookie);
            response.addCookie(c);
        } else {
            redirect = true;
        }

        Cookie[] cs = request.getCookies();

        if (feedback.length() == 0 || redirect) {
            //RequestDispatcher rd=request.getRequestDispatcher("index.html");
            //rd.forward(request, response);
            response.sendRedirect("index.html");
        } else {
            Feedback fe = new Feedback(firstname, surname, email, phonenumber, feedback, user);

            DBHandler.AddFeedbackToDataBase(fe);
            List<Feedback> feedbacks = DBHandler.getFeedbackFromDB(user);

            PrintWriter out = response.getWriter();
            response.setContentType("text/html");

            out.println("<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "   <title>Feedback Summary</title>"
                    + "   <link rel='stylesheet' href='styles.css'>"
                    + "</head>"
                    + "<h2>Feedback Summary</h2>"
                    + "<table>"
                    + "<tr><td>Feedback of " + fe.getUsername()+ " </td></tr>"
                    + "</table>");
            String feeds = "";
            for (Feedback f : feedbacks) {
                feeds += "<table><tr><th>Firstname</th><td>" +
                        f.getFirstname()
                        + "</td></tr>";
                feeds += "<tr><th>Surname</th><td>" +
                        f.getSurname()
                        + "</td></tr>";
                feeds += "<tr><th>Email</th><td>" +
                        f.getEmail()
                        + "</td></tr>";
                feeds += "<tr><th>Phonenumber</th><td>" +
                        f.getPhonenumber()
                        + "</td></tr>";
                feeds += "<tr><th>Feedback</th><td>" +
                        f.getFeedback()
                        + "</td></tr>" +
                        "   <tr><td><br></td></tr></table>";
            }

            out.println(
                    feeds
                            + "<a href='index.html'> Back </a>"
                            + "</body>"
                            + "</html>");
        }
    }


}

