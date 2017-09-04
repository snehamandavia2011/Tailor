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

public class Category {
    private int id, parent_id;
    private String category_name, category_description, category_for, image;

    public Category(int id, int parent_id, String category_name, String category_description, String category_for, String image) {
        this.id = id;
        this.parent_id = parent_id;
        this.category_name = category_name;
        this.category_description = category_description;
        this.category_for = category_for;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public String getCategory_for() {
        return category_for;
    }

    public void setCategory_for(String category_for) {
        this.category_for = category_for;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static ArrayList<Category> parseJSON(String strJSON) {
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            ArrayList<Category> arrCategory = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
                int parent_id = objJSON.getString("parent_id").equals("null") ? 0 : objJSON.getInt("parent_id");
                String category_name = objJSON.getString("category_name").equals("null") ? "" : objJSON.getString("category_name");
                String category_description = objJSON.getString("category_description").equals("null") ? "" : objJSON.getString("category_description");
                String category_for = objJSON.getString("category_for").equals("null") ? "" : objJSON.getString("category_for");
                String image = objJSON.getString("image").equals("null") ? "" : objJSON.getString("image");
                Category objCategory = new Category(id, parent_id, category_name, category_description, category_for, image);
                arrCategory.add(objCategory);
            }
            return arrCategory;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }


    public static ArrayList<Category> getDataFromDatabase(Context mContext) {
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetchAll(DataBase.category_master, DataBase.category_master_int);
        ArrayList<Category> arrCategory = null;
        try {
            arrCategory = new ArrayList<>();
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    Category obj = new Category(cur.getInt(1), cur.getInt(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6));
                    arrCategory.add(obj);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            cur.close();
            db.close();
            return arrCategory;
        }
    }

    @Override
    public String toString() {
        return this.getCategory_name();
    }
}
