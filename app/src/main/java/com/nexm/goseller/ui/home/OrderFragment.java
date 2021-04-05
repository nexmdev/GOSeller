package com.nexm.goseller.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.nexm.goseller.Adapters.OrderHolder;
import com.nexm.goseller.GO_SELLER_APPLICATION;
import com.nexm.goseller.R;
import com.nexm.goseller.models.Order;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.nexm.goseller.ui.home.OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String userID;
    private String mParam2;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
            userID = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        final Query query = GO_SELLER_APPLICATION.reference
                .child("ORDERS").orderByChild("sellerId").equalTo(GO_SELLER_APPLICATION.sellerID);

      FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Order, OrderHolder>(options
        ) {
            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_item_layout, parent, false);
                return new OrderHolder(view);
            }

            @Override
            protected void onBindViewHolder(OrderHolder viewHolder, final int position, final Order model) {

                viewHolder.bindData(model, getActivity());
                viewHolder.setOnItemClickListner(new OrderHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position, String task) {
                        GO_SELLER_APPLICATION.reference.child("ORDERS").child(mFirebaseAdapter.getRef(position).getKey())
                                .child("status").setValue(task);

                    }
                });
            }

                @Override
                public void onDataChanged () {
                    // Called each time there is a new data snapshot. You may want to use this method
                    // to hide a loading spinner or check for the "no documents" state and update your UI.
                    // ...


                }

                @Override
                public void onError (DatabaseError e){
                    // Called when there is an error getting data. You may want to update
                    // your UI to display an error message to the user.
                    // ...
                }




        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = view.findViewById(R.id.order_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);

        return view;
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
}