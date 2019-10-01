package com.project.alertavecinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import utils.SendNotification;

public class GroupAlertActivity extends AppCompatActivity {

    private Button btnAvisoLlegada, btnAvisoEntradaSegura;
    private String currentGroupName, currentUserId, currentUserName;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_alert);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        currentUserName = mAuth.getCurrentUser().getEmail();

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        InitializateFields();

        btnAvisoLlegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotification(currentUserName + " ha llegado a casa.","Aviso de llegada",null);
            }
        });

        btnAvisoEntradaSegura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotification(currentUserName + " ha entrado de forma segura a su casa.","Aviso de Entrada Segura",null);
            }
        });

    }


    private void InitializateFields() {
        toolbar = findViewById(R.id.group_alert_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        btnAvisoLlegada = findViewById(R.id.btnAvisoLleada);
        btnAvisoEntradaSegura = findViewById(R.id.btnAvisoEntradaSegura);
    }
}
