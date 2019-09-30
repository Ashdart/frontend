package com.project.alertavecinal;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class GroupAlertActivity extends AppCompatActivity {

    private Button avisoDeLLegada, avisoDeEntradaSegura;
    private String currentGroupName, currentUserId, currentUserName;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_alert);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        currentGroupName = getIntent().getExtras().get("groupName").toString();
        InitializateFields();



    }

    private void InitializateFields() {
        toolbar = findViewById(R.id.group_alert_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        avisoDeLLegada = findViewById(R.id.aviso_de_llegada);
        avisoDeEntradaSegura = findViewById(R.id.aviso_de_entrada_segura);
    }
}
