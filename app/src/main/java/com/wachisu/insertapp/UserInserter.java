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

    // mUserLocalStorage = UserPreferences class

    UserLocalStorage mUserLocalStorage;

    // Views injecten ( Met behulp van ButterKnife ). Google SquareUp ButterKnife voor documentatie.

    @InjectView(R.id.etEmailAddress)    EditText    etUsername;
    @InjectView(R.id.etPassword)        EditText    etPassword;
    @InjectView(R.id.etDatabase)        EditText    etDatabase;
    @InjectView(R.id.btLogin)           Button      btLogin;
    @InjectView(R.id.cbRememberMe)      CheckBox    cbRememberMe;

    // editText array die we doorgeven

    ArrayList < EditText > editTexts = new ArrayList < > ();

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.home_layout);
                // Hier geven we de lokale Context door aan onze functie.
             mUserLocalStorage = new UserLocalStorage(this);
        // Alle views injecten. Zie documentatie als je dit niet begrijpt.
        ButterKnife.inject(this);

        saveLogin = mUserLocalStorage.userLocalDatabase.getBoolean("saveLogin", false);

        if (saveLogin) {

            // Als de gebruiken ervoor gekozen heeft om zijn/haar data te rememberen, dan vullen we alle editText's met userData.

            etUsername.setText(mUserLocalStorage.userLocalDatabase.getString("usernameValue", ""));
            etPassword.setText(mUserLocalStorage.userLocalDatabase.getString("passwordValue", ""));
            etDatabase.setText(mUserLocalStorage.userLocalDatabase.getString("databaseValue", ""));
            cbRememberMe.setChecked(true);
        }

            // Hier voeg je editText's toe aan onze array.

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

        // Alle EditText values doorgeven aan een variable.

        usernameValue = etUsername.getText().toString();
        passwordValue = etPassword.getText().toString();
        databaseValue = etDatabase.getText().toString();

        if (isnetworkavaible()) {

            // We gebruiken MimeCraft om alle data door te sturen naar onze web_request. .add("sessie-naam", data)
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

                    // Alle JSON data uitprinten.
                        _(jsonData);

                        if(response.isSuccessful())
                        {
                            try {
                                returnedData = getCurrentData(jsonData);

                                // Alles op een UI thread runnen.

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(returnedData.get_isSuccess() == 1)
                                        {
                                            if (cbRememberMe.isChecked()) {
                                                _("Remember checked so we're storing data");

                                                // cbRememberMe checked? storeUserData(value1, value2, value3)

                                                mUserLocalStorage.storeUserData(usernameValue, passwordValue, databaseValue);
                                            } else {

                                                // Data clearen.

                                                mUserLocalStorage.clearUserData();
                                            }
                                            // UserLogged TRUE
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

    // Zie volgende comment.

    private boolean isEmpty(EditText textCheck) {
        return textCheck.getText().toString().trim().length() <= 0;
    }

    // Hier gebruiken we de isEmpty functie ^^ om te kijken of elke veld ( die IN de array zit ) leeg is of niet.

    public boolean isEditTextEmpty(ArrayList < EditText > editTexts) {
        for (int i = 0; i < editTexts.size(); i++) {
            if (isEmpty(editTexts.get(i))) {
                return true;
            }
        }
        return false;
    }

    // Hier gebruiken we onze getters en setters om onze JSON data te verwelkomen.

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

    // Kijken of gebruiker verbonden is met een netwerk ( wifi ). Zo niet, laat een error message zien.

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

    // Hier gebruiken we de AlertDialogFragment class om een alert aan te maken.

    private void errorAlert() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setDialogTitle(getString(R.string.error_dialog_one));
        dialog.setDialogMessage(getString(R.string.error_dialog_one_message));
        dialog.setDialogButtonText(getString(R.string.error_dialog_one_button_text));
        dialog.show(getFragmentManager(), "error_dialog");
    }

    // Hier gebruiken we de AlertDialogFragment class om een alert aan te maken.

    private void errorNoNetworkAlert() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setDialogTitle(getString(R.string.network_error_title));
        dialog.setDialogMessage(getString(R.string.network_error_message));
        dialog.setDialogButtonText(getString(R.string.network_error_button_text));
        dialog.show(getFragmentManager(), "error_dialog_network");
    }


    /*                      _T(1, 2, 3); ********** Example
    *
    *  1 -- Geef de context door ( lokale context )
    *  2 -- Geef de message door ( kan ook een string resource zijn )
    *  3 -- Geef een toast length door ( Toast.LENGTH_SHORT/LONG )
    * */

     private void _T(Context context, CharSequence text, int duration)
    {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // String s = message die je wilt loggen. ( E.G, " Hier begint de ... activity/function "

    private void _(String s) {
        Log.d("################ " + "MyApp ", "MainActivity " + "################################# " + s);
    }
}