package utility;

import android.content.Context;

import tailor.mafatlal.com.tailor.R;

/**
 * Created by SAI on 9/1/2017.
 */

public class ConstantVal {

    public static class Gender {
        public static final String MALE = "Male";
        public static final String FEMALE = "Female";
    }

    public static class ToastBGColor {
        public static final int SUCCESS = R.color.Success;
        public static final int INFO = R.color.info;
        public static final int WARNING = R.color.warning;
        public static final int DANGER = R.color.danger;
    }

    public static class ServerResponseCode {
        public static final String SESSION_EXISTS = "1";//value receive from server as response
        public static final String NO_INTERNET = "001";
        public static final String PARSE_ERROR = "002";
        public static String SERVER_NOT_RESPONDING = "003";
        public static String REQUEST_TIMEOUT = "004";//30 seconds
        public static String SESSION_EXPIRED = "005";//value
        public static String INVALID_LOGIN = "006";//value receive from server as response
        public static String SERVER_ERROR = "007";
        public static String SUCCESS = "008";
        public static String CLIENT_ERROR = "010";
        public static String BLANK_RESPONSE = "011";
        public static String USER_ALREADY_EXISTS = "012";

        public static String getMessage(Context ctx, String strCode) {
            try {
                int intCode = Integer.parseInt(strCode);
                if (intCode == Integer.parseInt(NO_INTERNET)) {
                    return ctx.getString(R.string.strInternetNotAvaiable);
                } else if (intCode == Integer.parseInt(PARSE_ERROR)) {
                    return ctx.getString(R.string.strUnableToParseData);
                } else if (intCode == Integer.parseInt(SERVER_NOT_RESPONDING)) {
                    return ctx.getString(R.string.strServerNotResponding);
                } else if (intCode == Integer.parseInt(REQUEST_TIMEOUT)) {
                    return ctx.getString(R.string.strRequestTimeout);
                } else if (intCode == Integer.parseInt(SESSION_EXPIRED)) {
                    return ctx.getString(R.string.strSessionExpire);
                } else if (intCode == Integer.parseInt(INVALID_LOGIN)) {
                    return ctx.getString(R.string.strInvalidUserNameAndPassword);
                } else if (intCode == Integer.parseInt(SERVER_ERROR)) {
                    return ctx.getString(R.string.strServerError);
                } else if (intCode == Integer.parseInt(SUCCESS)) {
//
                } else if (intCode == Integer.parseInt(CLIENT_ERROR)) {
                    return ctx.getString(R.string.strClientError);
                } else if (intCode == Integer.parseInt(BLANK_RESPONSE)) {
                    return ctx.getString(R.string.strDatacannotReceive);
                } else if (intCode == Integer.parseInt(USER_ALREADY_EXISTS)) {
                    return ctx.getString(R.string.strEmailIdAlreadyExists);
                }
                return strCode;
            } catch (NumberFormatException e) {
                Logger.writeToCrashlytics(e);
                return strCode;
            }
        }
    }

    private static String getWebURLPrefix() {
        //return "https://stackio.co/mWebApi/v1/";
        return "http://45.249.111.13/~glamourapp/mWebApi/v1/";
    }

    public static URLMapping employeeCredentialVerification() {
        String[] paramNames = {"email_id", "password", "android_id", "deviceName", "deviceVersion", "date", "time"};
        String URL = getWebURLPrefix() + "Credentialsmanager/employeeCredentialVerification";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping resetPasswordForEmployee() {
        String[] paramNames = {"to_email", "date", "time"};
        String URL = getWebURLPrefix() + "Emailmanager/resetPasswordForEmployee";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping registerEmployee() {
        String[] paramNames = {"firstName", "lastName", "emailId", "mobileNumber", "gender", "birthDate", "photo", "date", "time"};
        String URL = getWebURLPrefix() + "Credentialsmanager/registerEmployee";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping changePasswordForTailor() {
        String[] paramNames = {"emailId", "new_password", "token"};
        String URL = getWebURLPrefix() + "Credentialsmanager/changePasswordForTailor";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping logoutUser() {
        String[] paramNames = {"userID", "token"};
        String URL = getWebURLPrefix() + "Credentialsmanager/logoutUser";
        return new URLMapping(paramNames, URL);
    }
}
