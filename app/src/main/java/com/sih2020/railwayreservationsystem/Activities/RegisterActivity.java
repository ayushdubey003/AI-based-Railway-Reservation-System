package com.sih2020.railwayreservationsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private TextView mobile,registerButton;
    private EditText email, username, password, confirmPassword,
            firstName, middleName, lastName, dob;
    private ImageView calender;
    private Spinner nationality, occupation, maritalStatus;
    private RadioGroup radioGroup;
    private RadioButton male, female, transGender;
    private FirebaseAuth mauth;

    private String phoneno, codeSent;

    private DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dref= FirebaseDatabase.getInstance().getReference("Users");
        init();
        takePhoneNoInput();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        if(validate()){
            HashMap<String,String> map=new HashMap();
            map.put("uid",mauth.getCurrentUser().getUid());
            map.put("mobile",mauth.getCurrentUser().getPhoneNumber());
            map.put("email",email.getText().toString().trim());
            map.put("username",username.getText().toString().trim());
            map.put("password",password.getText().toString().trim());
            map.put("name",firstName.getText().toString().trim()+middleName.getText().toString().trim()+
                    lastName.getText().toString().trim());
            map.put("dob",dob.getText().toString().trim());
            map.put("gender","Male");
            map.put("nationality","IND");
            map.put("occupation","");
            map.put("maritalStatus","");

            dref.child(mauth.getCurrentUser().getUid()).setValue(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registeration failed", Toast.LENGTH_SHORT).show();
                                Log.e( "onComplete: ",task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    private boolean validate() {
        return true;
    }

    private void takePhoneNoInput() {
        Rect displayRectangle = new Rect();
        Window window = RegisterActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.mobile_dialog, viewGroup, false);

        dialogView.findViewById(R.id.upper_text).setBackground(GenerateBackground.generateBackground());
        TextView sendOtp = dialogView.findViewById(R.id.send_otp);
        final EditText enterPhone = dialogView.findViewById(R.id.enter_phone_no_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCancelable(false);
        alertDialog.show();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneno = enterPhone.getText().toString().trim();
                if (phoneno.equals("")) {
                    enterPhone.setError("Enter Phone Number");
                    enterPhone.requestFocus();
                    return;
                }
                if (phoneno.length() != 10) {
                    enterPhone.setError("Enter a valid 10 digit Phone Number");
                    enterPhone.requestFocus();
                    return;
                }

                sendVerificationCode();
                alertDialog.hide();
            }
        });
    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Log.e("onVerificationFailed: ", e.getMessage());

                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeSent = s;
                    openEnterOtpDialog();
                }
            };

    private void openEnterOtpDialog() {
        Rect displayRectangle = new Rect();
        Window window = RegisterActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.mobile_dialog, viewGroup, false);

        TextView upper_text = dialogView.findViewById(R.id.upper_text);
        upper_text.setBackground(GenerateBackground.generateBackground());
        upper_text.setText("User Registration : Step 2");

        TextView sendOtp = dialogView.findViewById(R.id.send_otp);

        final EditText enterOtp = dialogView.findViewById(R.id.enter_phone_no_et);
        enterOtp.setHint("Enter OTP");

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog2 = builder.create();
        alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog2.setCancelable(false);
        alertDialog2.show();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = enterOtp.getText().toString().trim();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,enteredOtp);
                signInWithPhoneAuthCredential(credential);
                alertDialog2.hide();
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("level", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            mobile.setText(user.getPhoneNumber());
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            finish();
                            Log.e("level", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void init() {
        mauth = FirebaseAuth.getInstance();

        registerButton=findViewById(R.id.register_button);
        mobile = findViewById(R.id.phone_no_text);
        email = findViewById(R.id.email_register);
        username = findViewById(R.id.user_name_register);
        password = findViewById(R.id.password_register);
        confirmPassword = findViewById(R.id.confirm_password_register);
        firstName = findViewById(R.id.first_name);
        middleName = findViewById(R.id.middle_name);
        lastName = findViewById(R.id.last_name);
        dob = findViewById(R.id.date_text_register);

        calender = findViewById(R.id.calender_image_register);
        nationality = findViewById(R.id.nationality);
        occupation = findViewById(R.id.occupation);
        maritalStatus = findViewById(R.id.marital_status);

        radioGroup = findViewById(R.id.radio_group);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        transGender = findViewById(R.id.transgender);

    }
}
