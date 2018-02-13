package com.dev1.vishalmishra.grocnow;



public class myorders_list {

    private String pname;
    private String pid;
    private String price;
    private String quantity;
    private String status;
    private String time;

    public myorders_list(String pid,String pname,String price,String quantity,String status,String time){
        this.setPid(pid);
        this.setPname(pname);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setStatus(status);
        this.setTime(time);
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

}
