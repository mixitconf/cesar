package org.mixit.cesar.security.service.authentification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CryptoServiceTest {

    private CryptoService cryptoService = new CryptoService();

    @Test
    public void encrypt_password(){
        assertThat(cryptoService.passwordHash("test")).isNotEmpty();
    }

    @Test
    public void genereate_password() {
        assertThat(cryptoService.generateRandomPassword()).isNotEmpty().hasSize(9);
    }
}