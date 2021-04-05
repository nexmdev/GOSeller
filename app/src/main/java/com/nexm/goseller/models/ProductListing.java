package com.nexm.goseller.models;

public class ProductListing {
    private String productThumb = "y" ;
    private String productName ;
    private String sellerName ;
    private String sellerID ;
    private String price ;
    private String department ;
    private String deptCat ;
    private String deptCatSubCat ;
    private int ratings = 0 ;
    private int noOfRaters = 0 ;
    private int displayPriority = 1;
    private int qSold = 0;
    private int qAvailable ;
    private long dateOfListing ;
    private long validity ;

    public ProductListing(){}

    public void setProductThumb(String productThumb){
        this.productThumb = productThumb;
    }
    public void setProductName(String productname){
        this.productName = productname;
    }
    public void setSellerName(String productThumb){
        this.sellerName = productThumb;
    }
    public void setSellerID(String productThumb){
        this.sellerID = productThumb;
    }
    public void setDepartment(String productThumb){
        this.department = productThumb;
    }
    public void setDeptCat(String productThumb){
        this.deptCat = productThumb;
    }
    public void setDeptCatSubCat(String productThumb){
        this.deptCatSubCat = productThumb;
    }
    public void setPrice(String productThumb){
        this.price = productThumb;
    }

    public void setRatings(int productThumb){
        this.ratings = productThumb;
    }
    public void setNoOfRaters(int productThumb){
        this.noOfRaters = productThumb;
    }
    public void setDisplayPriority(int productThumb){
        this.displayPriority = productThumb;
    }
    public void setqSold(int productThumb){
        this.qSold = productThumb;
    }
    public void setqAvailable(int productThumb){
        this.qAvailable = productThumb;
    }
    public void setDateOfListing(long productThumb){
        this.dateOfListing = productThumb;
    }
    public void setValidity(long productThumb){
        this.validity = productThumb;
    }

    public String getProductThumb(){
        return this.productThumb;
    }
    public String getProductName(){
        return this.productName;
    }
    public String getSellerName(){
        return this.sellerName;
    }
    public String getSellerID(){
        return this.sellerID;
    }
    public String getDepartment(){
        return this.department;
    }
    public String getDeptCat(){
        return this.deptCat;
    }
    public String getDeptCatSubCat(){
        return this.deptCatSubCat;
    }
    public String getPrice(){
        return this.price;
    }

    public int getRatings(){
        return this.ratings;
    }
    public int getNoOfRaters(){
        return this.noOfRaters;
    }
    public int getDisplayPriority(){
        return this.displayPriority;
    }
    public int getqSold(){
        return this.qSold;
    }
    public int getqAvailable(){
        return this.qAvailable;
    }
    public long getDateOfListing(){
        return this.dateOfListing;
    }
    public long getValidity(){
        return this.validity;
    }
}
