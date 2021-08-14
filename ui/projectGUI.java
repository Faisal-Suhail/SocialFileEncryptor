package com.company.ui;

import com.company.domain.*;
import com.company.persistance.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ENC_TableModel extends DefaultTableModel {
    ENC_TableModel( Object[] header, int rowcount){
        super(header,  rowcount);
    }

    public boolean isCellEditable(int row, int column){
        return false;
    }

}

public class projectGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel changePanel;
    private JPanel homePanel;
    private JPanel encryptionPanel;
    private JPanel friendsPanel;
    private JLabel welcome_lb;
    private JButton logout_bt;
    private JLabel encryption_lb;
    private JButton chooseEN_bt;
    private JPanel resultPanel;
    private JLabel result_lb;
    private JButton chooseDE_bt;
    private JTable friends_tbl;
    private JButton addFriend_bt;
    private JButton removeFriend_bt;
    private JButton giveAccess_bt;
    private JButton removeAccess_bt;
    private JScrollPane Friend_pane;
    private JTabbedPane tabs;
    private JTable give_tbl;
    private JButton give_access_bt;
    private JButton back_bt;
    private JButton back_2_bt;
    private JPanel AccessPanel;
    private JScrollPane give_pane;
    private JPanel givePanel;
    private JPanel remove_panel;
    private JScrollPane remove_pane;
    private JTable remove_tbl;
    private JButton remove_bt;
    private JButton EN_DE_bt;
    private int row;
    private int column;
    private String friendName;
    private String answer;
    private int fileID;


    private final FileDialog fd = new FileDialog(new JFrame(), "Choose a file", FileDialog.LOAD); // creates a frame
    // that gets the file that the user chooses

    private final CardLayout cl = (CardLayout)changePanel.getLayout(); // creates the cardLayout that is used for
    // changing panels

    private final CardLayout ap = (CardLayout)AccessPanel.getLayout(); // creates the cardLayout that is used for
    // changing panels

    private DefaultTableModel model = new ENC_TableModel(new String[]{"Friend Name"}, 0);

    projectGUI(int UserID){

        systemHandler.createuser(UserID);



        // section for text
        welcome_lb.setText("<html><div align=center><p style=\"width:300px\">"+"Welcome to our encryption system. There"
                + " are three tabs to choose from being Home, Encrypt/Decrypt, and Friends. Click on the Encrypt/Decrypt"
                + " tab will show those functions, while Friends shows you your current friends. you can click on the " +
                "logout button to go back to the login page."
                + "</p></div></html>"); // sets label to specified text and use HTML and CSS to set some properties
        encryption_lb.setText("<html><div align=center><p style=\"width:300px\">"+"In this page you can choose the file"
                + " you want to be encrypted or decrypted."
                + "</p></div></html>"); // sets label to specified text and use HTML and CSS to set some properties
        // sets label to specified text and use HTML and CSS to set some properties




        // section for frame properties
        database_logic logic = new database_logic(); // creates a new database_logic
        model = logic.friend_table_logic(model, UserID); // uses the friend_table_logic to set the model for friends_tbl
        friends_tbl.setModel(model);
        this.setTitle("Encryption System");//sets title of JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit application when closed
        this.setContentPane(mainPanel);
        this.pack();
        ImageIcon image = new ImageIcon("src\\com\\company\\ui\\logo.png"); // create our icon
        this.setIconImage(image.getImage()); // changes our icon from default to new
        this.setResizable(false); // prevents the frame from being resized
        this.setLocationRelativeTo(null); // centers the frame
        this.setSize(440, 400); // sets the size of the window
        this.setVisible(true); // makes frame visible
        // section for event listeners
        // makes button go to the loginGUI form and destroys the current window
        logout_bt.addActionListener(e -> {
            loginGUI loginGUI = new loginGUI();// creat new frame
            dispose(); // destroy old frame
        });
        // goes back to the previous card




        // after the click of this button open file explorer and let the user chose a file
        chooseEN_bt.addActionListener(e -> {
            fd.setFile("*.jpg;*.png;*.txt");
            fd.setDirectory("C:\\"); // set directory for file explorer to the c drive
            fd.setVisible(true);
            String filename = fd.getFile();
            if (filename == null) {
                JOptionPane.showMessageDialog(null, "You didn't choose any file");
            }
            else {
                String extension = filename.substring(filename.lastIndexOf('.') + 1);
                filename = fd.getFile();
                String AbsolutePath = fd.getDirectory() + fd.getFile();
                String directoryPath = fd.getDirectory();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "You chose "
                        + filename + " with Absolute path of " + AbsolutePath + " would you like to encrypt this file"
                        ,"conformation",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    String result = systemHandler.encrypt(AbsolutePath, directoryPath, extension);
                    result_lb.setText("<html><div align=center><p style=\"width:300px\">"
                            + result + "</p></div></html>");
                    cl.show(changePanel, "Card_result");
                }
            }
            }
        );

        // lets the user choose the file to decrypt and then decrypts it
        chooseDE_bt.addActionListener(e -> {
            fd.setFile("*.encrypted");
            fd.setDirectory("C:\\"); // set directory for file explorer to the c drive
            fd.setVisible(true);
            String filename = fd.getFile();
            if (filename == null) {
                JOptionPane.showMessageDialog(null, "You didn't choose any file");
            }
            else {
                String extension = filename.substring(filename.lastIndexOf('.') + 1);
                filename = fd.getFile();
                String AbsolutePath = fd.getDirectory() + fd.getFile();
                String directoryPath = fd.getDirectory();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "You chose "
                                + filename + " with Absolute path of " + AbsolutePath + " would you like to encrypt this file"
                        ,"conformation",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    String result = systemHandler.decrypt(AbsolutePath, directoryPath, extension);
                    result_lb.setText("<html><div align=center><p style=\"width:300px\">"
                            + result + "</p></div></html>");
                    cl.show(changePanel, "Card_result");
                }
            }
        });



        // goes back to the previous card
        // adds friend to account
        addFriend_bt.addActionListener(e -> {
            String result = JOptionPane.showInputDialog(
                    changePanel,
                    "Write the name of the friend you want to add",
                    "add friend",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (result != null && result.length() > 0) {
                String answer = logic.add_friend(UserID, result);
                model = new ENC_TableModel(new String[]{"Friend Name"}, 0);
                model = logic.friend_table_logic(model, UserID);
                friends_tbl.setModel(model);
                JOptionPane.showMessageDialog(null, answer);
                } else

            {
                JOptionPane.showMessageDialog(null, "no name was written");
            }
            }
        );


        // removes friend from user
        removeFriend_bt.addActionListener(e -> {
            row = friends_tbl.getSelectedRow();
            column = friends_tbl.getSelectedColumn();
            if (column == 0){
                friendName = (String) friends_tbl.getValueAt(row, column);
                logic.removeFriend_remove_access(friendName, UserID);
                answer = logic.remove_friend(friendName, UserID);
                model = new ENC_TableModel(new String[]{"Friend Name"}, 0);
                model = logic.friend_table_logic(model, UserID);
                friends_tbl.setModel(model);
                JOptionPane.showMessageDialog(null, answer);
            } else{
                JOptionPane.showMessageDialog(null, "Please select a name in the friends name column");
            }
        });


        // goes to the cardGP and sets the table
        giveAccess_bt.addActionListener(e -> {
            row = friends_tbl.getSelectedRow();
            column = friends_tbl.getSelectedColumn();
            if (column == 0) {
                friendName = (String) friends_tbl.getValueAt(row, column);
                model = new ENC_TableModel(new String[]{"You can give access to"}, 0);
                model = logic.access_table_logic(model, UserID, friendName);
                give_tbl.setModel(model);
                ap.show(AccessPanel, "CardGP");
            }else{
                JOptionPane.showMessageDialog(null, "Please select a name in the friends name column");
            }
        });


        // goes to the cardRP and sets the table
        removeAccess_bt.addActionListener(e -> {
            row = friends_tbl.getSelectedRow();
            column = friends_tbl.getSelectedColumn();
            if (column == 0) {
                friendName = (String) friends_tbl.getValueAt(row, column);
                model = new ENC_TableModel(new String[]{"Can remove access from"}, 0);
                model = logic.remove_access_table_logic(model, UserID, friendName);
                remove_tbl.setModel(model);
                ap.show(AccessPanel, "CardRP");
            }else{
                JOptionPane.showMessageDialog(null, "Please select a name in the friends name column");
            }
        });


        // removes access from a friend
        remove_bt.addActionListener(e -> {
            row = remove_tbl.getSelectedRow();
            column = remove_tbl.getSelectedColumn();
            if (column == 0) {
                fileID = Integer.parseInt(remove_tbl.getValueAt(row, column).toString());
                answer = logic.remove_access(fileID,friendName, UserID);
                model = new ENC_TableModel(new String[]{"Can remove access to"}, 0);
                model = logic.remove_access_table_logic(model, UserID, friendName);
                remove_tbl.setModel(model);
                JOptionPane.showMessageDialog(null, answer);
            }else{
                JOptionPane.showMessageDialog(null, "Please a FileID in from the column");
            }
        });


        // gives access to a friend
        give_access_bt.addActionListener(e -> {
            row = give_tbl.getSelectedRow();
            column = give_tbl.getSelectedColumn();
            if (column == 0){
                fileID = Integer.parseInt(give_tbl.getValueAt(row, column).toString());
                answer = logic.add_access(fileID,friendName, UserID);
                model = new ENC_TableModel(new String[]{"You can give access to"}, 0);
                model = logic.access_table_logic(model, UserID, friendName);
                give_tbl.setModel(model);
                JOptionPane.showMessageDialog(null, answer);
            } else{
                JOptionPane.showMessageDialog(null, "Please a FileID in from the column");
            }
        });


        // both buttons go to cardFP
        back_2_bt.addActionListener(e -> ap.show(AccessPanel, "CardFP"));
        back_bt.addActionListener(e -> ap.show(AccessPanel, "CardFP"));


        // both buttons go to cardEN
        EN_DE_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(changePanel,"CardEN");
            }
        });
    }

//    public static void main(String[] args) {
//        database_logic database_logic = new database_logic();
//        int UserID = 4;
////        database_logic.add_to_table(UserID);
//        systemHandler.createuser(UserID);
//        projectGUI projectGUI = new projectGUI(UserID); //creates new JFrame
//    }
}

