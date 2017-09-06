package entity;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.DataBase;
import utility.Logger;

/**
 * Created by SAI on 9/3/2017.
 */

public class MeasurementType {
    private int id;
    private String type_name;

    public MeasurementType(int id, String type_name) {
        this.id = id;
        this.type_name = type_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public static ArrayList<MeasurementType> parseJSON(String strJSON) {
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            ArrayList<MeasurementType> arrMeasurementType = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
                String type_name = objJSON.getString("type_name").equals("null") ? "" : objJSON.getString("type_name");
                MeasurementType objMeasurementType = new MeasurementType(id, type_name);
                arrMeasurementType.add(objMeasurementType);
            }
            return arrMeasurementType;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static MeasurementType getDataFromDatabase(Context mContext, int intTypeId) {
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetch(DataBase.measurement_type, "id=" + intTypeId);
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                MeasurementType obj = new MeasurementType(cur.getInt(1), cur.getString(2));
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
}
