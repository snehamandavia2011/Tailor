package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import entity.EstimatedSize;
import tailor.mafatlal.com.tailor.R;
import tailor.mafatlal.com.tailor.acMeasurementFour;

/**
 * Created by SAI on 9/5/2017.
 */

public class SizeAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView txtSize;
        RadioButton rdSelect;
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
            holder.rdSelect = (RadioButton) convertView.findViewById(R.id.rdSelect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EstimatedSize objSize = arrEstimatedSize.get(position);
        holder.txtSize.setText(mContext.getString(R.string.strSize) + ": " + objSize.getSize());

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
        return convertView;
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
