package com.project.alertavecinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import utils.SendNotification;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        initToolbar();
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);

        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.main_app_bar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_log_out_option){
            mFirebaseAuth.signOut();
            OneSignal.setSubscription(false);
            Intent intToMain = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intToMain);
        }

        if(item.getItemId() == R.id.main_find_contacts_option){
            SendUserToFindFriendsActivity();
        }

        if(item.getItemId() == R.id.main_profile_option){
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        }

        if(item.getItemId() == R.id.main_crear_grupo_option){
            RequestNewGroup();
        }

        return true;
    }

    private void SendUserToFindFriendsActivity() {
        Intent findFriendsIntent = new Intent(HomeActivity.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialog);
        builder.setTitle("Ingrese el nombre del grupo :");

        final EditText groupNameField = new EditText(HomeActivity.this);

        groupNameField.setHint("ej. Alerta Familiar");
        builder.setView(groupNameField);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(HomeActivity.this,"Por favor ingrese el nombre del grupo...",Toast.LENGTH_SHORT).show();
                } else {
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(final String groupName) {
        String currentUserId = mFirebaseAuth.getCurrentUser().getUid();
        rootRef.child("Groups").child(currentUserId).child(groupName).setValue("Created").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(HomeActivity.this,groupName + " fue creado con exito!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}
