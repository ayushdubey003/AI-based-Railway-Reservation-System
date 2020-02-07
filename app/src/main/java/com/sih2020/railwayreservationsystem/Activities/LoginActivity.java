package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

public class LoginActivity extends AppCompatActivity {

    private ScrollView root_layout;
    private TextView captcha;
    private TextView login_button, openRegisterActivity;
    private EditText userName, passWord, enterCaptcha;
    private ImageView refreshCaptcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        receiveclicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            finish();
        }
    }

    private void receiveclicks() {
        openRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start register activity
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void init() {
        openRegisterActivity = findViewById(R.id.open_register_activity);
        userName = findViewById(R.id.user_name_login);
        passWord = findViewById(R.id.password_login);
        enterCaptcha = findViewById(R.id.enter_captcha);
        refreshCaptcha = findViewById(R.id.refresh_captcha);

        root_layout = findViewById(R.id.root_layout);
        captcha = findViewById(R.id.captcha);
        login_button = findViewById(R.id.login_button);

        root_layout.setBackground(GenerateBackground.generateBackground());
        captcha.setBackground(GenerateBackground.generateBackground());
        login_button.setBackground(GenerateBackground.generateBackground());
    }
}
