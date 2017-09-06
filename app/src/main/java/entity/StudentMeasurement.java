package entity;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import utility.DataBase;
import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 9/6/2017.
 */

public class StudentMeasurement {
    private int _ID;
    private String serverPK, studFirstName, studLastName, studRollNumber, school_id, class_id, age_group_id, size_id, category_id, is_successfully_submitted, user_id, datetime, size;

    public StudentMeasurement(int _ID, String serverPK, String studFirstName, String studLastName, String studRollNumber, String school_id, String class_id, String age_group_id, String size_id, String category_id, String is_successfully_submitted, String user_id, String datetime, String size) {
        this._ID = _ID;
        this.serverPK = serverPK;
        this.studFirstName = studFirstName;
        this.studLastName = studLastName;
        this.studRollNumber = studRollNumber;
        this.school_id = school_id;
        this.class_id = class_id;
        this.age_group_id = age_group_id;
        this.size_id = size_id;
        this.category_id = category_id;
        this.is_successfully_submitted = is_successfully_submitted;
        this.user_id = user_id;
        this.datetime = datetime;
        this.size = size;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getServerPK() {
        return serverPK;
    }

    public void setServerPK(String serverPK) {
        this.serverPK = serverPK;
    }

    public String getStudFirstName() {
        return studFirstName;
    }

    public void setStudFirstName(String studFirstName) {
        this.studFirstName = studFirstName;
    }

    public String getStudLastName() {
        return studLastName;
    }

    public void setStudLastName(String studLastName) {
        this.studLastName = studLastName;
    }

    public String getStudRollNumber() {
        return studRollNumber;
    }

    public void setStudRollNumber(String studRollNumber) {
        this.studRollNumber = studRollNumber;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getAge_group_id() {
        return age_group_id;
    }

    public void setAge_group_id(String age_group_id) {
        this.age_group_id = age_group_id;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getIs_successfully_submitted() {
        return is_successfully_submitted;
    }

    public void setIs_successfully_submitted(String is_successfully_submitted) {
        this.is_successfully_submitted = is_successfully_submitted;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public static ArrayList<StudentMeasurement> getDataFromDatabase(Context mContext) {
        ArrayList<StudentMeasurement> arrStudentMeasurement = new ArrayList<>();
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetch(DataBase.student_measurement, DataBase.student_measurement_int, "user_id=" + Helper.getStringPreference(mContext, User.Fields.ID, ""));
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    StudentMeasurement obj = new StudentMeasurement(cur.getInt(0), cur.getString(1), cur.getString(2),
                            cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6), cur.getString(7),
                            cur.getString(8), cur.getString(9), cur.getString(10), cur.getString(11), cur.getString(12), cur.getString(13));
                    arrStudentMeasurement.add(obj);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            cur.close();
            db.close();
        }
        return arrStudentMeasurement;
    }
}
