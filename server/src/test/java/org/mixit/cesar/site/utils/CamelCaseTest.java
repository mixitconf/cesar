package org.mixit.cesar.site.utils;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.junit.Assert.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 22/02/16.
 */
public class CamelCaseTest {

    @Test
    public void should_return_null_when_string_null(){
        assertThat(CamelCase.camelCase(null)).isNull();
    }

    @Test
    public void should_return_A_when_string_is_a(){
        assertThat(CamelCase.camelCase("a")).isEqualTo("A");
    }

    @Test
    public void should_convertString(){
        assertThat(CamelCase.camelCase("Guillaume")).isEqualTo("guillaume");
    }
}