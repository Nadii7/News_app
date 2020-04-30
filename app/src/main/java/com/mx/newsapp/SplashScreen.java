package com.mx.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG ="" ;
    private Button google;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=0;
    private ProgressBar bar;
    private de.hdodenhof.circleimageview.CircleImageView circle_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        bar = findViewById(R.id.progressBar);
        circle_bar = findViewById(R.id.bar);
        google=findViewById(R.id.google_sign);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_sign:
                        GoogleLog();
                        break;
                }
            }
        });

    }

    private void GoogleLog() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            StartUi();
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void StartUi(){
        bar.setVisibility(View.VISIBLE);
        circle_bar.setVisibility(View.VISIBLE);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });thread.start();
    }

    @Override
    protected void onStart() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!= null){
            StartUi();
        }
        super.onStart();
    }

}
