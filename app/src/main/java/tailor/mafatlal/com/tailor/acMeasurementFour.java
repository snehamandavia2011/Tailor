package tailor.mafatlal.com.tailor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import entity.AgeGroup;
import entity.Category;
import entity.ClassMaster;
import entity.SchoolMaster;
import utility.Helper;
import utility.TabManager;

public class acMeasurementFour extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;

    SchoolMaster objSelectedSchoolMaster;
    ClassMaster objSelectedClassMaster;
    AgeGroup objSelectedAgeGroup;
    Category objSelectedCategory;
    RelativeLayout lySizeSubmitingScreen, lyMainScreen;
    String studFirstName, studLastName, studRollNumber;
    Button btnSubmit;
    ListView lvlSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_measurement_four);
        objHelper.setActionBar(this, getString(R.string.strSizeResult), true);
        objTabManager = new TabManager(TabManager.MEASUREMENT, ac);
        objTabManager.setCurrentSelection();
        setData();
    }

    private void setData() {
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                btnSubmit = (Button) findViewById(R.id.btnSubmit);
                lvlSize = (ListView) findViewById(R.id.lvlSize);
                lySizeSubmitingScreen = (RelativeLayout) findViewById(R.id.lySizeSubmitingScreen);
                lyMainScreen = (RelativeLayout) findViewById(R.id.lyMainScreen);
                if (ac.getIntent().getExtras() != null) {
                    objSelectedSchoolMaster = (SchoolMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.SCHOOL_MASTER);
                    objSelectedClassMaster = (ClassMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.CLASS_MASTER);
                    objSelectedAgeGroup = (AgeGroup) ac.getIntent().getSerializableExtra(acMeasurementOne.AGE_GROUP);
                    objSelectedCategory = (Category) ac.getIntent().getSerializableExtra(acMeasurementThree.CATEGORY_MASTER);
                    studFirstName = ac.getIntent().getStringExtra(acMeasurementThree.STUDENT_FIRST_NAME);
                    studLastName = ac.getIntent().getStringExtra(acMeasurementThree.STUDENT_LAST_NAME);
                    studRollNumber = ac.getIntent().getStringExtra(acMeasurementThree.STUDENT_ROLL_NUMBER);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveDataToLocalDatabase();
                    }
                });
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void saveDataToLocalDatabase() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                saveDataOnServer();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void saveDataOnServer() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

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

}
