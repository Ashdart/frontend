package com.project.alertavecinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import utils.SendNotification;

public class GroupAlertActivity extends AppCompatActivity {

    private Button btnAvisoLlegada, btnAvisoEntradaSegura, btnAvisoAlertaSospecha;
    private String currentGroupName, currentUserId, currentUserName, currentUserDireccion;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef, MensajesRef;
    private ProgressDialog loadingBar;
    private RecyclerView ShowMensajesRecyclerList;
    private Query MensajesRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_alert);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        rootRef = FirebaseDatabase.getInstance().getReference();

/* COPIANDO CODIGO */

        MensajesRef = FirebaseDatabase.getInstance().getReference().child("Mensajes").child(currentGroupName);
        MensajesRef2 = MensajesRef.orderByChild("fechaHora").limitToLast(5);

        ShowMensajesRecyclerList = findViewById(R.id.mensajes_recycler_list);
        ShowMensajesRecyclerList.setLayoutManager(new LinearLayoutManager(this));


        /* FIN */


        currentGroupName = getIntent().getExtras().get("groupName").toString();
        InitializateFields();

        rootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String currentUserName = dataSnapshot.child("nombre").getValue().toString();
                        final String currentUserDireccion = dataSnapshot.child("direccion").getValue().toString();
                        final String currentUserImagen = dataSnapshot.child("imagen").getValue().toString();

                        btnAvisoLlegada.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SendNotification(currentUserName + " esta llegando a su domicilio " + currentUserDireccion,"Aviso de llegada", null);
                                loadingBar.dismiss();

                                Date currentTime = Calendar.getInstance().getTime();

                                Alerta nAlerta = new Alerta("Aviso de Llegada", currentUserId, currentUserName, currentUserDireccion, "nil", "Estoy llegando a mi casa", currentGroupName,currentTime , currentUserImagen);
                                //DatabaseReference myRef = database1.getReference();
                                MensajesRef.push().setValue(nAlerta);
                            }
                        });

                        btnAvisoEntradaSegura.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SendNotification(currentUserName + " ha entrado de forma segura a su casa.","Aviso de Entrada Segura", null);
                                loadingBar.dismiss();

                                Date currentTime = Calendar.getInstance().getTime();

                                Alerta nAlerta = new Alerta("Aviso de Entrada Segura", currentUserId, currentUserName, currentUserDireccion, "nil", "Ya ingrese de forma segura", currentGroupName,currentTime , currentUserImagen);
                                //DatabaseReference myRef = database1.getReference();
                                MensajesRef.push().setValue(nAlerta);
                            }
                        });

                        btnAvisoAlertaSospecha.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(GroupAlertActivity.this, SospechaActivity.class);
                                intent.putExtra("currentUserName", currentUserName);
                                intent.putExtra("currentUserDireccion", currentUserDireccion);
                                intent.putExtra("currentUserImagen", currentUserImagen);
                                intent.putExtra("groupName", currentGroupName);
                                startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    /* Agregando */
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Alerta> options =
                new FirebaseRecyclerOptions.Builder<Alerta>()
                        .setQuery(MensajesRef2, Alerta.class)
                        .build();

        FirebaseRecyclerAdapter<Alerta, ShowMensajesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Alerta, ShowMensajesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ShowMensajesViewHolder holder, final int position, @NonNull Alerta model)
                    {
                        holder.userNombre.setText(model.getUserName());
                        holder.userDireccion.setText(model.getDireccion());
                        holder.userMensaje.setText(model.getMensaje());
                        holder.userHora.setText(model.getFormatTime());
                        Picasso.get().load(model.getImagen()).placeholder(R.drawable.profile_image).into(holder.profileImage);


                    }

                    @NonNull
                    @Override
                    public ShowMensajesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mensajes_display_layout, viewGroup, false);
                        ShowMensajesViewHolder viewHolder = new ShowMensajesViewHolder(view);
                        return viewHolder;
                    }
                };

        ShowMensajesRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }

    /* Agregando */


    private void InitializateFields() {
        toolbar = findViewById(R.id.group_alert_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        btnAvisoLlegada = findViewById(R.id.btnAvisoLleada);
        btnAvisoEntradaSegura = findViewById(R.id.btnAvisoEntradaSegura);
        btnAvisoAlertaSospecha = findViewById(R.id.btnAvisoAlertaSospecha);
        loadingBar = new ProgressDialog(this);
    }

    public static class ShowMensajesViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNombre, userDireccion, userMensaje, userHora;
        CircleImageView profileImage;


        public ShowMensajesViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userNombre = itemView.findViewById(R.id.mensajes_profile_name);
            userDireccion = itemView.findViewById(R.id.mensajes_profile_direccion);
            userMensaje = itemView.findViewById(R.id.mensajes_profile_mensaje);
            profileImage = itemView.findViewById(R.id.mensajes_profile_image);
            userHora = itemView.findViewById(R.id.mensajes_profile_hora);
        }
    }
}
