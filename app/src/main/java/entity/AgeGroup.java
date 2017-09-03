package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.Logger;

/**
 * Created by SAI on 9/3/2017.
 */

public class AgeGroup {
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
}
