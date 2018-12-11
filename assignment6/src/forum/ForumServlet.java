package forum;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;

@MultipartConfig(fileSizeThreshold=1024*1024*10,  // 10 MB
        maxFileSize=1024*1024*50,       // 50 MB
        maxRequestSize=1024*1024*100
)    // 100 MB

public class ForumServlet extends HttpServlet {

    public void init() {
        MessageController.initLogFile();
        MessageController.readMsgLogFile();

        MessageController.initImageFolder();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map<String, String[]> parameterMap = request.getParameterMap();
        //ArrayList<forum.Message> searchResults = new ArrayList<forum.Message>();
        String paraAction[] = parameterMap.get("action");

        try {
            if (paraAction[0].equalsIgnoreCase("Search Name")) {
                String input[] = parameterMap.get("sName");
                out.println(MessageController.printSearchedMessages(MessageController.searchName(input[0]), input[0]));
            } else if (paraAction[0].equalsIgnoreCase("Search Date")) {
                String input[] = parameterMap.get("sDate");
                out.println(MessageController.printSearchedMessages(MessageController.searchDate(input[0]), input[0]));
            }
        } catch (NullPointerException e) {
            //.printStackTrace();
            System.out.println("User tried to reload the webside without a session");
            response.sendRedirect("index.html");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String actionValue[] = parameterMap.get("action");
        String fileName = "";
        LinkedList<String> imageList;
        File fileObj = null;

        if (actionValue[0].equals("Submit")) {

            String username[] = parameterMap.get("userName");
            HttpSession mySession = request.getSession();
            // String username = (String)mySession.getAttribute("username");
            String message[] = parameterMap.get("message");
            LinkedList<String> views = new LinkedList<>();
            if (parameterMap.get("views") != null) {
                for (String s : parameterMap.get("views")) {
                    views.add(s);
                }
            }
            LinkedList<String> sports = new LinkedList<>();
            if (parameterMap.get("sports") != null) {

                for (String s : parameterMap.get("sports")) {
                    sports.add(s);
                }
            }
            if (username[0] != null && message[0] != null && !message[0].trim().isEmpty()) {
                String msg = message[0];
                if (msg.length() > 199) {
                    msg = msg.substring(0, 199);
                }

                imageList = new LinkedList<>();

                for (Part part : request.getParts()) {
                    fileName = MessageController.getFilename(part);
                    if (!fileName.equals("")) {
                        fileObj = new File(fileName);
                        fileName = fileObj.getName();
                        fileName = username[0] + "_" + MessageController.setCurrentDate() + "_" + fileName;
                       String tmp = MessageController.saveFileToDisk(part, fileName);
                       if(!tmp.equals(""))
                       {

                           imageList.add(tmp);
                       }

                    }
                }

                Message m = new Message(username[0], MessageController.setCurrentDate(), msg, views, sports, imageList);
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