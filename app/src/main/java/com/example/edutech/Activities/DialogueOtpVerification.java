package com.example.edutech.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.edutech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class DialogueOtpVerification extends Dialog {

    private EditText et_Otp1,et_Otp2,et_Otp3,et_Otp4,et_Otp5,et_Otp6;
    private TextView tv_resend_otp;
    private Button bt_verify_otp;

    String string_otp_id;

    private boolean resendEnabled;
    private int resendTime;
    private int selectedETposition;
    private final String mobile_number;
    private String otp_code;

    public DialogueOtpVerification(@NonNull Context context, String verificationsID, String mobile_number) {
        super(context);
        this.mobile_number = mobile_number;
        string_otp_id=verificationsID;
    }
    private DialogInterface dialogInterface;
    public void setListener(DialogInterface dInterface){
        this.dialogInterface = dInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue_otp_verification);

        et_Otp1=(EditText) findViewById(R.id.et_otp1);
        et_Otp2=(EditText)findViewById(R.id.et_otp2);
        et_Otp3=(EditText)findViewById(R.id.et_otp3);
        et_Otp4=(EditText)findViewById(R.id.et_otp4);
        et_Otp5=(EditText)findViewById(R.id.et_otp5);
        et_Otp6=(EditText)findViewById(R.id.et_otp6);
        tv_resend_otp=(TextView) findViewById(R.id.tv_resend_btn);
        bt_verify_otp=(Button) findViewById(R.id.bt_verifyBtn);

        TextView tv_mobile_number1 = (TextView) findViewById(R.id.mobileNumber);

        tv_mobile_number1.setText(mobile_number);

        //resend otp time in second
        resendTime = 60;

        //will be true in 60 second
        resendEnabled = false;

        selectedETposition =0;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent)));

        et_Otp1.addTextChangedListener(textWatcher);
        et_Otp2.addTextChangedListener(textWatcher);
        et_Otp3.addTextChangedListener(textWatcher);
        et_Otp4.addTextChangedListener(textWatcher);
        et_Otp5.addTextChangedListener(textWatcher);
        et_Otp6.addTextChangedListener(textWatcher);

        //by default show keyboard on
        showKeyboard(et_Otp1);

        //start count down timer
        startCountDownTimer();
        tv_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnabled){
                    startCountDownTimer();
                    //OTP RESEND
                }
            }
        });
        bt_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp_code=et_Otp1.getText().toString()+et_Otp2.getText().toString()+et_Otp3.getText().toString()+et_Otp4.getText().toString()+et_Otp5.getText().toString()+et_Otp6.getText().toString();
                submit_otp();
            }
        });
    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {


            if(editable.length()>0){
                if(selectedETposition==0){
                    //select next edit text
                    selectedETposition=1;
                    showKeyboard(et_Otp2);
                }
                else if (selectedETposition == 1){
                    //select next edit text
                    selectedETposition=2;
                    showKeyboard(et_Otp3);
                }
                else if (selectedETposition == 2){
                    //select next edit text
                    selectedETposition=3;
                    showKeyboard(et_Otp4);
                }
                else if (selectedETposition == 3){
                    //select next edit text
                    selectedETposition=4;
                    showKeyboard(et_Otp5);
                }
                else if (selectedETposition == 4){
                    //select next edit text
                    selectedETposition=5;
                    showKeyboard(et_Otp6);
                }
                else{
                    //bt_verify_otp.setBackgroundColor(R.drawable.round_black_brown_100);
                    bt_verify_otp.setBackgroundResource(R.drawable.round_black_red_100);
                }
               //
            }
        }
    };
    private void showKeyboard(EditText etOtp){
        etOtp.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(etOtp, InputMethodManager.SHOW_IMPLICIT);
    }
    public void startCountDownTimer(){
        resendEnabled=false;
        tv_resend_otp.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime* 1000L, 1000 ){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                tv_resend_otp.setText("Resend code ("+(l/1000)+")");

            }
            @Override
            public void onFinish() {
                resendEnabled=true;
                tv_resend_otp.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            }
        }.start();
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_DEL){

            if (selectedETposition == 5){
                //selected previous Edit text
                selectedETposition=4;
                showKeyboard(et_Otp5);
            }
            else if(selectedETposition == 4){

                //selected previous Edit text
                selectedETposition=3;
                showKeyboard(et_Otp4);

            }
            else if(selectedETposition == 3){

                //selected previous Edit text
                selectedETposition=2;
                showKeyboard(et_Otp3);

            }
            else if(selectedETposition == 2){

                //selected previous Edit text
                selectedETposition=1;
                showKeyboard(et_Otp2);

            }
            else if(selectedETposition == 1){

                //selected previous Edit text
                selectedETposition=0;
                showKeyboard(et_Otp1);

            }

            //set color
            bt_verify_otp.setBackgroundResource(R.drawable.round_black_brown_100);

            return true;
        }
        else{
            return super.onKeyUp(keyCode, event);
        }
    }
    public void submit_otp(){
        if(otp_code.length()==6){
            //handle Otp verification process
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(string_otp_id,otp_code);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(dialogInterface == null){
                                dismiss();
                                return;
                            }
                            if (task.isSuccessful()) {
                                dialogInterface.onSuccess(task);
                            } else {
                                dialogInterface.failure(task);
                            }
                            dismiss();
                        }
                    });
        }
    }
    public interface DialogInterface{
        void onSuccess(Task<AuthResult> task);
        void failure(Task<AuthResult> task);
    }
}