package org.mixit.cesar.cfp.service;

import static org.mixit.cesar.cfp.model.ProposalError.Code.REQUIRED;
import static org.mixit.cesar.cfp.model.ProposalError.Entity.PROPOSAL;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.cfp.model.ProposalError;
import org.mixit.cesar.cfp.model.ProposalStatus;
import org.mixit.cesar.cfp.repository.ProposalRepository;
import org.mixit.cesar.security.service.mail.MailBuilder;
import org.mixit.cesar.security.service.mail.MailerService;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.repository.InterestRepository;
import org.mixit.cesar.site.service.EventService;
import org.mixit.cesar.site.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProposalService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private MailBuilder mailBuilder;

    @Autowired
    private MailerService mailerService;

    /**
     * Check the required fields. A user can save a partial proposal and complete it later
     */
    public Set<ProposalError> check(Proposal proposal) {
        Set<ProposalError> errors = Sets.newHashSet();

        //A proposal is valid when speakers are valid
        proposal.getSpeakers().forEach(speaker -> errors.addAll(memberService.checkSpeakerData(speaker)));

        if (Objects.isNull(proposal.getFormat())) {
            errors.add(new ProposalError().setEntity(PROPOSAL).setCode(REQUIRED).setProperty("format"));
        }
        if (Objects.isNull(proposal.getSummary())) {
            errors.add(new ProposalError().setEntity(PROPOSAL).setCode(REQUIRED).setProperty("summary"));
        }
        if (Objects.isNull(proposal.getLevel())) {
            errors.add(new ProposalError().setEntity(PROPOSAL).setCode(REQUIRED).setProperty("level"));
        }
        if (Objects.isNull(proposal.getDescription())) {
            errors.add(new ProposalError().setEntity(PROPOSAL).setCode(REQUIRED).setProperty("description"));
        }
        if (Objects.isNull(proposal.getIdeaForNow())) {
            errors.add(new ProposalError().setEntity(PROPOSAL).setCode(REQUIRED).setProperty("ideaForNow"));
        }
        if (Objects.isNull(proposal.getLang())) {
            errors.add(new ProposalError().setEntity(PROPOSAL).setCode(REQUIRED).setProperty("lang"));
        }
        //To be valid the profile of the speakers have to be valid
        if (proposal.getSpeakers().isEmpty()) {
            errors.add(new ProposalError().setEntity(PROPOSAL).setCode(REQUIRED).setProperty("speaker"));
        }
        return errors;
    }

    public Proposal save(Proposal proposal) {
        Objects.requireNonNull(proposal, "proposal is required");
        boolean newProposal = false;

        Proposal proposalPersisted = proposalRepository.findOne(proposal.getId());
        Set<Interest> interests = proposal.getInterests();

        if (Objects.isNull(proposalPersisted)) {
            newProposal = true;
            proposalPersisted = proposalRepository.save(proposal
                    .setAddedAt(LocalDateTime.now())
                    .setEvent(EventService.getCurrent())
                    .setGuest(false)
                    .setStatus(ProposalStatus.CREATED)
                    .clearInterests());
        }
        else {
            proposalPersisted
                    .setCategory(proposal.getCategory())
                    .setDescription(proposal.getDescription())
                    .setFormat(proposal.getFormat())
                    .setTitle(proposal.getTitle())
                    .setSummary(proposal.getSummary())
                    .setMessageForStaff(proposal.getMessageForStaff())
                    .setMaxAttendees(proposal.getMaxAttendees())
                    .clearInterests()
                    .clearSpeakers()
                    .getSpeakers().addAll(proposal.getSpeakers());
        }

        //Interests
        proposalPersisted.getInterests().addAll(interests
                .stream()
                .map(i -> {
                    Interest interest = interestRepository.findByName(i.getName());
                    if (i == null) {
                        interest = interestRepository.save(new Interest().setName(i.getName()));
                    }
                    return interest;
                })
                .collect(Collectors.toSet()));

        //Validity
        proposalPersisted.setValid(check(proposalPersisted).isEmpty());
        if (proposalPersisted.isValid()) {
            proposalPersisted.setStatus(ProposalStatus.VALID);
        }
        else{
            proposalPersisted.setStatus(ProposalStatus.CREATED);
        }

        if (newProposal) {
            proposalPersisted.getSpeakers()
                    .stream()
                    .forEach(speaker -> mailerService.send(
                                    speaker.getEmail(),
                                    mailBuilder.getTitle(MailBuilder.TypeMail.SESSION_SUBMITION, null),
                                    mailBuilder.buildContent(MailBuilder.TypeMail.SESSION_SUBMITION, null, Optional.empty()))
                    );
        }

        return proposalPersisted;
    }
}
