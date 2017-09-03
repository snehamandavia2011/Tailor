package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.Logger;

/**
 * Created by SAI on 9/3/2017.
 */

public class CategoryMeasurementRelation {
    private int id, category_id, measurement_type_id, size_id;
    private String size, measurement_value;

    public CategoryMeasurementRelation(int id, int category_id, int measurement_type_id, int size_id, String size, String measurement_value) {
        this.id = id;
        this.category_id = category_id;
        this.measurement_type_id = measurement_type_id;
        this.size_id = size_id;
        this.size = size;
        this.measurement_value = measurement_value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getMeasurement_type_id() {
        return measurement_type_id;
    }

    public void setMeasurement_type_id(int measurement_type_id) {
        this.measurement_type_id = measurement_type_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public String getMeasurement_value() {
        return measurement_value;
    }

    public void setMeasurement_value(String measurement_value) {
        this.measurement_value = measurement_value;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public static ArrayList<CategoryMeasurementRelation> parseJSON(String strJSON) {
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            ArrayList<CategoryMeasurementRelation> arrobjCategoryMeasurementRelation = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
                int category_id = objJSON.getString("category_id").equals("null") ? 0 : objJSON.getInt("category_id");
                int measurement_type_id = objJSON.getString("measurement_type_id").equals("null") ? 0 : objJSON.getInt("measurement_type_id");
                int size_id = objJSON.getString("size_id").equals("null") ? 0 : objJSON.getInt("size_id");
                String size = objJSON.getString("size").equals("null") ? "" : objJSON.getString("size");
                String measurement_value = objJSON.getString("measurement_value").equals("null") ? "" : objJSON.getString("measurement_value");
                CategoryMeasurementRelation objCategoryMeasurementRelation = new CategoryMeasurementRelation(id, category_id, measurement_type_id, size_id, size, measurement_value);
                arrobjCategoryMeasurementRelation.add(objCategoryMeasurementRelation);
            }
            return arrobjCategoryMeasurementRelation;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }
}
