package tailor.mafatlal.com.tailor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import entity.AgeGroup;
import entity.Category;
import entity.CategoryMeasurementRelation;
import entity.ClassMaster;
import entity.EstimatedSize;
import entity.MeasurementData;
import entity.MeasurementParameter;
import entity.SchoolMaster;
import me.zhanghai.android.materialedittext.MaterialEditText;
import me.zhanghai.android.materialedittext.MaterialTextInputLayout;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.InputFilterMinMax;
import utility.Logger;
import utility.TabManager;

public class acMeasurementThree extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;
    int intAgeGroup;
    SchoolMaster objSelectedSchoolMaster;
    ClassMaster objSelectedClassMaster;
    AgeGroup objSelectedAgeGroup;
    Button btnCalculateSize;
    LinearLayout lyMeasurementParameter;
    Spinner spnCategory;
    int selectedCategoryId;
    MaterialEditText edRollNumber, edStudentFirstName, edStudentLastName;
    RelativeLayout lySizeCalculationScreen, lyMainScreen;
    public static final String STUDENT_FIRST_NAME = "stud_first_name";
    public static final String STUDENT_LAST_NAME = "stud_last_name";
    public static final String STUDENT_ROLL_NUMBER = "stud_roll_number";
    public static final String CATEGORY_MASTER = "selected_category";
    public static final String ESTIMATED_SIZE_LIST = "estimated_size_list";
    public static final String MEASUREMENT_DATA = "measurement_data";
    ArrayList<MeasurementData> arrMeasurementData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_measurement_three);
        objHelper.setActionBar(this, getString(R.string.strCalculateSize), true);
        objTabManager = new TabManager(TabManager.MEASUREMENT, ac);
        objTabManager.setCurrentSelection();
        setData();
    }

    private void setData() {
        new AsyncTask() {
            ArrayList<Category> arrCategory = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                btnCalculateSize = (Button) findViewById(R.id.btnCalculateSize);
                btnCalculateSize.setOnClickListener(calSizeClick);
                lyMeasurementParameter = (LinearLayout) findViewById(R.id.lyMeasurementParameter);
                spnCategory = (Spinner) findViewById(R.id.spnCategory);
                edRollNumber = (MaterialEditText) findViewById(R.id.edRollNumber);
                edStudentFirstName = (MaterialEditText) findViewById(R.id.edStudentFirstName);
                edStudentLastName = (MaterialEditText) findViewById(R.id.edStudentLastName);
                lySizeCalculationScreen = (RelativeLayout) findViewById(R.id.lySizeCalculationScreen);
                lyMainScreen = (RelativeLayout) findViewById(R.id.lyMainScreen);
                if (ac.getIntent().getExtras() != null) {
                    objSelectedSchoolMaster = (SchoolMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.SCHOOL_MASTER);
                    objSelectedClassMaster = (ClassMaster) ac.getIntent().getSerializableExtra(acMeasurementOne.CLASS_MASTER);
                    objSelectedAgeGroup = (AgeGroup) ac.getIntent().getSerializableExtra(acMeasurementOne.AGE_GROUP);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrCategory = Category.getDataFromDatabase(mContext);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrCategory != null) {
                    final ArrayAdapter<Category> adpCategory = new ArrayAdapter<Category>(mContext, R.layout.spinner_item_no_padding, arrCategory);
                    adpCategory.setDropDownViewResource(R.layout.spinner_item);
                    spnCategory.setAdapter(adpCategory);
                    spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategoryId = arrCategory.get(position).getId();
                            setMeasurementParameter(arrCategory.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener calSizeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask() {
                boolean isDataEnteredProper = true;
                ArrayList<EstimatedSize> arrEstimatedSize = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (Helper.isFieldBlank(edStudentFirstName.getText().toString())) {
                        isDataEnteredProper = false;
                        edStudentFirstName.setError(getString(R.string.msgEnterFirstName));
                        Helper.requestFocus(ac, edStudentFirstName);
                    } else if (Helper.isFieldBlank(edStudentLastName.getText().toString())) {
                        isDataEnteredProper = false;
                        edStudentLastName.setError(getString(R.string.msgEnterLastName));
                        Helper.requestFocus(ac, edStudentLastName);
                    } else if (Helper.isFieldBlank(edRollNumber.getText().toString())) {
                        isDataEnteredProper = false;
                        edRollNumber.setError(getString(R.string.msgEnterRollNumber));
                        Helper.requestFocus(ac, edRollNumber);
                    }
                    for (int i = lyMeasurementParameter.getChildCount() - 1; i >= 0; i--) {
                        MaterialTextInputLayout lyed = (MaterialTextInputLayout) lyMeasurementParameter.getChildAt(i);
                        MaterialEditText ed = (MaterialEditText) ((FrameLayout) lyed.getChildAt(0)).getChildAt(0);
                        if (Helper.isFieldBlank(ed.getText().toString())) {
                            isDataEnteredProper = false;
                            ed.setError(lyed.getHint());
                            Helper.requestFocus(ac, ed);
                        }
                    }

                    if (isDataEnteredProper) {
                        lyMainScreen.setVisibility(View.GONE);
                        lySizeCalculationScreen.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    if (isDataEnteredProper) {
                        arrEstimatedSize = calculateSize();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    lyMainScreen.setVisibility(View.VISIBLE);
                    lySizeCalculationScreen.setVisibility(View.GONE);
                    if (isDataEnteredProper) {
                        Intent i = new Intent(mContext, acMeasurementFour.class);
                        i.putExtra(acMeasurementOne.SCHOOL_MASTER, objSelectedSchoolMaster);
                        i.putExtra(acMeasurementOne.CLASS_MASTER, objSelectedClassMaster);
                        i.putExtra(acMeasurementOne.AGE_GROUP, objSelectedAgeGroup);
                        i.putExtra(acMeasurementThree.STUDENT_FIRST_NAME, edStudentFirstName.getText().toString());
                        i.putExtra(acMeasurementThree.STUDENT_LAST_NAME, edStudentLastName.getText().toString());
                        i.putExtra(acMeasurementThree.STUDENT_ROLL_NUMBER, edRollNumber.getText().toString());
                        i.putExtra(acMeasurementThree.CATEGORY_MASTER, (Category) spnCategory.getSelectedItem());
                        i.putExtra(acMeasurementThree.ESTIMATED_SIZE_LIST, arrEstimatedSize);
                        i.putExtra(acMeasurementThree.MEASUREMENT_DATA, arrMeasurementData);
                        startActivityForResult(i, ConstantVal.REQUEST_MEASUREMENT_FOUR);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    };

    private ArrayList<EstimatedSize> calculateSize() {
        DataBase db = new DataBase(mContext);
        db.open();
        ArrayList<EstimatedSize> arrEstimatedSize = new ArrayList<>();
        arrMeasurementData = new ArrayList<>();
        try {
            for (int i = lyMeasurementParameter.getChildCount() - 1; i >= 0; i--) {
                MaterialTextInputLayout lyed = (MaterialTextInputLayout) lyMeasurementParameter.getChildAt(i);
                MaterialEditText ed = (MaterialEditText) ((FrameLayout) lyed.getChildAt(0)).getChildAt(0);
                int type_id = Integer.parseInt(ed.getTag().toString());
                String type_val = ed.getText().toString();
                arrMeasurementData.add(new MeasurementData(type_id, type_val));
                String where = "category_id=" + selectedCategoryId + " and (measurement_type_id=" + type_id + " and measurement_value>=" + type_val + ")";
                Cursor cur = db.fetch(DataBase.category_measurement_relation,
                        where, "measurement_value", "1");
                try {
                    if (cur != null && cur.getCount() > 0) {
                        cur.moveToFirst();
                        CategoryMeasurementRelation objCategoryMeasurementRelation = new CategoryMeasurementRelation(cur.getInt(1), cur.getInt(2), cur.getInt(3), cur.getInt(4), cur.getInt(5), cur.getString(6), cur.getString(7));
                        if (!ifSizeAlreadyAdded(arrEstimatedSize, objCategoryMeasurementRelation.getSize_id(), objCategoryMeasurementRelation.getAge_group_id())) {
                            arrEstimatedSize.add(new EstimatedSize(objCategoryMeasurementRelation.getSize_id(), objCategoryMeasurementRelation.getCategory_id(), objCategoryMeasurementRelation.getAge_group_id(), objCategoryMeasurementRelation.getSize(), false));
                            Logger.debug("category_id=" + selectedCategoryId + " and (measurement_type_id=" + type_id + " and measurement_value>=" + type_val + ")" + "    ::::    " + objCategoryMeasurementRelation.getSize_id());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                } finally {
                    cur.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            db.close();
        }
        return arrEstimatedSize;
    }

    private boolean ifSizeAlreadyAdded(ArrayList<EstimatedSize> arrEstimatedSize, int size_id, int age_group_id) {
        for (EstimatedSize s : arrEstimatedSize) {
            if (s.getSize_id() == size_id && s.getAge_group_id() == age_group_id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVal.REQUEST_MEASUREMENT_FOUR && resultCode == ConstantVal.RESPONSE_MEASUREMENT_FOUR) {
            finish();
        }
    }

    private void setMeasurementParameter(Category objCategory) {
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor curCategoryMeasurementRel = db.fetchDistinctMeasurementType(DataBase.category_measurement_relation, "category_id=" + objCategory.getId());
        try {
            ArrayList<MeasurementParameter> arrMeasurementParameter = new ArrayList<>();
            if (curCategoryMeasurementRel != null && curCategoryMeasurementRel.getCount() > 0) {
                curCategoryMeasurementRel.moveToFirst();
                do {
                    int measurement_type_id = curCategoryMeasurementRel.getInt(0);
                    Cursor curMeasurementType = db.fetch(DataBase.measurement_type, DataBase.measurement_type_int, "id=" + measurement_type_id);
                    if (curMeasurementType != null && curMeasurementType.getCount() > 0) {
                        String type_name = curMeasurementType.getString(2);
                        arrMeasurementParameter.add(new MeasurementParameter(measurement_type_id, objCategory.getId(), type_name));
                    }
                } while (curCategoryMeasurementRel.moveToNext());
            }
            addEditTextToLayout(arrMeasurementParameter);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        } finally {
            curCategoryMeasurementRel.close();
            db.close();
        }
    }

    private void addEditTextToLayout(ArrayList<MeasurementParameter> arrMeasurementParameter) {
        lyMeasurementParameter.removeAllViews();
        for (MeasurementParameter obj : arrMeasurementParameter) {
            MaterialEditText ed = new MaterialEditText(mContext);
            MaterialTextInputLayout lyed = new MaterialTextInputLayout(mContext);

            LinearLayout.LayoutParams edParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ed.setLayoutParams(edParam);
            lyed.setLayoutParams(edParam);
            ed.setTextAppearance(mContext, R.style.stySubTitleBlackSingleLine);
            ed.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            InputFilterMinMax filter = new InputFilterMinMax((double) 0, (double) 1000);
            ed.setFilters(new InputFilter[]{filter});

            lyed.setHint(obj.getType_name());
            ed.setTag(obj.getMeasurement_type_id());
            lyed.addView(ed);
            lyMeasurementParameter.addView(lyed);
        }
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
