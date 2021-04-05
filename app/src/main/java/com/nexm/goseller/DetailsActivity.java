package com.nexm.goseller;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.nexm.goseller.fragments.AddNewProductFragment;
import com.nexm.goseller.fragments.UpdateFragment;

public class DetailsActivity extends AppCompatActivity
        implements AddNewProductFragment.OnFragmentInteractionListener,
                    UpdateFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final String caller = getIntent().getStringExtra("CALLER");
        if(caller.matches("New")){
            AddNewProductFragment addNewProductFragment = AddNewProductFragment.newInstance(getIntent().getStringExtra("CALLER"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailActivity_fragHolder,addNewProductFragment)
                    .commit();
        }else if(caller.matches("update")){
            UpdateFragment fragment = UpdateFragment.newInstance(getIntent().getStringExtra("KEY"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailActivity_fragHolder,fragment)
                    .commit();
        }else{
            AddNewProductFragment addNewProductFragment = AddNewProductFragment.newInstance(getIntent().getStringExtra("KEY"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailActivity_fragHolder,addNewProductFragment)
                    .commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onUpdateSelection(Uri uri) {

    }
}
