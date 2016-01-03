package org.mixit.cesar.cfp.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test of {@link ProposalParamController}
 *
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 15/12/15.
 */
public class ProposalParamControllerTest {

    private MockMvc mockMvc;

    @Before
    public void beforeEach() throws Exception {
        mockMvc = standaloneSetup(new ProposalParamController()).build();
    }

    @Test
    public void should_find_categories() throws Exception {
        mockMvc
                .perform(get("/app/cfp/param/category"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"AGILE\",\"BIGDATA\",\"CLOUD\",\"JAVA\",\"LEAN_STARTUP\",\"MICROSOFT\",\"WEB\"]"))
                .andExpect(content().encoding("UTF-8"));
    }

    @Test
    public void should_find_status() throws Exception {
        mockMvc
                .perform(get("/app/cfp/param/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"ACCEPTED\",\"CREATED\",\"REJECTED\",\"VALID\"]"))
                .andExpect(content().encoding("UTF-8"));
    }

    @Test
    public void should_find_format() throws Exception {
        mockMvc
                .perform(get("/app/cfp/param/format"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"Keynote\",\"LightningTalk\",\"Talk\",\"Workshop\"]"))
                .andExpect(content().encoding("UTF-8"));
    }

}