package com.dev1.vishalmishra.grocnow;



public class category_set {
    private String cname;
    private String cid;

    public category_set(String cid,String cname){
        this.setCid(cid);
        this.setCname(cname);
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
