package utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Амфетамин on 27.10.14.
 */
public class Utils {
    public final static String Auth_Result = "AUTH_RESULT";
    public final static String Auth_User = "USER";
    public final static String Upload_Result = "UPLOAD_RESULT";
    public final static String Register_Result = "REGISTER_RESULT";
    public final static String Uploaded_File = "UPLOADED_FILENAME";
    public final static String Xml_Result = "XML_RESULT";
    public final static String FILE_TO_SHOW = "SHOW_FILE";

    public static String checkCache(HttpServletRequest request) {

        String result = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Utils.Auth_User))
                    result = cookie.getValue();
            }
        }
        return result;
    }
}
