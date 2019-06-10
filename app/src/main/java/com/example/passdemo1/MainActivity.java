package com.example.passdemo1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
        }
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    finish();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(getBaseContext(),"login unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signInButton(View view){
        EditText ed=findViewById(R.id.editTextEmail);
        EditText pa=findViewById(R.id.editTextPass);
        if(ed.getText().toString().trim().length() == 0 || pa.getText().toString().trim().length() == 0){
            Toast.makeText(getBaseContext(),"Please fill credentials",Toast.LENGTH_SHORT).show();
            return;
        }
        signIn(ed.getText().toString(),pa.getText().toString());
    }
}
