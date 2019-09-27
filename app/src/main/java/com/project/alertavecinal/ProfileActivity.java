package com.project.alertavecinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Button UpdateAccountProfile;
    private EditText userNombre, userDireccion, userTelefono;
    private CircleImageView userImagen;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        InitializeFields();

        mFirebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        UpdateAccountProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = userNombre.getText().toString();
                String direccion = userDireccion.getText().toString();
                String telefono = userTelefono.getText().toString();
                if(nombre.isEmpty() && direccion.isEmpty() && telefono.isEmpty()){
                    Toast.makeText(ProfileActivity.this,"Campos vacios!",Toast.LENGTH_SHORT).show();
                } else {
                    String currentUserId = mFirebaseAuth.getCurrentUser().getUid();
                    if(!nombre.isEmpty()){
                        rootRef.child("Users").child(currentUserId).child("nombre").setValue(nombre);
                    }
                    if(!direccion.isEmpty()){
                        rootRef.child("Users").child(currentUserId).child("direccion").setValue(direccion);
                    }
                    if(!telefono.isEmpty()){
                        rootRef.child("Users").child(currentUserId).child("telefono").setValue(telefono);
                    }
                    Toast.makeText(ProfileActivity.this,"Gurdado!",Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                }
            }
        });
    }

    private void InitializeFields() {
        UpdateAccountProfile = findViewById(R.id.set_user_button);
        userNombre = findViewById(R.id.set_user_name);
        userDireccion = findViewById(R.id.set_user_direccion);
        userTelefono = findViewById(R.id.set_user_phone);
        userImagen = findViewById(R.id.set_user_imagen);
    }
}
