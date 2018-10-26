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
        MessageController.readMsgLogFile();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map<String, String[]> parameterMap = request.getParameterMap();
        //ArrayList<Message> searchResults = new ArrayList<Message>();
        String paraAction[] = parameterMap.get("action");

        try {
            if (paraAction[0].equalsIgnoreCase("Search Name")) {
                String input[] = parameterMap.get("sName");
                out.println(MessageController.printSearchedMessages(MessageController.searchName(input[0]), input[0]));
            } else if (paraAction[0].equalsIgnoreCase("Search Date")) {
                String input[] = parameterMap.get("sDate");
                out.println(MessageController.printSearchedMessages(MessageController.searchDate(input[0]), input[0]));
            }
        }catch (NullPointerException e)
        {
            //.printStackTrace();
            System.out.println("User tried to reload the webside without a session");
            response.sendRedirect("login.html");
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
            LinkedList<String> views = new LinkedList<>();
            if(parameterMap.get("views") != null) {
                for (String s : parameterMap.get("views")) {
                    views.add(s);
                }
            }
            LinkedList<String> sports = new LinkedList<>();
            if(parameterMap.get("sports")!= null) {

                for (String s : parameterMap.get("sports")) {
                    sports.add(s);
                }
            }
            if(username[0] != null && message[0] != null && !message[0].trim().isEmpty()) {
                String msg = message[0];
                if(msg.length() > 199)
                {
                    msg = msg.substring(0,199);
                }

                Message m = new Message(username[0], MessageController.setCurrentDate(), msg, views, sports);
                MessageController.addToList(m);
                MessageController.logMessageXML(m);
            }



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