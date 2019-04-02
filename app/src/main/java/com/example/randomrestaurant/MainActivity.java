package com.example.randomrestaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Register";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseUsers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseApp.initializeApp(this);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening lost activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);


        databaseUsers = FirebaseDatabase.getInstance().getReference("/");

    }

    private void registerUser() {

        String email = editTextEmail.getText().toString().trim();
        final String username = email;
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final String uid = user.getUid();
        final List<String> restaurants = new ArrayList<>();
//        restaurants.add("DummyDataToRemove");
        String password = editTextPassword.getText().toString().trim();


        if( TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = user.getUid();
                    User CurrentUser = new User(uid, username, restaurants);
                    databaseUsers.child(uid).setValue(CurrentUser);
                }else{
                    //display some message here
                    Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view){
        if(view == buttonSignup){
            registerUser();
        }
        if(view == textViewSignin){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


}
