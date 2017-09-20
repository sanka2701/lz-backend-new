package sk.liptovzije.model.DO;

import io.jsonwebtoken.impl.TextCodec;
import sk.liptovzije.model.DTO.UserCredentialsDTO;

import java.security.SecureRandom;

/**
 * Created by jan.husenica on 8/31/2016.
 */
public class UserCredentials {
    static final String charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private String username;
    private String password;
    private String saltedPassword;
    private String salt;

    public UserCredentials(UserCredentialsDTO credentials){
        this(credentials.getUsername(), credentials.getPassword());
    }

    public UserCredentials(String username, String password ) {
        this.username = username;
        this.password = password;
        this.hashPasswordWithNewSalt();
    }

    public UserCredentials(UserCredentialsDTO credentials, String salt){
        this(credentials.getUsername(), credentials.getPassword(), salt);
    }

    public UserCredentials(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSaltedPassword() {
        return saltedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void hashPassword() {
        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = digest.digest((saltedPass).getBytes("UTF-8"));
            String saltedPass = salt != null ? salt + password : password;
            String hash = TextCodec.BASE64.encode(saltedPass.getBytes("UTF-8"));
            String resovled = new String(TextCodec.BASE64.decode(hash));

            System.out.println("saltedPass: " + saltedPass + "\nresolved: " + resovled + "\nequals: " + saltedPass.equals(resovled));

            this.saltedPassword = hash;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hashPasswordWithNewSalt() {
        this.salt = genRandomString(127);
        hashPassword();
    }

    private String genRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            sb.append(charset.charAt(random.nextInt(charset.length())));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCredentials that = (UserCredentials) o;

        if (!username.equals(that.username)) return false;
        return saltedPassword.equals(that.saltedPassword);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + saltedPassword.hashCode();
        return result;
    }
}
