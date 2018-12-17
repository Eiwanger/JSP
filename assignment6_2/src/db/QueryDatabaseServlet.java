package db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QueryDatabaseServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("index.html");
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // String query="select * from Students";
        String dbUserName = request.getParameter("db_username");
        String dbPassword = request.getParameter("db_password");

        String dbName = request.getParameter("db_name");
        String dbTableName = request.getParameter("db_table_name");
        String query = "select * from " + dbTableName;
        /*
         * Here we initializae tools for making the database connection and
         * reading from the database
         */
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMeatData = null;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Database Access Servlet</title></head><body>");
        out.println("<h2>" + dbTableName + " Information");
        out.println("<table border='1'><tr>");
        try {
            // Here we load the database driver for Oracle database
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            // For mySQL database the above code would look like this:
            Class.forName("com.mysql.jdbc.Driver");
            // Here we set the JDBC URL for the Oracle database
            //String url = "jdbc:oracle:thin:@db.cc.puv.fi:1521:ora817";
            // For mySQL database the above code would look like
            // something this.
            // Notice that here we are accessing mg_db database
            String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
            // Here we create a connection to the database
            //conn = DriverManager.getConnection(url, "scott", "tiger");
            // For mg_db mySQL database the above code would look like
            // the following:
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            // Herey we create the statement object for executing SQL commands.
            stmt = conn.createStatement();
            // Here we executethe SQL query and save the results to a ResultSet
            // object
            resultSet = stmt.executeQuery(query);
            // Here we get the metadata of the query results
            resultSetMeatData = resultSet.getMetaData();
            // Here we calculate the number of columns
            int columns = resultSetMeatData.getColumnCount();
            // Here we print column names in table header cells
            // Pay attention that the column index starts with 1
            for (int i = 1; i <= columns; i++) {
                out.println("<th> " + resultSetMeatData.getColumnName(i)
                        + "</th>");
            }
            out.println("</tr>");
            out.println("<tr>");
            for (int i = 1; i <= columns; i++) {
                out.println("<th> " + resultSetMeatData.getColumnTypeName(i)
                        + "</th>");
            }
            out.println("</tr>");
            while (resultSet.next()) {
                out.println("<tr>");
                // Here we print the value of each column
                for (int i = 1; i <= columns; i++) {
                    if (resultSet.getObject(i) != null)
                        out.println("<td>" + resultSet.getObject(i).toString()
                                + "</td>");
                    else
                        out.println("<td>---</td>");
                }
                /*
                 * out.println("<td>" + resultSet.getString(1)+"</td>");
                 * out.println("<td>" + resultSet.getInt(2)+"</td>");
                 * out.println("<td>" + resultSet.getInt(3)+"</td>");
                 */
                out.println("</tr>");
            }
            out.println("</table>");

            out.println("<hr><div style='text-align: center;'><a href='index.html'>Back</a></div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            out.println("<p>" + e.getMessage());
        } finally {
            try {
                // Here we close all open streams
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException sqlexc) {
                out.println("EXception occurred while closing streams!");
            }
        }
    }
}