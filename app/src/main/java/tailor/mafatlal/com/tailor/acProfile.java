package tailor.mafatlal.com.tailor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import java.util.Calendar;
import java.util.Date;

import entity.User;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DataBase;
import utility.DateTimeUtils;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.TabManager;
import utility.URLMapping;

public class acProfile extends AppCompatActivity implements View.OnClickListener {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;
    TextView txtCustomerName;
    LinearLayout lyPersonalInformation;
    Button btnEditPersonalInfor, btnChangePassword, btnLogout;
    RelativeLayout lyMainScreen, lyLogoutScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_profile);
        objHelper.setActionBar(this, getString(R.string.strProfile), false);
        objTabManager = new TabManager(TabManager.PROFILE, ac);
        objTabManager.setCurrentSelection();
        lyMainScreen = (RelativeLayout) findViewById(R.id.lyMainScreen);
        lyLogoutScreen = (RelativeLayout) findViewById(R.id.lyLogoutScreen);
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtCustomerName.setText(Helper.getStringPreference(mContext, User.Fields.FIRST_NAME, "") + " " + Helper.getStringPreference(mContext, User.Fields.LAST_NAME, ""));
        lyPersonalInformation = (LinearLayout) findViewById(R.id.lyPersonalInformation);
        btnEditPersonalInfor = (Button) findViewById(R.id.btnEditPersonalInfor);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        lyPersonalInformation.setOnClickListener(this);
        btnEditPersonalInfor.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyPersonalInformation:
                Intent i = new Intent(getApplicationContext(), acViewProfile.class);
                startActivity(i);
                break;
            case R.id.btnEditPersonalInfor:
                editPersonalInfo();
                break;
            case R.id.btnChangePassword:
                changePassword();
                break;
            case R.id.btnLogout:
                logoutUser();
                break;
        }
    }

    private void editPersonalInfo() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view = infalInflater.inflate(R.layout.dlg_edit_personal_info, null, true);
        final TextView txtErrorMessage = (TextView) view.findViewById(R.id.txtErrorMessage);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        final MaterialEditText edFirstName = (MaterialEditText) view.findViewById(R.id.edFirstName);
        final MaterialEditText edLastName = (MaterialEditText) view.findViewById(R.id.edLastName);
        final MaterialEditText edMobileNumber = (MaterialEditText) view.findViewById(R.id.edMobileNumber);
        final MaterialEditText edBirthDate = (MaterialEditText) view.findViewById(R.id.edBirthDate);
        final RadioButton rdMale = (RadioButton) view.findViewById(R.id.rdMale);
        final RadioButton rdFemale = (RadioButton) view.findViewById(R.id.rdFemale);
        final DotProgressBar dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        final Button btnPickDate = (Button) view.findViewById(R.id.btnPickDate);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        final Calendar calBirthDate = Calendar.getInstance();

        edFirstName.setText(Helper.getStringPreference(mContext, User.Fields.FIRST_NAME, ""));
        edLastName.setText(Helper.getStringPreference(mContext, User.Fields.LAST_NAME, ""));
        edMobileNumber.setText(Helper.getStringPreference(mContext, User.Fields.CONTACT_NO, ""));
        String strBirthDate = Helper.getStringPreference(mContext, User.Fields.BIRTH_DATE, "");
        Date dtBirthDate = DateTimeUtils.convertStringToDate(strBirthDate, DateTimeUtils.DATE_FORMAT);
        calBirthDate.setTime(dtBirthDate);
        edBirthDate.setText(DateTimeUtils.convertDateToString(dtBirthDate, DateTimeUtils.DATE_FORMAT1));
        String selGender = Helper.getStringPreference(mContext, User.Fields.GENDER, "");
        if (selGender.equals(ConstantVal.Gender.MALE))
            rdMale.setChecked(true);
        else if (selGender.equals(ConstantVal.Gender.FEMALE))
            rdFemale.setChecked(true);

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calBirthDate.set(Calendar.YEAR, year);
                        calBirthDate.set(Calendar.MONTH, month);
                        calBirthDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        edBirthDate.setText(DateTimeUtils.convertDateToString(calBirthDate.getTime(), DateTimeUtils.DATE_FORMAT1));
                    }
                }, calBirthDate.get(Calendar.YEAR), calBirthDate.get(Calendar.MONTH), calBirthDate.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask() {
                    User objUser;
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
                        } else if (Helper.isFieldBlank(edMobileNumber.getText().toString())) {
                            isDataEnteredProper = false;
                            edMobileNumber.setError(getString(R.string.msgEnterMobileNumber));
                            Helper.requestFocus(ac, edMobileNumber);
                        }
                        if (isDataEnteredProper) {
                            dot_progress_bar.setVisibility(View.VISIBLE);
                            String gender = "";
                            if (rdMale.isChecked())
                                gender = ConstantVal.Gender.MALE;
                            else if (rdFemale.isChecked())
                                gender = ConstantVal.Gender.FEMALE;
                            String emp_id = Helper.getStringPreference(mContext, User.Fields.EMPLOYEE_ID, "");
                            String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                            data = new String[]{emp_id, edFirstName.getText().toString(), edLastName.getText().toString(),
                                    edMobileNumber.getText().toString(), gender, DateTimeUtils.getDate(calBirthDate.getTime()), token};
                            objUser = new User(edFirstName.getText().toString(), edLastName.getText().toString(),
                                    edMobileNumber.getText().toString(), gender, DateTimeUtils.getDate(calBirthDate.getTime()));
                        }
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        if (isDataEnteredProper) {
                            URLMapping umRegister = ConstantVal.updateEmployeePersonalInfo();
                            HttpEngine objHttpEngine = new HttpEngine();
                            sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dot_progress_bar.clearAnimation();
                        dot_progress_bar.setVisibility(View.GONE);
                        if (isDataEnteredProper) {
                            if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                dialog.dismiss();
                                objUser.savePersonalDetailToPreference(mContext);
                                txtCustomerName.setText(Helper.getStringPreference(mContext, User.Fields.FIRST_NAME, "") + " " + Helper.getStringPreference(mContext, User.Fields.LAST_NAME, ""));
                            } else {
                                txtErrorMessage.setVisibility(View.VISIBLE);
                                txtErrorMessage.setText(ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                            }
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        dialog.show();
    }

    private void changePassword() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view = infalInflater.inflate(R.layout.dlg_change_password, null, true);
        final MaterialEditText edOldPassword = (MaterialEditText) view.findViewById(R.id.edOldPassword);
        final MaterialEditText edNewPassword = (MaterialEditText) view.findViewById(R.id.edNewPassword);
        final MaterialEditText edConfirmPassword = (MaterialEditText) view.findViewById(R.id.edConfirmPassword);
        final DotProgressBar dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        final TextView txtErrorMessage = (TextView) view.findViewById(R.id.txtErrorMessage);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask() {
                    User objUser;
                    boolean isDataEnteredProper = true;
                    String[] data;
                    ServerResponse sr = null;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if (Helper.isFieldBlank(edOldPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edOldPassword.setError(getString(R.string.strEnterOldPassword));
                            Helper.requestFocus(ac, edOldPassword);
                        } else if (Helper.isFieldBlank(edNewPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edNewPassword.setError(getString(R.string.strEnterNewPassword));
                            Helper.requestFocus(ac, edNewPassword);
                        } else if (Helper.isFieldBlank(edConfirmPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edConfirmPassword.setError(getString(R.string.msgEnterConfirmPassword));
                            Helper.requestFocus(ac, edConfirmPassword);
                        } else if (!edNewPassword.getText().toString().equals(edConfirmPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edConfirmPassword.setError(getString(R.string.msgPasswordConfirmShouldSame));
                            Helper.requestFocus(ac, edConfirmPassword);
                        } else if (!Helper.getStringPreference(mContext, User.Fields.PASSWORD, "").equals(edOldPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edOldPassword.setError(getString(R.string.msgInvalidOldPassword));
                            Helper.requestFocus(ac, edOldPassword);
                        }
                        if (isDataEnteredProper) {
                            dot_progress_bar.setVisibility(View.VISIBLE);
                            String strEmail = Helper.getStringPreference(mContext, User.Fields.EMAIL, "");
                            String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                            data = new String[]{strEmail, edNewPassword.getText().toString(), token};
                            objUser = new User(strEmail, edNewPassword.getText().toString());
                        }
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        if (isDataEnteredProper) {
                            URLMapping umRegister = ConstantVal.changeEmployeePassword();
                            HttpEngine objHttpEngine = new HttpEngine();
                            sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                            objUser.savePasswordDetailToPreference(mContext);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dot_progress_bar.clearAnimation();
                        dot_progress_bar.setVisibility(View.GONE);
                        if (isDataEnteredProper) {
                            if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                dialog.dismiss();
                            } else {
                                txtErrorMessage.setVisibility(View.VISIBLE);
                                txtErrorMessage.setText(ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                            }
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        dialog.show();
    }

    private void logoutUser() {
        new AsyncTask() {
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lyMainScreen.setVisibility(View.GONE);
                lyLogoutScreen.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                String userId = Helper.getStringPreference(mContext, User.Fields.ID, "");
                URLMapping objURLMapping = ConstantVal.logoutUser();
                HttpEngine objHttpEngine = new HttpEngine();
                sr = objHttpEngine.getDataFromWebAPI(ac, objURLMapping.getUrl(), objURLMapping.getParamNames(), new String[]{userId, token});
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    logOutUser();
                } else {
                    Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO);
                    lyMainScreen.setVisibility(View.VISIBLE);
                    lyLogoutScreen.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    private void logOutUser() {
        User.clearCache(mContext);
        DataBase db = new DataBase(mContext);
        db.open();
        db.cleanWhileLogout();
        db.close();
        Intent i = new Intent(ac, acLogin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ac.startActivity(i);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

}
