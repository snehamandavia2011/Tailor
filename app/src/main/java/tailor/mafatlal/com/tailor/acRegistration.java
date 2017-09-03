package tailor.mafatlal.com.tailor;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import java.util.Calendar;
import java.util.Date;

import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DateTimeUtils;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

public class acRegistration extends AppCompatActivity implements View.OnClickListener {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    MaterialEditText edFirstName, edLastName, edEmailId, edMobileNumber, edBirthDate;
    RadioButton rdMale, rdFemale;
    Button btnCancel, btnSave;
    RelativeLayout rlDotProgress, rlMainContent;
    DotProgressBar dot_progress_bar;
    Button btnPickDate;
    Calendar calBirthDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_registration);
        objHelper.setActionBar(this, getString(R.string.strRegistration), true);
        intializeControl();
    }

    private void intializeControl() {
        btnPickDate = (Button) findViewById(R.id.btnPickDate);
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        rlDotProgress = (RelativeLayout) findViewById(R.id.rlDotProgress);
        rlMainContent = (RelativeLayout) findViewById(R.id.rlMainContent);
        edFirstName = (MaterialEditText) findViewById(R.id.edFirstName);
        edLastName = (MaterialEditText) findViewById(R.id.edLastName);
        edEmailId = (MaterialEditText) findViewById(R.id.edEmailId);
        edMobileNumber = (MaterialEditText) findViewById(R.id.edMobileNumber);
        edBirthDate = (MaterialEditText) findViewById(R.id.edBirthDate);
        rdMale = (RadioButton) findViewById(R.id.rdMale);
        rdFemale = (RadioButton) findViewById(R.id.rdFemale);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPickDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                registerUser();
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnPickDate:
                openDatePicker();
                break;
        }
    }

    private void openDatePicker() {
        DatePickerDialog dp = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calBirthDate.set(Calendar.YEAR, year);
                calBirthDate.set(Calendar.MONTH, month);
                calBirthDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edBirthDate.setText(DateTimeUtils.getDate(calBirthDate.getTime()));
            }
        }, calBirthDate.get(Calendar.YEAR), calBirthDate.get(Calendar.MONTH), calBirthDate.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    private void registerUser() {
        new AsyncTask() {
            boolean isDataEnteredProper = true;
            String[] data;
            ServerResponse sr = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (Helper.isFieldBlank(edFirstName.getText().toString())) {
                    isDataEnteredProper = false;
                    edFirstName.setError(getString(R.string.msgEnterFirstName));
                    Helper.requestFocus(ac, edFirstName);
                } else if (Helper.isFieldBlank(edLastName.getText().toString())) {
                    isDataEnteredProper = false;
                    edLastName.setError(getString(R.string.msgEnterLastName));
                    Helper.requestFocus(ac, edLastName);
                } else if (Helper.isFieldBlank(edEmailId.getText().toString())) {
                    isDataEnteredProper = false;
                    edEmailId.setError(getString(R.string.strEnterEmailId));
                    Helper.requestFocus(ac, edEmailId);
                } else if (!Helper.isValidEmailId(edEmailId.getText().toString())) {
                    isDataEnteredProper = false;
                    edEmailId.setError(getString(R.string.strEnterValidEmailId));
                    Helper.requestFocus(ac, edEmailId);
                } else if (Helper.isFieldBlank(edMobileNumber.getText().toString())) {
                    isDataEnteredProper = false;
                    edMobileNumber.setError(getString(R.string.msgEnterMobileNumber));
                    Helper.requestFocus(ac, edMobileNumber);
                } else if (Helper.isFieldBlank(edBirthDate.getText().toString())) {
                    isDataEnteredProper = false;
                    edBirthDate.setError(getString(R.string.msgSelectBirthDate));
                    Helper.requestFocus(ac, edBirthDate);
                }
                if (isDataEnteredProper) {
                    rlDotProgress.setVisibility(View.VISIBLE);
                    rlMainContent.setVisibility(View.GONE);
                    String gender = "";
                    if (rdMale.isChecked())
                        gender = ConstantVal.Gender.MALE;
                    else if (rdFemale.isChecked())
                        gender = ConstantVal.Gender.FEMALE;

                    data = new String[]{edFirstName.getText().toString(), edLastName.getText().toString(), edEmailId.getText().toString(),
                            edMobileNumber.getText().toString(), gender, DateTimeUtils.getDate(calBirthDate.getTime()), "", DateTimeUtils.getDate(new Date()), DateTimeUtils.getTime(new Date())};
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isDataEnteredProper) {
                    URLMapping umRegister = ConstantVal.registerEmployee();
                    HttpEngine objHttpEngine = new HttpEngine();
                    sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                    Logger.debug("response code:" + sr.getResponseCode() + " " + sr.getResponseString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.clearAnimation();
                rlDotProgress.setVisibility(View.GONE);
                rlMainContent.setVisibility(View.VISIBLE);
                if (isDataEnteredProper) {
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        Helper.displaySnackbar((AppCompatActivity) mContext, mContext.getString(R.string.msgPasswordSend), ConstantVal.ToastBGColor.SUCCESS).setCallback(new TSnackbar.Callback() {
                            @Override
                            public void onDismissed(TSnackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                ((AppCompatActivity) mContext).finish();
                            }
                        });
                    } else {
                        Helper.displaySnackbar((AppCompatActivity) mContext, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO).setCallback(new TSnackbar.Callback() {
                            @Override
                            public void onDismissed(TSnackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                //((AppCompatActivity) mContext).finish();
                            }
                        });
                    }
                }
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
