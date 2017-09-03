package asyncTask;

import android.content.Context;
import android.text.Html;

import java.util.ArrayList;
import java.util.Date;

import entity.AgeGroup;
import entity.Category;
import entity.CategoryMeasurementRelation;
import entity.MeasurementType;
import entity.User;
import utility.ConstantVal;
import utility.DataBase;
import utility.DateTimeUtils;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 9/3/2017.
 */

public class loadData {
    Context mContext;

    public loadData(Context mContext) {
        this.mContext = mContext;
        try {
            loadAgeGroup().join();
            getMeasurementType().join();
            getCategoryMeasurementRelation().join();
            getCategory().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Thread loadAgeGroup() {
        Thread t = new Thread() {
            public void run() {
                final HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.getAgeGroup();
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                        um.getParamNames(), new String[]{Helper.getStringPreference(mContext, User.Fields.TOKEN, "")});
                String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                if (result != null && !result.equals("")) {
                    ArrayList<AgeGroup> arrAgeGroup = AgeGroup.parseJSON(result);
                    if (arrAgeGroup != null && arrAgeGroup.size() > 0) {
                        DataBase db = new DataBase(mContext);
                        db.open();
                        for (AgeGroup objAgeGroup : arrAgeGroup) {
                            db.insert(DataBase.age_group, DataBase.age_group_int, new String[]{String.valueOf(objAgeGroup.getId()), String.valueOf(objAgeGroup.getFrom_age()), String.valueOf(objAgeGroup.getTo_age())});
                        }
                        db.close();
                    }
                }
            }
        };
        t.start();
        return t;
    }


    public Thread getMeasurementType() {
        Thread t = new Thread() {
            public void run() {
                final HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.getMeasurementType();
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                        um.getParamNames(), new String[]{Helper.getStringPreference(mContext, User.Fields.TOKEN, "")});
                String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                if (result != null && !result.equals("")) {
                    ArrayList<MeasurementType> arrMeasurementType = MeasurementType.parseJSON(result);
                    if (arrMeasurementType != null && arrMeasurementType.size() > 0) {
                        DataBase db = new DataBase(mContext);
                        db.open();
                        for (MeasurementType objMeasurementType : arrMeasurementType) {
                            db.insert(DataBase.measurement_type, DataBase.measurement_type_int,
                                    new String[]{String.valueOf(objMeasurementType.getId()), objMeasurementType.getType_name()});
                        }
                        db.close();
                    }
                }
            }
        };
        t.start();
        return t;
    }

    public Thread getCategoryMeasurementRelation() {
        Thread t = new Thread() {
            public void run() {
                final HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.getCategoryMeasurementRelation();
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                        um.getParamNames(), new String[]{Helper.getStringPreference(mContext, User.Fields.TOKEN, "")});
                String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                if (result != null && !result.equals("")) {
                    ArrayList<CategoryMeasurementRelation> arrCategoryMeasurementRelation = CategoryMeasurementRelation.parseJSON(result);
                    if (arrCategoryMeasurementRelation != null && arrCategoryMeasurementRelation.size() > 0) {
                        DataBase db = new DataBase(mContext);
                        db.open();
                        for (CategoryMeasurementRelation obj : arrCategoryMeasurementRelation) {
                            db.insert(DataBase.category_measurement_relation, DataBase.category_measurement_relation_int,
                                    new String[]{String.valueOf(obj.getId()), String.valueOf(obj.getCategory_id()),
                                            String.valueOf(obj.getMeasurement_type_id()), String.valueOf(obj.getSize_id()),
                                            obj.getSize(), obj.getMeasurement_value()});
                        }
                        db.close();
                    }
                }
            }
        };
        t.start();
        return t;
    }


    public Thread getCategory() {
        Thread t = new Thread() {
            public void run() {
                final HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.getCategory();
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                        um.getParamNames(), new String[]{Helper.getStringPreference(mContext, User.Fields.TOKEN, "")});
                String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                if (result != null && !result.equals("")) {
                    ArrayList<Category> arrCategory = Category.parseJSON(result);
                    if (arrCategory != null && arrCategory.size() > 0) {
                        DataBase db = new DataBase(mContext);
                        db.open();
                        for (Category obj : arrCategory) {
                            db.insert(DataBase.category_master, DataBase.category_master_int,
                                    new String[]{String.valueOf(obj.getId()), String.valueOf(obj.getParent_id()),
                                            obj.getCategory_name(), obj.getCategory_description(), obj.getCategory_for(), obj.getImage()});
                        }
                        db.close();
                    }
                }
            }
        };
        t.start();
        return t;
    }
}
