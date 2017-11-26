package tailor.mafatlal.com.tailor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import entity.AgeGroup;
import entity.ClassMaster;
import entity.SchoolMaster;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.Helper;
import utility.TabManager;

public class acMeasurementOne extends AppCompatActivity {

    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;
    Spinner spnAgeGroup, spnClassName, spnSchoolName;
    Button btnNext;
    public static final String SCHOOL_MASTER = "school_master";
    public static final String CLASS_MASTER = "class_master";
    public static final String AGE_GROUP = "age_group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_measurement_one);
        objHelper.setActionBar(this, getString(R.string.strSchoolAndCLass), false);
        objTabManager = new TabManager(TabManager.MEASUREMENT, ac);
        objTabManager.setCurrentSelection();
        setData();
    }

    private void setData() {
        new AsyncTask() {
            ArrayList<AgeGroup> arrAgeGroup = null;
            ArrayList<SchoolMaster> arrSchool = null;
            ArrayList<ClassMaster> arrClass = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                spnSchoolName = (Spinner) findViewById(R.id.spnSchoolName);
                spnClassName = (Spinner) findViewById(R.id.spnClassName);
                spnAgeGroup = (Spinner) findViewById(R.id.spnAgeGroup);
                btnNext = (Button) findViewById(R.id.btnNext);
                btnNext.setOnClickListener(clickNext);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrAgeGroup = AgeGroup.getDataFromDatabase(mContext);
                arrSchool = SchoolMaster.getDataFromDatabase(mContext);
                arrClass = ClassMaster.getDataFromDatabase(mContext);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrAgeGroup != null) {
                    ArrayAdapter<AgeGroup> adpAgeGroup = new ArrayAdapter<AgeGroup>(mContext, R.layout.spinner_item_no_padding, arrAgeGroup);
                    adpAgeGroup.setDropDownViewResource(R.layout.spinner_item);
                    spnAgeGroup.setAdapter(adpAgeGroup);
                }

                if (arrSchool != null) {
                    ArrayAdapter<SchoolMaster> adp = new ArrayAdapter<SchoolMaster>(mContext, R.layout.spinner_item_no_padding, arrSchool);
                    adp.setDropDownViewResource(R.layout.spinner_item);
                    spnSchoolName.setAdapter(adp);
                }

                if (arrClass != null) {
                    ArrayAdapter<ClassMaster> adp = new ArrayAdapter<ClassMaster>(mContext, R.layout.spinner_item_no_padding, arrClass);
                    adp.setDropDownViewResource(R.layout.spinner_item);
                    spnClassName.setAdapter(adp);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener clickNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask() {
                boolean isDataEnteredProper = true;
                String[] data;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (isDataEnteredProper) {
                        Intent i = new Intent(mContext, acMeasurementTwo.class);
                        i.putExtra(acMeasurementOne.SCHOOL_MASTER, (SchoolMaster) spnSchoolName.getSelectedItem());
                        i.putExtra(acMeasurementOne.CLASS_MASTER, (ClassMaster) spnClassName.getSelectedItem());
                        i.putExtra(acMeasurementOne.AGE_GROUP, (AgeGroup) spnAgeGroup.getSelectedItem());
                        startActivity(i);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        objHelper.registerSessionTimeoutBroadcast(ac);
    }

    @Override
    protected void onStop() {
        super.onStop();
        objHelper.unRegisterSesionTimeOutBroadcast(ac);
    }
}
