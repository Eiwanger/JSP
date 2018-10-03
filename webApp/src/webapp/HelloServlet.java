package webapp;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


    public class HelloServlet extends HttpServlet {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, java.io.IOException {
            // Here we set the MIME type of the response, "text/html"
            response.setContentType("text/html");
            // Here we use a PrintWriter to send text data
            // to the client who has requested the servlet
            java.io.PrintWriter out = response.getWriter();
            // Here we start assembling the HTML content
            out.println("<html><head>");
            out.println("<title>Hello Servlet</title>");
            out.println("<link rel='stylesheet' type='text/css' href='styles.css' />");
            out.println("</head><body>");
            out.print("<p>Today is: " + new Date());
            out.print("</p>");
            out.print("<h1>The Hello Servlet Demo fck yeah</h1>");
            out.println("<p>This is the first hello servlet</p>");
            out.println("<img src='images/Tulips.jpeg' width='100' height='100' />");
            out.println("</body></html>");
        }


    }

