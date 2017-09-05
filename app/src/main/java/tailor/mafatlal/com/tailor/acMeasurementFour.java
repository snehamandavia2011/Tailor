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

import java.util.ArrayList;
import java.util.Date;

import adapter.SizeAdapter;
import entity.AgeGroup;
import entity.Category;
import entity.CategoryMeasurementRelation;
import entity.ClassMaster;
import entity.EstimatedSize;
import entity.SchoolMaster;
import entity.User;
import utility.DataBase;
import utility.Helper;
import utility.Logger;
import utility.TabManager;

public class acMeasurementFour extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;
    ArrayList<EstimatedSize> arrEstimatedSize;
    SchoolMaster objSelectedSchoolMaster;
    ClassMaster objSelectedClassMaster;
    AgeGroup objSelectedAgeGroup;
    Category objSelectedCategory;
    RelativeLayout lySizeSubmitingScreen, lyMainScreen, lyNoSizeFound;
    String studFirstName, studLastName, studRollNumber;
    Button btnSubmit;
    ListView lvlSize;
    public static int selectedSizePosition = 0;

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
                lyNoSizeFound = (RelativeLayout) findViewById(R.id.lyNoSizeFound);
                if (ac.getIntent().getExtras() != null) {
                    objSelectedSchoolMaster = (SchoolMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.SCHOOL_MASTER);
                    objSelectedClassMaster = (ClassMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.CLASS_MASTER);
                    objSelectedAgeGroup = (AgeGroup) ac.getIntent().getSerializableExtra(acMeasurementOne.AGE_GROUP);
                    objSelectedCategory = (Category) ac.getIntent().getSerializableExtra(acMeasurementThree.CATEGORY_MASTER);
                    studFirstName = ac.getIntent().getStringExtra(acMeasurementThree.STUDENT_FIRST_NAME);
                    studLastName = ac.getIntent().getStringExtra(acMeasurementThree.STUDENT_LAST_NAME);
                    studRollNumber = ac.getIntent().getStringExtra(acMeasurementThree.STUDENT_ROLL_NUMBER);
                    arrEstimatedSize = (ArrayList<EstimatedSize>) ac.getIntent().getSerializableExtra(acMeasurementThree.ESTIMATED_SIZE_LIST);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrEstimatedSize != null && arrEstimatedSize.size() > 0) {
                    lyNoSizeFound.setVisibility(View.GONE);
                    lyMainScreen.setVisibility(View.VISIBLE);
                    lvlSize.setAdapter(new SizeAdapter(mContext, arrEstimatedSize));
                } else {
                    lyNoSizeFound.setVisibility(View.VISIBLE);
                    lyMainScreen.setVisibility(View.GONE);
                }
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
                DataBase db = new DataBase(mContext);
                db.open();
                try {
                    db.insert(DataBase.student_measurement, DataBase.student_measurement_int, new String[]{"0",
                            studFirstName, studLastName, studRollNumber, String.valueOf(objSelectedSchoolMaster.getId()),
                            String.valueOf(objSelectedClassMaster.getId()), String.valueOf(objSelectedAgeGroup.getId()),
                            String.valueOf(arrEstimatedSize.get(selectedSizePosition).getSize_id()),
                            String.valueOf(objSelectedCategory.getId()), "N", Helper.getStringPreference(mContext,
                            User.Fields.EMPLOYEE_ID, ""), String.valueOf(new Date().getTime())});
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                } finally {
                    db.close();
                }
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
