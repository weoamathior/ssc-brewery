package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncodingTest {

    static final String  PASSWORD = "password";
    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD)); //password!
    }
    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salt = "heytheremysalt";
        System.out.println(DigestUtils.md5DigestAsHex((PASSWORD+salt).getBytes()));
    }
}
