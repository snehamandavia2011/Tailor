package utility;

import tailor.mafatlal.com.tailor.R;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tailor.mafatlal.com.tailor.acMeasurementOne;
import tailor.mafatlal.com.tailor.acProfile;


public class TabManager {
    public static final int MEASUREMENT = 0;
    public static final int PROFILE = 1;

    LinearLayout lyMeasurement, lyProfile;
    ImageView imgMeasurement, imgProfile;
    TextView textMeasurement, textProfile;
    int currentTab;
    AppCompatActivity ac;

    public TabManager(int currentTab, AppCompatActivity ac) {
        this.currentTab = currentTab;
        this.ac = ac;

    }

    public void setCurrentSelection() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                lyMeasurement = (LinearLayout) ac.findViewById(R.id.lyMeasurement);
                lyProfile = (LinearLayout) ac.findViewById(R.id.lyProfile);
                imgMeasurement = (ImageView) ac.findViewById(R.id.imgMeasurement);
                imgProfile = (ImageView) ac.findViewById(R.id.imgProfile);
                textMeasurement = (TextView) ac.findViewById(R.id.textMeasurement);
                textProfile = (TextView) ac.findViewById(R.id.textProfile);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (currentTab == MEASUREMENT) {
                    setSelection(ac, lyMeasurement, imgMeasurement, R.drawable.ic_measurement_red, textMeasurement);
                    setDeSelection(ac, lyProfile, imgProfile, R.drawable.ic_profile_white, textProfile);
                } else if (currentTab == PROFILE) {
                    setDeSelection(ac, lyMeasurement, imgMeasurement, R.drawable.ic_measurement_white, textMeasurement);
                    setSelection(ac, lyProfile, imgProfile, R.drawable.ic_profile_red, textProfile);
                }
                handleMeasurementClick(ac, lyMeasurement);
                handleProfileClick(ac, lyProfile);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void handleMeasurementClick(final AppCompatActivity ac, LinearLayout lyMeasurement) {
        if (ac.getClass() == acMeasurementOne.class) {
            lyMeasurement.setOnClickListener(null);
        } else {
            lyMeasurement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ac, acMeasurementOne.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
        }
    }


    private void handleProfileClick(final AppCompatActivity ac, LinearLayout lyProfile) {
        if (ac.getClass() == acProfile.class) {
            lyProfile.setOnClickListener(null);
        } else {
            lyProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ac, acProfile.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
        }
    }

    private void setSelection(AppCompatActivity ac, LinearLayout ly, ImageView img, int imgResource, TextView txt) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.white)));
        txt.setTextColor(ContextCompat.getColor(ac, R.color.red));
        img.setImageResource(imgResource);
    }

    private void setDeSelection(AppCompatActivity ac, LinearLayout ly, ImageView img, int imgResource, TextView txt) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.red)));
        txt.setTextColor(ContextCompat.getColor(ac, R.color.white));
        img.setImageResource(imgResource);
    }
}
