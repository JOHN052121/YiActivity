package com.yiactivity.Utils;

import android.graphics.Bitmap;
import android.util.Log;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.Trend;
import com.yiactivity.model.User;

import javax.xml.transform.Result;
import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DBOperation {


    /**
     * 主办方注册，注册成功返回1，失败返回0
     * @param sponsor
     */
    public static int sponsorRegister(Sponsor sponsor){
        Connection connection = DataBase.getSQLConnection();
        String sql = "INSERT INTO sponsor VALUES(?,?,?,?,?,?,?,?,?,?)";
        String ver_sql = "select * from sponsor where phoneNum='"+sponsor.getPhoneNum()+"'";
        Log.d("调试","主办方注册");
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(ver_sql);
                if(!rs.next())
                {
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1,sponsor.getSpon_Name());
                    pstmt.setString(2,sponsor.getOrg_Name());
                    pstmt.setString(3,sponsor.getPassword());
                    pstmt.setString(4,sponsor.getUniversity());
                    pstmt.setString(5,sponsor.getSponsorIntro());
                    pstmt.setString(6,sponsor.getPhoneNum());
                    pstmt.setString(7,sponsor.getEmail());
                    pstmt.setInt(8,sponsor.getFlag());
                    pstmt.setString(9,sponsor.getProof());
                    pstmt.setBytes(10,sponsor.getSponsorImage());
                    pstmt.executeUpdate();
                    pstmt.close();
                    st.close();
                    connection.close();
                    return 1;
                }
                else
                {
                    rs.close();
                    st.close();
                    connection.close();
                    Log.d("调试","主办方用户已存在");
                    return 0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            Log.d("调试","连接数据库失败");
            return 2;
        }
        return 3;
    }

    /*
    找活动页面 返回一个包含所有活动的ArrayList
     */
    public static ArrayList<Activity> getAllOfActivity() {
        ArrayList<Activity> activityList = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from activity";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Activity activity = new Activity();
                activity.setActivityId(rs.getInt("activityId"));
                activity.setActivityName(rs.getString("activityName"));
                activity.setAddress(rs.getString("address"));
                activity.setAddressContent(rs.getString("activityContent"));
                activity.setTime(rs.getString("time"));
                activity.setState(rs.getInt("state"));
                activity.setType(rs.getString("type"));
                activity.setSponsorId(rs.getInt("sponsorId"));
                activity.setPoster(rs.getBytes("poster"));
                activityList.add(activity);
            }
            rs.close();
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityList;
    }

    /**
     * 更改过期活动的state为2
     * @param activityId
     */
    public static void overdueActivity(int activityId){
        Connection connection = DataBase.getSQLConnection();
        String sql = "update activity set state=? where activityId = '" + activityId + "'";
        if(connection != null){
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,2);
                pstmt.executeUpdate();
                pstmt.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}