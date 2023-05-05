package com.example.myapplication.Service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Common.Common;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Common.mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_SHORT).show();
                                    Common.currentUser = Common.mAuth.getCurrentUser();
                                    setResult(RESULT_OK);
                                    finish();
                                    //restartActivity();
                                }   else{
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Failed sign in", Toast.LENGTH_SHORT).show();
                                    Common.currentUser = null;
                                    setResult(RESULT_CANCELED);
                                    finish();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Register.class);
                startActivity(intent);
            }
        });
    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        usernameEditText = view.findViewById(R.id.username);
//        passwordEditText = view.findViewById(R.id.password);
//        loginButton = view.findViewById(R.id.loginButton);
//        signupText = view.findViewById(R.id.signupText);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = usernameEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//                Common.mAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()){
//                                    Log.d(TAG, "signInWithEmail:success");
//                                    Toast.makeText(getActivity(), "Signed In", Toast.LENGTH_SHORT).show();
//                                    Common.currentUser = Common.mAuth.getCurrentUser();
//                                    restartActivity();
//                                }   else{
//                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                    Toast.makeText(getActivity(), "Failed sign in", Toast.LENGTH_SHORT).show();
//                                    Common.currentUser = null;
//                                    //updateUI(null);
//                                }
//                            }
//                        });
//            }
//        });
//        signupText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), Register.class);
//                startActivity(intent);
//            }
//        });
//    }

}