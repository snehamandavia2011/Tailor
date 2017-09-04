package entity;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import utility.DataBase;
import utility.Logger;

/**
 * Created by SAI on 9/3/2017.
 */

public class SchoolMaster implements Serializable{
    private int id;
    private String school_name, address, contact_no, email;

    public SchoolMaster(int id, String school_name, String address, String contact_no, String email) {
        this.id = id;
        this.school_name = school_name;
        this.address = address;
        this.contact_no = contact_no;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public static ArrayList<SchoolMaster> parseJSON(String strJSON) {
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            ArrayList<SchoolMaster> arrSchool = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
                String school_name = objJSON.getString("school_name").equals("null") ? "" : objJSON.getString("school_name");
                String address = objJSON.getString("address").equals("null") ? "" : objJSON.getString("address");
                String contact_no = objJSON.getString("contact_no").equals("null") ? "" : objJSON.getString("contact_no");
                String email = objJSON.getString("email").equals("null") ? "" : objJSON.getString("email");
                SchoolMaster objSchool = new SchoolMaster(id, school_name, address, contact_no, email);
                arrSchool.add(objSchool);
            }
            return arrSchool;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static ArrayList<SchoolMaster> getDataFromDatabase(Context mContext) {
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetchAll(DataBase.school_master, DataBase.school_master_int);
        ArrayList<SchoolMaster> arrSchool = null;
        try {
            arrSchool = new ArrayList<>();
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    SchoolMaster obj = new SchoolMaster(cur.getInt(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5));
                    arrSchool.add(obj);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            cur.close();
            db.close();
            return arrSchool;
        }
    }

    @Override
    public String toString() {
        return this.school_name;
    }
}
