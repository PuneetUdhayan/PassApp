package com.example.passdemo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calendarView=findViewById(R.id.calenderView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selected = Calendar.getInstance();
                selected.set(year,month,dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
                String day = dateFormat.format(selected.getTime());
                String currentDate= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String date= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected.getTime());
                if(dateCompare(currentDate,date)){
                    calendarToCalendarSub(day,date);
                }
                else{
                    Toast.makeText(getBaseContext(), "Why worry about the past.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void calendarToCalendarSub(String a,String b){
        Intent i = new Intent(this, CalendarSubActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("daysent", a);
        bundle.putString("datesent",b);
        i.putExtras(bundle);
        startActivity(i);
    }

    public boolean dateCompare(String a,String b){
        if(Integer.parseInt(a.substring(0,4))>Integer.parseInt(b.substring(0,4)))
            return false;
        else if(Integer.parseInt(a.substring(5,7))>Integer.parseInt(b.substring(5,7)))
            return false;
        else if(Integer.parseInt(a.substring(8,10))>Integer.parseInt(b.substring(8,10)))
            return false;
        return true;
    }

    public void calenderToHome(View view){
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void calenderToTable(View view){
        startActivity(new Intent(this,TableActivity.class));
    }

    public void calenderToSettings(View view){
        startActivity(new Intent(this,SettingsActivity.class));
    }
}
