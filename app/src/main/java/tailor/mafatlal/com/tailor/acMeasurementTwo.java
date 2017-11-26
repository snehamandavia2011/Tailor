package tailor.mafatlal.com.tailor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.StudentMeasurementAdapter;
import entity.AgeGroup;
import entity.ClassMaster;
import entity.SchoolMaster;
import entity.StudentMeasurement;
import utility.Helper;
import utility.TabManager;

public class acMeasurementTwo extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;
    FloatingActionButton btnAddItem;
    RelativeLayout lySchoolAndClassDetailChild, lylySchoolAndClassDetailParent;
    TextView txtSchoolName, txtClass, txtAgegroup;
    SchoolMaster objSelectedSchoolMaster;
    ClassMaster objSelectedClassMaster;
    AgeGroup objSelectedAgeGroup;
    ListView lvl;
    ImageButton btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_measurement_two);
        objHelper.setActionBar(this, getString(R.string.strAddStudent), true);
        objTabManager = new TabManager(TabManager.MEASUREMENT, ac);
        objTabManager.setCurrentSelection();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeListView();
    }

    private void initializeListView() {
        new AsyncTask() {
            ArrayList<StudentMeasurement> arrStudentMeasurement = null;

            @Override
            protected Object doInBackground(Object[] params) {
                arrStudentMeasurement = StudentMeasurement.getDataFromDatabase(mContext);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrStudentMeasurement != null && arrStudentMeasurement.size() > 0)
                    lvl.setAdapter(new StudentMeasurementAdapter(arrStudentMeasurement, mContext));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setData() {
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lvl = (ListView) findViewById(R.id.lvl);
                btnEdit = (ImageButton) findViewById(R.id.btnEdit);
                txtSchoolName = (TextView) findViewById(R.id.txtSchoolName);
                txtClass = (TextView) findViewById(R.id.txtClass);
                txtAgegroup = (TextView) findViewById(R.id.txtAgegroup);
                btnAddItem = (FloatingActionButton) findViewById(R.id.btnAddItem);
                lySchoolAndClassDetailChild = (RelativeLayout) findViewById(R.id.lySchoolAndClassDetailChild);
                lylySchoolAndClassDetailParent = (RelativeLayout) findViewById(R.id.lylySchoolAndClassDetailParent);
                if (ac.getIntent().getExtras() != null) {
                    objSelectedSchoolMaster = (SchoolMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.SCHOOL_MASTER);
                    objSelectedClassMaster = (ClassMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.CLASS_MASTER);
                    objSelectedAgeGroup = (AgeGroup) ac.getIntent().getSerializableExtra(acMeasurementOne.AGE_GROUP);
                }
                btnEdit.setOnClickListener(performClick);
                btnAddItem.setOnClickListener(performClick);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                txtClass.setText(mContext.getString(R.string.strClass) + ": " + objSelectedClassMaster.getClass_name());
                txtSchoolName.setText(objSelectedSchoolMaster.getSchool_name());
                txtAgegroup.setText(mContext.getString(R.string.strAgeGroup) + ": " + objSelectedAgeGroup.toString());
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener performClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddItem:
                    Intent i = new Intent(mContext, acMeasurementThree.class);
                    i.putExtra(acMeasurementOne.SCHOOL_MASTER, objSelectedSchoolMaster);
                    i.putExtra(acMeasurementOne.CLASS_MASTER, objSelectedClassMaster);
                    i.putExtra(acMeasurementOne.AGE_GROUP, objSelectedAgeGroup);
                    startActivity(i);
                    break;
                case R.id.btnEdit:
                    finish();
                    break;
            }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
