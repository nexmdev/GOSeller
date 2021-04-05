package com.nexm.goseller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.goseller.R;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {

    private String[] Prices;
    private static OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(String functionName, String newPriceString, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        UpdateAdapter.listener = listener;
    }

    public UpdateAdapter(String[] data){
        this.Prices = data;
    }
    public  void swapData(String[] newData,int position){
        this.Prices = newData;
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.update_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String[] details = Prices[position].split("-");
        if(details.length == 6){
            holder.itemView.setBackgroundResource(android.R.color.darker_gray);
            holder.update.setText("Restore");
            holder.remove.setText("Removed");
            holder.remove.setEnabled(false);
            holder.mrpfield.setBackgroundResource(android.R.color.darker_gray);
            holder.offerfield.setBackgroundResource(android.R.color.darker_gray);
            holder.stockfield.setBackgroundResource(android.R.color.darker_gray);
            holder.mrpfield.setEnabled(false);
            holder.offerfield.setEnabled(false);
            holder.stockfield.setEnabled(false);
        }else{
            holder.itemView.setBackgroundResource(android.R.color.white);
            holder.update.setText("Update");
            holder.remove.setText("Remove");
            holder.remove.setEnabled(true);
            holder.mrpfield.setBackgroundResource(R.drawable.button_outline);
            holder.offerfield.setBackgroundResource(R.drawable.button_outline);
            holder.stockfield.setBackgroundResource(R.drawable.button_outline);
            holder.mrpfield.setEnabled(true);
            holder.offerfield.setEnabled(true);
            holder.stockfield.setEnabled(true);
        }
        holder.unit1.setText(details[1]+details[0]);
        holder.unit2.setText(details[1]+details[0]);
        holder.mrp.setText(details[2]);
        holder.mrpfield.setText(details[2]);
        holder.offer.setText(details[3]);
        holder.offerfield.setText(details[3]);
        holder.stock.setText(details[4]);
        holder.stockfield.setText(details[4]);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPriceString = details[0]+"-"+details[1]+"-"+holder.mrpfield.getText().toString().trim()
                        +"-"+holder.offerfield.getText().toString().trim()
                        +"-"+holder.stockfield.getText().toString().trim();

                if(holder.mrpfield.getText().toString().trim().matches(details[2]) &&
                        holder.offerfield.getText().toString().trim().matches(details[3]) &&
                        holder.stockfield.getText().toString().trim().matches(details[4]) ){

                    if(listener!= null){
                        if(holder.update.getText().toString().matches("Update")) {
                        }else{
                            listener.onItemClick("Restore",newPriceString,position );
                        }
                    }
                }else{
                    if(listener!= null){
                        listener.onItemClick("Update",newPriceString,position );
                    }
                }
            }


        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!= null ){

                        listener.onItemClick("Remove", Prices[position] + "-removed", position);

                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return Prices.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mrp,offer,stock,unit1,unit2;
        final EditText mrpfield,offerfield,stockfield;
        final Button remove,update;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            mrp = itemView.findViewById(R.id.update_mrp_1);
            offer = itemView.findViewById(R.id.update_offer_1);
            stock = itemView.findViewById(R.id.update_stock_1);
            unit1 = itemView.findViewById(R.id.update_unit_1);
            unit2 = itemView.findViewById(R.id.update_unit_2);

            mrpfield = itemView.findViewById(R.id.update_mrp_2);
            offerfield = itemView.findViewById(R.id.update_offer_2);
            stockfield = itemView.findViewById(R.id.update_stock_2);

            remove = itemView.findViewById(R.id.update_remove);
            update = itemView.findViewById(R.id.update_update);
        }
    }
}
