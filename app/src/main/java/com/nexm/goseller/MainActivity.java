package com.nexm.goseller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.nexm.goseller.fragments.SignInFragment;
import com.nexm.goseller.fragments.SignUpFragment;

public class MainActivity extends AppCompatActivity implements
        SignInFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseApp.initializeApp(getApplicationContext());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.signActivity_fragholder,new SignInFragment())
                .commit();
    }

    @Override
    public void onSignInSelection() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.signActivity_fragholder,new SignUpFragment())
                .commit();
    }

    @Override
    public void onSignUpSelection() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.signActivity_fragholder,new SignInFragment())
                .commit();
    }
}
