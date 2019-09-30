package com.project.alertavecinal;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupAlertActivity extends AppCompatActivity {

    private Button avisoDeLLegada, avisoDeEntradaSegura;
    private String currentGroupName, currentUserId, currentUserName;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database1 = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_alert);



        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        InitializateFields();
        avisoDeLLegada = findViewById(R.id.aviso_de_llegada);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.lstMensajesGrp,
                listItems);
        setListAdapter(adapter);

        avisoDeLLegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Date currentTime = Calendar.getInstance().getTime();

            Alerta nAlerta = new Alerta("Aviso de Llegada", currentUserId, "dir1", "nil", "Estoy llegando a mi casa", currentGroupName,currentTime );
            DatabaseReference myRef = database1.getReference();
            myRef.child("Mensajes").child(currentGroupName).push().setValue(nAlerta);

            //lstMensajesGrp
            }
        });



    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(View v) {
        listItems.add("Clicked : "+clickCounter++);
        adapter.notifyDataSetChanged();
    }

    private void InitializateFields() {
        toolbar = findViewById(R.id.group_alert_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        avisoDeLLegada = findViewById(R.id.aviso_de_llegada);
        avisoDeEntradaSegura = findViewById(R.id.aviso_de_entrada_segura);
    }

    }


}
