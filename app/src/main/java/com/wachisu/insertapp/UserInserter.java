package com.wachisu.insertapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class UserInserter extends Activity {
    private OkHttpClient httpClient = new OkHttpClient();
    private static final String TAG = UserInserter.class.getSimpleName();

    private String usernameValue;
    private String passwordValue;
    private String databaseValue;
    private DataReturnedHandler returnedData;
    private Boolean saveLogin;

    UserLocalStorage mUserLocalStorage;

    @InjectView(R.id.etEmailAddress)    EditText    etUsername;
    @InjectView(R.id.etPassword)        EditText    etPassword;
    @InjectView(R.id.etDatabase)        EditText    etDatabase;
    @InjectView(R.id.btLogin)           Button      btLogin;
    @InjectView(R.id.cbRememberMe)      CheckBox    cbRememberMe;

    ArrayList < EditText > editTexts = new ArrayList < > ();

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.home_layout);
             mUserLocalStorage = new UserLocalStorage(this);

        ButterKnife.inject(this);

        saveLogin = mUserLocalStorage.userLocalDatabase.getBoolean("saveLogin", false);

        if (saveLogin) {
            etUsername.setText(mUserLocalStorage.userLocalDatabase.getString("usernameValue", ""));
            etPassword.setText(mUserLocalStorage.userLocalDatabase.getString("passwordValue", ""));
            etDatabase.setText(mUserLocalStorage.userLocalDatabase.getString("databaseValue", ""));
            cbRememberMe.setChecked(true);
        }

            editTexts.add(etUsername);
            editTexts.add(etPassword);
            editTexts.add(etDatabase);

            btLogin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isEditTextEmpty(editTexts)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.form_filled_warning),
                                Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            runLogin(getString(R.string.json_url));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
    public void runLogin(String url) throws JSONException {
        usernameValue = etUsername.getText().toString();
        passwordValue = etPassword.getText().toString();
        databaseValue = etDatabase.getText().toString();

        if (isnetworkavaible()) {
            RequestBody formData = new FormEncodingBuilder()
                    .add(getString(R.string.jsonUsername), usernameValue)
                    .add(getString(R.string.jsonPassword), passwordValue)
                    .add(getString(R.string.jsonDatabase), databaseValue)
                    .build();
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.post(formData);
            Request request = builder.build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                    public void onResponse(Response response) throws IOException {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if(response.isSuccessful())
                        {
                            try {
                                returnedData = getCurrentData(jsonData);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(returnedData.get_isSuccess() == 1)
                                        {
                                            if (cbRememberMe.isChecked()) {
                                                _("Remember checked so we're storing data");
                                                mUserLocalStorage.storeUserData(usernameValue, passwordValue, databaseValue);
                                            } else {
                                                mUserLocalStorage.clearUserData();
                                            }
                                            mUserLocalStorage.setUserLoggedIn(true);

                                           _T(getApplicationContext(), getString(R.string.logged_successfully), Toast.LENGTH_LONG);
                                        }
                                        else
                                        {
                                            _T(getApplicationContext(), getString(R.string.logged_faulty), Toast.LENGTH_LONG);
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    else
                        {
                            errorAlert();
                        }
            }
             @Override
                public void onFailure(Request request, IOException e) {
                    errorAlert();
            }
            });
        } else {
            errorNoNetworkAlert();
        }
    }
    private boolean isEmpty(EditText textCheck) {
        return textCheck.getText().toString().trim().length() <= 0;
    }
    public boolean isEditTextEmpty(ArrayList < EditText > editTexts) {
        for (int i = 0; i < editTexts.size(); i++) {
            if (isEmpty(editTexts.get(i))) {
                return true;
            }
        }
        return false;
    }
    private DataReturnedHandler getCurrentData(String jsonData) throws JSONException {
        JSONObject currentData = new JSONObject(jsonData);
        DataReturnedHandler currentDataClass = new DataReturnedHandler();
        currentDataClass.set_isSuccess(currentData.getInt("success"));
        currentDataClass.set_returnedMessage(currentData.getString("message"));
        currentDataClass.set_returnedSessionUsername(currentData.getString("sesUsername"));
        currentDataClass.set_returnedSessionPassword(currentData.getString("sesPassword"));
        currentDataClass.set_returnedSessionDatabase(currentData.getString("sesDB"));
        return currentDataClass;
    }
    private boolean isnetworkavaible() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvaible = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvaible = true;
        }
        return isAvaible;
    }
    private void errorAlert() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setDialogTitle(getString(R.string.error_dialog_one));
        dialog.setDialogMessage(getString(R.string.error_dialog_one_message));
        dialog.setDialogButtonText(getString(R.string.error_dialog_one_button_text));
        dialog.show(getFragmentManager(), "error_dialog");
    }
    private void errorNoNetworkAlert() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setDialogTitle(getString(R.string.network_error_title));
        dialog.setDialogMessage(getString(R.string.network_error_message));
        dialog.setDialogButtonText(getString(R.string.network_error_button_text));
        dialog.show(getFragmentManager(), "error_dialog_network");
    }

    /*
    *  _T(getApplicationContext(), getString(R.string.logged_faulty), Toast.LENGTH_LONG); ********** Example
    * */
    private void _T(Context context, CharSequence text, int duration)
    {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void _(String s) {
        Log.d("################ " + "MyApp ", "MainActivity " + "################################# " + s);
    }
}