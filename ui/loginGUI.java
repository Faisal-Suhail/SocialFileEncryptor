package com.company.ui;

import com.company.persistance.database_logic;

import javax.swing.*;
import java.awt.*;


public class loginGUI extends JFrame {
    private JButton login_bt;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JTextField user_txt;
    private JPasswordField pass_txt;
    private JButton register_bt;
    private JLabel user_lb;
    private JLabel pass_lb;
    private JPanel RegisterPanel;
    private JButton NewRegister_bt;
    private JTextField newUser_txt;
    private JPasswordField newPassword_txt;
    private JLabel newUser_lb;
    private JLabel newPassword_lb;
    private JLabel newEmail_lb;
    private JTextField newEmail_txt;
    private JButton backLog_bt;
    private JLabel loginError_lb;

    private final CardLayout cl = (CardLayout)mainPanel.getLayout();

    loginGUI(){

        this.setTitle("Login Page");//sets title of JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit application when closed
        this.setContentPane(mainPanel);
        this.pack();
        ImageIcon image = new ImageIcon("src\\com\\company\\ui\\logo.png"); // create our icon
        this.setIconImage(image.getImage()); // changes our icon from default to new
        this.setResizable(false); // prevents the frame from being resized
        this.setLocationRelativeTo(null); // centers the frame
        this.setVisible(true); // makes frame visible
        // section for event listeners
        // checks if the username and password are in the database
        login_bt.addActionListener(e -> {
            database_logic logic = new database_logic(); // creates new instance of database_logic
            boolean login_bool = logic.login_logic(user_txt.getText(), pass_txt.getText()); // creates a bool that
            // get a true or false from login_logic. login logic takes username and password
            if (login_bool) { // checks if login bool is true or false if true go to project GUI if false show massage
                int userID = logic.getUserID(user_txt.getText());
                if (userID != 0){
                    projectGUI projectGUI = new projectGUI(userID); // creat new frame
                    dispose(); // destroy old frame
                } else {JOptionPane.showMessageDialog(null, "big error");}
            } else {
                JOptionPane.showMessageDialog(null, "username or password incorrect");
            }
        });
        // a button that goes to the account creation page
        register_bt.addActionListener(e -> cl.show(mainPanel, "CardRegister"));
        // a button that creates a new account
        NewRegister_bt.addActionListener(e -> {
            database_logic logic = new database_logic(); // creates new instance of database_logic
            // creates a string called register_string that takes from register_logic. register logic is given username email and password
            String register_string = logic.register_logic(newUser_txt.getText(), newEmail_txt.getText(), newPassword_txt.getText());
            if (register_string.equals("clear")) { // if there was no error in the operation then go back
                // to login back otherwise print error
                cl.show(mainPanel, "CardLogin");
            } else {
                JOptionPane.showMessageDialog(null, register_string);
            }
        });
        // a button that goes to the login page
        backLog_bt.addActionListener(e -> cl.show(mainPanel, "CardLogin"));
    }

    public static void main(String[] args) {

        loginGUI loginGUI = new loginGUI(); //creates new JFrame
    }
}