package forum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ForumServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String bValue = request.getParameter("action");
        ArrayList<Message> searchResults= new ArrayList<Message>();

        if(bValue.equalsIgnoreCase("Search Name")){
            String input = request.getParameter("sName");
            out.println(Message.printSearchedMessages(Message.searchName(input), input));
        }else if(bValue.equalsIgnoreCase("Search Date")){
            String input = request.getParameter("sDate");
            out.println(Message.printSearchedMessages(Message.searchDate(input), input));
        }
   }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String bValue = request.getParameter("action");

        if(bValue.equals("Submit")) {

            String username;
            String message;
            if(request.getParameter("userName").isEmpty()){
                username = "noUser";
            }else{
                username = request.getParameter("userName");
            }
            if(request.getParameter("message").isEmpty()){
                message = "noMessage";
            }else{
                message = request.getParameter("message");
            }

            Message m = new Message(username, Message.setCurrentDate(), message);
            Message.addToList(m);
            out.println(Message.printAllMessages());

        }else if(bValue.equalsIgnoreCase("Delete Forum")) {
            Message.deletePostList();
            out.println(Message.printAllMessages());

        }else if(bValue.equalsIgnoreCase("Go to Forum")){

            out.println(Message.printAllMessages());
        }
        out.close();
    }
}