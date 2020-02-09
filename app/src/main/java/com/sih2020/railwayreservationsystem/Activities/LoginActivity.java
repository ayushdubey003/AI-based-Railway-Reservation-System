package com.sih2020.railwayreservationsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private ScrollView root_layout;
    private TextView captcha;
    private TextView login_button, openRegisterActivity, loginBack;
    private EditText phoneNoLogin, passWord, enterCaptcha;
    private ImageView refreshCaptcha;

    private DatabaseReference pref;
    private FirebaseAuth mauth;

    private String codeSent;

    private AlertDialog alertDialogOtp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = FirebaseDatabase.getInstance().getReference("Phone");
        mauth = FirebaseAuth.getInstance();
        init();
        receiveclicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
        }
        setRandomCaptcha();
    }

    private void setRandomCaptcha() {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 48; i <= 57; i++)
            values.add(i);
        for (int i = 65; i <= 90; i++)
            values.add(i);
        for (int i = 97; i <= 122; i++)
            values.add(i);
        String lpassword = "";
        while (lpassword.length() < 6) {
            int x = values.get((int) (Math.random() * values.size()));
            char ch = (char) x;
            lpassword = lpassword + Character.toString(ch);
        }
        captcha.setText(lpassword);
    }

    private void receiveclicks() {

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    progressDialog.show();

                    pref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean flag2 = false;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getKey().equals("+91" + phoneNoLogin.getText().toString())) {
                                    Log.e("onDataChange: ", "levelofquality");
                                    flag2 = true;
                                    break;
                                }
                            }
                            if (flag2) {
                                //progressDialog.show();
                                sendVerificationCode();
                            } else {
                                phoneNoLogin.setError("User does not exist, Register to proceed");
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        openRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start register activity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setRandomCaptcha();
                    }
                }, 1000);
            }
        });
    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNoLogin.getText().toString().trim(),        // Phone number to verify
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
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeSent = s;


                    progressDialog.dismiss();
                    openEnterOtpDialog();
                }
            };

    private void openEnterOtpDialog() {
        Rect displayRectangle = new Rect();
        Window window = LoginActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.mobile_dialog, viewGroup, false);

        TextView upper_text = dialogView.findViewById(R.id.upper_text);
        upper_text.setBackground(GenerateBackground.generateBackground());
        upper_text.setText("User Login");

        TextView sendOtp = dialogView.findViewById(R.id.send_otp);
        sendOtp.setText("Submit OTP");

        final EditText enterOtp = dialogView.findViewById(R.id.enter_phone_no_et);
        enterOtp.setHint("Enter OTP");

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        alertDialogOtp = builder.create();
        alertDialogOtp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogOtp.setCancelable(false);
        alertDialogOtp.show();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String enteredOtp = enterOtp.getText().toString().trim();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, enteredOtp);
                signInWithPhoneAuthCredential(credential);
                alertDialogOtp.hide();
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("level", "signInWithCredential:success");
                            progressDialog.dismiss();
                            FirebaseUser user = task.getResult().getUser();
                            finish();
                            Log.e("onComplete: ", user.getPhoneNumber());
                            //mauth.signOut();
                        } else {
                            finish();
                            Log.e("level", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private boolean validate() {
        if (phoneNoLogin.getText().toString().length() != 10) {
            phoneNoLogin.setError("Enter a 10 digit phone number");
            phoneNoLogin.requestFocus();
            return false;
        } else if (!enterCaptcha.getText().toString().equals(captcha.getText().toString())) {
            enterCaptcha.setError("Enter correct captcha");
            setRandomCaptcha();
            enterCaptcha.requestFocus();
            return false;
        }
        return true;
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        loginBack = findViewById(R.id.login_back);
        openRegisterActivity = findViewById(R.id.open_register_activity);
        phoneNoLogin = findViewById(R.id.phone_no_login);
        //passWord = findViewById(R.id.password_login);
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
