

package login;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login.html")
public class LoginServlet extends HttpServlet {
    public void init() {
        LoginController.initUserDataFile();
        LoginController.collectData();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        String username = request.getParameter("userName");
        String password = request.getParameter("password");

        // Map<String, String[]> logMap = request.getParameterMap();


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        if (action.equalsIgnoreCase("create account")) {
            out.println("<html><head>");
            out.println("<title>User Login Page</title>");
            out.println("<link rel='stylesheet' type='text/css' href='styles.css'>");
            out.println("</head><body>");
            out.println("<h2>Registration</h2>");
            try {
                if (LoginController.registUser(username, password)) {
                    out.println("<p>Account successfully created</p>");
                }
            } catch (UserInputException Ui) {
                out.println("<p>" + Ui.getMessage() + "</p>");
            }

            out.println("<br><a href='login.html'>Back</a>");
            out.println("</body></html>");

        } else {
            action = request.getParameter("action");

            out.println("<html><head>");
            out.println("<title>User Login Page</title>");
            out.println("<link rel='stylesheet' type='text/css' href='styles.css'>");
            out.println("</head><body>");


            out.println(LoginController.createServlet(action, username, password));
            if(!(action==null)) {
                if (action.equalsIgnoreCase("register")) {
                    response.sendRedirect("regist.html");
                    return;
                    // test if return changes something
                }
                if (LoginController.checkIfEmpty(username) && LoginController.checkIfEmpty(password)
                        && LoginController.checkLogin(username, password)) {
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("forum.html");

                    HttpSession mySession = request.getSession();
                    mySession.setAttribute("username", username);


                    requestDispatcher.forward(request, response);
                }

                if (action.equalsIgnoreCase("login") && !LoginController.checkLogin(username, password)
                        && LoginController.checkIfEmpty(username) && LoginController.checkIfEmpty(password)) {
                    out.println("<p>User name or password wrong</p>");
                }
                out.println("</body></html>");


            }
            out.close();
        }

    }
}





