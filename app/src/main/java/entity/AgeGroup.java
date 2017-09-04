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

public class AgeGroup implements Serializable{
    private int id, from_age, to_age;

    public AgeGroup(int id, int from_age, int to_age) {
        this.id = id;
        this.from_age = from_age;
        this.to_age = to_age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_age() {
        return from_age;
    }

    public void setFrom_age(int from_age) {
        this.from_age = from_age;
    }

    public int getTo_age() {
        return to_age;
    }

    public void setTo_age(int to_age) {
        this.to_age = to_age;
    }

    public static ArrayList<AgeGroup> parseJSON(String strJSON) {
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            ArrayList<AgeGroup> arrAgeGroup = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
                int from_age = objJSON.getString("from_age").equals("null") ? 0 : objJSON.getInt("from_age");
                int to_age = objJSON.getString("to_age").equals("null") ? 0 : objJSON.getInt("to_age");
                AgeGroup objAgeGroup = new AgeGroup(id, from_age, to_age);
                arrAgeGroup.add(objAgeGroup);
            }
            return arrAgeGroup;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static AgeGroup getDataFromDatabase(Context mContext, int intAgeGroup) {
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetch(DataBase.age_group, DataBase.age_group_int, "id=" + intAgeGroup);
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                AgeGroup obj = new AgeGroup(cur.getInt(1), cur.getInt(2), cur.getInt(3));
                return obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cur.close();
            db.close();
        }
        return null;
    }

    public static ArrayList<AgeGroup> getDataFromDatabase(Context mContext) {
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetchAll(DataBase.age_group, DataBase.age_group_int);
        ArrayList<AgeGroup> arrAgeGroup = null;
        try {
            arrAgeGroup = new ArrayList<>();
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    AgeGroup obj = new AgeGroup(cur.getInt(1), cur.getInt(2), cur.getInt(3));
                    arrAgeGroup.add(obj);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            cur.close();
            db.close();
            return arrAgeGroup;
        }
    }

    @Override
    public String toString() {
        return this.from_age + " - " + this.to_age;
    }
}
