package db;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateColumnsServlet extends HttpServlet {
    private static String username ="";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String columsHeader ="<tr><td>Name</td><td>Type</td><td>Länge/Werte</td><td>Standard</td>" +
                "<td>Kollation</td><td>Attribute</td><td>Null</td><td>Index</td><td>A_I</td>" +
                "<td>Kommentare</td><td>MIME-Typ</td>" +
                "<form method=\"POST\" action=\"cdbts\">";

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Database Columnscreation Servlet</title></head><body>");
        out.println("<h2>Create Columns</h2>");
        out.println(columsHeader);
        out.println("<table border='1'>");
        int number = Integer.parseInt(request.getParameter("db_column_number"));

        for (int i = 0; i <= number; i++) {

            out.println(" <tr>" +
                    "        <td><input type=\"text\" name=\"name\""+i+"></td>" +
                    "        <td>" +
                    "            <select name=\"type\""+i+">" +
                    "            <option value=\"INT\">INT</option>" +
                    "            <option value=\"VARCHAR\">VARCHAR</option>" +
                    "            <option value=\"TEXT\">TEXT</option>" +
                    "            <option value=\"DATE\">DATE</option>" +
                    "        </select>" +
                    "        </td>" +
                    "        <td><input type=\"text\" name=\"length\""+i+"></td>" +
                    "        <td><select name=\"standard\">" +
                    "            <option value=\"Kein(e)\">Kein(e)</option>" +
                    "            <option value=\"Wie definiert:\">Wie definiert:</option>" +
                    "            <option value=\"NULL\">NULL</option>" +
                    "            <option value=\"CURRENT_TIMESTAMP\">CURRENT_TIMESTAMP</option>" +
                    "        </select></td>" +
                    "        <td><input type=\"checkbox\" value=\"null\" name=\"nullCheck\""+i+"></td>" +
                    "        <td><select name=\"index\""+i+">" +
                    "            <option value=\"nothing\">---</option>" +
                    "            <option value=\"PRIMARY\">PRIMARY</option>" +
                    "            <option value=\"UNIQUE\">UNIQUE</option>" +
                    "            <option value=\"INDEX\">INDEX</option>" +
                    "            <option value=\"FULLTEXT\">FULLTEXT</option>" +
                    "        </select></td>" +
                    "        <td><input type=\"text\" name=\"comment\""+i+"></td>");
        }

        out.println("</table></form>");
    }
}
