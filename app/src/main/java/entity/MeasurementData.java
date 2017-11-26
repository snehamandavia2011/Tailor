package entity;

import java.io.Serializable;

/**
 * Created by SAI on 11/26/2017.
 */

public class MeasurementData implements Serializable{
    int measurement_type_id;
    String measurement_value;

    public MeasurementData(int measurement_type_id, String measurement_value) {
        this.measurement_type_id = measurement_type_id;
        this.measurement_value = measurement_value;
    }

    public int getMeasurement_type_id() {
        return measurement_type_id;
    }

    public void setMeasurement_type_id(int measurement_type_id) {
        this.measurement_type_id = measurement_type_id;
    }

    public String getMeasurement_value() {
        return measurement_value;
    }

    public void setMeasurement_value(String measurement_value) {
        this.measurement_value = measurement_value;
    }
}
