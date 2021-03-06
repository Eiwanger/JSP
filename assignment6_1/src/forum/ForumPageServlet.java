package forum;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/forum.html")
public class ForumPageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession mySession = request.getSession();
        String username = (String) mySession.getAttribute("username");
        out.println(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "    <title>Forum</title>" +
                        "    <link rel=\"stylesheet\" href=\"styles.css\">" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "    <div class=\"topright\">" +
                        "        <form action=\"index.html\">" +
                        "            <input type=\"submit\" value=\"Logout\"/>" +
                        "        </form>" +
                        "    </div>" +
                        "</div>" +
                        "<h1>Forum</h1>" +
                        "<form action=\"fs\" method=\"POST\">" +
                        "    <input type=\"submit\" VALUE=\"Go to Forum\" name=\"action\">" +
                        "</form>" +
                        "<form action=\"fs\" method=\"POST\" enctype='multipart/form-data'>" +
                        "    <table>" +
                        "        <tr>" +
                        "            <td>User Name</td>" +
                        "            <td>" +
                        "        <input type='hidden' name='userName' value='" + username + "' />" +
                        "        <input type='text' name='username' value='" + username + "' disabled='disabled' />" +
                        "            </td>" +
                        "        </tr>" +
                        "<tr>"+
                        "            <td>Image upload</td>" +
                        "<td><input type='file' name='file' accept='image/*' multiple/></td>"+
                        "</tr>"+
                        "        <tr>" +
                        "            <td>Message</td>" +
                        "            <td> " +
                        "                <textarea placeholder='Write here your message till 200 characters' maxlength='200' name=\"message\" rows=\"3\" cols=\"38\"></textarea>" +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <table>" +
                        "        <tr>" +
                        "            <td><input type=\"submit\" VALUE=\"Submit\" name=\"action\"></td>" +
                        "        </tr>" +
                        "    </table>" +
                        "</form>" +
                        "<form action=\"fs\" method=\"POST\">" +
                        "    <table>" +
                        "        <tr>" +
                        "            <td>");

        try {
            if (username.equals("admin")) {
                out.println("                <input type=\"submit\" VALUE=\"Delete Forum\" name=\"action\">");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.sendRedirect("index.html");
        }

        out.println("            </td>" +
                "        </tr>" +
                "    </table>" +
                "</form>" +
                "<h3> Search by: </h3>" +
                "<table>" +
                "    <form action=\"fs\" method=\"GET\">" +
                "        <tr>" +
                "            <td>Name</td>" +
                "            <td><input type=\"text\" size=\"40\" name=\"sName\"></td>" +
                "            <td><input type=\"submit\" VALUE=\"Search Name\" name=\"action\"></td>" +
                "        </tr>" +
                "    </form>" +
                "    <form action=\"fs\" method=\"GET\">" +
                "        <tr>" +
                "            <td>Date</td>" +
                "            <td><input type=\"date\" size=\"40\" name=\"sDate\"></td>" +
                "            <td><input type=\"submit\" VALUE=\"Search Date\" name=\"action\"></td>" +
                "        </tr>" +
                "    </form>" +
                "</table>" +
                "</body>" +
                "</html>");
    }
}

