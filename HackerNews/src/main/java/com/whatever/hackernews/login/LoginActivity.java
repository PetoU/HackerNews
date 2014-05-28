package com.whatever.hackernews.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.whatever.hackernews.R;


public class LoginActivity extends ActionBarActivity implements AsyncLogin.AsyncLoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userNameText = (EditText) findViewById(R.id.userName);
        final EditText passwordText = (EditText) findViewById(R.id.password);


        Button loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = userNameText.getText().toString();
                String password = passwordText.getText().toString();

                AsyncLogin asyncLogin = new AsyncLogin(userName, password, LoginActivity.this);
                asyncLogin.execute();

            }
        });

    }


    @Override
    public void onTaskComplete(int responseCode, String sessionIDcookie) {

        //
        // send session ID to mainActvitity and finish
        //
        Intent resultIntent = new Intent();
        resultIntent.putExtra("sessionID", sessionIDcookie);

        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }
}
