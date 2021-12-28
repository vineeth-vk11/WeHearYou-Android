package com.wehearyou.LoginUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.ListenerDashboardActivity;
import com.wehearyou.MainActivity;
import com.wehearyou.R;
import com.wehearyou.SeekerDashboardActivity;
import com.wehearyou.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    EditText otp;
    String sentOTP;
    String mobile;

    Button validate;

    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;
    TextView time;

    Button resend;

    String code;

    MixpanelAPI mixpanel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        mixpanel = MixpanelAPI.getInstance(LoginOtpActivity.this, "d1729e123d88f668650b9197c333f789\n");

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        sentOTP = intent.getStringExtra("otp");
        mobile = intent.getStringExtra("phone");

        otp = findViewById(R.id.login_otp_edit);
        validate = findViewById(R.id.validate_button);
        time = findViewById(R.id.textView24);
        resend = findViewById(R.id.resend);

        progressBar = findViewById(R.id.progressBar7);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                validate.setEnabled(false);
                if(TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(LoginOtpActivity.this,"Please enter OTP", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    validate.setEnabled(true);
                }
                else {
                    verifyOTPSign();
                }
            }
        });

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText("Please wait: " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                time.setText("done!");
            }
        }.start();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
                resend.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void sendOTP(){

        String number = "+" + String.valueOf(GetCountryZipCode()) + mobile;
        sentOTP = "";
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(LoginOtpActivity.this,"OTP sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(LoginOtpActivity.this,"Please enter correct Number", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            sentOTP = s;
            Toast.makeText(getApplicationContext(), "OTP resend successful", Toast.LENGTH_SHORT).show();
        }

    };

    private void verifyOTPSign(){
        code = otp.getText().toString();
        Log.i("code",code);
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(sentOTP,code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    redirect();
                }
                else {
                    Toast.makeText(LoginOtpActivity.this, "Please enter correct otp", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    validate.setEnabled(true);
                }
            }
        });
    }

    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    private void redirect(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            AppEventsLogger logger = AppEventsLogger.newLogger(getApplicationContext());
            logger.logEvent("Loggedin");

            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()){

                        Boolean isListener = documentSnapshot.getBoolean("isListener");

                        if(isListener){

                            JSONObject props = new JSONObject();
                            try {
                                props.put("Mobile Number", mobile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mixpanel.track("Listener Logged In", props);

                            Intent intent = new Intent(getApplicationContext(), ListenerDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {

                            JSONObject props = new JSONObject();
                            try {
                                props.put("Mobile Number", mobile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mixpanel.track("Seeker Logged In", props);

                            Intent intent = new Intent(getApplicationContext(), SeekerDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {

                        JSONObject props = new JSONObject();
                        try {
                            props.put("Mobile Number", mobile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mixpanel.track("First Time Login", props);

                        Intent intent = new Intent(getApplicationContext(), LoginDetailsCollectionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}