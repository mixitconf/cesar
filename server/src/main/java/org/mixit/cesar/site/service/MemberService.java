package org.mixit.cesar.site.service;

import static org.mixit.cesar.cfp.model.ProposalError.Code.REQUIRED;
import static org.mixit.cesar.cfp.model.ProposalError.Entity.MEMBER;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;
import org.mixit.cesar.cfp.model.ProposalError;
import org.mixit.cesar.security.service.exception.UserNotFoundException;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.member.MemberEvent;
import org.mixit.cesar.site.model.member.SharedLink;
import org.mixit.cesar.site.repository.InterestRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.repository.SharedLinkRepository;
import org.mixit.cesar.site.utils.Gravatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private SharedLinkRepository sharedLinkRepository;

    /**
     * Check the required fields. A user can save a partial proposal and complete it later
     */
    public Set<ProposalError> checkSpeakerData(Member member) {
        Set<ProposalError> errors = Sets.newHashSet();

        //When the user comes on this method, his profile is created and some minimal informations have to be defined
        //before like lastname, firstname, login, mail... He can't change his email and his login on this screen
        //because these fields help to link an account and a member. But he can change other fields
        if (member.getFirstname()==null) {
            errors.add(new ProposalError().setEntity(MEMBER).setCode(REQUIRED).setProperty("firstname"));
        }
        if (member.getLastname()==null) {
            errors.add(new ProposalError().setEntity(MEMBER).setCode(REQUIRED).setProperty("lastname"));
        }
        if (member.getShortDescription()==null) {
            errors.add(new ProposalError().setEntity(MEMBER).setCode(REQUIRED).setProperty("shortDescription"));
        }
        if (member.getLongDescription()==null) {
            errors.add(new ProposalError().setEntity(MEMBER).setCode(REQUIRED).setProperty("longDescription"));
        }
//        if (member.getEmail()==null || !Gravatar.imageExist(member.getEmail())) {
//            errors.add(new ProposalError().setEntity(MEMBER).setCode(REQUIRED).setProperty("image"));
//        }

        return errors;
    }

    /**
     * Save a member
     */
    public <T extends Member> Member save(Member<T> member) {
        Objects.requireNonNull(member, "member is required");

        Member<T> persistedMember = memberRepository.findOne(member.getId());
        if (persistedMember == null) {
            throw new UserNotFoundException();
        }

        persistedMember
                .setLastname(member.getLastname())
                .setFirstname(member.getFirstname())
                .setCompany(member.getCompany())
                .setShortDescription(member.getShortDescription())
                .setLongDescription(member.getLongDescription())
                .setPublicProfile(Boolean.TRUE)
                .clearInterests()
                .clearSharedLinks();

        //If the user is not linked to the current event we do that
        if (!persistedMember.getMemberEvents().stream().anyMatch(e -> EventService.getCurrent().equals(e.getEvent()))) {
            persistedMember.getMemberEvents().add(new MemberEvent().setMember(persistedMember).setEvent(EventService.getCurrent()));
        }

        member.getSharedLinks().stream().forEach(link ->
                persistedMember.addSharedLink(
                        sharedLinkRepository.save(new SharedLink()
                                .setMember(persistedMember)
                                .setName(link.getName())
                                .setURL(link.getURL()))));

        member.getInterests().forEach(i -> {
            Interest interest = interestRepository.findByName(i.getName());
            if (i == null) {
                interest = interestRepository.save(new Interest().setName(i.getName()));
            }
            persistedMember.addInterest(interest);
        });

        return persistedMember;
    }
}
