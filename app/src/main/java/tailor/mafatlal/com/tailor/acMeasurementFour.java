package tailor.mafatlal.com.tailor;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import utility.Helper;
import utility.TabManager;

public class acMeasurementFour extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TabManager objTabManager;

    String strSchoolName, strClass, strAgeGroup;
    int intAgeGroup;
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
    }

}
