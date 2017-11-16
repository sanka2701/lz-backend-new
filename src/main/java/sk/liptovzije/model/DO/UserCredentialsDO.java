package sk.liptovzije.model.DO;

import io.jsonwebtoken.impl.TextCodec;
import sk.liptovzije.model.DTO.UserCredentialsDTO;

import javax.persistence.*;
import java.security.SecureRandom;

/**
 * Created by jan.husenica on 8/31/2016.
 */
@Entity
@Table(name = "credentials")
public class UserCredentialsDO {
    static final String charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    public UserCredentialsDO() {
    }

    public UserCredentialsDO(Long userId, String username, String password, String salt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.salt = salt;
//        this.hashPassword();
    }

    public UserCredentialsDO(Long userId, String username, String password ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        // todo reevaulata
//        this.hashPasswordWithNewSalt();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public void hashPassword() {
        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = digest.digest((saltedPass).getBytes("UTF-8"));
            String saltedPass = salt + password;
            this.password = TextCodec.BASE64.encode(saltedPass.getBytes("UTF-8"));;

//            String resovled = new String(TextCodec.BASE64.decode(hash));
//            System.out.println("saltedPass: " + saltedPass + "\nresolved: " + resovled + "\nequals: " + saltedPass.equals(resovled));

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

        UserCredentialsDO that = (UserCredentialsDO) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return salt != null ? salt.equals(that.salt) : that.salt == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }
}
