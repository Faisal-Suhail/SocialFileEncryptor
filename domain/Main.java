package com.company.domain;

import com.company.persistance.database_logic;

import java.io.*;
import java.security.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


//test class
//class Main {
//
//    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException {
//
//	//EncKey encKey = new EncKey("AES");
//        File inputFile = new File("C:\\Users\\Devilzwrath\\IdeaProjects\\EncProject\\src\\com\\company\\dd.jpg");
//        File encryptedFile = new File("C:\\Users\\Devilzwrath\\IdeaProjects\\EncProject\\src\\com\\company\\document.encrypted");
//       encryptedFile.createNewFile();
//        File decryptedFile = new File("C:\\Users\\Devilzwrath\\IdeaProjects\\EncProject\\src\\com\\company\\document.decrypted");
//        //decryptedFile.createNewFile();
//        //EncryptionAlgorithm enc = new EncryptionAlgorithm("AES");
//        //enc.crypt(Cipher.DECRYPT_MODE,encKey.key, encryptedFile, decryptedFile);
//
//
//        String k = "[B@aec6354";
//        //Decryptor d = new Decryptor(encryptedFile,decryptedFile,"AES");
////        Encryptor e = new Encryptor(inputFile,encryptedFile,"AES",1);
////        e.encrypt();
//        //d.decrypt();
//
//
//        /*try {
//            CryptoUtils.encrypt(key, inputFile, encryptedFile);
//            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
//        } catch ( ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }*/
//    }
//}

class friend{
    int id;
    String userName;
    public friend(int id, String userName){
        this.id = id;
        this.userName = userName;
    }
}
class User {
    String userName;
    static int id;
    friend [] friends;
    String [] files;

    //gets created on login
    public User(int user_id){
        id = user_id;
    }

    public void addFriend(){

    }
    public void removeFriend(){

    }
    public void giveAccess(){

    }
    public void removeAccess(){

    }
}
class EncKey{

    protected byte[] keyRep; //array byte that holds the key
    protected String encAlg; //holds the type of encryption algorithm
    protected Key key, key1; // holds the encryption key in Key object

    //constructor,int mode checks if it should generate a new key or load an existing one
    public EncKey(int mode,String algorithm) throws NoSuchAlgorithmException, IOException {
        encAlg = algorithm;
        if (mode == 1){
        genKey();
//        saveKey();
        savekey();
        }
        else{loadKey();}
    }

    private void saveKey() {
        try{

            //Creating stream and writing the object
            FileOutputStream fout=new FileOutputStream("C:\\Users\\Devilzwrath\\IdeaProjects\\EncProject\\src\\com\\company\\f.txt");
            ObjectOutputStream out=new ObjectOutputStream(fout);
            out.writeObject(key);
            out.flush();
            //closing the stream
            out.close();
            System.out.println("success");
        }catch(Exception e){System.out.println(e);}
    }
    private void savekey() throws IOException {
        PersistanceFacade.savekey(keyRep);
    }
    private void loadke(String s) throws FileNotFoundException {
        
    }



    private void loadKey(){
        try{
        ObjectInputStream in=new ObjectInputStream(new FileInputStream("C:\\Users\\Devilzwrath\\IdeaProjects\\EncProject\\src\\com\\company\\f.txt"));
        key1=(Key)in.readObject();
        //instead of key1.getEncoded() we just replace it with a key byte array from DB
        key = new SecretKeySpec(key1.getEncoded(), 0, key1.getEncoded().length, "AES");
        //closing the stream
        in.close();
            ////////////////////////
           /* String s = "";
            BufferedReader reader =  new BufferedReader(new FileReader("C:\\Users\\Devilzwrath\\IdeaProjects\\EncProject\\src\\com\\company\\f.txt"));
            s = reader.readLine();
            reader.close();
            byte[] sk = Files.readAllBytes(Paths.get("C:\\Users\\Devilzwrath\\IdeaProjects\\EncProject\\src\\com\\company\\f.txt"));
            key = new SecretKeySpec(sk, 0, sk.length, "AES");*/
    }catch(Exception e){System.out.println(e);}
    }

    void genKey() throws NoSuchAlgorithmException {
        //create a keyGenerator instance
        KeyGenerator keyGen = KeyGenerator.getInstance(encAlg);
        //create this instance to set randomness
        SecureRandom secRan = new SecureRandom();
        keyGen.init(secRan);
        //generate the key
        key = keyGen.generateKey();

        //save the key in a byte array
        keyRep = key.getEncoded();
        //key1 = new SecretKeySpec(keyRep, encAlg);


        //test
        /*
        System.out.println(keyRep.length);
        System.out.println(keyRep.toString());
        System.out.println(key1.getEncoded().toString());
        for (byte bada : key.getEncoded() ) {
            System.out.println(bada);
        }
        System.out.println("\\");
        for (byte bada : key1.getEncoded() ) {
            System.out.println(bada);
        }*/
    }
}
class EncryptionAlgorithm{
    String AlgType = "AES"; //holds the type of algorithm
    EncAlgReturn encAlgReturn;
    //constructor,
    public EncryptionAlgorithm(String AlgType){
        this.AlgType = AlgType;
        this.encAlgReturn = new EncAlgReturn();
    }
    public EncAlgReturn crypt(int cipherType, Key key, File inputFile,
    File outputFile) {
        try {


            Cipher cipher = Cipher.getInstance(AlgType);
            cipher.init(cipherType, key);
            FileInputStream inStream = new FileInputStream(inputFile);
            byte[] inBytes = new byte[(int) inputFile.length()];
            inStream.read(inBytes);
            byte[] outBytes = cipher.doFinal(inBytes);
            FileOutputStream outStream = new FileOutputStream(outputFile);
            outStream.write(outBytes);

            inStream.close();
            outStream.close();
            encAlgReturn.out = outBytes;
            if (cipherType == 1) {
                encAlgReturn.Mes = "Encryption Successful";
            } else
                encAlgReturn.Mes = "Decryption Successful";
            return encAlgReturn;
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IOException |BadPaddingException
                | IllegalBlockSizeException ex) {
            System.out.println(ex.getMessage());
            encAlgReturn.Mes = ex.getMessage();
            return encAlgReturn;
        }

    }
    public void setAlgType(String AlgorithmType){
        this.AlgType = AlgorithmType;
    }
}
class Decryptor{
    Key key;
    File encryptedFile;
    File decryptedFile;
    String Alg;
    EncryptionAlgorithm encryptionAlgorithm;
    int fileType;
    byte[] keyrep;

    Decryptor(File encryptedFile, File decryptedFile, String Alg, int fileType,byte[]keyrep) throws NoSuchAlgorithmException, IOException {
        this.keyrep = keyrep;
        this.encryptedFile = encryptedFile;
        this.decryptedFile = decryptedFile;
        this.Alg = Alg;
        this.key = new SecretKeySpec(keyrep, 0, keyrep.length, "AES");
        this.encryptionAlgorithm = new EncryptionAlgorithm("AES");
        this.fileType = fileType;
    }

    public String decrypt() {
        return encryptionAlgorithm.crypt(Cipher.DECRYPT_MODE,this.key, encryptedFile, decryptedFile).Mes;
    }
}
class EncAlgReturn{
    byte[] out;
    String Mes;
}

class AccessReturn{
    byte[] key;
    String mess;
}

class Encryptor{
    EncKey enckey;
    File inFile;
    File encryptedFile;
    Key key;
    int fileType;
    byte[] outBytes;
    EncryptionAlgorithm encryptionAlgorithm;
    EncAlgReturn encAlgReturn;
    String filepath;

    Encryptor(File inFile, File encryptedFile, String Alg, int fileType, String filepath) throws NoSuchAlgorithmException, IOException {
        this.enckey = new EncKey(1,Alg);
        this.inFile = inFile;
        this.encryptedFile = encryptedFile;
        this.key = new SecretKeySpec(enckey.keyRep, Alg);
        this.fileType = fileType;
        this.encryptionAlgorithm = new EncryptionAlgorithm("AES");
        this.encAlgReturn = new EncAlgReturn();
        this.filepath = filepath;
    }

    String encrypt() throws IOException {

        this.encAlgReturn = encryptionAlgorithm.crypt(Cipher.ENCRYPT_MODE,this.key, inFile, encryptedFile);
        this.outBytes=this.encAlgReturn.out;
        //this creates the hash and sends it to the db facade for storing
        PersistanceFacade.savehash(makeHash());
        return encAlgReturn.Mes;
    }
    long makeHash() throws IOException {
        long hash = 1;
        long c = (long) Math.pow(2,25);
        int png=0, jpg = 1, txt= 2;
        boolean startxor = false;
        File encryptedFile2 = new File(this.filepath + "\\document.encrypted");
        FileInputStream in = new FileInputStream(encryptedFile2);
        byte[] be = new byte[(int)encryptedFile2.length()];
        in.read(be);

        for (byte b:be) {
            if(b!=0 && !startxor){
                hash = b * hash;
                if(Math.abs(hash) > c){
                    startxor = true;
                    }
                }
            else if(startxor){
                break;
            }
            System.out.println("hash: "+hash);
            System.out.println(b);
        }
        hash = hash ^ 31;

//        so this part modifies the end of the file to determine the file type
//        if (this.fileType == 0){
//        hash = Long.valueOf(String.valueOf(hash) + String.valueOf(png));
//        }
//        else if (this.fileType == 1){
//            hash = Long.valueOf(String.valueOf(hash) + String.valueOf(jpg));
//        }
//        else if (this.fileType == 2){
//            hash = Long.valueOf(String.valueOf(hash) + String.valueOf(txt));
//        }
        System.out.println("hash is "+hash);


        /////////


        return hash;
    }
}
class PersistanceFacade{
    static com.company.persistance.database_logic database_logic;
    static void savekey(byte[] key){
        database_logic = new database_logic();
        database_logic.storekey(key);
    }
    static void savehash(long hash){
        int file_id = database_logic.storehash(hash);
        database_logic.add_to_table(file_id, User.id);
    }
    static int checkAccess(long hash, int userid){
        System.out.println("l321");
        com.company.persistance.database_logic database_logic1 = new database_logic();
        return database_logic1 .CHECKACCESS(hash, userid);
    }
    byte[] getkey(int fid){
        com.company.persistance.database_logic database_logic1 = new database_logic();
        return database_logic1 .getkeyacc(fid);
    }
}


class AccessChecker{
    AccessReturn accessReturn = new AccessReturn();
    int fileid;
    long hash;
    public AccessReturn chkAccess(File infile, int fileType) throws IOException {
        makeHash(infile, fileType);
        //this is not final
        fileid = PersistanceFacade.checkAccess(hash, User.id);
        if(fileid >0){
            PersistanceFacade persistanceFacade = new PersistanceFacade();
            accessReturn.key = persistanceFacade.getkey(fileid);
            accessReturn.mess = "Successful";
            System.out.println(Arrays.toString(accessReturn.key));
            return accessReturn;
        }
        else {
            accessReturn.key = null;
            accessReturn.mess = "You don't have decryption privileges for this file";
            return accessReturn;
        }
    }
    void makeHash(File inFile, int fileType) throws IOException {
        hash = 1;
        long c = (long) Math.pow(2,25);
        int png=0, jpg = 1, txt= 2;
        boolean startxor = false;
        FileInputStream in = new FileInputStream(inFile);
        byte[] be = new byte[(int)inFile.length()];
        in.read(be);

        for (byte b:be ) {
            if(b!=0 && !startxor){
                hash = b * hash;
                if(Math.abs(hash) > c){
                    startxor = true;
                }
            }
            else if(startxor){
                break;
            }
            System.out.println("hash: "+hash);
            System.out.println(b);
        }
        hash = hash ^ 31;

        //so this part modifies the end of the file to determine the file type
//        if (fileType == 0){
//            hash = Long.valueOf(String.valueOf(hash) + String.valueOf(png));
//        }
//        else if (fileType == 1){
//            hash = Long.valueOf(String.valueOf(hash) + String.valueOf(jpg));
//        }
//        else if (fileType == 2){
//            hash = Long.valueOf(String.valueOf(hash) + String.valueOf(txt));
//        }
        System.out.println("hash is "+hash);
    }
}