package org.mixit.cesar;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = CesarApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Ignore
public class ContextTest {
    @Test
    public void shouldLoadConfig(){
        Assert.assertTrue(true);
    }

}
