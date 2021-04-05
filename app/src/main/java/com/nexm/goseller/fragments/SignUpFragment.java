package com.nexm.goseller.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nexm.goseller.R;
import com.nexm.goseller.Seller;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView createAcTab,saveProfileTab,namefield,addressfield,moNofield,altmoNofield,emailfield;
    private RadioGroup radioGroup;
    private LinearLayout createAcForm;
    private ScrollView saveProfileForm;
    private EditText createAcmobileNo,createAcpin,createAcpin2;
    private Button createAcButton,signupButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String uid,name,address,moNo,moNo2,acType,email;
    private Seller seller;

    private OnFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Go Seller - Sign up");
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        createAcTab = view.findViewById(R.id.signup_createAccountTab);
        saveProfileTab = view.findViewById(R.id.signup_saveProfileTab);
        createAcForm = view.findViewById(R.id.signup_createAccountForm);
        saveProfileForm = view.findViewById(R.id.signup_createProfileForm);
        createAcTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAcTab.setTypeface(null, Typeface.BOLD);
                createAcTab.setTextColor(getResources().getColor(android.R.color.background_light));
                createAcForm.setVisibility(View.VISIBLE);
                saveProfileTab.setTypeface(null, Typeface.NORMAL);
                saveProfileTab.setTextColor(getResources().getColor(R.color.orange2));
                saveProfileForm.setVisibility(View.GONE);

            }
        });
        saveProfileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAcTab.setTypeface(null, Typeface.NORMAL);
                createAcTab.setTextColor(getResources().getColor(R.color.orange2));
                createAcForm.setVisibility(View.GONE);
                saveProfileTab.setTypeface(null, Typeface.BOLD);
                saveProfileTab.setTextColor(getResources().getColor(android.R.color.background_light));
                saveProfileForm.setVisibility(View.VISIBLE);

            }
        });
        createAcmobileNo = view.findViewById(R.id.signup_emailInput);
        createAcpin = view.findViewById(R.id.signup_pinInput);
        createAcpin2 = view.findViewById(R.id.signup_confirmPinInput);
        createAcButton = view.findViewById(R.id.signup_createAcButton);
        radioGroup = view.findViewById(R.id.signup_radioGroup);
        progressBar = view.findViewById(R.id.signup_progressbar);
        createAcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                String email = createAcmobileNo.getText().toString();
                email = email+"@domain.com";
                String password = createAcpin.getText().toString();
                password = password+"@go";
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null) uid = user.getUid();
                                    createAcTab.setTypeface(null, Typeface.NORMAL);
                                    createAcTab.setTextColor(getResources().getColor(android.R.color.background_dark));
                                    createAcForm.setVisibility(View.GONE);
                                    saveProfileTab.setTypeface(null, Typeface.BOLD);
                                    saveProfileTab.setTextColor(getResources().getColor(R.color.orange2));
                                    saveProfileForm.setVisibility(View.VISIBLE);

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });

            }
        });
        signupButton = view.findViewById(R.id.signup_saveProfileButton);
        namefield = view.findViewById(R.id.signup_nameInput);
        addressfield = view.findViewById(R.id.signup_addressInput);
        moNofield = view.findViewById(R.id.signup_monoInput);
        altmoNofield = view.findViewById(R.id.signup_monoAltInput);
        emailfield = view.findViewById(R.id.signup_emailInput1);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        acType = ((RadioButton)view.findViewById(checkedId)).getText().toString();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                progressBar.setVisibility(View.VISIBLE);
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(checkedId);
                acType = radioButton.getText().toString();
                if (!validateProfile()) {
                    return;
                }
                seller = new Seller();
                assignSeller();
                FirebaseDatabase.getInstance().getReference()
                        .child("Sellers")
                        .child(uid)
                        .setValue(seller)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.GONE);
                                showSuccessDialog();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                showFailureDialog();
                            }
                        });
            }
        });
        return view;
    }

    private void showFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Oops....!")
                .setMessage("Something went wrong .... Please try again\n\n" +
                        "If problem persists contact Ghatanji Online customer care(7775971543) ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Success !")
                .setMessage("Seller Account is successfully created")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final SharedPreferences preferences = getActivity().getSharedPreferences("USER",MODE_PRIVATE);
                        final SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Name",seller.getName());
                        editor.putString("MoNo",createAcmobileNo.getText().toString());
                        editor.apply();

                        if(mListener != null)
                            mListener.onSignUpSelection();
                    }
                });
        builder.show();
    }

    private void assignSeller() {
        seller.setSellerId(uid);
        seller.setAccount(acType);
        seller.setAddress(address);
        seller.setEmail(email);
        seller.setIdUrl("x");
        seller.setPhotoUrl("x");
        seller.setMoNo(moNo);
        seller.setMoNoSecond(moNo2);
        seller.setName(name);
        seller.setProducts(0);
        //seller.setProductsLimit(3);
        Date date = new Date();
        long today = date.getTime();
        seller.setJoinDate(today);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,30);
        date = calendar.getTime();
        long validity = date.getTime();
        seller.setPlanExpiry(validity);
        int productsLimit = 0;
        int planAmount = 0;
        switch (acType){
            case "Basic : FREE : Max 3 products":productsLimit = 3;planAmount=0; break;
            case "Silver : Rs. 100 / month : 10 products":productsLimit = 10;planAmount=120; break;
            case "Gold : Rs. 300 / month : 25 products":productsLimit = 25;planAmount=300; break;
            case "Platinum : Rs. 450 / month : 50 products":productsLimit = 50;planAmount=450; break;
            case "Diamond : Rs. 900 / month : 500 products":productsLimit = 500;planAmount=900; break;
        }
        seller.setProductsLimit(productsLimit);
        seller.setPlanAmount(planAmount);
    }

    private boolean validateProfile(){
        boolean valid = true;
        name = namefield.getText().toString();
        if (TextUtils.isEmpty(name)) {
            namefield.setError("Enter name.");
            valid = false;
        } else {
            namefield.setError(null);
        }
        address = addressfield.getText().toString();
        if (TextUtils.isEmpty(address)) {
            addressfield.setError("Enter address.");
            valid = false;
        } else {
            addressfield.setError(null);
        }
        email = emailfield.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailfield.setError("Enter email address.");
            valid = false;
        } else {
            emailfield.setError(null);
        }
        moNo = moNofield.getText().toString();
        if(moNo != null)moNo.trim();
        if (TextUtils.isEmpty(moNo) || moNo.length()!=10) {
            moNofield.setError("Enter proper mobile number.");
            valid = false;
        } else {
            moNofield.setError(null);
        }
        moNo2 = altmoNofield.getText().toString();
        if(moNo2 != null)moNo2.trim();
        if (TextUtils.isEmpty(moNo2) || moNo2.length()!=10) {
            altmoNofield.setError("Enter proper mobile number.");
            valid = false;
        } else {
            altmoNofield.setError(null);
        }

        return valid;
    }
    private boolean validateForm() {
        boolean valid = true;


        String email = createAcmobileNo.getText().toString();
        if (TextUtils.isEmpty(email)) {
            createAcmobileNo.setError("Required.");
            valid = false;
        } else {
            createAcmobileNo.setError(null);
        }

        String password = createAcpin.getText().toString();
        if (TextUtils.isEmpty(password)) {
            createAcpin.setError("Required.");
            valid = false;
        } else {
            createAcpin.setError(null);
        }

        String password2 = createAcpin2.getText().toString();
        if (TextUtils.isEmpty(password)) {
            createAcpin2.setError("Confirm pin");
            valid = false;
        } else {
            createAcpin2.setError(null);
        }
        if(!password.matches(password2)){
            createAcpin2.setError("pin does not match");
        }else{
            createAcpin2.setError(null);
        }

        return valid;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSignUpSelection();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignUpSelection();
    }
}
