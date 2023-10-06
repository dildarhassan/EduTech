package com.example.edutech.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.edutech.R;
import com.example.edutech.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogInActivity extends AppCompatActivity {
    ActivityLogInBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_log_in);
        // Initialize Firebase Auth
          mAuth = FirebaseAuth.getInstance();
//        onStart();


        binding.tvPhoneLogin.setOnClickListener(view -> {
            Intent intent=new Intent(LogInActivity.this, ActivityPhoneLogin.class);
            startActivity(intent);
        });

        binding.tvSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,UserRegisterActivity.class));
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_id=binding.etEmail.getText().toString();
                String password=binding.etPassword.getText().toString();
                if (email_id.isEmpty() && password.isEmpty()){
                    binding.etEmail.setError("Email is empty");
                    binding.etPassword.setError("Password is empty");
                }else {
                    signIn(email_id,password);
                }
            }
        });
    }
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent intent=new Intent(LogInActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LogInActivity.this, "Email or Password mismatched",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }


    private void updateUI(FirebaseUser user) {
        mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if (currentUser!=null){
            // String userId= currentUser.getUid();
            //  startActivity(new Intent(Activity_Login.this,MainActivity.class));
            Intent intent=new Intent(LogInActivity.this,NavigationButtons.class);
            //intent.putExtra("userId",userId);
            startActivity(intent);

            // mAuth.updateCurrentUser(currentUser);
        }
        // Check if user is signed in (non-null) and update UI accordingly.
    }

}