package com.wachisu.insertapp;

import android.os.Bundle;
import android.app.Activity;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    @InjectView(R.id.usernameEditText)          EditText editUsername;
    @InjectView(R.id.passwordEditText)          EditText editPassword;
    @InjectView(R.id.databaseEditText)          EditText editDatabase;
    @InjectView(R.id.register_post_submit)      Button   button;

    ArrayList<EditText> editTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        ButterKnife.inject(this);

        editTexts.add(editUsername);
        editTexts.add(editPassword);
        editTexts.add(editDatabase);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isEditTextEmpty(editTexts) )
                {
                    Toast.makeText(getApplicationContext(), "Please make sure that every form is filled out.",
                            Toast.LENGTH_LONG).show();
                }
                else
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

        RequestBody formData = new FormEncodingBuilder()
                .add("Username", usernameValue)
                .add("Password", passwordValue)
                .add("Database", databaseValue)
                .build();


        Request.Builder builder = new Request.Builder();
        builder.url("http://10.0.2.2/newnew/login.php");
        builder.post(formData);

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
        } else
        {
            return true;
        }
    }

    public boolean isEditTextEmpty(ArrayList<EditText> editTexts)
    {
        for (int i = 0; i < editTexts.size(); i++) {
            if (isEmpty(editTexts.get(i))){
                return true;
            }
        }
        return false;
    }

}