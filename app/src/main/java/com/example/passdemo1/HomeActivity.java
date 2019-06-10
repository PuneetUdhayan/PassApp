package com.example.passdemo1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser currentUser;
    private ListView listV; //change made here
    private String dayOfWeek;
    private String currentDate;
    private NotificationHelper notificationHelper;

    ArrayList<Cards> cardClassArray;
    ArrayList<DataEntry> cancelledClasses;
    ArrayList<Cards> acceptedClasses;
    CustomListAdapter customListAdapter;

    //temp
    private static int flag;
    public static String[][] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notificationHelper=new NotificationHelper(this);

        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase=mFirebaseInstance.getReference();
        final Calendar currentTime = Calendar.getInstance();
        int hrs = currentTime.get(Calendar.HOUR_OF_DAY);//24
        int min = currentTime.get(Calendar.MINUTE);
        Log.d("bobo","test "+hrs+" "+min);
        currentDate= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        if (currentUser==null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

        listV=findViewById(R.id.listView);

        cardClassArray=new ArrayList<Cards>();
        cancelledClasses=new ArrayList<DataEntry>();
        acceptedClasses=new ArrayList<Cards>();

        customListAdapter =new CustomListAdapter(this,R.layout.class_card,cardClassArray);
        listV.setAdapter(customListAdapter);
        loadUserInformation();

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
                            if(useri.getKey().equals(currentUser.getUid())){
                                for(DataSnapshot datei:useri.getChildren()){
                                    if(datei.getKey().equals(currentDate)){
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
                                            int h1=Integer.parseInt(time.substring(6, 8));
                                            int m1=Integer.parseInt(time.substring(9,11));
                                            if (compareUI(k) && lesst(currentTime.get(Calendar.HOUR_OF_DAY),currentTime.get(Calendar.MINUTE),h1,m1)) {
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
                                    if(datei.getKey().equals(currentDate)){
                                        for(DataSnapshot rani:datei.getChildren()){
                                            Log.d("test200",rani.getKey());
                                            room = rani.child("room").getValue(String.class);
                                            subject = rani.child("subject").getValue(String.class);
                                            time = rani.child("time").getValue(String.class);
                                            int h1=Integer.parseInt(time.substring(6, 8));
                                            int m1=Integer.parseInt(time.substring(9,11));
                                            if (lesst(currentTime.get(Calendar.HOUR_OF_DAY),currentTime.get(Calendar.MINUTE),h1,m1)) {
                                                cardClassArray.add(new Cards(time, subject, room, "", rani.getKey(), false));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(outerData.getKey().equals("AvailableClasses")){
                        for(DataSnapshot datei:outerData.getChildren()){
                            Log.d("datecheck",datei.getKey()+currentDate);
                            if(currentDate.equals(datei.getKey())){
                                for(DataSnapshot rani:datei.getChildren()){
                                    room = rani.child("room").getValue(String.class);
                                    subject = rani.child("subject").getValue(String.class);
                                    time = rani.child("time").getValue(String.class);
                                    int h1=Integer.parseInt(time.substring(6, 8));
                                    int m1=Integer.parseInt(time.substring(9,11));
                                    if (lesst(currentTime.get(Calendar.HOUR_OF_DAY),currentTime.get(Calendar.MINUTE),h1,m1)) {
                                        cardClassArray.add(new Cards(time, subject, room, "", rani.getKey(), true));
                                    }
                                }
                            }
                        }
                    }
                }

                //notification here

                for(int i=0;i<cardClassArray.size();i++){
                    Cards temp=cardClassArray.get(i);
                    if (!temp.getAvailable()) {
                        Log.d("cards", temp.getSubject());
                        String notificationTime = temp.getTime();
                        int h1 = Integer.parseInt(notificationTime.substring(0, 2));
                        int m1 = Integer.parseInt(notificationTime.substring(3, 5));
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, h1);
                        cal.set(Calendar.MINUTE, m1);
                        cal.set(Calendar.SECOND, 0);
                        startAlarm(cal,temp.getTime(),temp.getSubject(),i);
                    }
                }

                //notifications end here

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
                    new AlertDialog.Builder(HomeActivity.this)
                            .setMessage("Do you want to cancel the class?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id=mFirebaseDatabase.push().getKey();
                                    Cards cards=cardClassArray.get(position);
                                    DataEntry data=new DataEntry(cards.getTime(),cards.getSubject(),cards.getRoom());
                                    mFirebaseDatabase.child("AvailableClasses").child(currentDate).child(id).setValue(data);
                                    if(cards.getKey().equals("")){
                                        mFirebaseDatabase.child("DeletedClasses").child(currentUser.getUid()).child(currentDate).child(id).setValue(data);
                                    }
                                    else{
                                        DatabaseReference deldatax=FirebaseDatabase.getInstance().getReference("AcceptedClasses").child(currentUser.getUid()).child(currentDate).child(cards.getKey());
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
                    new AlertDialog.Builder(HomeActivity.this)
                            .setMessage("Do you want to accept the class?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id=mFirebaseDatabase.push().getKey();
                                    Cards cards=cardClassArray.get(position);
                                    DatabaseReference deldata=FirebaseDatabase.getInstance().getReference("AvailableClasses").child(currentDate).child(cards.getKey());
                                    deldata.removeValue();
                                    DataEntry data=new DataEntry(cards.getTime(),cards.getSubject(),cards.getRoom());
                                    mFirebaseDatabase.child("AcceptedClasses").child(currentUser.getUid()).child(currentDate).child(id).setValue(data);
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

        /*
        DataEntry data=new DataEntry("21:00-21:15","test1","testroom1");
        DataEntry data2=new DataEntry("22:00-22:15","test2","testroom2");
        mFirebaseDatabase.child("Users").child(currentUser.getUid()).child("Mon").child("c1").setValue(data);
        mFirebaseDatabase.child("Users").child(currentUser.getUid()).child("Mon").child("c2").setValue(data2);
        */
    }

    public void loadUserInformation(){
        FirebaseUser currentu=mAuth.getCurrentUser();
        String username=currentu.getEmail();
        String userid=currentu.getUid();
        TextView a=findViewById(R.id.sarName);
        a.setText(username);
        TextView set_Date=findViewById(R.id.date);
        TextView set_Day=findViewById(R.id.day);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        String date = dateFormat.format(calendar.getTime());
        set_Date.setText(date.substring(4,(date.length()-5)));
        dayOfWeek=date.substring(0,3);
        set_Day.setText(dayOfWeek);

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

    public void startAlarm(Calendar c,String mes1,String mes2,int i){
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(this,AlertReceiver.class);
        intent.putExtra("test", mes1);
        intent.putExtra("testone",mes2);//data to pass
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,flag,intent,i);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        flag++;
    }

    boolean lesst(int h1,int m1, int h2, int m2)
    {
        if (h1<h2)
            return true;
        else if(h1==h2)
        {
            if(m1<=m2)
            {
                return true;
            }
        }
        return false;
    }

    public void homeToCalender(View view){
        startActivity(new Intent(this,CalenderActivity.class));
    }

    public void homeToTable(View view){
        startActivity(new Intent(this,TableActivity.class));
    }

    public void homeToSettings(View view){
        startActivity(new Intent(this,SettingsActivity.class));
    }

}
