package tailor.mafatlal.com.tailor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.androidadvance.topsnackbar.TSnackbar;

import org.json.JSONObject;

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
import utility.ConstantVal;
import utility.DataBase;
import utility.DateTimeUtils;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.TabManager;
import utility.URLMapping;

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
            long id;
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lySizeSubmitingScreen.setVisibility(View.VISIBLE);
                lyMainScreen.setVisibility(View.GONE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(mContext);
                db.open();
                try {
                    id = db.insert(DataBase.student_measurement, DataBase.student_measurement_int, new String[]{"0",
                            studFirstName, studLastName, studRollNumber, String.valueOf(objSelectedSchoolMaster.getId()),
                            String.valueOf(objSelectedClassMaster.getId()), String.valueOf(objSelectedAgeGroup.getId()),
                            String.valueOf(arrEstimatedSize.get(selectedSizePosition).getSize_id()),
                            String.valueOf(objSelectedCategory.getId()), "N", Helper.getStringPreference(mContext,
                            User.Fields.ID, ""), String.valueOf(new Date().getTime())});
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                } finally {
                    db.close();
                }
                sr = saveDataOnServer(mContext, id);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                lySizeSubmitingScreen.setVisibility(View.GONE);
                lyMainScreen.setVisibility(View.VISIBLE);
                if (sr != null && sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    Helper.displaySnackbar(ac, mContext.getString(R.string.msgMeasurementDataSave), ConstantVal.ToastBGColor.SUCCESS).setCallback(new TSnackbar.Callback() {
                        @Override
                        public void onDismissed(TSnackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            setResult(ConstantVal.RESPONSE_MEASUREMENT_FOUR);
                            finish();
                        }
                    });
                } else {
                    Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static ServerResponse saveDataOnServer(Context mContext, long localDbPK) {
        ServerResponse sr = null;
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetch(DataBase.student_measurement, "_ID=" + localDbPK);
        String[] data = null;
        try {
            if (cur != null && cur.getCount() > 0) {
                String first_name = cur.getString(2);
                String last_name = cur.getString(3);
                String roll_number = cur.getString(4);
                String school_id = cur.getString(5);
                String class_id = cur.getString(6);
                String age_group_id = cur.getString(7);
                String size_id = cur.getString(8);
                String category_id = cur.getString(9);
                String user_id = cur.getString(11);
                String date = DateTimeUtils.getDate(new Date(cur.getLong(12)));
                String time = DateTimeUtils.getTime(new Date(cur.getLong(12)));
                String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                data = new String[]{String.valueOf(localDbPK), first_name, last_name, roll_number, school_id, class_id, age_group_id, size_id, category_id, user_id, date, time, token};
            }
            if (data != null) {
                final HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.saveStudentMeasurement();
                sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                        um.getParamNames(), data);
                if (sr != null && sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    JSONObject objJSON = new JSONObject(sr.getResponseString());
                    String serverPK = objJSON.getString("serverPK");
                    ContentValues cv = new ContentValues();
                    cv.put("serverPK", serverPK);
                    cv.put("is_successfully_submitted", "Y");
                    db.update(DataBase.student_measurement, DataBase.student_measurement_int, "_ID=" + localDbPK, cv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            cur.close();
            db.close();
        }
        return sr;
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
