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

public class ClassMaster implements Serializable{
    private int id;
    private String class_name;

    public ClassMaster(int id, String class_name) {
        this.id = id;
        this.class_name = class_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public static ArrayList<ClassMaster> parseJSON(String strJSON) {
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            ArrayList<ClassMaster> arrClass = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
                String class_name = objJSON.getString("class_name").equals("null") ? "" : objJSON.getString("class_name");
                ClassMaster obj = new ClassMaster(id, class_name);
                arrClass.add(obj);
            }
            return arrClass;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static ArrayList<ClassMaster> getDataFromDatabase(Context mContext) {
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetchAll(DataBase.class_master, DataBase.class_master_int);
        ArrayList<ClassMaster> arrClass = null;
        try {
            arrClass = new ArrayList<>();
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    ClassMaster obj = new ClassMaster(cur.getInt(1), cur.getString(2));
                    arrClass.add(obj);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            cur.close();
            db.close();
            return arrClass;
        }
    }

    @Override
    public String toString() {
        return this.getClass_name();
    }
}
