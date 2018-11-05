import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public class DownloadServlet extends HttpServlet {
    PrintWriter out;
    String separator;
    String downloadDir;

    public void init() {
        separator = File.separator;
        downloadDir = this.getServletContext().getRealPath(getServletContext().getInitParameter("download-dir")) + separator;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        File dDir = new File(downloadDir);
        String[] fileArray = dDir.list();
        String action = request.getParameter("action");

        if (action == null) {
            response.setContentType("text/html");
            try {
                out = response.getWriter();
                out.println(
                        "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "    <title>Download</title>" +
                                "<link rel='stylesheet' href='styles.css'>" +
                                "</head>" +
                                "<body>" +
                                "<p>" + downloadDir + "</p>"
                );
                out.println("<h2>Downloadable Files</h2>"
                        + "<form action='ds' method='post'>"
                        + "<table class='downloads'>"
                );

                for (String s : fileArray) {
                    out.println("<tr><td></td><td>" +
                            "<input type='submit' value='" + s + "'name='action'>" +
                            "</td></tr>");
                }
                out.println("</table></form></body></html>");

            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {
            File downloadFile = new File(downloadDir + action);
            if (!downloadFile.exists()) {
                // throw new
                // ServletException("Invalid or non-existent 'pdf-dir context-param!");
            }
            ServletOutputStream outputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                outputStream = response.getOutputStream();
                // Here we set the response headers
                response.setContentType(getContentType(action));
                response.addHeader("Content-Disposition", "attachment;filename="
                        + action);
                response.setContentLength((int) downloadFile.length());
                // Here we read the content of the file.
                /*
                 * FileInputStream inputStream = new FileInputStream(downloadFile);
                 * inputStreamBuffer = new BufferedInputStream(inputStream);
                 */
                bufferedInputStream = new BufferedInputStream(new FileInputStream(
                        downloadFile));

                int readBytes = 0;
                // Here we read from the file and write to the ServletOutputStream
                while ((readBytes = bufferedInputStream.read()) != -1)
                    outputStream.write(readBytes);
            } catch (IOException e) {
                // throw new ServletException(ioex.getMessage());
                e.printStackTrace();
            } finally {
                // Here we close the input/output streams
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        }

    private String getContentType(String fileName) {
        String fileExt = "";
        String contentType = "";
        int i;

        if ((i = fileName.indexOf(".")) != -1) {
            /*
             * Here we read s substring of filename starting from ".", which
             * will be the file extension
             */
            fileExt = fileName.substring(i);
        }
        if (fileExt.equals("doc") || fileExt.equals("docx"))
            contentType = "application/msword";
        else if (fileExt.equals("pdf"))
            contentType = "application/pdf";
        else if (fileExt.equals("mp3"))
            contentType = "audio/mpeg";
        else if (fileExt.equals("jpg") || fileExt.equals("gif")
                || fileExt.equals("tif") || fileExt.equals("jpeg") ||fileExt.equals("bmp"))
            contentType = "application/img";
        else if (fileExt.equals("xml"))
            contentType = "text/xml";
        else if (fileExt.equals("rtf"))
            contentType = "applictaion/rtf";
        else
            contentType = "application/unknown";
        return contentType;
    }
}
