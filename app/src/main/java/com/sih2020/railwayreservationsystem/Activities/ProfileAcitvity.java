package com.sih2020.railwayreservationsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Services.TatkalService;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ProfileAcitvity extends AppCompatActivity {

    private TextView userName, userEmail, currentBalance,
            addBalance, noHistoryText, noMasterText, logOut;
    private ProgressBar progressBar;
    private RelativeLayout zerothLayer;
    private ImageView backButton;

    private DatabaseReference dref;
    private FirebaseAuth mAuth;
    private HashMap<String, Object> lMap;

    private AlertDialog alertDialogOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acitvity);

        dref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        init();
        setProfileDataFromFirebase();
        setProfileDataFromBlockChain();
        receiveClicks();
    }

    private void receiveClicks() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileAcitvity.this)
                        .setTitle("LOGOUT")
                        .setMessage("Do You Want to Logout ?")

                        .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                mAuth.signOut();
                                //Toast.makeText(AutomatedTatkal.this, "Set the service", Toast.LENGTH_SHORT).show();
                                //startService(new Intent(ProfileAcitvity.this, TatkalService.class));
                            }
                        })

                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openEnterAmountDialog();
            }
        });
    }

    private void openEnterAmountDialog() {
        Rect displayRectangle = new Rect();
        Window window = ProfileAcitvity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.mobile_dialog, viewGroup, false);

        TextView upper_text = dialogView.findViewById(R.id.upper_text);
        upper_text.setBackground(GenerateBackground.generateBackground());
        upper_text.setText("Enter Amount");

        TextView sendOtp = dialogView.findViewById(R.id.send_otp);
        sendOtp.setText("Add Money");

        final EditText enterOtp = dialogView.findViewById(R.id.enter_phone_no_et);
        enterOtp.setHint("Enter amount to be added");

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileAcitvity.this, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        alertDialogOtp = builder.create();
        alertDialogOtp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogOtp.setCancelable(true);
        alertDialogOtp.show();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                String enteredamount = enterOtp.getText().toString().trim();

                int i;
                try {
                    i = Integer.parseInt(enteredamount);
                } catch (Exception e) {
                    i = 0;
                }

                createBlockChainTransaction(i);

                alertDialogOtp.hide();
            }
        });
    }

    private void createBlockChainTransaction(int amount) {

        try {
            JsonObject jsonBody = new JsonObject();
            jsonBody.addProperty("sender", "504E1EA3CFCDFC105454A406046E51F24BC4D2865212CF179DB018801F701749");
            jsonBody.addProperty("receiver", mAuth.getCurrentUser().getUid());
            jsonBody.addProperty("amount", amount);

            Ion.with(ProfileAcitvity.this)
                    .load(AppConstants.mUrl + "/new_transaction")
                    .setTimeout(5000)
                    .setJsonObjectBody(jsonBody)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {

                            if (e == null) {

                                RequestQueue requestQueue = Volley.newRequestQueue(ProfileAcitvity.this);

                                String url2 = AppConstants.mUrl + "/mine";
                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    setProfileDataFromBlockChain();
                                                    //JSONObject jobj = response.getJSONObject("status");
                                                    Log.e("onResponse: ", response.getString("Success"));
                                                } catch (JSONException e) {
                                                    Log.e("onResponse: ", e.getMessage());
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });

                                requestQueue.add(jsonObjectRequest2);
                            } else {
                                //Toast.makeText(context, R.string.flag_error_response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void setProfileDataFromBlockChain() {
        String url = AppConstants.mUrl + "/calculateWalletAmount/" + mAuth.getCurrentUser().getUid();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int jobj = response.getInt("walletAmount");
                            currentBalance.setText("Rs." + Integer.toString(jobj));
                            Log.e("onResponse: ", response.getString("Success"));
                        } catch (JSONException e) {
                            Log.e("onResponse: ", e.getMessage());
                            e.printStackTrace();
//                            Toast.makeText(PnrActivity.this, "PNR Not Available", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PnrActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setProfileDataFromFirebase() {
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        userName.setText(ds.child("username").getValue().toString());
                        userEmail.setText(ds.child("email").getValue().toString());
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        lMap = new HashMap<>();
        backButton = findViewById(R.id.profile_back);
        userName = findViewById(R.id.username_pa);
        userEmail = findViewById(R.id.email_pa);
        currentBalance = findViewById(R.id.current_balance_pa);
        addBalance = findViewById(R.id.add_balance_pa);
        noHistoryText = findViewById(R.id.no_history_text_pa);
        noMasterText = findViewById(R.id.no_master_text_pa);
        logOut = findViewById(R.id.logout_pa);
        logOut.setBackground(GenerateBackground.generateBackground());
        progressBar = findViewById(R.id.progress_pa);

        zerothLayer = findViewById(R.id.zeroth_layer_pa);
        zerothLayer.setBackground(GenerateBackground.generateBackground());
    }
}
