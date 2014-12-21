package Sample;

import org.apache.poi.hssf.OldExcelFormatException;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

/**
 * Created by Амфетамин on 26.10.14.
 */
@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 10)   // 10MB
public class UploadFile extends HttpServlet {
    /**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "uploadFiles";
    private String currentUserID;

    /**
     * handles file upload
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        try {
            currentUserID = Utils.checkCache(request);
            if (!(currentUserID == null || currentUserID.isEmpty())) {
                // gets absolute path of the web application
                String appPath = request.getServletContext().getRealPath("");
                // constructs path of the directory to save uploaded file
                String savePath = appPath + File.separator + SAVE_DIR;

                String fullPath = "";
                String fileName = "";

                // creates the save directory if it does not exists
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdir();
                }

                for (Part part : request.getParts()) {
                    fileName = extractFileName(part);
                    if (fileName == null || fileName.isEmpty()) {
                        request.setAttribute(Utils.Upload_Result, "File not chosen");
                        forward(request, response, "/upload.jsp");
                        return;
                    }
                    fullPath = savePath + File.separator + fileName;
                    part.write(fullPath);

                }
                request.setAttribute(Utils.Upload_Result, "Upload has been done successfully!");
                request.setAttribute(Utils.Uploaded_File, fullPath);
                // forward(request, response, "/showxml.jsp");
                forward(request, response, "/showxml.jsp");
                return;
            } else {
                request.setAttribute(Utils.Auth_Result, "Authentication error");
                forward(request, response, "/index.jsp");
            }
        } catch (IllegalStateException e) {//file's size more then specified
            request.setAttribute(Utils.Upload_Result, "File can't be more then 10 mb");
            forward(request, response, "/upload.jsp");
        } catch (OldExcelFormatException e) {
            request.setAttribute(Utils.Upload_Result, "This old Excel format is not supported.");
            forward(request, response, "/upload.jsp");
        } catch (Exception e) {
            request.setAttribute(Utils.Upload_Result, "Unknown error");
            forward(request, response, "/upload.jsp");
        }


    }

    private void forward(HttpServletRequest request,
                         HttpServletResponse response, String address) throws ServletException, IOException {
        getServletContext().getRequestDispatcher(address).forward(
                request, response);
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}
