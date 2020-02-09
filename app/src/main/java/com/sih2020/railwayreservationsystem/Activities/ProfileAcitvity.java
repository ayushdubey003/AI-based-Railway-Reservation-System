package com.sih2020.railwayreservationsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Services.TatkalService;

import java.util.HashMap;

public class ProfileAcitvity extends AppCompatActivity {

    private TextView userName, userEmail, currentBalance,
            addBalance, noHistoryText, noMasterText,logOut;
    private ProgressBar progressBar;

    private DatabaseReference dref;
    private FirebaseAuth mAuth;
    private HashMap<String,Object> lMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acitvity);

        dref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();

        init();
        setProfileData();
        receiveClicks();
    }

    private void receiveClicks() {
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
    }

    private void setProfileData() {
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    if (ds.getKey().equals(mAuth.getCurrentUser().getUid())){
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
        lMap=new HashMap<>();
        userName = findViewById(R.id.username_pa);
        userEmail = findViewById(R.id.email_pa);
        currentBalance = findViewById(R.id.current_balance_pa);
        addBalance = findViewById(R.id.add_balance_pa);
        noHistoryText = findViewById(R.id.no_history_text_pa);
        noMasterText = findViewById(R.id.no_master_text_pa);
        logOut=findViewById(R.id.logout_pa);
        progressBar=findViewById(R.id.progress_pa);
    }
}
