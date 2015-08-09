package org.mixit.cesar;

import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.mixit.cesar.model.event.Event;
import org.mixit.cesar.model.member.Interest;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.member.Participant;
import org.mixit.cesar.model.member.SharedLink;
import org.mixit.cesar.model.member.Speaker;
import org.mixit.cesar.model.member.Sponsor;
import org.mixit.cesar.model.member.Staff;
import org.mixit.cesar.model.session.Format;
import org.mixit.cesar.model.session.Keynote;
import org.mixit.cesar.model.session.LightningTalk;
import org.mixit.cesar.model.session.Session;
import org.mixit.cesar.model.session.Talk;
import org.mixit.cesar.model.session.Vote;
import org.mixit.cesar.model.session.Workshop;
import org.mixit.cesar.repository.EventRepository;
import org.mixit.cesar.repository.InterestRepository;
import org.mixit.cesar.repository.MemberRepository;
import org.mixit.cesar.repository.SessionRepository;
import org.mixit.cesar.repository.SharedLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Initialize data if db is empty
 */
@Component
public class CesarInitializer {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private SharedLinkRepository sharedLinkRepository;
    @Autowired
    private SessionRepository sessionRepository;

    private Long id = -1L;
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        if (eventRepository.count() == 0) {
            Event event = eventRepository.save(new Event().setId(-1L).setYear(2016).setCurrent(true));
            addInterests();

            //Friend
            addMember(new Member(), "Elodie", "Dupond", "Agilite");
            //Participant
            addMember(new Participant().addEvent(event), "Laurent", "Gayet", "Java");
            addMember(new Participant().addEvent(event), "Alfred", "Almendra", "Agilite");
            //Sponsor
            addMember(new Sponsor().addEvent(event).setLevel(Sponsor.Level.SILVER).setLogoUrl("viseo.png"), "Viseo", "", "Java");
            //Staff
            addMember(new Staff(), "Agnes", "Crepet", "Java");
            addMember(new Staff(), "Gregory", "Alexandre", "Agilite");
            //Speaker
            addSpeakers(event);
        }
    }

    private void addInterests() {
        interestRepository.save(new Interest().setName("Agilite"));
        interestRepository.save(new Interest().setName("Java"));
        interestRepository.save(new Interest().setName("Scala"));
    }

    private <T extends Member> T addMember(T member, String firstname, String lastname, String interest) {
        return (T) memberRepository.save(
                member
                        .setId(id--)
                        .setLogin(UUID.randomUUID().toString())
                        .setLastname(lastname)
                        .setFirstname(firstname)
                        .addInterest(interestRepository.findOne("Scala"))
        );
    }

    private <T extends Session> T addSession(T session, Speaker speaker, Event event, String title) {
        return (T) sessionRepository.save(
                session
                        .setId(id--)
                        .setTitle(title)
                        .addSpeaker(speaker)
                        .setEvent(event)
                        .setDescription("description of " + title)
                        .addVote(new Vote().setId(id--).setSession(session).setValue(false))
                        .addVote(new Vote().setId(id--).setSession(session).setValue(true))
                        .setValid(true)
        );
    }

    private void addSpeakers(Event event) {
        Speaker speaker;

        speaker = addMember(new Speaker().addEvent(event).setSessionType(Format.Keynote).setSessionAccepted(false), "Martin", "Odersky", "Scala");
        sharedLinkRepository.save(new SharedLink()
                        .setName("Twitter")
                        .setMember(speaker)
                        .setOrdernum(0)
                        .setURL("http://twitter.com/martin.ordersky")
        );
        sharedLinkRepository.save(new SharedLink()
                        .setName("Site")
                        .setMember(speaker)
                        .setOrdernum(1)
                        .setURL("http://martin.ordersky.com")
        );
        addSession(new Keynote(), speaker, event, "Le scala c'est super bien");
        addSession(new LightningTalk(), speaker, event, "Le scala en 5 minutes");

        speaker = addMember(new Speaker().addEvent(event).setSessionType(Format.Workshop).setSessionAccepted(true), "James", "Gosling", "Java");
        addSession(new Workshop(), speaker, event, "Le Java c'est super bien");

        speaker = addMember(new Speaker().addEvent(event).setSessionType(Format.Talk).setSessionAccepted(true), "Jean François", "Zobrist", "Agilite");
        addSession(new Talk(), speaker, event, "Favi l'entreprise libérée");
    }

}
