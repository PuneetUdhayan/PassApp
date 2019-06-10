package com.example.passdemo1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ArrayList<String> monTimes=new ArrayList<>();
    private ArrayList<String> monSubject=new ArrayList<>();
    private ArrayList<String> monRoom=new ArrayList<>();
    private ArrayList<String> tueTimes=new ArrayList<>();
    private ArrayList<String> tueSubject=new ArrayList<>();
    private ArrayList<String> tueRoom=new ArrayList<>();
    private ArrayList<String> wedTimes=new ArrayList<>();
    private ArrayList<String> wedSubject=new ArrayList<>();
    private ArrayList<String> wedRoom=new ArrayList<>();
    private ArrayList<String> thuTimes=new ArrayList<>();
    private ArrayList<String> thuSubject=new ArrayList<>();
    private ArrayList<String> thuRoom=new ArrayList<>();
    private ArrayList<String> friTimes=new ArrayList<>();
    private ArrayList<String> friSubject=new ArrayList<>();
    private ArrayList<String> friRoom=new ArrayList<>();
    private ArrayList<String> satTimes=new ArrayList<>();
    private ArrayList<String> satSubject=new ArrayList<>();
    private ArrayList<String> satRoom=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase=mFirebaseInstance.getReference().child("Users").child(currentUser.getUid());


        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String room;
                String subject;
                String time;
                for(DataSnapshot outerData:dataSnapshot.getChildren()){
                    if(outerData.getKey().equals("Mon")){
                        for (DataSnapshot cls : outerData.getChildren()) {
                            room = cls.child("room").getValue(String.class);
                            subject = cls.child("subject").getValue(String.class);
                            time = cls.child("time").getValue(String.class);
                            Log.d("hoo",room+subject+time);
//                            DataEntry k = new DataEntry(time, subject, room);
                            monTimes.add(time);
                            monRoom.add(room);
                            monSubject.add(subject);
                        }
                    }
                    else if(outerData.getKey().equals("Tue")){
                        for (DataSnapshot cls : outerData.getChildren()) {
                            room = cls.child("room").getValue(String.class);
                            subject = cls.child("subject").getValue(String.class);
                            time = cls.child("time").getValue(String.class);
                            Log.d("hoo",room+subject+time);
//                            DataEntry k = new DataEntry(time, subject, room);
                            tueTimes.add(time);
                            tueRoom.add(room);
                            tueSubject.add(subject);
                        }
                    }
                    else if(outerData.getKey().equals("Wed")){
                        for (DataSnapshot cls : outerData.getChildren()) {
                            room = cls.child("room").getValue(String.class);
                            subject = cls.child("subject").getValue(String.class);
                            time = cls.child("time").getValue(String.class);
                            Log.d("hoo",room+subject+time);
//                            DataEntry k = new DataEntry(time, subject, room);
                            wedTimes.add(time);
                            wedRoom.add(room);
                            wedSubject.add(subject);
                        }
                    }
                    else if(outerData.getKey().equals("Thu")){
                        for (DataSnapshot cls : outerData.getChildren()) {
                            room = cls.child("room").getValue(String.class);
                            subject = cls.child("subject").getValue(String.class);
                            time = cls.child("time").getValue(String.class);
                            Log.d("hoo",room+subject+time);
//                            DataEntry k = new DataEntry(time, subject, room);
                            thuTimes.add(time);
                            thuRoom.add(room);
                            thuSubject.add(subject);
                        }
                    }
                    else if(outerData.getKey().equals("Fri")){
                        for (DataSnapshot cls : outerData.getChildren()) {
                            room = cls.child("room").getValue(String.class);
                            subject = cls.child("subject").getValue(String.class);
                            time = cls.child("time").getValue(String.class);
                            Log.d("hoo",room+subject+time);
//                            DataEntry k = new DataEntry(time, subject, room);
                            friTimes.add(time);
                            friRoom.add(room);
                            friSubject.add(subject);
                        }
                    }
                    else if(outerData.getKey().equals("Sat")){
                        for (DataSnapshot cls : outerData.getChildren()) {
                            room = cls.child("room").getValue(String.class);
                            subject = cls.child("subject").getValue(String.class);
                            time = cls.child("time").getValue(String.class);
                            Log.d("hoo",room+subject+time);
//                            DataEntry k = new DataEntry(time, subject, room);
                            satTimes.add(time);
                            satRoom.add(room);
                            satSubject.add(subject);
                        }
                    }
                }
                initRecyclerView();
                initRecyclerViewTue();
                initRecyclerViewWed();
                initRecyclerViewThu();
                initRecyclerViewFri();
                initRecyclerViewSat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerViewMon);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(monTimes,monSubject,monRoom,this);
        recyclerView.setAdapter(adapter);
        ProgressBar progressBar=findViewById(R.id.tableProgressBar);
        progressBar.setVisibility(View.GONE);
    }

    private void initRecyclerViewTue(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerViewTue);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(tueTimes,tueSubject,tueRoom,this);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewWed(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerViewWed);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(wedTimes,wedSubject,wedRoom,this);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewThu(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerViewThu);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(thuTimes,thuSubject,thuRoom,this);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewFri(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerViewFri);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(friTimes,friSubject,friRoom,this);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewSat(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerViewSat);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(satTimes,satSubject,satRoom,this);
        recyclerView.setAdapter(adapter);
    }

    public void tableToHome(View view){
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void tableToCalender(View view){
        startActivity(new Intent(this,CalenderActivity.class));
    }

    public void tableToSettings(View view){
        startActivity(new Intent(this,SettingsActivity.class));
    }
}
