package com.wachisu.insertapp;

import android.os.Bundle;
import android.app.Activity;
import java.io.IOException;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONStringer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserInserter extends Activity {

    private OkHttpClient httpClient = new OkHttpClient();
    public static final String TAG = UserInserter.class.getSimpleName();

    private String usernameValue;
    private String passwordValue;
    private String databaseValue;

    @InjectView(R.id.usernameEditText)          EditText editUsername;
    @InjectView(R.id.passwordEditText)          EditText editPassword;
    @InjectView(R.id.databaseEditText)          EditText editDatabase;
    @InjectView(R.id.register_post_submit)      Button   button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        ButterKnife.inject(this);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                usernameValue = editUsername.getText().toString();
                passwordValue = editPassword.getText().toString();
                databaseValue = editDatabase.getText().toString();

                if ( isEmpty(editUsername) == true )
                {
                    Toast.makeText(getApplicationContext(), "Username is required.",
                            Toast.LENGTH_LONG).show();
                } else if ( isEmpty(editPassword) == true )
                {
                    Toast.makeText(getApplicationContext(), "Password is required.",
                            Toast.LENGTH_LONG).show();
                } else if ( isEmpty(editDatabase) == true )
                {
                    Toast.makeText(getApplicationContext(), "Database is required.",
                            Toast.LENGTH_LONG).show();
                }else
                {
                    try {
                        runLogin();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void runLogin() throws JSONException {
        usernameValue = editUsername.getText().toString();
        passwordValue = editPassword.getText().toString();
        databaseValue = editDatabase.getText().toString();
        String jsonString = null;

        JSONStringer stringer = new JSONStringer();

        stringer.object();
        stringer.key("Username");
        stringer.value(usernameValue);
        stringer.endObject();

        jsonString = stringer.toString();

        RequestBody body = RequestBody.create((MediaType.parse("application/json;charset=utf-8")), jsonString);


        Request.Builder builder = new Request.Builder();
        builder.url("http://10.0.2.2/newnew/login.php");
        builder.post(body);

        Request request = builder.build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback(){
            @Override
            public void onResponse(Response response) throws IOException {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
            }
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("error_tag", "Error: " + e.toString());
            }
        });
    }

    private boolean isEmpty(EditText textCheck) {
        if (textCheck.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

}