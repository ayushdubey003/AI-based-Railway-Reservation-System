package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.R;

public class RegisterActivity extends AppCompatActivity {

    private TextView mobile;
    private EditText email,username,password,confirmPassword,
            firstName,middleName,lastName, dob;
    private ImageView calender;
    private Spinner nationality,occupation,maritalStatus;
    private RadioGroup radioGroup;
    private RadioButton male,female,transGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        takePhoneNoInput();
    }

    private void takePhoneNoInput() {

    }

    private void init() {
        mobile=findViewById(R.id.phone_no_text);
        email=findViewById(R.id.email_register);
        username=findViewById(R.id.user_name_register);
        password=findViewById(R.id.password_register);
        confirmPassword=findViewById(R.id.confirm_password_register);
        firstName=findViewById(R.id.first_name);
        middleName=findViewById(R.id.middle_name);
        lastName=findViewById(R.id.last_name);
        dob=findViewById(R.id.date_text_register);

        calender=findViewById(R.id.calender_image_register);
        nationality=findViewById(R.id.nationality);
        occupation=findViewById(R.id.occupation);
        maritalStatus=findViewById(R.id.marital_status);

        radioGroup=findViewById(R.id.radio_group);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        transGender=findViewById(R.id.transgender);

    }
}
