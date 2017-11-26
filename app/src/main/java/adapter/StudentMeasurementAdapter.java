package adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.thomsonreuters.rippledecoratorview.RippleDecoratorView;

import java.util.ArrayList;

import entity.StudentMeasurement;
import tailor.mafatlal.com.tailor.R;
import tailor.mafatlal.com.tailor.acMeasurementFour;
import utility.ConstantVal;
import utility.Helper;
import utility.ServerResponse;

/**
 * Created by SAI on 9/6/2017.
 */

public class StudentMeasurementAdapter extends BaseAdapter {
    private class ViewHolder {
        RippleDecoratorView rplTryAgain;
        Button btnTryAgain;
        TextView txtStatus, txtStudentName, txtRollNumber, txtSize;
    }

    ArrayList<StudentMeasurement> arrStudentMeasurement;
    Context mContext;

    public StudentMeasurementAdapter(ArrayList<StudentMeasurement> arrStudentMeasurement, Context mContext) {
        this.arrStudentMeasurement = arrStudentMeasurement;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return arrStudentMeasurement.size();
    }

    @Override
    public Object getItem(int position) {
        return arrStudentMeasurement.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.student_measurement_list_item, null);
            holder = new ViewHolder();
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtStudentName = (TextView) convertView.findViewById(R.id.txtStudentName);
            holder.txtRollNumber = (TextView) convertView.findViewById(R.id.txtRollNumber);
            holder.txtSize = (TextView) convertView.findViewById(R.id.txtSize);
            holder.btnTryAgain = (Button) convertView.findViewById(R.id.btnTryAgain);
            holder.rplTryAgain = (RippleDecoratorView) convertView.findViewById(R.id.rplTryAgain);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StudentMeasurement obj = arrStudentMeasurement.get(position);
        holder.txtStudentName.setText(obj.getStudFirstName() + " " + obj.getStudLastName());
        holder.txtRollNumber.setText(mContext.getString(R.string.strRollNumber) + ": " + obj.getStudRollNumber());
        holder.txtSize.setText(mContext.getString(R.string.strSize) + ": " + obj.getSize());
        if (obj.getIs_successfully_submitted().equals("Y")) {
            holder.rplTryAgain.setVisibility(View.GONE);
            holder.btnTryAgain.setOnClickListener(null);
            holder.txtStatus.setText(mContext.getString(R.string.strSent));
        } else {
            holder.txtStatus.setText(mContext.getString(R.string.strFailed));
            holder.rplTryAgain.setVisibility(View.VISIBLE);
            holder.btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask() {
                        ServerResponse sr;

                        @Override
                        protected Object doInBackground(Object[] params) {
                            sr = acMeasurementFour.saveDataOnServer(mContext, obj.get_ID());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            if (sr != null && sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                Helper.displaySnackbar((AppCompatActivity) mContext, mContext.getString(R.string.msgMeasurementDataSave), ConstantVal.ToastBGColor.SUCCESS).setCallback(new TSnackbar.Callback() {
                                    @Override
                                    public void onDismissed(TSnackbar snackbar, int event) {
                                        super.onDismissed(snackbar, event);
                                        obj.setIs_successfully_submitted("Y");
                                        notifyDataSetChanged();
                                    }
                                });
                            } else if (!sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
                                Helper.displaySnackbar((AppCompatActivity) mContext, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO);
                            }
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            });
        }
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
