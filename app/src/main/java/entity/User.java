package entity;

import android.content.Context;

import org.json.JSONObject;

import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 9/1/2017.
 */

public class User {
    private String id, user_name, password, employee_id, admin_usertype_id, user_type_name, first_name, last_name, contact_no, email, gender, birth_date, photo, token;

    public User(String id, String user_name, String password, String employee_id, String admin_usertype_id, String user_type_name, String first_name, String last_name, String contact_no, String email, String gender, String birth_date, String photo, String token) {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.employee_id = employee_id;
        this.admin_usertype_id = admin_usertype_id;
        this.user_type_name = user_type_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact_no = contact_no;
        this.email = email;
        this.gender = gender;
        this.birth_date = birth_date;
        this.photo = photo;
        this.token = token;
    }

    public class Fields {
        public static final String ID = "id";
        public static final String USER_NAME = "user_name";
        public static final String PASSWORD = "password";
        public static final String EMPLOYEE_ID = "employee_id";
        public static final String ADMIN_USER_TYPE_ID = "admin_user_type_id";
        public static final String USER_TYPE_NAME = "user_type_name";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String CONTACT_NO = "contact_no";
        public static final String EMAIL = "email";
        public static final String GENDER = "gender";
        public static final String BIRTH_DATE = "birth_date";
        public static final String PHOTO = "photo";
        public static final String TOKEN = "token";
    }

    public static void clearCache(Context c) {
        Helper.clearPreference(c, Fields.ID);
        Helper.clearPreference(c, Fields.USER_NAME);
        Helper.clearPreference(c, Fields.PASSWORD);
        Helper.clearPreference(c, Fields.EMPLOYEE_ID);
        Helper.clearPreference(c, Fields.ADMIN_USER_TYPE_ID);
        Helper.clearPreference(c, Fields.USER_TYPE_NAME);
        Helper.clearPreference(c, Fields.FIRST_NAME);
        Helper.clearPreference(c, Fields.LAST_NAME);
        Helper.clearPreference(c, Fields.CONTACT_NO);
        Helper.clearPreference(c, Fields.EMAIL);
        Helper.clearPreference(c, Fields.GENDER);
        Helper.clearPreference(c, Fields.BIRTH_DATE);
        Helper.clearPreference(c, Fields.PHOTO);
        Helper.clearPreference(c, Fields.TOKEN);
    }

    public void saveFieldsToPreferences(Context c) {
        Helper.setStringPreference(c, Fields.ID, this.getId());
        Helper.setStringPreference(c, Fields.USER_NAME, this.getUser_name());
        Helper.setStringPreference(c, Fields.PASSWORD, this.getPassword());
        Helper.setStringPreference(c, Fields.EMPLOYEE_ID, this.getEmployee_id());
        Helper.setStringPreference(c, Fields.ADMIN_USER_TYPE_ID, this.getAdmin_usertype_id());
        Helper.setStringPreference(c, Fields.USER_TYPE_NAME, this.getUser_type_name());
        Helper.setStringPreference(c, Fields.FIRST_NAME, this.getFirst_name());
        Helper.setStringPreference(c, Fields.LAST_NAME, this.getLast_name());
        Helper.setStringPreference(c, Fields.CONTACT_NO, this.getContact_no());
        Helper.setStringPreference(c, Fields.EMAIL, this.getEmail());
        Helper.setStringPreference(c, Fields.GENDER, this.getGender());
        Helper.setStringPreference(c, Fields.BIRTH_DATE, this.getBirth_date());
        Helper.setStringPreference(c, Fields.PHOTO, this.getPhoto());
        Helper.setStringPreference(c, Fields.TOKEN, this.getToken());
    }

    public void parseJSON(JSONObject objJSON) {
        try {
            this.setId(objJSON.getString("id").equals("null") ? "" : objJSON.getString("id"));
            this.setUser_name(objJSON.getString("user_name").equals("null") ? "" : objJSON.getString("user_name"));
            this.setPassword(objJSON.getString("password").equals("null") ? "" : objJSON.getString("password"));
            this.setEmployee_id(objJSON.getString("employee_id").equals("null") ? "" : objJSON.getString("employee_id"));
            this.setAdmin_usertype_id(objJSON.getString("admin_usertype_id").equals("null") ? "" : objJSON.getString("admin_usertype_id"));
            this.setUser_type_name(objJSON.getString("user_type_name").equals("null") ? "" : objJSON.getString("user_type_name"));
            this.setFirst_name(objJSON.getString("first_name").equals("null") ? "" : objJSON.getString("first_name"));
            this.setLast_name(objJSON.getString("last_name").equals("null") ? "" : objJSON.getString("last_name"));
            this.setContact_no(objJSON.getString("contact_no").equals("null") ? "" : objJSON.getString("contact_no"));
            this.setEmail(objJSON.getString("email").equals("null") ? "" : objJSON.getString("email"));
            this.setGender(objJSON.getString("gender").equals("null") ? "" : objJSON.getString("gender"));
            this.setBirth_date(objJSON.getString("birth_date").equals("null") ? "" : objJSON.getString("birth_date"));
            this.setPhoto(objJSON.getString("photo").equals("null") ? "" : objJSON.getString("photo"));
            this.setToken(objJSON.getString("token").equals("null") ? "" : objJSON.getString("token"));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getAdmin_usertype_id() {
        return admin_usertype_id;
    }

    public void setAdmin_usertype_id(String admin_usertype_id) {
        this.admin_usertype_id = admin_usertype_id;
    }

    public String getUser_type_name() {
        return user_type_name;
    }

    public void setUser_type_name(String user_type_name) {
        this.user_type_name = user_type_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
