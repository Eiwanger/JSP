package forum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

public class ForumServlet extends HttpServlet {

    public void init() {
        MessageController.initLogFile();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map<String, String[]> parameterMap = request.getParameterMap();
        ArrayList<Message> searchResults = new ArrayList<Message>();
        String paraAction[] = parameterMap.get("action");
        if (paraAction[0].equalsIgnoreCase("Search Name")) {
            String input[] = parameterMap.get("sName");
            out.println(MessageController.printSearchedMessages(MessageController.searchName(input[0]), input[0]));
        } else if (paraAction[0].equalsIgnoreCase("Search Date")) {
            String input[] = parameterMap.get("sDate");
            out.println(MessageController.printSearchedMessages(MessageController.searchDate(input[0]), input[0]));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map<String, String[]> parameterMap = request.getParameterMap();
        //String bValue = request.getParameter("action");
        String actionValue[] = parameterMap.get("action");

        if (actionValue[0].equals("Submit")) {

              String username[] = parameterMap.get("userName");
            HttpSession mySession = request.getSession();
            // String username = (String)mySession.getAttribute("username");
            String message[] = parameterMap.get("message");

            Message m = new Message(username[0], MessageController.setCurrentDate(), message[0], parameterMap.get("views"), parameterMap.get("sports"));
            MessageController.addToList(m);


            //MessageController.logMessage(m);
            MessageController.logMessageXML(m);
            out.println(MessageController.printAllMessages());


        } else if (actionValue[0].equalsIgnoreCase("Delete Forum")) {
            MessageController.deletePostList();
            out.println(MessageController.printAllMessages());

        } else if (actionValue[0].equalsIgnoreCase("Go to Forum")) {

            out.println(MessageController.printAllMessages());
        }
        out.close();

    }
}