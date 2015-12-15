package org.mixit.cesar.security.web;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.account.CreateCesarAccountService;
import org.mixit.cesar.security.service.account.CreateSocialAccountService;
import org.mixit.cesar.security.service.account.TokenService;
import org.mixit.cesar.security.service.authentification.CookieService;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test of {@link AccountController}
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 15/12/15.
 */
public class AccountControllerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CreateCesarAccountService createCesarAccountService;

    @Mock
    private CreateSocialAccountService createSocialAccountService;

    @Mock
    private AbsoluteUrlFactory urlFactory;

    @Mock
    private TokenService tokenService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private CookieService cookieService;

    @InjectMocks
    private AccountController accountController;

    @Before
    public void beforeEach() throws Exception {
        mockMvc = standaloneSetup(accountController).build();
    }

    @Test
    public void should_return_user_info() throws Exception {
        when(accountRepository.findByLogin("myLogin")).thenReturn(new Account().setLogin("myLogin"));
        mockMvc
                .perform(get("/app/account/cesar/myLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("login"))
                .andExpect(jsonPath("$.value").value("myLogin"));
    }

    @Test
    public void should_not_return_user_info_when_login_invalid() throws Exception {
        when(accountRepository.findByLogin("myLogin")).thenReturn(null);
        mockMvc
                .perform(get("/app/account/cesar/myLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("login"))
                .andExpect(jsonPath("$.value").isEmpty());
    }

    @Test
    public void should_return_acount() throws Exception {
        when(applicationContext.getBean(CurrentUser.class)).thenReturn(new CurrentUser().setCredentials(new Account().setOauthId("myOAuthId")));
        mockMvc
                .perform(get("/app/account/myOAuthId"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_update_account() throws Exception {
        when(applicationContext.getBean(CurrentUser.class)).thenReturn(new CurrentUser().setCredentials(new Account().setOauthId("myOAuthId")));
        mockMvc
                .perform(put("/app/account/myOAuthId")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(new Account()).getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_check_account() throws Exception {
        when(applicationContext.getBean(CurrentUser.class)).thenReturn(new CurrentUser().setCredentials(new Account().setOauthId("myOAuthId")));
        when(accountRepository.findByToken(anyString())).thenReturn(new Account().setToken("token"));

        mockMvc
                .perform(get("/app/account/check"))
                .andExpect(status().isOk());
    }

}