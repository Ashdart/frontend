package com.project.alertavecinal;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    private View groupFragmentView;
    private DatabaseReference groupRef;
    FirebaseAuth mFirebaseAuth;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfGroups = new ArrayList<>();
    private String currentUserID;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_group, container, false);
        InitializeFields();

        mFirebaseAuth = FirebaseAuth.getInstance();


        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() != null){
            currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        }

        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentUserID);

        RetrieveAndDisplayGroups();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();
                Intent groupAlertIntent = new Intent(getContext(), GroupAlertActivity.class);
                groupAlertIntent.putExtra("groupName", currentGroupName);
                startActivity(groupAlertIntent);
            }
        });

        return groupFragmentView;
    }

    private void RetrieveAndDisplayGroups() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Iterator iterator2 = dataSnapshot.getChildren().iterator();
                while (iterator2.hasNext()){
                    String userId = ((DataSnapshot)iterator2.next()).child(currentUserID).getKey();
                    if(currentUserID.equals(userId)) {
                        while(iterator.hasNext()) {
                            set.add(((DataSnapshot)iterator.next()).getKey());
                        }
                    }
                }
                listOfGroups.clear();
                listOfGroups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitializeFields() {
        list_view = groupFragmentView.findViewById(R.id.list_group_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listOfGroups);
        list_view.setAdapter(arrayAdapter);
    }

}
