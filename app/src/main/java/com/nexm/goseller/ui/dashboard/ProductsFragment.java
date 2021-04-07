package com.nexm.goseller.ui.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nexm.goseller.Adapters.OrderHolder;
import com.nexm.goseller.Adapters.ProductsViewHolder;
import com.nexm.goseller.DetailsActivity;
import com.nexm.goseller.GO_SELLER_APPLICATION;
import com.nexm.goseller.R;
import com.nexm.goseller.models.Order;
import com.nexm.goseller.models.ProductListing;

import java.util.HashMap;
import java.util.Map;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private boolean flag;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_products, container, false);
        final FloatingActionButton addnewProduct = root.findViewById(R.id.products_floatingActionButton);
        addnewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("CALLER","New");
                getActivity().startActivity(intent);
            }
        });
        progressBar = root.findViewById(R.id.products_progressBar);


        recyclerView = root.findViewById(R.id.products_recyclerView);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.onDataChanged();
        return root;
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Query query = GO_SELLER_APPLICATION.reference
                .child("ProductListings")
                .orderByChild("sellerID")
                .equalTo(GO_SELLER_APPLICATION.sellerID);
        FirebaseRecyclerOptions<ProductListing> options = new FirebaseRecyclerOptions.Builder<ProductListing>()
                .setQuery(query, ProductListing.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ProductListing, ProductsViewHolder>(
               options
        ) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_layout, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ProductsViewHolder viewHolder, final int position,final ProductListing model) {

                viewHolder.bindData(model,getActivity());
                viewHolder.setOnItemClickListner(new ProductsViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(String itemView,int currentposition) {
                        DatabaseReference productRef = mFirebaseAdapter.getRef(currentposition);
                        switch (itemView){
                            case "update":
                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                intent.putExtra("CALLER","update");
                                intent.putExtra("KEY",productRef.getKey());
                                getActivity().startActivity(intent);break;
                            case "remove": confirmRemoval(productRef,currentposition);break;
                            case "edit":
                                Intent intent1 = new Intent(getActivity(), DetailsActivity.class);
                                intent1.putExtra("CALLER","edit");
                                intent1.putExtra("KEY",productRef.getKey());
                                getActivity().startActivity(intent1);

                        }
                    }
                });
            }
        };
    }

    private void showUpdateDialog(final DatabaseReference reference,final int position) {
        final Dialog dialog;

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.setCanceledOnTouchOutside(true);

        final TextView price = dialog.findViewById(R.id.product_update_price);
        final TextView mrp = dialog.findViewById(R.id.product_update_mrp);
        final TextView quantity = dialog.findViewById(R.id.product_update_quantity);
        final TextView update = dialog.findViewById(R.id.product_update_update);
        final TextView cancel = dialog.findViewById(R.id.product_update_cancel);
        //final ProgressBar progress = dialog.findViewById(R.id.login_progressbar);
        final EditText pricefield = dialog.findViewById(R.id.product_update_pricefield);
        final EditText mrpfield = dialog.findViewById(R.id.product_update_mrpfield);
        final EditText quantityfield = dialog.findViewById(R.id.product_update_quantityfield);
        final ProgressBar updateProgress = dialog.findViewById(R.id.update_progressBar);
        final TextView error = dialog.findViewById(R.id.update_error);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    price.setText(dataSnapshot.child("discount").getValue().toString());
                    mrp.setText(dataSnapshot.child("price").getValue().toString());
                    quantity.setText(dataSnapshot.child("qAvailable").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    dialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProgress.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                String p = price.getText().toString();
                String m = mrp.getText().toString();
                String q = quantity.getText().toString();
                if(!pricefield.getText().toString().isEmpty())p=pricefield.getText().toString();
                if(!mrpfield.getText().toString().isEmpty())m=mrpfield.getText().toString();
                if(!quantityfield.getText().toString().isEmpty())q=quantityfield.getText().toString();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/ProductListings/"+reference.getKey()+"/price",Integer.parseInt(m));
                childUpdates.put("/ProductListings/"+reference.getKey()+"/discount",Integer.parseInt(p));
                childUpdates.put("/ProductListings/"+reference.getKey()+"/qAvailable",Integer.parseInt(q));
                GO_SELLER_APPLICATION.reference.updateChildren(childUpdates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateProgress.setVisibility(View.GONE);
                                dialog.dismiss();
                                mFirebaseAdapter.notifyItemChanged(position);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        updateProgress.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();


    }
    @Override
    public void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();

    }

    private void removeProduct(final DatabaseReference productRef, final int position) {

            progressBar.setVisibility(View.VISIBLE);
            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/RemovedProducts/"+productRef.getKey(),dataSnapshot.getValue());
                        GO_SELLER_APPLICATION.noOfProducts--;
                        childUpdates.put("/Sellers/"+GO_SELLER_APPLICATION.sellerID+"/products",GO_SELLER_APPLICATION.noOfProducts);
                        GO_SELLER_APPLICATION.reference.updateChildren(childUpdates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        alert("Product removed successfully");
                                        productRef.setValue(null);
                                        progressBar.setVisibility(View.GONE);
                                        mFirebaseAdapter.notifyItemRemoved(position);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                GO_SELLER_APPLICATION.noOfProducts++;
                                progressBar.setVisibility(View.GONE);
                                alert("Something went wrong try again !");
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                    alert("Something went wrong try again !");
                }
            });

    }
    private void alert(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Check this...!")
                .setMessage(s)
                .setIcon(ContextCompat.getDrawable(getActivity(),R.drawable.ic_warning_black_24dp))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
        builder.show();
    }

    private void confirmRemoval(final DatabaseReference productRef, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Check this...!")
                .setMessage("Product will be removed.You can see it in Removed Products tab and can " +
                        "activate it again.")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeProduct(productRef,position);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();

    }
}