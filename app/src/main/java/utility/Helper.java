package utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.User;
import io.fabric.sdk.android.Fabric;
import tailor.mafatlal.com.tailor.R;
import tailor.mafatlal.com.tailor.acLogin;

/**
 * Created by SAI on 8/2/2017.
 */

public class Helper {
    public static void startFabric(Context mContext) {
        Fabric.with(mContext, new Crashlytics());
        String strEmail = Helper.getStringPreference(mContext, User.Fields.EMAIL, "");
        CrashlyticsCore.getInstance().setString("Email Address", strEmail);
        CrashlyticsCore.getInstance().setUserIdentifier(strEmail);
        CrashlyticsCore.getInstance().setUserEmail(strEmail);
        CrashlyticsCore.getInstance().setUserName(strEmail);
    }

    public static void clearPreference(Context c, String key) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.remove(key);
        e.commit();
    }

    public static void setStringPreference(Context c, String pref, String val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getStringPreference(Context context, String pref,
                                             String def) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(pref, def);
    }

    public static int getIntPreference(Context context, String pref, int def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                pref, def);
    }

    public static void setIntPreference(Context c, String pref, int val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.commit();
    }

    public static float getFloatPreference(Context context, String pref, float def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
                pref, def);
    }

    public static void setFloatPreference(Context c, String pref, float val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putFloat(pref, val);
        e.commit();
    }

    public static boolean getBooleanPreference(Context context, String pref,
                                               boolean def) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(pref, def);
    }

    public static void setBooleanPreference(Context c, String pref, boolean val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();
    }

    public static void setLongPreference(Context c, String pref, long val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.commit();
    }

    public static long getLongPreference(Context context, String pref,
                                         long def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(
                pref, def);
    }

    public static boolean isFieldBlank(String val) {
        if (val.equals("") || val == null) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmailId(String emailID) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailID;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    Toolbar toolbar;

    public void setActionBar(final AppCompatActivity ac, final String text, final boolean needToShowBAckButton) {//Back button navigation
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (toolbar == null) {
                    toolbar = (Toolbar) ac.findViewById(R.id.toolbar);
                    ac.setSupportActionBar(toolbar);
                    ac.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ac.getResources()
                            .getColor(R.color.red)));
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ac.invalidateOptionsMenu();
                ActionBar actionBar = ac.getSupportActionBar();
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.red)));
                LayoutInflater mInflater = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = mInflater.inflate(R.layout.home_action, null);
                actionBar.setCustomView(v);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                if (needToShowBAckButton)
                    actionBar.setDisplayHomeAsUpEnabled(true);
                ((TextView) v.findViewById(R.id.txtName)).setText(text);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void requestFocus(AppCompatActivity ac, View view) {
        if (view.requestFocus()) {
            ac.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static boolean isValidJSON(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            Logger.writeToCrashlytics(ex);
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                Logger.writeToCrashlytics(ex1);
                return false;
            }
        }
        return true;
    }

    public static TSnackbar displaySnackbar(final AppCompatActivity ac, final String result, int toastType) {
        final Context ctx = ac;
        if (result.equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
            //Logger.debug("displaySnackbar:" + new Date());
            TSnackbar snackbar = TSnackbar.make(ac.findViewById(android.R.id.content), ConstantVal.ServerResponseCode.getMessage(ctx, result), TSnackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundResource(toastType);
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setCallback(new TSnackbar.Callback() {
                @Override
                public void onDismissed(TSnackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    //Logger.debug("displaySnackbar onDismissed:" + new Date());
                    Intent i = new Intent(ac, acLogin.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
            snackbar.show();
            return snackbar;
        } else {
            TSnackbar snackbar = TSnackbar
                    .make(ac.findViewById(android.R.id.content), ConstantVal.ServerResponseCode.getMessage(ctx, result), TSnackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundResource(toastType);
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(3);
            snackbar.show();
            return snackbar;
        }
    }

    public static Bitmap convertBase64ImageToBitmap(String strBase64) {
        try {
            byte[] decodedString = Base64.decode(strBase64.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception e) {
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static Date convertStringToDate(String strDate, String strFormate) {
        if (!strDate.equals("null") || !strDate.equals("")) {
            DateFormat df = new SimpleDateFormat(strFormate);
            try {
                return df.parse(strDate);
            } catch (ParseException e) {
                Logger.writeToCrashlytics(e);
                return new Date(0);
            }
        } else {
            return new Date(0);
        }
    }

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        Logger.debug(imgString);
        return imgString;
    }

    public static String getCurrencySymbol() {
        Locale defaultLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(defaultLocale);
        return currency.getSymbol();
    }

    public static int getScreenWidth(AppCompatActivity ac) {
        int width = ac.getWindowManager().getDefaultDisplay().getWidth();
        return width;
    }

    public static synchronized void logOutUser(Context mContext, boolean isNeedTosendBroadcast) {
        if (!Helper.getStringPreference(mContext, User.Fields.TOKEN, "").equals("")) {
            User.clearCache(mContext);
            DataBase db = new DataBase(mContext);
            db.open();
            db.cleanWhileLogout();
            db.close();
            if (isNeedTosendBroadcast) {
                Intent intent = new Intent();
                intent.setAction(ConstantVal.BroadcastAction.SESSION_EXPIRE);
                mContext.sendBroadcast(intent);
            } else {
                Intent i = new Intent((AppCompatActivity) mContext, acLogin.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(i);
            }
        }
    }

    private AppCompatActivity objAppCompatActivity;
    private BroadcastReceiver objSessionTimeoutBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.debug("brdSessionTimeOut1:" + objAppCompatActivity.getLocalClassName());
            Helper.displaySnackbar(objAppCompatActivity, ConstantVal.ServerResponseCode.SESSION_EXPIRED, ConstantVal.ToastBGColor.INFO);
        }
    };

    public void registerSessionTimeoutBroadcast(final AppCompatActivity ac) {
        objAppCompatActivity = ac;
        objAppCompatActivity.registerReceiver(objSessionTimeoutBroadcast, new IntentFilter(ConstantVal.BroadcastAction.SESSION_EXPIRE));
    }

    public void unRegisterSesionTimeOutBroadcast(final AppCompatActivity ac) {
        objAppCompatActivity = ac;
        objAppCompatActivity.unregisterReceiver(objSessionTimeoutBroadcast);
    }

}
