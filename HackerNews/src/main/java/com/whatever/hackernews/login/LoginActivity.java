package com.whatever.hackernews.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.whatever.hackernews.R;
import com.whatever.hackernews.model.Blur;


public class LoginActivity extends ActionBarActivity implements AsyncLogin.AsyncLoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActionBar().hide();

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("screenshot");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        BitmapDrawable finalDrawable = new BitmapDrawable(getResources(), bitmap);

        ImageView image = (ImageView) findViewById(R.id.login_backgroundImage);
        image.setBackground(finalDrawable);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.blur_out);
    }
}
