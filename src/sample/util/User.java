package sample.util;

public class User {
    public String id;
    public String ip;
    public String isOnline;
    public String pwd;
    public String offlineMsg;

    public User(String id,String pwd,String ip){
        this.id=id;
        this.pwd=pwd;
        this.ip=ip;
        isOnline = "true";
        offlineMsg = "false";
    }

    public void setOnline(){
        this.isOnline = "true";
    }

    public void setNotOnLine(){
        this.isOnline = "false";
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public String getOfflineMsg() {
        return offlineMsg;
    }

    public void setOfflineMsg(String offlineMsg) {
        this.offlineMsg = offlineMsg;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public String getPwd() {
        return pwd;
    }
}
