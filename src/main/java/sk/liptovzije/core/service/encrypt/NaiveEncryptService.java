package sk.liptovzije.core.service.encrypt;

import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NaiveEncryptService implements IEncryptService {
    @Value("${app.salt}")
    private String salt;

    @Override
    public String encrypt(String password) {
        try {
            String saltedPass = salt + password;
            String hash       = TextCodec.BASE64.encode(saltedPass.getBytes("UTF-8"));
//            String resovled = new String(TextCodec.BASE64.decode(hash));
//            System.out.println("saltedPass: " + saltedPass + "\nresolved: " + resovled + "\nequals: " + saltedPass.equals(resovled));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return password;
    }

    @Override
    public boolean check(String checkPassword, String realPassword) {
        return checkPassword.equals(realPassword);
    }
}
