package com.project.alertavecinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Button UpdateAccountProfile;
    private EditText userNombre, userDireccion, userTelefono;
    private CircleImageView userImagen;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference rootRef;
    private String currentUserId;
    private ProgressDialog loadingBar;

    private static final int GalleryPicture = 1;
    private StorageReference UserProfileImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        InitializeFields();

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserId = mFirebaseAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

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

        RetrieveUserInfo();

        userImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GalleryPicture);
            }
        });
    }

    private void InitializeFields() {
        UpdateAccountProfile = findViewById(R.id.set_user_button);
        userNombre = findViewById(R.id.set_user_name);
        userDireccion = findViewById(R.id.set_user_direccion);
        userTelefono = findViewById(R.id.set_user_phone);
        userImagen = findViewById(R.id.set_user_imagen);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPicture && resultCode==RESULT_OK && data!=null){
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            userImagen.setImageURI(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK){
                loadingBar.setTitle("Set Profile Image");
                loadingBar.setMessage("Please wait, your profile image is updating...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImagesRef.child(currentUserId + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this,"Imagen de perfil subida con exito!",Toast.LENGTH_SHORT).show();
                            final String downloadUri = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                            rootRef.child("Users").child(currentUserId).child("imagen").setValue(downloadUri)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProfileActivity.this,"Imagen de perfil gurdada!",Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }else {
                                                String message = task.getException().toString();
                                                Toast.makeText(ProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(ProfileActivity.this,"Error: " + message,Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }

    private void RetrieveUserInfo()
    {
        rootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("nombre") && (dataSnapshot.hasChild("imagen"))))
                        {
                            String retrieveUserName = dataSnapshot.child("nombre").getValue().toString();
                            String retrieveUserDireccion = dataSnapshot.child("direccion").getValue().toString();
                            String retrieveUserTelefono = dataSnapshot.child("telefono").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("imagen").getValue().toString();

                            userNombre.setText(retrieveUserName);
                            userDireccion.setText(retrieveUserDireccion);
                            userTelefono.setText(retrieveUserTelefono);
                            //Picasso.get().load(retrieveProfileImage).into(userImagen);
                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                        {
                            String retrieveUserName = dataSnapshot.child("nombre").getValue().toString();
                            String retrieveUserDireccion = dataSnapshot.child("direccion").getValue().toString();
                            String retrieveUserTelefono = dataSnapshot.child("telefono").getValue().toString();

                            userNombre.setText(retrieveUserName);
                            userDireccion.setText(retrieveUserDireccion);
                            userTelefono.setText(retrieveUserTelefono);
                        }
                        else
                        {
                            userNombre.setVisibility(View.VISIBLE);
                            Toast.makeText(ProfileActivity.this, "Please set & update your profile information...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
