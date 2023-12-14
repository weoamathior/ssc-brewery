package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

public class PasswordEncodingTest {

    static final String  PASSWORD = "password";
    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salt = "heytheremysalt";
        System.out.println(DigestUtils.md5DigestAsHex((PASSWORD+salt).getBytes()));
    }
}
