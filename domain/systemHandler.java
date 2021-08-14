package com.company.domain;

import java.io.File;

public class systemHandler {

    public static void createuser(int user_id){
        User.id = user_id;
    }

    public static String encrypt(String filepath, String directorypath, String fileType){
        String mes = null;
        try {
            int filetype = 0;
            if(fileType.equalsIgnoreCase("png")){;}
            else if(fileType.equalsIgnoreCase("jpg")){filetype = 1;}
            else if(fileType.equalsIgnoreCase("txt")){filetype = 2;}
            File inputFile = new File(filepath);
            File encryptedFile = new File(directorypath+"\\document.encrypted");
            encryptedFile.createNewFile();
            Encryptor encryptor = new Encryptor(inputFile, encryptedFile, "AES",filetype,directorypath);
            mes = encryptor.encrypt();
            return mes;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return mes;
        }
    }

    public static String decrypt(String filepath, String directorypath, String fileType){
        String mes = null;
        try {
            int filetype = 0;
            if (fileType.toLowerCase() == "png") {
                ;
            } else if (fileType.toLowerCase() == "jpg") {
                filetype = 1;
            } else if (fileType.toLowerCase() == "txt") {
                filetype = 2;
            }
            AccessChecker accessChecker = new AccessChecker();
            AccessReturn accessReturn;
            File inputFile = new File(filepath);
            accessReturn = accessChecker.chkAccess(inputFile, filetype);
            mes = accessReturn.mess;
            if(!mes.equals("Successful")){
                return mes;
            }
            else {


                File decryptedFile = new File(directorypath + "\\document.decrypted");
                decryptedFile.createNewFile();
                Decryptor decryptor = new Decryptor(inputFile, decryptedFile, "AES", filetype, accessReturn.key);
                mes = decryptor.decrypt();
                return mes;
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return mes;
        }
    }

}
