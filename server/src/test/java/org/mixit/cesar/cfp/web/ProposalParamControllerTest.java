package org.mixit.cesar.cfp.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mixit.cesar.security.model.Account;
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
                .andExpect(content().string("[\"AGILE_GAME\",\"CLOUD\",\"DESIGN\",\"DEVOPS\",\"INNOVATION\",\"IOT\",\"METHODO\",\"PROGRAMMING\",\"UX\",\"WEB\"]"))
                .andExpect(content().encoding("UTF-8"));
    }


}