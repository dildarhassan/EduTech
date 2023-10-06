package com.example.edutech.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.edutech.R;
import com.example.edutech.databinding.ActivityPhoneLoginBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ActivityPhoneLogin extends AppCompatActivity implements DialogueOtpVerification.DialogInterface {
    ActivityPhoneLoginBinding binding;
    private DialogueOtpVerification dialogue_otp_verification;
    private String mobile_number;
    private String string_otp_id;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_phone_login);


        mAuth=FirebaseAuth.getInstance();

        binding.btnSubmitotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile_number=binding.etPhoneNumber.getText().toString();
                if(mobile_number.isEmpty() ){
                    binding.etPhoneNumber.setError("Phone number is empty.");
                }else if (mobile_number.length()<10){
                    binding.etPhoneNumber.setError("Enter a valid phone number");
                }else {
                    initiate_otp();
                }
            }
        });
    }
    private void initiate_otp() {
        FirebaseAuth.getInstance().getFirebaseAuthSettings()
                .setAppVerificationDisabledForTesting(false);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+mobile_number)                            //Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)                       // Timeout and unit
                        .setActivity(ActivityPhoneLogin.this)                           // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            //getOwnerActivity()
                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                string_otp_id = verificationId;
                                Toast.makeText(ActivityPhoneLogin.this,"OTP send ",Toast.LENGTH_LONG).show();
                                dialogue_otp_verification=new DialogueOtpVerification(ActivityPhoneLogin.this,string_otp_id,mobile_number);
                                dialogue_otp_verification.setCancelable(false);
                                dialogue_otp_verification.setListener(ActivityPhoneLogin.this);
                                dialogue_otp_verification.show();
                            }
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                //signInWithPhoneAuthCredential(phoneAuthCredential);
                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ActivityPhoneLogin.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        })
                        //nVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dialogue_otp_verification.dismiss();
    }

    @Override
    public void onSuccess(Task<AuthResult> task) {
        //start activity
        String userId = Objects.requireNonNull(task.getResult().getUser()).getUid();
        boolean isNew=Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser();
        Intent intent;
        sharedPreferences=getSharedPreferences("MY_FILE",MODE_PRIVATE);
        SharedPreferences.Editor myFile=sharedPreferences.edit();
        myFile.putString("USERID",userId).apply();
        if(isNew) {
            intent = new Intent(ActivityPhoneLogin.this, UserRegisterActivity.class);
            intent.putExtra("Mobile_No", mobile_number);
            intent.putExtra("userId", userId);
        }else {
            intent = new Intent(ActivityPhoneLogin.this, MainActivity.class);// startActivity(new Intent(Activity_otpValidation.this,MainActivity.class));

        }
        startActivity(intent);
        finish();

    }
    @Override
    public void failure(Task<AuthResult> task) {
        Toast.makeText(ActivityPhoneLogin.this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }
}