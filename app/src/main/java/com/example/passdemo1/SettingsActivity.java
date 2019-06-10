package com.example.passdemo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public void settingsSignOut(View view){
        finish();
        mAuth.signOut();
        startActivity(new Intent(this,MainActivity.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser bob=mAuth.getCurrentUser();
        TextView email=findViewById(R.id.settingEmail);
        email.setText(bob.getEmail());
    }

    public void settingsToHome(View view){
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void settingsToCalender(View view){
        startActivity(new Intent(this,CalenderActivity.class));
    }

    public void settingsToTable(View view){
        startActivity(new Intent(this,TableActivity.class));
    }
}
