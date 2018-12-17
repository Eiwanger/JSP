package feedback;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/index.html")
public class FeedbackFormServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession hs = request.getSession();
        hs.setMaxInactiveInterval(120);

        PrintWriter out = response.getWriter();
        Cookie[] cookies = request.getCookies();
        String firstname = "";
        String surname = "";
        String email = "";
        String phonenumber = "";

if(cookies !=  null) {
    for (Cookie c : cookies) {
        try {
           // if (c.getDomain().equals(FeedbackServlet.schoolUrl)){
                switch (c.getName()) {
                    case "firstname":
                        firstname = c.getValue();
                        break;
                    case "surname":
                        surname = c.getValue();
                        break;
                    case "phonenumber":
                        phonenumber = c.getValue();
                        break;
                    case "email":
                        email = c.getValue();
                        break;
             //   }
                }
        }catch (NullPointerException e)
        {

        }
    }
}
        response.setContentType("text/html");

        out.println("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "   <title>Feedback</title>"
                + "   <link rel='stylesheet' href='styles.css'>"
                + "</head>"
                + "<h2>Feedback form</h2>"
                + "<form method='POST' action='FeedbackServlet'>"
                + "<table>"
                + "<tr><th>First name: </th><td>"
                + "<input type='text' name='firstname' value='" + firstname + "' size='20'>"
                + "</td></tr>"
                + "<tr><th>Surname: </th><td>"
                + "<input type='text' name='surname' value='" + surname + "' size='20'>"
                + "</td></tr>"
                + "<tr><th>Email: </th><td>"
                + "<input type='text' name='email' value='" + email + "' size='20'>"
                + "</td></tr>"
                + "<tr><th>Phonenumber: </th><td>"
                + "<input type='text' name='phonenumber' value='" + phonenumber + "' size='20'>"
                + "</td></tr>"
                + "<tr><th>Feedback: </th><td>"
                + "<textarea name='feedback' ></textarea>"
                + "</td></tr>"
                + "<tr>"
                + "<td><input type='Submit' name='action' value='Submit'></td>"
                + "</tr>"
                + "</table>"
                + "</form>"
                + "</body>"
                + "</html>");
    }


}
