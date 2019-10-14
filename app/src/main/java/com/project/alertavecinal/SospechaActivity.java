package com.project.alertavecinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SospechaActivity extends AppCompatActivity {


    private DatabaseReference contactsRef,rootRef,userRef, MensajesRef;
    private FirebaseAuth mAuth;
    private String currentGroupName,currentUserId, currentUserName, currentUserDireccion, currentUserImagen;
    private RecyclerView recycler_user;
    private TextView textView4;
    private EditText descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sospecha);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        InitializateFields();

        currentUserName = getIntent().getExtras().get("groupName").toString();
        currentUserDireccion = getIntent().getExtras().get("groupName").toString();
        currentUserImagen = getIntent().getExtras().get("groupName").toString();
        currentGroupName = getIntent().getExtras().get("groupName").toString();

        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);;
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        MensajesRef = FirebaseDatabase.getInstance().getReference().child("Mensajes").child(currentGroupName);
        rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        recycler_user.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(contactsRef, Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position, @NonNull Contacts contacts) {
                String usersIds = getRef(position).getKey();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String visit_user_id = getRef(position).getKey();
                        String descAlerta = descripcion.getText().toString();

                        textView4.setText(visit_user_id);

                        Date currentTime = Calendar.getInstance().getTime();

                        Alerta nAlerta = new Alerta("Aviso de Actitud Sospechosa", currentUserId, currentUserName, currentUserDireccion, visit_user_id, descAlerta, currentGroupName,currentTime , currentUserImagen);
                        MensajesRef.push().setValue(nAlerta);

                        finish();

                    }
                });

                userRef.child(usersIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("imagen")){
                            String userImagen = dataSnapshot.child("imagen").getValue().toString();
                            String profileDireccion = dataSnapshot.child("direccion").getValue().toString();
                            String profileNombre = dataSnapshot.child("nombre").getValue().toString();
                            String profileTelefono = dataSnapshot.child("telefono").getValue().toString();

                            holder.userName.setText(profileNombre);
                            holder.userDireccion.setText(profileDireccion);
                            holder.userTelefono.setText(profileTelefono);
                            Picasso.get().load(userImagen).placeholder(R.mipmap.profile_image_round).into(holder.profileImage);
                        } else{
                            String profileDireccion = dataSnapshot.child("direccion").getValue().toString();
                            String profileNombre = dataSnapshot.child("nombre").getValue().toString();
                            String profileTelefono = dataSnapshot.child("telefono").getValue().toString();

                            holder.userName.setText(profileNombre);
                            holder.userDireccion.setText(profileDireccion);
                            holder.userTelefono.setText(profileTelefono);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        recycler_user.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userDireccion, userTelefono;
        CircleImageView profileImage;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.users_profile_name);
            userDireccion = itemView.findViewById(R.id.users_profile_direccion);
            userTelefono = itemView.findViewById(R.id.users_profile_telefono);
            profileImage = itemView.findViewById(R.id.users_profile_image);

        }
    }

    private void InitializateFields() {
        recycler_user = findViewById(R.id.recycler_user);
        textView4 = findViewById(R.id.textView4);
        descripcion = findViewById(R.id.descripcion);
    }
}
