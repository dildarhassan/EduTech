package com.example.edutech.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.edutech.Model_Activity.UserDataModel;
import com.example.edutech.R;
import com.example.edutech.databinding.ActivityUserRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UserRegisterActivity extends AppCompatActivity  implements DialogueOtpVerification.DialogInterface  {
    ActivityUserRegisterBinding binding;
    private SharedPreferences sharedPreferences;
    private DialogueOtpVerification dialogue_otp_verification;
    private String mobileNo;
    private String password;
    private    String userId;
    private DatabaseReference mDatabase;
    private String Name,email;


    private String mobile_number;
    private FirebaseAuth mAuth;

    public UserRegisterActivity() {
    }
// ...
// Initialize Firebase Auth



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_register);
        mobileNo=getIntent().getStringExtra("Mobile_No");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        userId=getIntent().getStringExtra("userId");
        mobile_number=getIntent().getStringExtra("Mobile_No");

        if (userId!=null){
            binding.etMobNo.setText(mobile_number);
            binding.etMobNo.setFocusable(false);}

        //check all field are correctly filled or not
        binding.btRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobileNo=binding.etMobNo.getText().toString();
                Name=binding.etRegName.getText().toString();
                email=binding.etRegEmail.getText().toString();
                password=binding.etRegPassword.getText().toString();

                if(Name.isEmpty()){
                    binding.etRegName.setError("Filed is empty");
                }else if(email.isEmpty()){
                    binding.etRegEmail.setError("Field is empty");
                }else if(mobileNo.isEmpty()){
                    binding.etMobNo.setError("Field is empty");
                }else if(password.isEmpty()){
                    binding.etRegPassword.setError("Field is empty");
                }else{
                    if (userId!=null){

                        signUpUser(email,password,userId);

                    }else {
                        initiate_otp();
                    }
                    //calling method for user registration
                    
                    //signUpUser(email,binding.etRegPassword.getText().toString());

                    // registerUser(userId,Name,email,mobileNo);

                }
            }
        });
        //If already have An account
        binding.tvSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegisterActivity.this,LogInActivity.class));
            }
        });
    }

    //Register User


    public void onBackPressed(){
        super.onBackPressed();
    }



    private void initiate_otp() {
        FirebaseAuth.getInstance().getFirebaseAuthSettings()
                .setAppVerificationDisabledForTesting(false);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+mobileNo)                            //Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)                       // Timeout and unit
                        .setActivity(UserRegisterActivity.this)                           // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            //getOwnerActivity()
                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Toast.makeText(UserRegisterActivity.this,"OTP send ",Toast.LENGTH_LONG).show();
                                dialogue_otp_verification=new DialogueOtpVerification(UserRegisterActivity.this, verificationId,mobileNo);
                                dialogue_otp_verification.setCancelable(false);
                                dialogue_otp_verification.setListener(UserRegisterActivity.this);
                                dialogue_otp_verification.show();

                            }
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                //signInWithPhoneAuthCredential(phoneAuthCredential);
                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(UserRegisterActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        })
                        //nVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    @Override
    public void onSuccess(Task<AuthResult> task) {
        userId = Objects.requireNonNull(task.getResult().getUser()).getUid();
        sharedPreferences=getSharedPreferences("MY_FILE",MODE_PRIVATE);
        SharedPreferences.Editor myFile=sharedPreferences.edit();
        myFile.putString("USERID",userId).apply();
        Toast.makeText(UserRegisterActivity.this, "otp verification completed"+userId, Toast.LENGTH_SHORT).show();
        signUpUser(email,password,userId);

    }

    @Override
    public void failure(Task<AuthResult> task) {

    }
    private  void signUpUser(String email,String password,String userId){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserRegisterActivity.this, "signup completed", Toast.LENGTH_SHORT).show();
                    registerUser(userId,Name,email,mobileNo);
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void registerUser(String userId,String user_firstName,String userEmail, String userMobile_no){
        UserDataModel dataModel=new UserDataModel(user_firstName,userEmail,userMobile_no);
        mDatabase.child("USERS").child(userId).setValue(dataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UserRegisterActivity.this, "registered user", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserRegisterActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
