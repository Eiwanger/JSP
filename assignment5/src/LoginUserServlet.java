import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

@WebServlet("/loginUserServlet")
public class LoginUserServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(15);
        response.setIntHeader("Refresh", 20);

/*
        // force redirect if session isn't valid
        HttpSession tmp = request.getSession(false);// don't create if it doesn't exist
        if(session != null && !session.isNew()) {
            //doPost(request, response);
        } else {

            //response.sendRedirect("/index.html");
        }
*/

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");

        out.println("<head>");

        out.println("<title>User Page</title>");

        out.println("</head>");

        out.println("<body style='background-color:#fff90e;'>");

        out.println("<h1>Welcome to the world of users!</h1>");

        out.println("<p>Something will happen here...</P>");
        out.println("<p>Time: " + new Date() + "</P>");

        out.println("<hr/>");


        out.println("</body>");

        out.println("</html>");
    }
}

