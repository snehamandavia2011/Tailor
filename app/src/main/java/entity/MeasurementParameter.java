package entity;

/**
 * Created by SAI on 9/4/2017.
 */

public class MeasurementParameter {
    private  int measurement_type_id,category_id;
    private String type_name;

    public MeasurementParameter(int measurement_type_id, int category_id, String type_name) {
        this.measurement_type_id = measurement_type_id;
        this.category_id = category_id;
        this.type_name = type_name;
    }

    public int getMeasurement_type_id() {
        return measurement_type_id;
    }

    public void setMeasurement_type_id(int measurement_type_id) {
        this.measurement_type_id = measurement_type_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
