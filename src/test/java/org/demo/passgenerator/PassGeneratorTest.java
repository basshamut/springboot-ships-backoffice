package org.demo.passgenerator;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class PassGeneratorTest {

    @Test
    @Ignore
    public void getPass() {
        var encoder = new BCryptPasswordEncoder();
        var password = "miContraseña123";
        var passwordBase64Encode = Base64.getEncoder().encodeToString(password.getBytes());
        var hashedPassword = encoder.encode(password);
        var hashedAndBase64Encode = Base64.getEncoder().encodeToString(hashedPassword.getBytes());

        // Imprime la contraseña encriptada
        log.info("Contraseña: " + password);
        log.info("Contraseña en base 64: " + passwordBase64Encode);
        log.info("Contraseña encriptada: " + hashedPassword);
        log.info("Contraseña encriptada y en base64: " + hashedAndBase64Encode);
    }
}
