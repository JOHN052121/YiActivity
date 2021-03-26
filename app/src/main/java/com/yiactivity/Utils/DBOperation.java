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
     * 根据PhoneNum查询数据库中是否有这个user,有的话get到password并和输入的password进行比较，相等则返回该用户类，不相等
     * 或查不到记录返回null.
     */
    public static User loginOperation(String phone,String password){
        User user = new User();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from users where phoneNum='"+phone+"'";
        Log.d("调试",sql);
        if(connection != null){
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                if(rs.getString("password").equals(password)){
                    user.setUserId(rs.getInt("userId"));
                    user.setUserName(rs.getString("userName"));
                    user.setPhoneNum(rs.getString("phoneNum"));
                    user.setPassword(rs.getString("password"));
                    user.setGender(rs.getString("gender"));
                    user.setUniversity(rs.getString("university"));
                    user.setEmail(rs.getString("Email"));
                    user.setPoint(rs.getInt("Point"));
                    user.setImage(rs.getBytes("image"));
                    rs.close();
                    st.close();
                    connection.close();
                    return user;
                }
                else{
                    rs.close();
                    st.close();
                    connection.close();
                    return null;
                }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            Log.d("调试","连接数据库失败");
        }
        return null;
    }


    /**
     * 根据传入的电话号码查询数据库中是否有这个主办方，有的话密码进行相等检验，不等的话返回空
     * @param user
     * @param password
     * @return
     */
    public static Sponsor loginAsSponsor(String user,String password){
        Sponsor sponsor = new Sponsor();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from sponsor where phoneNum='"+user+"'";
        Log.d("调试",sql);
        if(connection != null){
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    if(rs.getString("password").equals(password)){
                        sponsor.setSponsorImage(rs.getBytes("sponsorImage"));
                        sponsor.setOrg_Name(rs.getString("org_Name"));
                        sponsor.setSpon_Name(rs.getString("spon_Name"));
                        sponsor.setUniversity(rs.getString("university"));
                        sponsor.setSponsorIntro(rs.getString("sponIntro"));
                        sponsor.setEmail(rs.getString("email"));
                        sponsor.setSponsorId(rs.getInt("sponsorId"));
                        rs.close();
                        st.close();
                        connection.close();
                        return sponsor;
                    }
                    else{
                        rs.close();
                        st.close();
                        connection.close();
                        return null;
                    }
                }
                else {
                    return null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            Log.d("调试","连接数据库失败");
        }
        return null;
    }


    /**
     * 用户注册，注册成功返回1，注册失败（已存在账户）返回0
     * @param user
     */
    public static int userRegister(User user){
        Connection connection = DataBase.getSQLConnection();
        String sql = "INSERT INTO users VALUES(?,?,?,?,?,?,?,?)";
        String ver_sql = "select * from users where phoneNum='"+user.getPhoneNum()+"'";
        Log.d("调试","用户注册");
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(ver_sql);
                if(!rs.next())
                {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1,user.getUserName());
                pstmt.setString(2,user.getPhoneNum());
                pstmt.setString(3,user.getPassword());
                pstmt.setString(4,user.getGender());
                pstmt.setString(5,user.getUniversity());
                pstmt.setString(6,user.getEmail());
                pstmt.setInt(7,user.getPoint());
                pstmt.setBytes(8,user.getImage());
                pstmt.executeUpdate();
                pstmt.close();
                rs.close();
                st.close();
                connection.close();
                return 1;
                }
                else
                {
                    rs.close();
                    st.close();
                    connection.close();
                    Log.d("调试","已存在");
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


    /**
     * 主页获得“为您推荐”的活动列表，返回一个ArrayList<Activity>
     */
    public static ArrayList<Activity> getRecommendActivity(){
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


    /**主页“他们在办活动”的活动列表，返回一个长度为3的随机的ArrayList<Sponsor>*/
    public static ArrayList<Sponsor> getRandomSponsor(){
        ArrayList<Sponsor> sponsorList = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "SELECT TOP 3 * FROM sponsor ORDER BY NEWID()";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Sponsor sponsor = new Sponsor();
                sponsor.setOrg_Name(rs.getString("org_Name"));
                sponsor.setSponsorImage(rs.getBytes("sponsorImage"));
                sponsor.setSponsorId(rs.getInt("sponsorId"));
                sponsorList.add(sponsor);
            }
            rs.close();
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sponsorList;
    }

    /**
     * 找活动页面 返回一个包含所有活动的ArrayList<Activity>
     * @return
     */
    public static ArrayList<Activity> getAllActivity(){
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
     * 搜索fragment 根据type返回不同种类的活动列表
     * @param type
     * @return
     */
    public static ArrayList<Activity> getDiffTypeActivity(String type){
        ArrayList<Activity> activityList = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from activity where type = '" + type + "'";
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
     * 根据传入活动详情页的activityId获取活动的具体内容
     * @param activityId
     * @return
     */
    public static Activity getDetailActivity(int activityId){
        Activity activity = new Activity();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select activityName, time, address,activityContent,poster,sponsorId from activity where activityId = '" + activityId +"'";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    activity.setActivityName(rs.getString("activityName"));
                    activity.setTime(rs.getString("time"));
                    activity.setAddress(rs.getString("address"));
                    activity.setAddressContent(rs.getString("activityContent"));
                    activity.setPoster(rs.getBytes("poster"));
                    activity.setSponsorId(rs.getInt("sponsorId"));
                    rs.close();
                    st.close();
                    connection.close();
                    return activity;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    public static ArrayList<String> getBannerImg(){
        ArrayList<String> string_img = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select poster from activity";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                string_img.add(rs.getString("poster"));
            }
            rs.close();
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string_img;
    }


    /**
     * 根据传入的userId，获取user的具体内容，返回给myinfo界面
     * @param userId
     * @return
     */
    public static User getUserInfo(int userId){
        User user = new User();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from users where userId = '" + userId + "'";
        if (connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    user.setUserName(rs.getString("userName"));
                    user.setPhoneNum(rs.getString("phoneNum"));
                    user.setPassword(rs.getString("password"));
                    user.setGender(rs.getString("gender"));
                    user.setUniversity(rs.getString("university"));
                    user.setEmail(rs.getString("Email"));
                    user.setPoint(rs.getInt("Point"));
                    user.setImage(rs.getBytes("image"));

                    rs.close();
                    st.close();
                    connection.close();
                    return user;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 根据传入的搜索关键字 展示出模糊查询搜索到的活动
     * @param search_keyword
     * @return
     */
    public static ArrayList<Activity> getTopSearchResult(String search_keyword){
        ArrayList<Activity> activityArrayList = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from activity where activityName like '%" + search_keyword+ "%'";
        if(connection!=null){
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
                    activityArrayList.add(activity);
                }
                rs.close();
                st.close();
                connection.close();
                return activityArrayList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 根据传入的搜索关键字，展示出模糊查询查到的主办方列表
     * @param search_keyword
     * @return
     */
    public static ArrayList<Sponsor> getTopSearchSponsor(String search_keyword){
        ArrayList<Sponsor> sponsorArrayList = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from sponsor where org_Name like '%" + search_keyword + "%'";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    Sponsor sponsor = new Sponsor();
                    sponsor.setSponsorImage(rs.getBytes("sponsorImage"));
                    sponsor.setOrg_Name(rs.getString("org_Name"));
                    sponsor.setSpon_Name(rs.getString("spon_Name"));
                    sponsor.setUniversity(rs.getString("university"));
                    sponsor.setSponsorIntro(rs.getString("sponIntro"));
                    sponsor.setEmail(rs.getString("email"));
                    sponsor.setSponsorId(rs.getInt("sponsorId"));
                    sponsorArrayList.add(sponsor);
                }
                rs.close();
                st.close();
                connection.close();
                return sponsorArrayList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 通过主办方id获取该主办方下的所有活动
     * @param sponsorId
     * @return
     */
    public static ArrayList<Activity> getActivityBySponsorId(int sponsorId){
        ArrayList<Activity> activityArrayList = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from activity where sponsorId = '" + sponsorId + "'";
        if (connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()){
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
                    activityArrayList.add(activity);
                }
                rs.close();
                st.close();
                connection.close();
                return activityArrayList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 通过主办方id获取主办方id详细信息
     */
    public static Sponsor getSponsorDetailById(int sponsorId){
        Sponsor sponsor = new Sponsor();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select * from sponsor where sponsorId = '" + sponsorId + "'";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()){

                    sponsor.setSponsorImage(rs.getBytes("sponsorImage"));
                    sponsor.setOrg_Name(rs.getString("org_Name"));
                    sponsor.setSpon_Name(rs.getString("spon_Name"));
                    sponsor.setUniversity(rs.getString("university"));
                    sponsor.setSponsorIntro(rs.getString("sponIntro"));
                    sponsor.setEmail(rs.getString("email"));
                    sponsor.setSponsorId(rs.getInt("sponsorId"));

                    return sponsor;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 通过查询userToactivity的statement字段来判定活动详情页报名按钮的变化
     */
    public static int getUserToActivity_status(int activityId,int userId) {
        Connection connection = DataBase.getSQLConnection();
        int statement;
        String sql = "select * from userToactivity where activityId='" + activityId + "' and userId = '" + userId + "'";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                statement = Integer.valueOf(rs.getString("statement"));
                rs.close();
                st.close();
                connection.close();
                return statement;
            }
            else {
                rs.close();
                st.close();
                connection.close();
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 4;
    }


    /**
     * 活动报名操作，插入至userToactivity表
     */
    public static void enroll_activity(int activityId, int userId){
        Connection connection = DataBase.getSQLConnection();
        String sql = "INSERT INTO userToactivity VALUES(?,?,?)";
        if(connection != null){
            try{
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,userId);
                pstmt.setInt(2,activityId);
                pstmt.setInt(3,1);
                pstmt.executeUpdate();
                pstmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 查找活动报名的参与者，通过activityID在userToActivity查找参与者user;
     */
    public static ArrayList<User> getParticipant(int activityId)  {
        ArrayList<User> userArrayList = new ArrayList<>();
        Connection connection = DataBase.getSQLConnection();
        String sql = "select users.userId,userName,phoneNum,gender,university,Email,image,statement from users,userToactivity where activityId = '"+ activityId +"' and users.userId = userToactivity.userId";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    User user = new User();
                    user.setUserId(rs.getInt("userId"));
                    user.setImage(rs.getBytes("image"));
                    user.setUniversity(rs.getString("university"));
                    user.setGender(rs.getString("gender"));
                    user.setPhoneNum(rs.getString("phoneNum"));
                    user.setUserName(rs.getString("userName"));
                    user.setEmail(rs.getString("Email"));
                    user.setStatement(rs.getInt("statement"));
                    userArrayList.add(user);
                }
                rs.close();
                st.close();
                connection.close();
                return userArrayList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 发布新活动，插入
     * @param activity
     */
    public static void releaseActivity(Activity activity){
        Activity mActivity = activity;
        Connection connection = DataBase.getSQLConnection();
        String sql = "INSERT INTO activity VALUES(?,?,?,?,?,?,?,?)";
        if(connection !=null){
            try{
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1,mActivity.getActivityName());
                pstmt.setString(2,mActivity.getTime());
                pstmt.setString(3,mActivity.getAddress());
                pstmt.setString(4,mActivity.getAddressContent());
                pstmt.setString(5,mActivity.getType());
                pstmt.setInt(6,mActivity.getState());
                pstmt.setInt(7,mActivity.getSponsorId());
                pstmt.setBytes(8,mActivity.getPoster());
                pstmt.executeUpdate();
                pstmt.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /*
    修改usertoactivity的statement  id 为1表示主办方同意用户参与活动；0表示主办方拒绝用户参与活动；
     */
    public static void updateStatement(int userId,int activityId,int id){
        Connection connection = DataBase.getSQLConnection();
        String sql = null;
        if (connection != null){
            try{
                switch (id){
                    case 1:
                        sql = "update userToactivity set statement = '2' where userId = ? and activityId = ?";
                        break;
                    case 2:
                        sql = "delete from userToactivity where userId = ? and activityId = ?";
                        break;
                }
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,userId);
                pstmt.setInt(2,activityId);
                pstmt.executeUpdate();
                pstmt.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }


        }



    }


    /*
    活动浏览量，用户每查看一个活动详情页就update该活动的count,如果是第一次查看就插入
     */
    public static void updateBrowserCount(int userId,int activityId){
        Connection connection = DataBase.getSQLConnection();
        String sql = "select browserCount from browser where userId = '" + userId + "' and activityId = '" + activityId + "' ";
        String sql_insert = "INSERT INTO browser VALUES(?,?,?)";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    int count = rs.getInt("browserCount");
                    count = count+1;
                    String sql_update = "UPDATE browser set browserCount = ? where userId = ? and activityId = ?";
                    PreparedStatement ps = connection.prepareStatement(sql_update);
                    ps.setInt(1,count);
                    ps.setInt(2,userId);
                    ps.setInt(3,activityId);
                    ps.executeUpdate();
                    ps.close();
                    rs.close();
                    st.close();
                    connection.close();
                }
                else {
                    PreparedStatement pstmt = connection.prepareStatement(sql_insert);
                    pstmt.setInt(1,userId);
                    pstmt.setInt(2,activityId);
                    pstmt.setInt(3,1);
                    pstmt.executeUpdate();
                    pstmt.close();
                    rs.close();
                    st.close();
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    /*
    获取活动浏览量，通过activityId获取浏览量总和
     */
    public static int getBrowserCount(int activityId){
        Connection connection = DataBase.getSQLConnection();
        String sql = "select sum(browserCount) as browserCount from browser where activityId = '" + activityId + "'";
        if(connection != null){
            try{
                Statement st =  connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    int count = rs.getInt("browserCount");
                    return count;
                }
                else {
                    return 0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    /*
    发布新活动圈动态
     */
    public static void releaseTrend(Trend trend){
        Connection connection = DataBase.getSQLConnection();
        String sql = "INSERT INTO trend VALUES(?,?,?)";
        if(connection != null){
            try{
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,trend.getUserId());
                pstmt.setString(2,trend.getContent());
                pstmt.setBytes(3,trend.getImage());
                pstmt.executeUpdate();
                pstmt.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /*
    主页人气活动top5查询
     */
    public static ArrayList<Activity> getPopularActivity(){
        Connection connection = DataBase.getSQLConnection();
        String sql = "select top 5 activity.activityId,poster,activityName,b.browserCount from activity,(select activityId,sum(browserCount) as browserCount from browser group by activityId) b where activity.activityId = b.activityId order by browserCount desc";
        ArrayList<Activity> activityArrayList = new ArrayList<>();
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()){
                    Activity activity = new Activity();
                    activity.setActivityId(rs.getInt("activityId"));
                    activity.setPoster(rs.getBytes("poster"));
                    activity.setActivityName(rs.getString("activityName"));
                    activity.setBrowserCount(rs.getInt("browserCount"));
                    activityArrayList.add(activity);
                }
                rs.close();
                st.close();
                connection.close();
                return activityArrayList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    根据userId获取用户的浏览记录
     */
    public static ArrayList<Activity> getBrowserHistory(int userId){
        Connection connection = DataBase.getSQLConnection();
        ArrayList<Activity> activityArrayList = new ArrayList<>();
        String sql = "";
        if(connection != null){
            try{



                return activityArrayList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    获取活动圈内容
     */
    public static ArrayList<Trend> getTrend(){
        Connection connection = DataBase.getSQLConnection();
        ArrayList<Trend> trends = new ArrayList<>();
        String sql = "select trend.id,trend.userId,trend.trend,trend.image,userName,users.image as userImg from trend,users where users.userId = trend.userId";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()){
                    Trend trend = new Trend();
                    trend.setId(rs.getInt("id"));
                    trend.setUserId(rs.getInt("userId"));
                    trend.setContent(rs.getString("trend"));
                    trend.setUserName(rs.getString("userName"));
                    trend.setImage(rs.getBytes("image"));
                    trend.setUserImg(rs.getBytes("userImg"));
                    trends.add(trend);
                }
                rs.close();
                st.close();
                connection.close();
                return trends;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
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

    public static int getActivityState(int activityId){
        Connection connection = DataBase.getSQLConnection();
        String sql = "select state from activity where activityId = '" + activityId + "'";
        if(connection != null){
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    return rs.getInt("state");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取展示在活动详情页的主办方的部分信息
     * @param sponsorId
     * @return
     */
    public static Sponsor getSponsorInfo(int sponsorId){
        Connection connection = DataBase.getSQLConnection();
        Sponsor sponsor = new Sponsor();
        String sql  = "select sponsor.sponsorId,org_Name,sponIntro,sponsorImage,c.count from sponsor,(select * from (select sponsorId,count(sponsorId) as count from activity group by sponsorId) as b where sponsorId = '" + sponsorId + "' ) as c where c.sponsorId = sponsor.sponsorId";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    sponsor.setSponsorId(rs.getInt("sponsorId"));
                    sponsor.setSponsorIntro(rs.getString("sponIntro"));
                    sponsor.setSponsorImage(rs.getBytes("sponsorImage"));
                    sponsor.setActivityNumOfSponsor(rs.getInt("count"));
                    sponsor.setOrg_Name(rs.getString("org_Name"));
                }
                rs.close();
                st.close();
                connection.close();
                return sponsor;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<Activity> getSchoolActivity(int userId){
        Connection connection = DataBase.getSQLConnection();
        ArrayList<Activity> activityArrayList = new ArrayList<>();
        String sql = "select activityId,activityName,time,type,activity.sponsorId,poster from activity ,(select sponsorId from sponsor,(select * from users where userId  = '" + userId + "') as b where b.university = sponsor.university) as c where c.sponsorId = activity.sponsorId";
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    Activity activity = new Activity();
                    activity.setActivityId(rs.getInt("activityId"));
                    activity.setActivityName(rs.getString("activityName"));
                    activity.setPoster(rs.getBytes("poster"));
                    activity.setTime(rs.getString("time"));
                    activity.setType(rs.getString("type"));
                    activityArrayList.add(activity);
                }
                rs.close();
                st.close();
                connection.close();
                return activityArrayList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}