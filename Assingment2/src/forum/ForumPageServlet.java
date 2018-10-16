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
                        "        <form action=\"login.html\">" +
                        "            <input type=\"submit\" value=\"Logout\"/>" +
                        "        </form>" +
                        "    </div>" +
                        "</div>" +
                        "<h1>Forum</h1>" +
                        "<form action=\"fs\" method=\"POST\">" +
                        "    <input type=\"submit\" VALUE=\"Go to Forum\" name=\"action\">" +
                        "</form>" +
                        "<form action=\"fs\" method=\"POST\">" +
                        "    <table>" +
                        "        <tr>" +
                        "            <td>User Name</td>" +
                        "            <td>" +
                        //"                <input type=\"text\" size=\"40\" name=\"userName\" value=\"\">" +
                        "        <input type='hidden' name='userName' value='" + username + "' />" +
                        "        <input type='text' name='username' value='" + username + "' disabled='disabled' />" +
                        "            </td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td>Message</td>" +
                        "            <td> <!--<input type=\"text\" size=\"40\"  name=\"message\" >-->" +
                        "                <textarea name=\"message\" rows=\"3\" cols=\"38\"></textarea>" +
                        "            </td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <table>" +
                        "        <tr>" +
                        "            <td>Favourite view:</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td><input type=\"checkbox\" name=\"views\" value=\"Sunrise\"/>Sunrise" +
                        "                <input type=\"checkbox\" name=\"views\" value=\"Sunset\"/>Sunset" +
                        "                <input type=\"checkbox\" name=\"views\" value=\"Lake in the morning\"/>Lake in the morning" +
                        "                <input type=\"checkbox\" name=\"views\" value=\"Rain on a window\"/>Rain on a window" +
                        "            </td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td>Favourite sports:</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td><input type=\"checkbox\" name=\"sports\" value=\"Basketball\"/>Basketball" +
                        "                <input type=\"checkbox\" name=\"sports\" value=\"Tennis\"/>Tennis" +
                        "                <input type=\"checkbox\" name=\"sports\" value=\"Soccer\"/>Soccer" +
                        "                <input type=\"checkbox\" name=\"sports\" value=\"Bavarian Curling\"/>Bavarian Curling" +
                        "            </td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td><input type=\"submit\" VALUE=\"Submit\" name=\"action\"></td>" +
                        "        </tr>" +
                        "    </table>" +
                        "    <table>" +
                        "    </table>" +
                        "</form>" +
                        "<form action=\"fs\" method=\"POST\">" +
                        "    <table>" +
                        "        <tr>" +
                        "            <td>" +
                        "                <input type=\"submit\" VALUE=\"Delete Forum\" name=\"action\">" +
                        "            </td>" +
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

