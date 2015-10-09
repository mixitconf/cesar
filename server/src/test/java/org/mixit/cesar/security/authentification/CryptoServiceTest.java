package org.mixit.cesar.security.authentification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CryptoServiceTest {

    private CryptoService cryptoService = new CryptoService();

    @Test
    public void should_encrypt_password(){
        assertThat(cryptoService.passwordHash("test")).isNotEmpty();
    }

}