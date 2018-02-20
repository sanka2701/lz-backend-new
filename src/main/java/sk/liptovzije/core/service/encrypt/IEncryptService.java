package sk.liptovzije.core.service.encrypt;

public interface IEncryptService {
    String encrypt(String password);
    boolean check(String checkPassword, String realPassword);
}
