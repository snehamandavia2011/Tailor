package entity;

import java.io.Serializable;

/**
 * Created by SAI on 9/5/2017.
 */

public class EstimatedSize implements Serializable {
    int size_id, category_id, age_group_id;
    String size;
    boolean isSelected;

    public EstimatedSize(int size_id, int category_id, int age_group_id, String size, boolean isSelected) {
        this.size_id = size_id;
        this.category_id = category_id;
        this.age_group_id = age_group_id;
        this.size = size;
        this.isSelected = isSelected;
    }

    public int getAge_group_id() {
        return age_group_id;
    }

    public void setAge_group_id(int age_group_id) {
        this.age_group_id = age_group_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
