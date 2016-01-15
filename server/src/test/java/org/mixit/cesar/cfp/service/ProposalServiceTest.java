package org.mixit.cesar.cfp.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.cfp.model.ProposalError;
import org.mixit.cesar.cfp.repository.ProposalRepository;
import org.mixit.cesar.security.service.mail.MailBuilder;
import org.mixit.cesar.security.service.mail.MailerService;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.session.Format;
import org.mixit.cesar.site.model.session.Level;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.site.repository.InterestRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.service.MemberService;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test de {@link ProposalService}
 */
public class ProposalServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MemberService memberService;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private MailBuilder mailBuilder;

    @Mock
    private MailerService mailerService;

    @InjectMocks
    private ProposalService proposalService;

    @Test
    public void should_validate_empty_proposal() {
        Set<ProposalError> errors = proposalService.check(new Proposal());
        assertThat(errors)
                .hasSize(6)
                .extracting("property")
                .contains("summary", "description", "level", "speaker", "format", "ideaForNow");

    }

    @Test
    public void should_validate_proposal() {
        Mockito.when(memberRepository.findOne(Matchers.anyLong())).thenReturn(new Member());

        Set<ProposalError> errors = proposalService.check(
                new Proposal()
                        .setFormat(Format.Keynote)
                        .setSummary("summary")
                        .setLang(SessionLanguage.en)
                        .setLevel(Level.Beginner)
                        .setDescription("desc")
                        .setIdeaForNow("idea")
                        .addSpeaker(new Member()));

        assertThat(errors).isEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_NPE_when_saving_null_proposal() {
        proposalService.save(null, null);
    }

}