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
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.Helper;
import utility.TabManager;

public class acMeasurementOne extends AppCompatActivity {

    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;
    MaterialEditText edSchoolName, edClass;
    Spinner spnAgeGroup;
    Button btnNext;
    public static final String SCHOOL_NAME = "school_name";
    public static final String CLASS = "class";
    public static final String AGE_GROUP = "age_group_id";

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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                edSchoolName = (MaterialEditText) findViewById(R.id.edSchoolName);
                edClass = (MaterialEditText) findViewById(R.id.edClass);
                spnAgeGroup = (Spinner) findViewById(R.id.spnAgeGroup);
                btnNext = (Button) findViewById(R.id.btnNext);
                btnNext.setOnClickListener(clickNext);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrAgeGroup = AgeGroup.getDataFromDatabase(mContext);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrAgeGroup != null && arrAgeGroup.size() > 0) {
                    ArrayAdapter<AgeGroup> adpAgeGroup = new ArrayAdapter<AgeGroup>(mContext, R.layout.spinner_item_no_padding, arrAgeGroup);
                    adpAgeGroup.setDropDownViewResource(R.layout.spinner_item);
                    spnAgeGroup.setAdapter(adpAgeGroup);
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
                    if (Helper.isFieldBlank(edSchoolName.getText().toString())) {
                        isDataEnteredProper = false;
                        edSchoolName.setError(getString(R.string.msgEnterSchoolName));
                        Helper.requestFocus(ac, edSchoolName);
                    } else if (Helper.isFieldBlank(edClass.getText().toString())) {
                        isDataEnteredProper = false;
                        edClass.setError(getString(R.string.msgClass));
                        Helper.requestFocus(ac, edClass);
                    }
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
                        i.putExtra(acMeasurementOne.SCHOOL_NAME, edSchoolName.getText().toString());
                        i.putExtra(acMeasurementOne.CLASS, edClass.getText().toString());
                        i.putExtra(acMeasurementOne.AGE_GROUP, ((AgeGroup) spnAgeGroup.getSelectedItem()).getId());
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

}
