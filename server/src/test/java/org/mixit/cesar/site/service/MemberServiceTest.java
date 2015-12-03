package org.mixit.cesar.site.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.cfp.model.ProposalError;
import org.mixit.cesar.security.service.exception.UserNotFoundException;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.member.SharedLink;
import org.mixit.cesar.site.repository.InterestRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.repository.SharedLinkRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test de {org.mixit.cesar.site.service.MemberService}
 */
public class MemberServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private SharedLinkRepository sharedLinkRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void should_validate_empty_member(){
        Set<ProposalError> errors = memberService.checkSpeakerData(new Member());
        assertThat(errors)
                .hasSize(5)
                .extracting("property")
                .contains("firstname", "lastname", "shortDescription", "longDescription", "image");

    }

    @Test
    public void should_validate_member(){
        Set<ProposalError> errors = memberService.checkSpeakerData(
                new Member()
                        .setLastname("name")
                        .setFirstname("toto")
                        .setShortDescription("desc")
                        .setLongDescription("long")
                        .setEmail("gui.ehret@gmail.com"));

        assertThat(errors).isEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_NPE_when_arg_empty(){
        memberService.save(null);
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_when_id_null(){
        memberService.save(new Member());
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_when_user_not_exist(){
        memberService.save(new Member().setId(1L));
    }

    @Test
    public void should_save_user(){

        when(memberRepository.findOne(1L)).thenReturn(new Member());

        Member member = memberService.save(new Member()
                        .setId(1L)
                        .setLastname("name")
                        .setFirstname("toto")
                        .setShortDescription("desc")
                        .setLongDescription("long")
                        .setEmail("gui.ehret@gmail.com")
                        .setCompany("company")
                        .addSharedLink(new SharedLink().setName("twitter").setURL("@twitter"))
                        .addInterest(new Interest().setName("Java"))
        );

        assertThat(member.getLongDescription()).isEqualTo("long");
    }
}