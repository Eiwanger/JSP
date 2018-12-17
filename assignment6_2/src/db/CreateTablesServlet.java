package db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateTablesServlet extends HttpServlet {
    int number;
    String dbUserName;
    String dbPassword;
    String dbName;
    String dbTableName;

    String tmpquery;


    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("index.html");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        Connection conn = null;
        Statement stmt = null;



        if (request.getParameter("name0") != null && request.getParameter("db_username") == null) {
            ResultSet metaDataSet = null;

            try {
                response.setContentType("text/html");
                out.println("<html><head><title>Create Tables Servlet</title></head><body>");
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
                conn = DriverManager.getConnection(url, dbUserName, dbPassword);
                stmt = conn.createStatement();
                String query = "CREATE TABLE " + dbTableName + " ( ";
                String primKey = "";
                tmpquery = query;
                // PreparedStatement preS = conn.prepareStatement("CREATE TABLE " + dbTableName + " ;");
                for (int i = 0; i < number; i++) {
                    String name = "";
                    if ((name = request.getParameter("name" + i)).length() == 0)
                        continue;
                    query += name + " ";

                    query += request.getParameter("type" + i);

                    String length = "";
                    if (request.getParameter("length" + i).length() != 0) {
                        length = "(" + request.getParameter("length" + i) + ")";
                    }
                    query += length + " ";

                    // if checkbox is set, set parameter that i can be null
                    String nullCheck = "";
                    if (request.getParameter("nullCheck" + i)== null) {
                        nullCheck = "NOT NULL";
                    }
                    query += nullCheck + " ";

                    if(request.getParameter("standard"+i).length() != 0) {
                        query += "DEFAULT " + request.getParameter("standard" + i) + " ";
                    }
                    String autoI = "";
                    if(request.getParameter("autoinkrement"+i) !=null)
                    {
                        autoI = "AUTO_INCREMENT";
                    }
                    query += autoI +" ";

                    query += request.getParameter("index" + i) + " ";

                    String comment = "";
                    if (request.getParameter("comment" + i).length() != 0) {
                        comment = " COMMENT '"+request.getParameter("comment" + i) +"'";
                    }
                    query += comment + " ";





/*
                    if(request.getParameter("index"+i).equals("PRIMARY KEY") )
                    {
                        if(primKey.length()!=0)
                            primKey+=",";
                        primKey += name;
                    }else
                    {
                        query += request.getParameter("index" + i) + " ";
                    }
*/
                    if (i == number - 1) {
                    /*    if(primKey.length()!=0)
                            query+=",PRIMARY KEY ("+primKey+")";
                        query += ") ;";
                        */
                        query += ") ;";
                    } else {
                        query += " ,";
                    }


                    /*
                    preS.setString(1, name);
                    preS.setString(2, request.getParameter("type" + i));
                    preS.setString(3, request.getParameter("length" + i) == null ? request.getParameter("length" + i) : "4");
                    preS.setString(4, request.getParameter("standart" + i));
                    preS.setString(5, "");
                    if (!Boolean.getBoolean(request.getParameter("nullCheck" + i))) {
                        preS.setString(5, "NOT_NULL");
                    }
                    preS.setString(6, request.getParameter("index" + i));
                    preS.setString(7, request.getParameter("comment" + i));
*/

                }
                try {
                    tmpquery = query;
                    stmt.executeUpdate(query);
                    DatabaseMetaData dmd = conn.getMetaData();
                    metaDataSet = dmd.getColumns(null, null, dbTableName, null);
                } catch (SQLException sqlE) {
                    out.println("<p>" + sqlE.getMessage() + "creation failed" +
                            "<a href ='index.html'>Back </a>" +
                            "</body></html>");
                    return;
                }

                //   out.println("<hr><p style='text-align: center;' <a href='index.html'>Back</a></p>");
                //   out.println("</body>");
                //   out.println("</html>");
            } catch (Exception e) {

                out.println("<p>" + e.getMessage() + "creation failed" +
                        "<a href ='index.html'>Back </a>" +
                        "</body></html>");
                return;
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                        if (stmt != null)
                            stmt.close();
                    } catch (SQLException e) {
                        out.println("<p>" + e.getMessage() + "creation failed" +
                                "<a href ='index.html'>Back </a>" +
                                "</body></html>");
                        return;
                    }
            }

            out.println("<table border='1'><tr><th>Name</th><th>Typ</th></tr>");
            try {
                String summary = "";
                while (metaDataSet.next()) {
                    summary += "<tr><td>" +
                            metaDataSet.getString("COLUMN_NAME")
                            +
                            "</td>" +
                            "<td>" + metaDataSet.getString("TYPE_NAME") +
                            "</td></tr>";
                }
                out.println(summary);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            out.println("</table>" +
                    "<a href ='index.html'>Back </a>" +
                    "</body></html>");
            return;

        }else {


            dbUserName = request.getParameter("db_username");
            if(dbUserName.isEmpty())
                dbUserName = "e1800820";
            dbPassword = request.getParameter("db_password");
            if(dbPassword.isEmpty())
                dbPassword= "GZ2cPTsEvFC3";
            dbName = request.getParameter("db_name");
            dbTableName = request.getParameter("db_table_name");
            try {
                number = Integer.parseInt(request.getParameter("db_column_number"));
            }catch (NumberFormatException n)
            {
                response.sendRedirect("index.html");
                return;
            }
            // this should be filled properly
        }

// check if parameter of index.html were filled
        if (dbUserName == null || dbPassword == null || dbName == null || dbTableName == null) {
            response.sendRedirect("index.html");

        } else {

            String createTableQuery = "create table " + dbTableName + " ID INT PRIMARY ;";
            // response.setContentType("text/html");
            // out.println("<html><head><title>Create Tables Servlet</title></head><body>");
            ResultSet resSet = null;
            String[] tableset;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://mysql.cc.puv.fi:3306/" + dbName;
                conn = DriverManager.getConnection(url, dbUserName, dbPassword);
                stmt = conn.createStatement();
                DatabaseMetaData dmd = conn.getMetaData();
                resSet = dmd.getTypeInfo();
/*

                try {
                    stmt.executeUpdate(createTableQuery);
                    DatabaseMetaData dmd = conn.getMetaData();
                    //  resSet =  dmd.getTableTypes();
            //        resSet = dmd.getTables(null, null, null, new String[]{"TABLE"});
            //   out.println("<p>Today: " + new Date() + " " + dbTableName
             //           + " was created successfully! ");

                } catch (SQLException sqlE) {
                    out.println("<p>" + sqlE.getMessage());
                }

                //   out.println("<hr><p style='text-align: center;' <a href='index.html'>Back</a></p>");
                //   out.println("</body>");
                //   out.println("</html>");
                */
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

            String selectTable = "";
            try {
                while (resSet.next()) {
                    selectTable += "<option value='" + resSet.getString("TYPE_NAME") + "'>" +
                            resSet.getString("TYPE_NAME") + "</option>";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String columsHeader = "<tr><th>Name</th><th>Type</th><th>Länge/Werte</th><th>Standard</th>" +
                    "<th>Null</th><th>Index</th><th>A_I</th>" +
                    "<th>Kommentare</th></tr>";

            response.setContentType("text/html");
            out.println("<html><head><title>Database Columnscreation Servlet</title></head><body>");
            out.println("<h2>Create Columns</h2>");
            out.println("<form method=\"POST\" action=\"cdbts\">");

            out.println("<table border='1'>");
            out.println(columsHeader);

            number = Integer.parseInt(request.getParameter("db_column_number"));

            for (int i = 0; i < number; i++) {

                out.println(" <tr>" +
                        "        <td><input type=\"text\" name='name" + i + "'></td>" +
                        "        <td>" +
                        "            <select name='type" + i + "'>" +
                        selectTable + //fixme check out if this works
                        //      "            <option value=\"INT\">INT</option>" +
                        //      "            <option value=\"VARCHAR\">VARCHAR</option>" +
                        //      "            <option value=\"TEXT\">TEXT</option>" +
                        //      "            <option value=\"DATE\">DATE</option>" +
                        "        </select>" +
                        "        </td>" +
                        "        <td><input type=\"text\" name='length" + i + "'></td>" +
                        "        <td><select name='standard" + i +"'>" +
                        "            <option value=\"\">Kein(e)</option>" +
                        "            <option value=\"NULL\">NULL</option>" +
                        "            <option value=\"CURRENT_TIMESTAMP\">CURRENT_TIMESTAMP</option>" +
                        "        </select></td>" +
                        "        <td><input type=\"checkbox\" value=\"null\" name='nullCheck" + i + "'></td>" +
                        "        <td><select name='index" + i + "'>" +
                        "            <option value=\"\">---</option>" +
                        "            <option value=\"PRIMARY KEY\">PRIMARY</option>" +
                        "            <option value=\"UNIQUE\">UNIQUE</option>" +
                        "            <option value=\"INDEX\">INDEX</option>" +
                        "            <option value=\"FULLTEXT\">FULLTEXT</option>" +
                        "        </select></td>" +
                        "<td><input type='checkbox' name='autoinkrement" + i + "'></td>" +
                        "<td><input type=\"text\" name='comment" + i + "'></td>");
            }

            out.println("</table>" +"<input type='submit' name='action' value='submit'>"+
                    "</form></body></html>");
        }
    }
}