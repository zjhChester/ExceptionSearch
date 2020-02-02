package xyz.zjhwork.resmodel.userResModel;

import java.util.ArrayList;
import java.util.List;

public class UserResModel{
    private String username;
    private long currTime;
    public static  List<UserResModel> getUserResModels(){
        List<UserResModel> list = new ArrayList();
        return list;
    }
    public  static  UserResModel getUserResModel(){
        return new UserResModel();
    }
    @Override
    public String toString() {
        return "UserResModel{" +
                "username='" + username + '\'' +
                ", currTime=" + currTime +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCurrTime() {
        return currTime;
    }

    public void setCurrTime(long currTime) {
        this.currTime = currTime;
    }
}
