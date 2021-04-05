package com.nexm.goseller.ui.notifications;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexm.goseller.GO_SELLER_APPLICATION;
import com.nexm.goseller.R;
import com.nexm.goseller.Seller;
import com.nexm.goseller.models.Payment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private TextView name,products,orders,ratings,joindate,plan,rechargedate,
                    rechargeButton,changePlanButton,address,mono,email,editButton,expiry;
    private  Seller seller;
    private int availableBalance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        seller = new Seller();
        initiateViews(root);
        setInitialData();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
        rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRechargeDialog();
            }
        });
        changePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlans();
            }
        });
        return root;
    }

    private void showPlans() {
        final Dialog dialog;

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.plan_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        final ImageView close = dialog.findViewById(R.id.plan_dialog_close);
        final TextView currentPlan = dialog.findViewById(R.id.plan_dialog_currentplan);
        final TextView balance = dialog.findViewById(R.id.plan_dialog_balance);
        final TextView submit = dialog.findViewById(R.id.plan_dialog_submit);
        final RadioGroup radioGroup = dialog.findViewById(R.id.plan_dialog_radiogroup);
        final ProgressBar progressBar = dialog.findViewById(R.id.plandialog_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        currentPlan.setText(seller.getAccount());
        GO_SELLER_APPLICATION.reference
                .child("Payments")
                .child(GO_SELLER_APPLICATION.sellerID)
                .child("Balance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if(dataSnapshot.exists()){
                            availableBalance = dataSnapshot.getValue(Integer.class);
                            balance.setText("₹."+availableBalance);

                        }else{
                            balance.setText("₹.0000");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        balance.setText("₹.0000");
                    }
                });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(checkedId);
                final String acType = radioButton.getText().toString();
                int productsLimit = 0;
                int planAmount = 0;
                switch (acType){
                    case "Basic : FREE : Max 3 products":productsLimit = 3;planAmount=0; break;
                    case "Silver : Rs. 100 / month : 10 products":productsLimit = 10;planAmount=120; break;
                    case "Gold : Rs. 300 / month : 25 products":productsLimit = 25;planAmount=300; break;
                    case "Platinum : Rs. 450 / month : 50 products":productsLimit = 50;planAmount=450; break;
                    case "Diamond : Rs. 900 / month : 500 products":productsLimit = 500;planAmount=900; break;
                }
                final int finalProductsLimit = productsLimit;

                final long remainingValiditity = seller.getPlanExpiry() - new Date().getTime();
                final double x = 24*60*60*1000;
                final int remainingDays = (int) (remainingValiditity/x);
                final int y = seller.getPlanAmount()/30;
                final int remainingAmount = remainingDays * y;
                final int finalPlanAmount = planAmount-remainingAmount;
                final int pamount = planAmount;
                if(acType.matches(seller.getAccount())){
                    progressBar.setVisibility(View.GONE);
                }else{
                    if(availableBalance == 0|| availableBalance<finalPlanAmount){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"Plan can not be changed because " +
                                "of insufficient balance.",Toast.LENGTH_SHORT).show();
                    }else{
                        seller.setAccount(acType);
                        seller.setProductsLimit(finalProductsLimit);
                        seller.setPlanAmount(pamount);
                        Date date = new Date();
                        long today = date.getTime();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,30);
                        date = calendar.getTime();
                        long validity = date.getTime();
                        seller.setPlanExpiry(validity);

                        Payment payment = new Payment();
                        payment.setDate(today);
                        payment.setAmount(finalPlanAmount);
                        payment.setDescription("RECHARGE");

                        String debitKey = GO_SELLER_APPLICATION.reference
                                .child("Payments")
                                .child(GO_SELLER_APPLICATION.sellerID)
                                .child("Debits")
                                .push()
                                .getKey();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/Sellers/"+GO_SELLER_APPLICATION.sellerID,seller);
                        childUpdates.put("/Payments/"+GO_SELLER_APPLICATION.sellerID+"/Balance",availableBalance-finalPlanAmount);
                        childUpdates.put("/Payments/"+GO_SELLER_APPLICATION.sellerID+"/Debits/"+debitKey,payment);

                        GO_SELLER_APPLICATION.reference.updateChildren(childUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if(task.isSuccessful()){
                                            Toast.makeText(getActivity(),"Plan changed successfully",Toast.LENGTH_SHORT).show();
                                            setInitialData();

                                        }else {
                                            Toast.makeText(getActivity(),"Plan change unsuccessful - try again",Toast.LENGTH_SHORT).show();
                                            availableBalance+=finalPlanAmount;
                                        }
                                    }
                                });

                    }
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showRechargeDialog() {
        final Dialog dialog;

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recharge_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        final TextView plan = dialog.findViewById(R.id.recharge_plan);
        final TextView validity = dialog.findViewById(R.id.recharge_validity);
        final TextView amount = dialog.findViewById(R.id.recharge_amount);
        final TextView balance = dialog.findViewById(R.id.recharge_balance);
        final TextView recharge = dialog.findViewById(R.id.recharge_recharge);
        final ImageView close = dialog.findViewById(R.id.recharge_close);
        final TextView change = dialog.findViewById(R.id.recharge_change_plan);
        final ProgressBar progressBar = dialog.findViewById(R.id.recharge_progressBar);

        plan.setText(seller.getAccount());
        amount.setText("₹."+seller.getPlanAmount());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        GO_SELLER_APPLICATION.reference
                .child("Payments")
                .child(GO_SELLER_APPLICATION.sellerID)
                .child("Balance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if(dataSnapshot.exists()){
                             availableBalance = dataSnapshot.getValue(Integer.class);
                            balance.setText("₹."+availableBalance);

                        }else{
                            balance.setText("₹.0000");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        balance.setText("₹.0000");
                    }
                });
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(availableBalance == 0 || availableBalance<seller.getPlanAmount()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"Insufficient balance - contact GO office-1",Toast.LENGTH_SHORT).show();

                }else{
                    availableBalance-=seller.getPlanAmount();
                    makePlanUpdates(availableBalance,recharge,progressBar);
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void recharge() {

        GO_SELLER_APPLICATION.reference
                .child("Payments")
                .child(GO_SELLER_APPLICATION.sellerID)
                .child("Balance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            int availableBalance = dataSnapshot.getValue(Integer.class);
                            if(availableBalance == 0 || availableBalance<seller.getPlanAmount()){
                                Toast.makeText(getActivity(),"Insufficient balance - contact GO office-1",Toast.LENGTH_SHORT).show();
                            }else{
                                availableBalance-=seller.getPlanAmount();
                               // makePlanUpdates(availableBalance, recharge);
                            }
                        }else{
                            Toast.makeText(getActivity(),"Insufficient balance - contact GO office-2",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void makePlanUpdates(int newBalance, final TextView recharge, final ProgressBar progressBar) {
        Date date = new Date();
        long today = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,30);
        date = calendar.getTime();
        long validity = date.getTime();
        seller.setPlanExpiry(validity);

        Payment payment = new Payment();
        payment.setDate(today);
        payment.setAmount(seller.getPlanAmount());
        payment.setDescription("RECHARGE");

        String debitKey = GO_SELLER_APPLICATION.reference
                            .child("Payments")
                            .child(GO_SELLER_APPLICATION.sellerID)
                            .child("Debits")
                            .push()
                            .getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Sellers/"+GO_SELLER_APPLICATION.sellerID+"/planExpiry",validity);
        childUpdates.put("/Payments/"+GO_SELLER_APPLICATION.sellerID+"/Balance",newBalance);
        childUpdates.put("/Payments/"+GO_SELLER_APPLICATION.sellerID+"/Debits/"+debitKey,payment);

        GO_SELLER_APPLICATION.reference.updateChildren(childUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Recharge successful",Toast.LENGTH_SHORT).show();
                            setInitialData();
                            recharge.setText("Recharge successfull !");
                            recharge.setEnabled(false);
                        }else {
                            Toast.makeText(getActivity(),"Recharge unsuccessful - try again",Toast.LENGTH_SHORT).show();
                            availableBalance+=seller.getPlanAmount();
                        }
                    }
                });

    }

    private void setInitialData() {
        GO_SELLER_APPLICATION.reference
                .child("Sellers")
                .child(GO_SELLER_APPLICATION.sellerID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            seller = dataSnapshot.getValue(Seller.class);
                            name.setText(seller.getName());
                            products.setText(Integer.toString(seller.getProducts()));

                            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");

                            String s = formater.format(new Date(seller.getJoinDate()));
                            joindate.setText(s);
                            plan.setText(seller.getAccount());
                            String e = formater.format(new Date(seller.getPlanExpiry()));
                            expiry.setText(e);

                            setLastRecharge();

                            address.setText(seller.getAddress());
                            email.setText(seller.getEmail());
                            mono.setText(seller.getMoNo()+" , "+seller.getMoNoSecond());

                        }else{
                            Toast.makeText(getActivity(),"Something went wrong , try again !",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(),"Something went wrong , try again !",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setLastRecharge() {
        GO_SELLER_APPLICATION.reference
                .child("Payments")
                .child(GO_SELLER_APPLICATION.sellerID)
                .child("Debits")
                .orderByChild("description")
                .equalTo("RECHARGE")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                Payment payment = data.getValue(Payment.class);
                                SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
                                String s = formater.format(new Date(payment.getDate()));
                                rechargedate.setText("₹."+payment.getAmount()+" - "+s);
                            }

                        }else{
                            rechargedate.setText("No record found !1");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        rechargedate.setText("No record found !2");
                    }
                });
    }

    private void showEditDialog() {
        final Dialog dialog;

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_profile_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        final EditText emailE = dialog.findViewById(R.id.editProfile_email);
        final EditText addressE = dialog.findViewById(R.id.editProfile_address);
        final EditText monoE = dialog.findViewById(R.id.editProfile_mono);
        final EditText mono2E = dialog.findViewById(R.id.editProfile_mono2);
        final TextView submit = dialog.findViewById(R.id.editProfile_submit);
        final ImageView close = dialog.findViewById(R.id.editProfile_closeicon);
        final ProgressBar progressBar = dialog.findViewById(R.id.editProfile_progressBar);

        emailE.setText(email.getText().toString());
        addressE.setText(address.getText().toString());
        String[] n = mono.getText().toString().split(",");
        monoE.setText(n[0]);
        mono2E.setText(n[1]);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                seller.setEmail(emailE.getText().toString());
                seller.setAddress(addressE.getText().toString());
                seller.setMoNo(monoE.getText().toString());
                seller.setMoNoSecond(mono2E.getText().toString());
                GO_SELLER_APPLICATION.reference
                        .child("Sellers")
                        .child(GO_SELLER_APPLICATION.sellerID)
                        .setValue(seller)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(),"Something went wrong , try again !",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    private void initiateViews(View root) {
        name = root.findViewById(R.id.profile_name);
        products = root.findViewById(R.id.profile_products);
        orders = root.findViewById(R.id.profile_orders);
        ratings = root.findViewById(R.id.profile_ratings);
        joindate = root.findViewById(R.id.profile_join);
        plan = root.findViewById(R.id.profile_plan);
        rechargedate = root.findViewById(R.id.profile_last_recharge);
        rechargeButton = root.findViewById(R.id.profile_recharge);
        changePlanButton = root.findViewById(R.id.profile_change_plan);
        address = root.findViewById(R.id.profile_address);
        email = root.findViewById(R.id.profile_email);
        mono = root.findViewById(R.id.profile_mono);
        editButton = root.findViewById(R.id.profile_edit);
        expiry = root.findViewById(R.id.profile_expiry);
    }
}