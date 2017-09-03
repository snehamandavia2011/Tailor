package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
}
