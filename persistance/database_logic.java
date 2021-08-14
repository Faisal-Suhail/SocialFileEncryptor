package com.company.persistance;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class database_logic {
    private String sql; // string that stores the sql query
    private byte [] key; // a byte array to store the key
    private final String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=project_database;integratedSecurity=true"; // a string of where the database is

    // constructor
    public database_logic() {
    }


    // the login button database logic it checks if the username and password of the same row are the same
    public boolean login_logic(String user_txt, String pass_txt) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // get the connection to the sql server connection library
            Connection con = DriverManager.getConnection(url); // creates the connection by give the database location
            sql = "Select * from project_database.dbo.Account where UserName = ? and Password = ?"; // makes sql string into our needed query
            PreparedStatement pst = con.prepareStatement(sql); // create a prepared statement for the sql query
            pst.setString(1, user_txt); // put this value inside the prepared statement
            pst.setString(2, pass_txt); // put this value inside the prepared statement
            ResultSet rs = pst.executeQuery(); // executes the prepared statement and saves the result in a result set called rs
            boolean result = rs.next(); // sees if the next value of rs is true or false and then saves the value in result
            con.close(); // closes the connection to the database to save resources
            return result; // returns the result
        } catch (Exception var7) {
            JOptionPane.showMessageDialog(null, var7);
            return false;
        }
    }


    // the register button logic which checks the input and the creates the user in the database
    public String register_logic(String user_txt, String email_txt, String pass_txt) {
        if (!user_txt.equals("") && !email_txt.equals("") && !pass_txt.equals("")) {
            String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email_txt);
            if (!matcher.matches()) {
                return "please enter appropriate email";
            } else {
                try {
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    Connection con = DriverManager.getConnection(url);
                    String search = "select * from project_database.dbo.Account WHERE UserName = ? AND Email = ?";
                    PreparedStatement udp = con.prepareStatement(search);
                    udp.setString(1, user_txt);
                    udp.setString(2, email_txt);
                    ResultSet rs = udp.executeQuery();
                    boolean has_results = rs.next();
                    if (!has_results) {
                        try {
                            sql = "insert into project_database.dbo.Account(UserName, Email, Password, AccountType) VALUES (?,?,?,?)";
                            PreparedStatement pst = con.prepareStatement(sql);
                            pst.setString(1, user_txt);
                            pst.setString(2, email_txt);
                            pst.setString(3, pass_txt);
                            pst.setString(4, "user");
                            pst.executeUpdate();
                            con.close();
                            return "clear";
                        } catch (Exception var14) {
                            JOptionPane.showMessageDialog(null, var14);
                        }
                    } else {
                        do {
                            String userName = rs.getString("UserName");
                            String Email = rs.getString("Email");
                            if (user_txt.equals(userName) || email_txt.equals(Email)) {
                                con.close();
                                return "this username or email have been used";
                            }
                        } while(rs.next());
                    }
                } catch (Exception var15) {
                    JOptionPane.showMessageDialog(null, var15);
                }

                return "error";
            }
        } else {
            return "please dont leave any field empty";
        }
    }


    // this creates the table to show the friends of the user
    public DefaultTableModel friend_table_logic(DefaultTableModel model, int userID) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "SELECT * FROM project_database.dbo.Friends where UserID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                String e = rs.getString("FriendName");
                model.addRow(new Object[]{e});
            }

            con.close();
            return model;
        } catch (Exception var7) {
            JOptionPane.showMessageDialog(null, var7);
            return model;
        }
    }


    // gets userID
    public int getUserID(String user_txt) {
        int userID = 0;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "Select * from project_database.dbo.Account where UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, user_txt);

            for(ResultSet rs = pst.executeQuery(); rs.next(); userID = rs.getInt("UserID")) {
            }

            con.close();
            return userID;
        } catch (Exception var6) {
            JOptionPane.showMessageDialog(null, var6);
            return userID;
        }
    }


    // adds a friend to the user
    public String add_friend(int user_id, String friend_txt) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "Select * from project_database.dbo.Account where UserID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, user_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String user_txt = rs.getString("UserName");
                if (user_txt.equals(friend_txt)) {
                    con.close();
                    return "this is your username";
                }

                sql = "Select * from project_database.dbo.Account where UserName = ?";
                PreparedStatement selection = con.prepareStatement(sql);
                selection.setString(1, friend_txt);
                ResultSet rsts = selection.executeQuery();
                if (rsts.next()) {
                    int friend_id = rsts.getInt("UserID");
                    sql = "Select * from project_database.dbo.Friends where UserID=? and FriendName = ?";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setInt(1, user_id);
                    st.setString(2, friend_txt);
                    rs = st.executeQuery();
                    if (rs.next()) {
                        con.close();
                        return "You have already added this friend";
                    }

                    try {
                        sql = "insert into project_database.dbo.Friends(UserID,FriendID, FriendName) VALUES (?,?,?)";
                        PreparedStatement t = con.prepareStatement(sql);
                        t.setInt(1, user_id);
                        t.setInt(2, friend_id);
                        t.setString(3, friend_txt);
                        t.executeUpdate();
                        sql = "insert into project_database.dbo.Friends(UserID,FriendID, FriendName) VALUES (?,?,?)";
                        PreparedStatement a = con.prepareStatement(sql);
                        a.setInt(1, friend_id);
                        a.setInt(2, user_id);
                        a.setString(3, user_txt);
                        a.executeUpdate();
                        con.close();
                        return "Friend has been added successfully";
                    } catch (Exception var13) {
                        JOptionPane.showMessageDialog(null, var13);
                    }
                }
            } else {
                con.close();
            }

            return "The username given isn't a user";
        } catch (Exception var14) {
            JOptionPane.showMessageDialog(null, var14);
            return "fatal error";
        }
    }


    // removes a friend and there access from the database
    public String remove_friend(String friend_txt, int user_id) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "Select * from project_database.dbo.Account where UserName = ?";
            PreparedStatement selection = con.prepareStatement(sql);
            selection.setString(1, friend_txt);
            ResultSet rsts = selection.executeQuery();
            if (rsts.next()) {
                int friend_id = rsts.getInt("UserID");
                sql = "delete from project_database.dbo.Friends where FriendID = ? and UserID = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, friend_id);
                pst.setInt(2, user_id);
                pst.executeUpdate();
                sql = "delete from project_database.dbo.Friends where FriendID = ? and UserID = ?";
                PreparedStatement st = con.prepareStatement(sql);
                st.setInt(1, user_id);
                st.setInt(2, friend_id);
                st.executeUpdate();
                con.close();
                return "delete was successful";
            }
        } catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
        }

        return "fatal error";
    }


    //
    public String add_access(int file_id, String friend_txt, int user_id) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "select * from project_database.dbo.Friends where FriendName = ? and UserID =?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, friend_txt);
            pst.setInt(2, user_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int friend_id = rs.getInt("FriendID");
                sql = "insert into project_database.dbo.Access(FileID,UserID,OwnerID) VALUES (?,?,?)";
                PreparedStatement st = con.prepareStatement(sql);
                st.setInt(1, file_id);
                st.setInt(2, friend_id);
                st.setInt(3, user_id);
                st.executeUpdate();
                con.close();
                return "Access was given to friend successfully";
            } else {
                return "fatal error";
            }
        } catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
            return "fatal error";
        }
    }


    public String remove_access(int file_id, String friend_txt, int user_id) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "select * from project_database.dbo.Friends where FriendName = ? and UserID =?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, friend_txt);
            pst.setInt(2, user_id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                int friend_id = rst.getInt("FriendID");
                PreparedStatement st;
                sql = "delete from project_database.dbo.Access where FileID = ? and UserID = ? and OwnerID = ?";
                st = con.prepareStatement(sql);
                st.setInt(1, file_id);
                st.setInt(2, friend_id);
                st.setInt(3, user_id);
                st.executeUpdate();
                con.close();
                return "access was removed successfully";
            }
        } catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
        }

        return "fatal error";
    }


    public DefaultTableModel access_table_logic(DefaultTableModel model, int user_id, String friend_txt) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "select * from project_database.dbo.Friends where FriendName = ? and UserID =?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, friend_txt);
            pst.setInt(2, user_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int friend_id = rs.getInt("FriendID");
                sql = "SELECT * FROM project_database.dbo.Access where UserID = ? and  OwnerID = ?";
                PreparedStatement selection = con.prepareStatement(sql);
                selection.setInt(1, user_id);
                selection.setInt(2, user_id);
                ResultSet resultSet = selection.executeQuery();
                while(resultSet.next()) {
                    int fileID = resultSet.getInt("FileID");
                    sql = "SELECT * FROM project_database.dbo.Access where FileID = ? and UserID = ? and OwnerID = ?";
                    PreparedStatement selection_2 = con.prepareStatement(sql);
                    selection_2.setInt(1, fileID);
                    selection_2.setInt(2, friend_id);
                    selection_2.setInt(3, user_id);
                    ResultSet resultSet_2 = selection_2.executeQuery();
                    if (!resultSet_2.next()) {
                        model.addRow(new Object[]{fileID});
                    }
                }
            }

            con.close();
            return model;
        } catch (Exception var13) {
            JOptionPane.showMessageDialog(null, var13);
            return model;
        }
    }


    public DefaultTableModel remove_access_table_logic(DefaultTableModel model, int user_id, String friend_txt) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "select * from project_database.dbo.Friends where FriendName = ? and UserID =?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, friend_txt);
            pst.setInt(2, user_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int friend_id = rs.getInt("FriendID");
                sql = "SELECT * FROM project_database.dbo.Access where UserID = ? and OwnerID = ?";
                PreparedStatement st = con.prepareStatement(sql);
                st.setInt(1, friend_id);
                st.setInt(2, user_id);
                ResultSet rst = st.executeQuery();

                while(rst.next()) {
                    int e = rst.getInt("FileID");
                    model.addRow(new Object[]{e});
                }
            }

            con.close();
            return model;
        } catch (Exception var11) {
            JOptionPane.showMessageDialog(null, var11);
            return model;
        }
    }


    public void removeFriend_remove_access(String friend_txt, int user_id) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "select * from project_database.dbo.Friends where FriendName = ? and UserID =?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, friend_txt);
            pst.setInt(2, user_id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                int friend_id = rst.getInt("FriendID");
                sql = "delete from project_database.dbo.Access where UserID = ? and OwnerID = ?";
                PreparedStatement st = con.prepareStatement(sql);
                st.setInt(1, friend_id);
                st.setInt(2, user_id);
                st.executeUpdate();
                con.close();
                }
        } catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
        }
    }



    public void storekey(byte[] got_key) {
            key = got_key;
    }

    public int storehash(long hash){
        int file_id = 0;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            String query = "INSERT INTO project_database.dbo.[File] ([Key],Hash) VALUES (?,?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setBytes(1, key);
            pstmt.setLong(2,hash);
            pstmt.execute();
            String query2 = "select * from project_database.dbo.[File] where [Key] = ? and Hash = ?";
            PreparedStatement pstm = con.prepareStatement(query2);
            pstm.setBytes(1, key);
            pstm.setLong(2,hash);
            ResultSet rst = pstm.executeQuery();
            if (rst.next()) {
                file_id = rst.getInt("FileID");
                con.close();
                return file_id;
            }
            con.close();
        }catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
        }
        return file_id;
    }

    public int CHECKACCESS(long hash, int user_id){
        int file_id = 0;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "SELECT * FROM project_database.dbo.Access where UserID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1,user_id);
            ResultSet rst = pst.executeQuery();
            while (rst.next()){
            int temp_id = rst.getInt("FileID");
            sql = "SELECT * FROM project_database.dbo.[File] where FileID = ?";
            PreparedStatement selection = con.prepareStatement(sql);
            selection.setInt(1,temp_id);
            ResultSet resultSet = selection.executeQuery();
            while (resultSet.next()){
                long temp_hash = resultSet.getLong("Hash");
                if (hash == temp_hash) {
                    file_id = resultSet.getInt("FileID");
                }
            }
        }
            con.close();
            return file_id;
        }catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
        }
        return file_id;
    }

    public byte[] getkeyacc(int file_id){
        try {
            System.out.println(file_id);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            sql = "SELECT * FROM project_database.dbo.[File] where FileID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1,file_id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()){
                System.out.println("hello");
                byte [] key = rst.getBytes("Key");
                con.close();
                return key;
            }
        }catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
        }
        return null;
    }

    public void add_to_table(int file_id ,int userID) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            String query = "INSERT INTO project_database.dbo.Access (FileID,UserID,OwnerID) VALUES (?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, file_id);
            pstmt.setInt(2,userID);
            pstmt.setInt(3,userID);
            pstmt.execute();
            con.close();
        }
        catch (Exception var9) {
            JOptionPane.showMessageDialog(null, var9);
        }
    }
}