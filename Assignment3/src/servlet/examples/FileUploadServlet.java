package servlet.examples;

        import java.io.File;
        import java.io.IOException;
        import java.io.PrintWriter;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import javax.servlet.ServletException;
        import javax.servlet.annotation.MultipartConfig;
        import javax.servlet.annotation.WebInitParam;
        import javax.servlet.annotation.WebServlet;
        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import javax.servlet.http.Part;

//Here we configure the servlet
@WebServlet(name = "FileUploadServlet", urlPatterns = {"/fus"},
        initParams = { @WebInitParam(name = "upload_path", value = "downloadDir") })
@MultipartConfig(fileSizeThreshold=1024*1024*10,  // 10 MB
        maxFileSize=1024*1024*50,       // 50 MB
        maxRequestSize=1024*1024*100
)    // 100 MB
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 205242440643911308L;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");

    //Here we define the destination directory for saving uploaded files.
    // private final String UPLOAD_DIR = "upload/files/".replace('/', File.separatorChar);
    String uploadFilePath;


    public void init() {

        //Here we get the absolute path of the destination directory
        //uploadFilePath = this.getServletContext().getRealPath(UPLOAD_DIR) + File.separator;

        uploadFilePath = this.getServletContext().getRealPath(getServletConfig().getInitParameter("upload_path")) + File.separator;

        //Here we create the destination directory under the project main directory if it does not exists
        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index.html");
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String fileName = null;
        File fileObj=null;
        String feedback = "File successfully uploaded!";
        String filename=request.getParameter("filename");
        //StringBuilder feedback = new StringBuilder("List of files uploaded by " + userName + "<br><ul>");


        //Here we get all the parts from request and write it to the file on server
        for (Part part : request.getParts()) {

            //This does not work for Java 7
            //fileName=part.getSubmittedFileName();
            //feedback.append("part.getSubmittedFileName()=" + part.getSubmittedFileName() + System.lineSeparator());
            //Here we read the value of the input, whose type is file.
            //Part filePart = request.getPart("fileName");

            //Here we extract the name of the file from the part. If the part is a file part, we get a valid
            //file name and otherwise null
            fileName = getFileName(part);

            //Here we check whether the part is really a file part or not. If the part is a file part,
            //we save it to the destination directory. The following line does not work with Java 7.
            //  if(part.getSubmittedFileName()!=null) {

            if(!fileName.equals("")) {
                fileObj=new File(fileName);
                fileName=fileObj.getName();

                //Here we rename the file by adding user name and date before the name of the file
                fileName= filename;

                fileObj=new File(uploadFilePath  + fileName);

                part.write(fileObj.getAbsolutePath());
                //feedback.append("<li>" + fileObj.getAbsolutePath()  + " " + fileObj.length() + "</li>");
                //feedback.append("<li>" + fileObj.getAbsolutePath()  + " " + fileObj.length() + "<br><img src='" + getServletConfig().getInitParameter("upload_path")  + File.separator + fileName + "' width='200' height='200'>"  + "</li>");
            }

        }

        //feedback.append("</ul>");

        PrintWriter out = response.getWriter();
        out.println("<html><head><title>" + "Response of " + this.getServletConfig().getInitParameter("urlPatterns") +
                "</title></head><body><h1>Summary</h1>");

        out.println(feedback.toString());
        out.println("<p style='text-align: center;'><a href='upload.html'>Main Page</a></p>");
        out.println("<p style='text-align: center;'><a href='ds'>Download side</a></p>");
        out.println("</body></html>");

        out.close();

       /* request.setAttribute("message", feedback.toString() );
       getServletContext().getRequestDispatcher("/response.jsp").forward(request, response);
          */
    }

    //This method extracts the name of the uploaded file from the header part
    private String getFileName(Part part) {

        String contentDisp = part.getHeader("content-disposition");

        if(contentDisp !=null) {

         /*In the following line we split a piece of text like the following:
            form-data; name="fileName"; filename="C:\Users\Public\Pictures\Sample Pictures\img.jpg"
            */
            String[] tokens = contentDisp.split(";");

            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    return new File(token.split("=")[1].replace('\\', '/')).getName().replace("\"", "");
                }
            }
        }

        return "";
    }
}