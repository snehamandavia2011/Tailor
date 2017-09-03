package tailor.mafatlal.com.tailor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import entity.AgeGroup;
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
    String strSchoolName, strClass, strAgeGroup;
    int intAgeGroup;
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

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                btnEdit = (ImageButton) findViewById(R.id.btnEdit);
                txtSchoolName = (TextView) findViewById(R.id.txtSchoolName);
                txtClass = (TextView) findViewById(R.id.txtClass);
                txtAgegroup = (TextView) findViewById(R.id.txtAgegroup);
                btnAddItem = (FloatingActionButton) findViewById(R.id.btnAddItem);
                lySchoolAndClassDetailChild = (RelativeLayout) findViewById(R.id.lySchoolAndClassDetailChild);
                lylySchoolAndClassDetailParent = (RelativeLayout) findViewById(R.id.lylySchoolAndClassDetailParent);
                if (ac.getIntent().getExtras() != null) {
                    strSchoolName = ac.getIntent().getStringExtra(acMeasurementOne.SCHOOL_NAME);
                    strClass = ac.getIntent().getStringExtra(acMeasurementOne.CLASS);
                    intAgeGroup = ac.getIntent().getIntExtra(acMeasurementOne.AGE_GROUP, 0);
                }
                btnEdit.setOnClickListener(performClick);
                btnAddItem.setOnClickListener(performClick);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                AgeGroup objAgeGroup = AgeGroup.getDataFromDatabase(mContext, intAgeGroup);
                if (objAgeGroup != null) {
                    strAgeGroup = objAgeGroup.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                txtClass.setText(mContext.getString(R.string.strClass) + ": " + strClass);
                txtSchoolName.setText(strSchoolName);
                if (strAgeGroup != null) {
                    txtAgegroup.setText(mContext.getString(R.string.strAgeGroup) + ": " + strAgeGroup);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener performClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddItem:
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

}
