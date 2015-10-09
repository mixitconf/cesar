package org.mixit.cesar.security.authentification;

import static org.mixit.cesar.utils.ControlAdviceForTest.createContextForTest;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.repository.AuthorityRepository;
import org.mixit.cesar.security.oauth.OAuthFactory;
import org.mixit.cesar.web.AbsoluteUrlFactory;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test of {@link AuthenticationController}
 */
public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private OAuthFactory oauthFactory;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private AbsoluteUrlFactory urlFactory;

    @Before
    public void setUp() {
        AuthenticationController authenticationController = new AuthenticationController(accountRepository, authorityRepository, oauthFactory, applicationContext, urlFactory);
        mockMvc = standaloneSetup(authenticationController)
                .setHandlerExceptionResolvers(createContextForTest().handlerExceptionResolver())
                .build();
    }

    @Test
    public void should_not_login_when_credentials_are_empty() throws Exception {
        mockMvc.perform(post("/app/login").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("toto=titi"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_login_when_credentials_are_valids() throws Exception {
        when(accountRepository.findByLogin("mylogin")).thenReturn(new Account().setLogin("mylogin").setPassword("password"));

        mockMvc.perform(post("/app/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "mylogin")
                        .param("password", "password")
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void should_not_login_when_user_is_invalid() throws Exception {
        when(accountRepository.findByLogin("mylogin")).thenReturn(null);

        mockMvc.perform(post("/app/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "mylogin")
                        .param("password", "password")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_login_when_password_dont_match() throws Exception {
        when(accountRepository.findByLogin("mylogin")).thenReturn(new Account().setLogin("mylogin").setPassword("passwordAA"));

        mockMvc.perform(post("/app/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "mylogin")
                        .param("password", "password")
        )
                .andExpect(status().isBadRequest());
    }

}