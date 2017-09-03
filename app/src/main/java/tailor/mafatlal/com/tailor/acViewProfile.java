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
import android.widget.TextView;

import java.util.Date;

import entity.User;
import utility.ConstantVal;
import utility.DateTimeUtils;
import utility.Helper;
import utility.TabManager;

public class acViewProfile extends AppCompatActivity {
    TextView txtUserType, txtName, txtEmailId, txtMobileNumber, txtGender, txtBirthDate;
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_view_profile);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_view_profile);
        objHelper.setActionBar(this, getString(R.string.strProfile), true);
        objTabManager = new TabManager(TabManager.PROFILE, ac);
        objTabManager.setCurrentSelection();
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                txtUserType = (TextView) findViewById(R.id.txtUserType);
                txtMobileNumber = (TextView) findViewById(R.id.txtMobileNumber);
                txtEmailId = (TextView) findViewById(R.id.txtEmailId);
                txtName = (TextView) findViewById(R.id.txtName);
                txtGender = (TextView) findViewById(R.id.txtGender);
                txtBirthDate = (TextView) findViewById(R.id.txtBirthDate);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                txtName.setText(Helper.getStringPreference(mContext, User.Fields.FIRST_NAME, "") + " " + Helper.getStringPreference(mContext, User.Fields.LAST_NAME, ""));
                txtEmailId.setText(Helper.getStringPreference(mContext, User.Fields.EMAIL, ""));
                txtMobileNumber.setText(Helper.getStringPreference(mContext, User.Fields.CONTACT_NO, ""));
                txtUserType.setText(Helper.getStringPreference(mContext, User.Fields.USER_TYPE_NAME, ""));
                txtGender.setText(Helper.getStringPreference(mContext, User.Fields.GENDER, ""));
                String strBirthDate = Helper.getStringPreference(mContext, User.Fields.BIRTH_DATE, "");
                Date dtBirthDate = DateTimeUtils.convertStringToDate(strBirthDate, DateTimeUtils.DATE_FORMAT);
                txtBirthDate.setText(DateTimeUtils.convertDateToString(dtBirthDate, DateTimeUtils.DATE_FORMAT1));
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
