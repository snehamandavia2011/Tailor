package adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import entity.CategoryMeasurementRelation;
import entity.EstimatedSize;
import entity.MeasurementType;
import tailor.mafatlal.com.tailor.R;
import tailor.mafatlal.com.tailor.acMeasurementFour;
import utility.DataBase;
import utility.Logger;

/**
 * Created by SAI on 9/5/2017.
 */

public class SizeAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView txtSize, txtShowHideDetail;
        RadioButton rdSelect;
        LinearLayout lyMeasurementDetail;
    }

    Context mContext;
    ArrayList<EstimatedSize> arrEstimatedSize;
    RadioButton mSelectedRB;

    public SizeAdapter(Context mContext, ArrayList<EstimatedSize> arrEstimatedSize) {
        this.mContext = mContext;
        this.arrEstimatedSize = arrEstimatedSize;
    }

    @Override
    public int getCount() {
        return arrEstimatedSize.size();
    }

    @Override
    public Object getItem(int position) {
        return arrEstimatedSize.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.size_list_item, null);
            holder = new ViewHolder();
            holder.txtSize = (TextView) convertView.findViewById(R.id.txtSize);
            holder.txtShowHideDetail = (TextView) convertView.findViewById(R.id.txtShowHideDetail);
            holder.rdSelect = (RadioButton) convertView.findViewById(R.id.rdSelect);
            holder.lyMeasurementDetail = (LinearLayout) convertView.findViewById(R.id.lyMeasurementDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EstimatedSize objSize = arrEstimatedSize.get(position);
        holder.txtSize.setText(mContext.getString(R.string.strSize) + ": " + objSize.getSize());
        holder.txtShowHideDetail.setPaintFlags(holder.txtShowHideDetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (acMeasurementFour.selectedSizePosition != position) {
            holder.rdSelect.setChecked(false);
            objSize.setSelected(false);
        } else {
            objSize.setSelected(true);
            holder.rdSelect.setChecked(true);
            //if (mSelectedRB != null && holder.rdSelect != mSelectedRB) {
            mSelectedRB = holder.rdSelect;
            //}
        }
        holder.rdSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != acMeasurementFour.selectedSizePosition && mSelectedRB != null) {
                    mSelectedRB.setChecked(false);
                    objSize.setSelected(false);
                }
                acMeasurementFour.selectedSizePosition = position;
                mSelectedRB = (RadioButton) v;
            }
        });
        if (!objSize.isSizeDetailLoaded())
            addDetailToLinearLayout(holder.lyMeasurementDetail, objSize);
        setTextForShowHideDetail(holder.lyMeasurementDetail, holder.txtShowHideDetail);
        holder.txtShowHideDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinearLayoutVisiblity(holder.lyMeasurementDetail);
                setTextForShowHideDetail(holder.lyMeasurementDetail, holder.txtShowHideDetail);
            }
        });

        return convertView;
    }

    private void addDetailToLinearLayout(final LinearLayout ly, final EstimatedSize objSize) {
        new AsyncTask() {
            ArrayList<CategoryMeasurementRelation> arrCategoryMeasurementRelation;

            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(mContext);
                db.open();
                String where = "category_id=" + objSize.getCategory_id() + " and age_group_id=" + objSize.getAge_group_id() + " and size_id=" + objSize.getSize_id();
                Cursor cur = db.fetch(DataBase.category_measurement_relation, DataBase.category_measurement_relation_int, where);
                try {
                    if (cur != null && cur.getCount() > 0) {
                        arrCategoryMeasurementRelation = new ArrayList<CategoryMeasurementRelation>();
                        cur.moveToFirst();
                        do {
                            CategoryMeasurementRelation obj = new CategoryMeasurementRelation(cur.getInt(1), cur.getInt(2), cur.getInt(3), cur.getInt(4), cur.getInt(5), cur.getString(6), cur.getString(7));
                            arrCategoryMeasurementRelation.add(obj);
                        } while (cur.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                } finally {
                    cur.close();
                    db.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrCategoryMeasurementRelation != null && arrCategoryMeasurementRelation.size() > 0) {
                    for (CategoryMeasurementRelation obj : arrCategoryMeasurementRelation) {
                        LayoutInflater mInflater = (LayoutInflater) mContext
                                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        View view = mInflater.inflate(R.layout.category_measurement_detail_item, null);
                        TextView txtMeasurementType = (TextView) view.findViewById(R.id.txtMeasurementType);
                        TextView txtMeasurementValue = (TextView) view.findViewById(R.id.txtMeasurementValue);
                        MeasurementType objMeasurementType = MeasurementType.getDataFromDatabase(mContext, obj.getMeasurement_type_id());
                        if (objMeasurementType != null) {
                            txtMeasurementType.setText(objMeasurementType.getType_name() + ": ");
                            txtMeasurementValue.setText(obj.getMeasurement_value());
                            ly.addView(view);
                        }
                    }
                    objSize.setSizeDetailLoaded(true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setLinearLayoutVisiblity(LinearLayout ly) {
        if (ly.getVisibility() == View.VISIBLE) {
            ly.setVisibility(View.GONE);
        } else if (ly.getVisibility() == View.GONE) {
            ly.setVisibility(View.VISIBLE);
        }
    }

    private void setTextForShowHideDetail(LinearLayout ly, TextView txt) {
        if (ly.getVisibility() == View.VISIBLE) {
            txt.setText(mContext.getString(R.string.strHideDetail));
        } else if (ly.getVisibility() == View.GONE) {
            txt.setText(mContext.getString(R.string.strShowDetail));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
}
