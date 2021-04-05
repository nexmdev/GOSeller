package com.nexm.goseller.models;

public class ProductDetails {
    private String prices;
    private String one ;
    private String two ;
    private String three ;
    private String four ;
    private String five ;
    private String six ;
    private String url1 = "y" ;
    private String url2 = "y" ;
    private String url3 = "y" ;
    private String discription ;

    public ProductDetails(){}

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public void setOne(String mone){
        this.one = mone;
    }
    public void setTwo(String mone){
        this.two = mone;
    }
    public void setThree(String mone){
        this.three = mone;
    }
    public void setFour(String mone){
        this.four = mone;
    }
    public void setFive(String mone){
        this.five = mone;
    }
    public void setSix(String mone){
        this.six = mone;
    }
    public void setUrl1(String mone){
        this.url1 = mone;
    }
    public void setUrl2(String mone){
        this.url2 = mone;
    }
    public void setUrl3(String mone){
        this.url3 = mone;
    }
    public void setDiscription(String mone){
        this.discription = mone;
    }


    public String getPrices() {
        return prices;
    }

    public String getOne(){
        return this.one;
    }
    public String getTwo(){
        return this.two;
    }
    public String getThree(){
        return this.three;
    }
    public String getFour(){
        return this.four;
    }
    public String getFive(){
        return this.five;
    }
    public String getSix(){
        return this.six;
    }
    public String getUrl1(){
        return this.url1;
    }
    public String getUrl2(){
        return this.url2;
    }
    public String getUrl3(){
        return this.url3;
    }
    public String getDiscription(){
        return this.discription;
    }
}
