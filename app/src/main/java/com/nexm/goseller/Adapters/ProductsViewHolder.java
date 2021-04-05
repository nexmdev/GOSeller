package com.nexm.goseller.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.goseller.GO_SELLER_APPLICATION;
import com.nexm.goseller.R;
import com.nexm.goseller.models.ProductListing;

public class ProductsViewHolder extends RecyclerView.ViewHolder {

    private static OnItemClickListener listener;
    private final TextView name,price,mrp,rating,dept,edit,remove,update,sold;
    private final RatingBar ratingBar;


    public interface OnItemClickListener {
        void onItemClick(String actionName, int position);
    }
    public void setOnItemClickListner(OnItemClickListener mlistener){
        ProductsViewHolder.listener = mlistener;
    }
    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.product_layout_name);
        price = itemView.findViewById(R.id.product_layout_price);
        mrp =  itemView.findViewById(R.id.product_layout_mrp);
        rating = itemView.findViewById(R.id.product_layout_rating);
        sold = itemView.findViewById(R.id.product_layout_sold);
        dept = itemView.findViewById(R.id.products_layput_dept);
        edit = itemView.findViewById(R.id.products_layout_edit);
        remove = itemView.findViewById(R.id.products_layout_remove);
        update = itemView.findViewById(R.id.products_layout_update);
        ratingBar = itemView.findViewById(R.id.product_layout_ratingBar);

    }
    public void bindData(final ProductListing productListing, Context context){

        name.setText(productListing.getProductName());
        String[] priceString = productListing.getPrice().split("-");
        mrp.setText("MRP : ₹."+ priceString[2]+" / "+priceString[1]+priceString[0]);
        price.setText("आँफर : ₹."+priceString[3]+" / "+priceString[1]+priceString[0]);
        dept.setText(productListing.getDeptCatSubCat());
       // sold.setText("Sold : "+productListing.getqSold()+" of "+productListing.getqAvailable());
        if(productListing.getNoOfRaters() != 0){
            float ratings = GO_SELLER_APPLICATION.round((float) productListing.getRatings()/productListing.getNoOfRaters());
            rating.setText(String.valueOf(ratings));
            ratingBar.setRating(ratings);
        }else {

            ratingBar.setRating((float) 0.0);
            rating.setText("Unrated");
        }
        ratingBar.setIsIndicator(true);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick("edit",getAdapterPosition());
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick("remove",getAdapterPosition());
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick("update",getAdapterPosition());
                }
            }
        });
    }
}
