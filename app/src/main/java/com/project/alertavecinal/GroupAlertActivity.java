package com.project.alertavecinal;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import utils.SendNotification;

public class GroupAlertActivity extends AppCompatActivity {

    private Button btnAvisoLlegada, btnAvisoEntradaSegura;
    private String currentGroupName, currentUserId, currentUserName, currentUserDireccion;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_alert);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        InitializateFields();

        rootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String currentUserName = dataSnapshot.child("nombre").getValue().toString();
                        final String currentUserDireccion = dataSnapshot.child("direccion").getValue().toString();

                        btnAvisoLlegada.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SendNotification(currentUserName + " esta llegando a su domicilio " + currentUserDireccion,"Aviso de llegada", null);
                                loadingBar.dismiss();
                            }
                        });

                        btnAvisoEntradaSegura.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SendNotification(currentUserName + " ha entrado de forma segura a su casa.","Aviso de Entrada Segura", null);
                                loadingBar.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    private void InitializateFields() {
        toolbar = findViewById(R.id.group_alert_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        btnAvisoLlegada = findViewById(R.id.btnAvisoLleada);
        btnAvisoEntradaSegura = findViewById(R.id.btnAvisoEntradaSegura);
        loadingBar = new ProgressDialog(this);
    }
}
