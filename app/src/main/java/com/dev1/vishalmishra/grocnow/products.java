package com.dev1.vishalmishra.grocnow;



public class products {

    private String pname;
    private String pid;
    private String price;
    private String category;

    public products(String pid,String pname,String price,String category){
        this.setPid(pid);
        this.setPname(pname);
        this.setPrice(price);
        this.setCategory(category);
    }
    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
