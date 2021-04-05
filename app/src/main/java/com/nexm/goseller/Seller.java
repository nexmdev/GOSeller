package com.nexm.goseller;

public class Seller {

    private String Account;
    private String Address;
    private String Email;
    private String IdUrl;
    private String Name;
    private String SellerId;
    private String MoNo;
    private String MoNoSecond;
    private String PhotoUrl;
    private long PlanExpiry;
    private int Products;
    private int PlanAmount=0;
    private int ProductsLimit;
    private long JoinDate;

    public Seller (){}

    public void setAccount(String account){this.Account = account;}
    public void setAddress(String address){this.Address = address;}
    public void setEmail(String email){this.Email = email;}
    public void setIdUrl(String idUrl){this.IdUrl = idUrl;}
    public void setName(String name){this.Name = name;}
    public void setSellerId(String sellerId){this.SellerId = sellerId;}
    public void setMoNo(String moNo){this.MoNo = moNo;}
    public void setMoNoSecond(String moNoSecond){this.MoNoSecond = moNoSecond;}
    public void setPhotoUrl(String photoUrl){this.PhotoUrl = photoUrl;}
    public void setPlanExpiry(long planExpiry){this.PlanExpiry = planExpiry;}
    public void setProducts(int products){this.Products = products;}
    public void setProductsLimit(int productsLimit){this.ProductsLimit = productsLimit;}
    public void setJoinDate(long joinDate){ this.JoinDate= joinDate;}
    public void setPlanAmount(int planAmount){this.PlanAmount = planAmount;}

    public String getAccount(){return this.Account ;}
    public String getAddress(){return this.Address ;}
    public String getEmail(){return this.Email ;}
    public String getIdUrl(){return this.IdUrl ;}
    public String getName(){return this.Name ;}
    public String getSellerId(){return this.SellerId ;}
    public String getMoNo(){return this.MoNo ;}
    public String getMoNoSecond(){return this.MoNoSecond ;}
    public String getPhotoUrl(){return this.PhotoUrl ;}
    public long getPlanExpiry(){return this.PlanExpiry ;}
    public int getProducts(){return this.Products ;}
    public int getProductsLimit(){return this.ProductsLimit ;}
    public long getJoinDate(){ return this.JoinDate;}

    public int getPlanAmount() {
        return PlanAmount;
    }
}
