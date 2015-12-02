package org.mixit.cesar.site.utils;

import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Test;

/**
 * Test {@link Gravatar}
 */
public class GravatarTest {

    @Test
    public void should_find_image() {
        assertThat(Gravatar.imageExist("gui.ehret@gmail.com")).isTrue();
    }

    @Test
    public void should_not_find_image() {
        assertThat(Gravatar.imageExist("toto.tutu@gmail.com")).isFalse();
    }
}