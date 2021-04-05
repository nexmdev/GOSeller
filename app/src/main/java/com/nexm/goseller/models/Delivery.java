package com.nexm.goseller.models;

public class Delivery {
    private String time ;
    private int charges ;
    private String returnPolicy ;
    private int minOrder ;
    private boolean cod ;
    private boolean returnAvailable ;

    public Delivery(){}

    public void setTime(String mtime){
        this.time = mtime;
    }
    public void setReturnPolicy(String mtime){
        this.returnPolicy = mtime;
    }
    public void setCharges(int mtime){
        this.charges = mtime;
    }
    public void setMinOrder(int mtime){
        this.minOrder = mtime;
    }
    public void setCod(boolean mtime){
        this.cod = mtime;
    }
    public void setReturnAvailable(boolean mtime){
        this.returnAvailable = mtime;
    }

    public String getTime(){
        return this.time;
    }
    public String getReturnPolicy(){
        return this.returnPolicy;
    }
    public int getCharges(){
        return this.charges;
    }
    public int getMinOrder(){
        return this.minOrder;
    }
    public boolean getCod(){
        return this.cod;
    }
    public boolean getReturnAvailable(){
        return this.returnAvailable;
    }

}
