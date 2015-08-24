package org.mixit.cesar.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test de {@link HashUtil}
 */
public class HashUtilTest {

    @Test
    public void should_compute_hash_for_mail(){
        assertThat(HashUtil.md5Hex("gui.ehret@gmail.com")).isEqualTo("aa4d47d1016e45c23b6af05ec11c0a9c");
    }
}