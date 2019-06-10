package com.example.passdemo1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarSubActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser currentUser;
    private ListView listV; //change made here
    private String dayOfWeek;
    private String selectedDate;

    ArrayList<Cards> cardClassArray;
    ArrayList<DataEntry> cancelledClasses;
    ArrayList<Cards> acceptedClasses;
    CustomListAdapter customListAdapter;

    private static int flag;
    public static String[][] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_sub);

        Bundle bundle = getIntent().getExtras();
        dayOfWeek = bundle.getString("daysent");
        selectedDate=bundle.getString("datesent");

        Log.d("test69","we got here.1");

        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase=mFirebaseInstance.getReference();

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        TextView dayTextView=findViewById(R.id.dayOfWeek);
        TextView dateTextView=findViewById(R.id.dateSelected);

        dayTextView.setText(dayOfWeek);
        dayOfWeek=dayOfWeek.substring(0,3);
        dateTextView.setText(selectedDate);

        listV=findViewById(R.id.listView);

        cardClassArray=new ArrayList<Cards>();
        cancelledClasses=new ArrayList<DataEntry>();
        acceptedClasses=new ArrayList<Cards>();

        customListAdapter =new CustomListAdapter(this,R.layout.class_card,cardClassArray);
        listV.setAdapter(customListAdapter);

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardClassArray.clear();
                flag=0;
                String room;
                String time;
                String subject;
                for(DataSnapshot outerData:dataSnapshot.getChildren()){

                    if(outerData.getKey().equals("DeletedClasses")){
                        for(DataSnapshot useri:outerData.getChildren()){
                            Log.d("test100",useri.getKey());
                            if(useri.getKey().equals(currentUser.getUid())){
                                for(DataSnapshot datei:useri.getChildren()){
                                    if(datei.getKey().equals(selectedDate)){
                                        for(DataSnapshot rani:datei.getChildren()){
                                            room = rani.child("room").getValue(String.class);
                                            subject = rani.child("subject").getValue(String.class);
                                            time = rani.child("time").getValue(String.class);
                                            cancelledClasses.add(new DataEntry(time,subject,room));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(outerData.getKey().equals("Users")){
                        for(DataSnapshot i:outerData.getChildren()){
                            if(i.getKey().equals(currentUser.getUid())) {
                                for (DataSnapshot j : i.getChildren()) {
                                    if (j.getKey().equals(dayOfWeek)) {
                                        for (DataSnapshot cls : j.getChildren()) {
                                            room = cls.child("room").getValue(String.class);
                                            subject = cls.child("subject").getValue(String.class);
                                            time = cls.child("time").getValue(String.class);
                                            DataEntry k = new DataEntry(time, subject, room);
                                            if (compareUI(k)) {
                                                cardClassArray.add(new Cards(time, subject, room,"","", false));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(outerData.getKey().equals("AcceptedClasses")){
                        for(DataSnapshot useri:outerData.getChildren()){
                            if(useri.getKey().equals(currentUser.getUid())){
                                for(DataSnapshot datei:useri.getChildren()){
                                    if(datei.getKey().equals(selectedDate)){
                                        for(DataSnapshot rani:datei.getChildren()){
                                            Log.d("test200",rani.getKey());
                                            room = rani.child("room").getValue(String.class);
                                            subject = rani.child("subject").getValue(String.class);
                                            time = rani.child("time").getValue(String.class);
                                            cardClassArray.add(new Cards(time, subject, room, "", rani.getKey(), false));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(outerData.getKey().equals("AvailableClasses")){
                        for(DataSnapshot datei:outerData.getChildren()){
                            Log.d("datecheck",datei.getKey()+selectedDate);
                            if(selectedDate.equals(datei.getKey())){
                                for(DataSnapshot rani:datei.getChildren()){
                                    room = rani.child("room").getValue(String.class);
                                    subject = rani.child("subject").getValue(String.class);
                                    time = rani.child("time").getValue(String.class);
                                    cardClassArray.add(new Cards(time,subject,room,"",rani.getKey(),true));
                                }
                            }
                        }
                    }
                }
                ProgressBar setOff=findViewById(R.id.waitforlist);
                setOff.setVisibility(View.GONE);
                cardClassArray.addAll(acceptedClasses);
                TextView bob=findViewById(R.id.homeDone);
                if(cardClassArray.size()==0){
                    bob.setVisibility(View.VISIBLE);
                }
                else{
                    bob.setVisibility(View.GONE);
                }
                customListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Cards sup=cardClassArray.get(position);
                if(!sup.getAvailable()){
                    new AlertDialog.Builder(CalendarSubActivity.this)
                            .setMessage("Do you want to cancel the class?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id=mFirebaseDatabase.push().getKey();
                                    Cards cards=cardClassArray.get(position);
                                    DataEntry data=new DataEntry(cards.getTime(),cards.getSubject(),cards.getRoom());
                                    mFirebaseDatabase.child("AvailableClasses").child(selectedDate).child(id).setValue(data);
                                    if(cards.getKey().equals("")){
                                        mFirebaseDatabase.child("DeletedClasses").child(currentUser.getUid()).child(selectedDate).child(id).setValue(data);
                                    }
                                    else{
                                        DatabaseReference deldatax=FirebaseDatabase.getInstance().getReference("AcceptedClasses").child(currentUser.getUid()).child(selectedDate).child(cards.getKey());
                                        Log.d("Do i get here",cards.getKey());
                                        deldatax.removeValue();
                                    }
                                    Toast.makeText(getBaseContext(), "Class canceled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
                else {
                    new AlertDialog.Builder(CalendarSubActivity.this)
                            .setMessage("Do you want to accept the class?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id=mFirebaseDatabase.push().getKey();
                                    Cards cards=cardClassArray.get(position);
                                    DatabaseReference deldata=FirebaseDatabase.getInstance().getReference("AvailableClasses").child(selectedDate).child(cards.getKey());
                                    deldata.removeValue();
                                    DataEntry data=new DataEntry(cards.getTime(),cards.getSubject(),cards.getRoom());
                                    mFirebaseDatabase.child("AcceptedClasses").child(currentUser.getUid()).child(selectedDate).child(id).setValue(data);
                                    Toast.makeText(getBaseContext(), "Class added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });


    }

    public boolean compareUI(DataEntry cardA){
        for(int i=0;i<cancelledClasses.size();i++){
            if(cardA.getTime().equals(cancelledClasses.get(i).getTime()) &&
                    cardA.getSubject().equals(cancelledClasses.get(i).getSubject()) &&
                    cardA.getRoom().equals(cancelledClasses.get(i).getRoom()))
                return false;
        }
        return true;
    }

    public void calendarSubtoCalendar(View view){
        startActivity(new Intent(this,CalenderActivity.class));
    }

    public void calendarSubtoHome(View view){
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void calendarSubtoTable(View view){
        startActivity(new Intent(this,TableActivity.class));
    }

    public void calendarSubtoSettings(View view){
        startActivity(new Intent(this,SettingsActivity.class));
    }

}


