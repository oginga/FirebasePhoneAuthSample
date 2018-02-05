package com.androidmads.firebaseuserauthsample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import Frags.AddPhoneFragment;
import Frags.VerifyPhoneFragment;

/**
 * Created by AJ
 * Created on 09-Jun-17.
 */

public class PhoneAuthActivity extends AppCompatActivity implements AddPhoneFragment.OnFragmentInteractionListener, VerifyPhoneFragment.OnVerifyInteractionListener {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    //Frags
    FragmentManager fragmentManager;
    String telephoneNumber;
    ProgressDialog pDialog;

    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        //Init progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        fragmentManager = getSupportFragmentManager ();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

        AddPhoneFragment phoneFragment=new AddPhoneFragment();

        fragmentTransaction.add(R.id.fragment_container,phoneFragment,"phone");
        fragmentTransaction.commit ();


        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                hideDialog();
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideDialog();
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    Toast.makeText(getApplicationContext(),"Invalid phone number",Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                hideDialog();
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;

                //replace fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();

                VerifyPhoneFragment verifyPhoneFragment=new VerifyPhoneFragment();

                fragmentTransaction.add(R.id.fragment_container,verifyPhoneFragment,"Verify");

//                fragmentTransaction.replace(R.id.fragment_container,verifyPhoneFragment,"verify");
                fragmentTransaction.commit ();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Invalid code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        telephoneNumber=phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        hideDialog();
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        hideDialog();
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // TO enable verify fragment go back to add phone fragment when back button is pressed
        // TO-DO
        int count = getFragmentManager().getBackStackEntryCount();
        Log.e(TAG,"--Back pressed");
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    private void showDialog() {
        Log.e("DIALOG","==show dialog called");
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onVerifyFragmentInteraction(String code) {

        if (code.equals("resend") && !telephoneNumber.isEmpty()){
            pDialog.setMessage("Resending...");
            showDialog();
            resendVerificationCode(telephoneNumber, mResendToken);
        }else{
            pDialog.setMessage("Verifying...");
            showDialog();
            verifyPhoneNumberWithCode(mVerificationId,code);
        }

    }

    @Override
    public void onFragmentInteraction(String msg,String phone) {
        switch (msg){
            case "init_auth":
                pDialog.setMessage("Autheticating...");
                showDialog();
                startPhoneNumberVerification(phone);
                break;
        }
    }
}
