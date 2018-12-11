import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet("/loginAdminServlet")
public class LoginAdminServlet extends HttpServlet{



    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
                response.sendRedirect("http://app3.cc.puv.fi/assignment5_1_eiw/loginAdminServlet");
/*
        response.setContentType( "text/html" );

        PrintWriter out=response.getWriter();

        out.println( "<html>" );

        out.println( "<head>" );

        out.println( "<title>Dynamic Update</title>" );

        out.println( "</head>" );

        out.println("<body style='background-color:#ffb90e;'>");

        out.println( "<h1>Welcome to the world of admins!</h1>" );

        out.println( "<p>Something will happen here...</P>" );

        out.println("<hr/>");

        out.println("Date is : " + new Date() + " Good day there...!");


        out.println( "</body>" );

        out.println( "</html>" );

*/
    }

}
