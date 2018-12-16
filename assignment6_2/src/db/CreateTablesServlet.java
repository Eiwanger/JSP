package db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class CreateTablesServlet extends HttpServlet {
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
  String dbUserName = request.getParameter("db_username");
  String dbPassword = request.getParameter("db_password");
  
  String dbName = request.getParameter("db_name");
  String dbTableName = request.getParameter("db_table_name");
  String createTableQuery = "create table " + dbTableName
    + " (ID INTEGER, NAME VARCHAR(40), PHONE VARCHAR(20))";
  /*
   * Here we initializae tools for making the database connection and
   * reading from the database
   */
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();
  out.println("<html><head><title>Create Tables Servlet</title></head><body>");
  Connection conn = null;
  Statement stmt = null;
  try {
   // Here we load the database driver
   // Class.forName("oracle.jdbc.driver.OracleDriver");
   // For mySQL database the above code would look like this:
   Class.forName("com.mysql.jdbc.Driver");
   // Here we set the JDBC URL for the Oracle database
   // String url="jdbc:oracle:thin:@db.cc.puv.fi:1521:ora817";
   // For mySQL database the above code would look like
   // something this.
   // Notice that here we are accessing mg_db database
   String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
   // Here we create a connection to the database
   // conn=DriverManager.getConnection(url, "scott", "tiger");
   // For mg_db mySQL database the above code would look like
   // the following:
   conn = DriverManager.getConnection(url, dbUserName, dbPassword);
   // Here we create the statement object for executing SQL commands
   stmt = conn.createStatement();
   try {
    stmt.executeUpdate(createTableQuery);
    out.println("<p>Today: " + new Date() + " " + dbTableName
      + " was created successfully! ");
   } catch (SQLException sqlE) {
    out.println("<p>" + sqlE.getMessage());
   }
   /*
    * out.println("<p>Return value for creation of table: " + dbTable +
    * ": " + table1Result);
    */
 out.println("<hr><p style='text-align: center;' <a href='index.html'>Back</a></p>");
   out.println("</body>");
   out.println("</html>");
  } catch (Exception e) {
   out.println("<p>" + e.getMessage());
  } finally {
   if (conn != null)
    try {
     conn.close();
     if (stmt != null)
      stmt.close();
    } catch (SQLException e) {
     out.println("<p>" + e.getMessage());
    }
  }
 }
}