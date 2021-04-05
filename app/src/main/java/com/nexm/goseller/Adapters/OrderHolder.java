package com.nexm.goseller.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nexm.goseller.R;
import com.nexm.goseller.models.Order;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 13-02-2018.
 */

public class OrderHolder extends RecyclerView.ViewHolder {

    private final TextView productName,quantity, deliveryEst, orderdate,amount,
            status,customerName,accept,reject,ready,
            approval,preparation,inTransit;
    private final ImageView approvalDot, preparationDot,inTransitDot;

    private static OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, String task);
    }
    public void setOnItemClickListner(OnItemClickListener mlistener){
        OrderHolder.listener = mlistener;
    }



    public OrderHolder(final View itemView) {
        super(itemView);

        status = itemView.findViewById(R.id.orderItem_status);
        quantity = itemView.findViewById(R.id.orderItem_qty_delv);
        deliveryEst = itemView.findViewById(R.id.orderItem_delivery_est);
        orderdate = itemView.findViewById(R.id.orderItem_date);
        amount = itemView.findViewById(R.id.orderItem_amt);
        productName = itemView.findViewById(R.id.orderItem_productName);
        customerName = itemView.findViewById(R.id.orderItem_customerName);

        accept = itemView.findViewById(R.id.order_item_accept);
        reject = itemView.findViewById(R.id.order_item_reject);
        ready = itemView.findViewById(R.id.order_item_ready);
        approval = itemView.findViewById(R.id.order_item_approval);
        preparation = itemView.findViewById(R.id.order_item_preparation);
        inTransit = itemView.findViewById(R.id.order_item_inTransit);
        approvalDot = itemView.findViewById(R.id.order_item_approval_dot);
        preparationDot = itemView.findViewById(R.id.order_item_preparation_dot);
        inTransitDot = itemView.findViewById(R.id.order_item_inTransitDot);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView,position,"ACCEPTED" );
                    }
                }
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView,position,"REJECTED" );
                    }
                }
            }
        });
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView,position,"READY" );
                    }
                }
            }
        });

    }
    public void bindData(Order order, Context mcontext){

        productName.setText(order.getProductName());

        String[] priceArray = order.getPrice().split("-") ;
        quantity.setText("Qty: "+ order.getQuantity()+" " +priceArray[0]);
        int oamount = Integer.parseInt(priceArray[3]) * order.getQuantity()+order.getDeliveryCharges();
        amount.setText("Amount: ₹"+oamount);
        Date date = new Date(order.getDate());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int orderHour=calendar.get(Calendar.HOUR_OF_DAY);
        int orderMin = calendar.get(Calendar.MINUTE);

        String dateString = format.format(date);
        orderdate.setText(dateString);
        status.setText(order.getStatus());
        orderHour++;
        String ampm = orderHour>12 ? "pm" : "am";

        orderMin+=30;
        if(orderMin>=60){
            orderMin-=60;
            orderHour++;
        }
        orderMin = Math.max(orderMin, 10);
        orderHour = orderHour>12 ? orderHour-12 : orderHour;
        deliveryEst.setText("डिलेवरी\n"+orderHour +":"+orderMin+" " +ampm +" पर्यंत.");

        String[] customerInfo = order.getCustomerID().split(",");
        customerName.setText("by :"+order.getCustomerNameAddress());



        switch (order.getStatus()){
            case "ORDERED":
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                ready.setVisibility(View.GONE);
                setViews(mcontext,1);
                approval.setText("Approval\nin progress");
                preparation.setText("Preparation\nPacking");
                inTransit.setText("in Transit");
                break;
            case "ACCEPTED":
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                ready.setVisibility(View.VISIBLE);
               setViews(mcontext,2);
                approval.setText("Approved ");
                preparation.setText("Preparation\nPacking\nin progress");
                inTransit.setText("in Transit");
                break;
            case "REJECTED":
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                ready.setVisibility(View.GONE);
                setViews(mcontext,4);
                approval.setText("Rejected ");
                preparation.setText("Preparation\nPacking");
                inTransit.setText("in Transit");
                break;
            case "READY":
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                ready.setVisibility(View.GONE);
                setViews(mcontext,3);
                approval.setText("Approved ");
                preparation.setText("Prepaired\nPacked");
                inTransit.setText("in Transit\nto delivery");
                break;
        }


    }


    private void setViews(Context mcontext,int index){
        switch (index){
            case 1 :
                approvalDot.setImageResource(R.drawable.ic_baseline_lens_24);
                approval.setTextColor(mcontext.getResources().getColor(R.color.blue1));
                approval.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,mcontext.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                preparationDot.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                inTransitDot.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                preparation.setTextColor(mcontext.getResources().getColor(R.color.textGrey));
                inTransit.setTextColor(mcontext.getResources().getColor(R.color.textGrey));
                preparation.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                inTransit.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                break;
            case 2 :
                approvalDot.setImageResource(R.drawable.ic_baseline_check_circle_24);
                approval.setTextColor(mcontext.getResources().getColor(R.color.green));
                approval.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                preparationDot.setImageResource(R.drawable.ic_baseline_lens_24);
                inTransitDot.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                preparation.setTextColor(mcontext.getResources().getColor(R.color.blue1));
                inTransit.setTextColor(mcontext.getResources().getColor(R.color.textGrey));
                preparation.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,mcontext.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                inTransit.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                break;
            case 3 :
                approvalDot.setImageResource(R.drawable.ic_baseline_check_circle_24);
                approval.setTextColor(mcontext.getResources().getColor(R.color.green));
                approval.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                preparationDot.setImageResource(R.drawable.ic_baseline_check_circle_24);
                inTransitDot.setImageResource(R.drawable.ic_baseline_lens_24);
                preparation.setTextColor(mcontext.getResources().getColor(R.color.green));
                inTransit.setTextColor(mcontext.getResources().getColor(R.color.blue1));
                preparation.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                inTransit.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,mcontext.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                break;
            case 4 :
                approvalDot.setImageResource(R.drawable.ic_warning_black_24dp);
                approval.setTextColor(mcontext.getResources().getColor(R.color.colorPrimary));
                approval.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                preparationDot.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                inTransitDot.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                preparation.setTextColor(mcontext.getResources().getColor(R.color.textGrey));
                inTransit.setTextColor(mcontext.getResources().getColor(R.color.textGrey));
                preparation.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                inTransit.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                break;
        }
    }
}
