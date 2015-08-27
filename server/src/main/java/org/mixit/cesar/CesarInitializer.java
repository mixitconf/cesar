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
import org.mixit.cesar.model.session.Level;
import org.mixit.cesar.model.session.LightningTalk;
import org.mixit.cesar.model.session.Session;
import org.mixit.cesar.model.session.SessionLanguage;
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
            addMember("Elodie", "Dupond", "Agilite", new Member());
            //Participant
            addMember("Laurent", "Gayet", "Java", new Participant().addEvent(event));
            addMember("Alfred", "Almendra", "Agilite", new Participant().addEvent(event));
            //Sponsor
            addMember("Open", "", "Java",
                    new Sponsor()
                            .addEvent(event)
                            .setLevel(Sponsor.Level.GOLD)
                            .setCompany("Open")
                            .setLogoUrl("logo-open.jpg")
                            .setShortDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
            addMember("Viseo", "", "Java",
                    new Sponsor()
                            .addEvent(event)
                            .setCompany("Viseo")
                            .setLevel(Sponsor.Level.SILVER)
                            .setLogoUrl("logo-od.png")
                            .setShortDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
            addMember("Atlassian", "", "Java",
                    new Sponsor()
                            .addEvent(event)
                            .setCompany("Atlassian")
                            .setLevel(Sponsor.Level.BRONZE)
                            .setLogoUrl("logo-atlassian.png")
                            .setShortDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
            //Staff
            addMember("Agnes",
                    "Crepet",
                    "Java",
                    new Staff()
                            .setShortDescription("Ninja Squad Co-founder - JUG Leader : Lyon JUG & Duchess France - Mix-IT Co-founder")
                            .setLongDescription("Agnès est une activiste. Une Java activiste tout d'abord ! Depuis 11 ans, elle prend plaisir à bâtir des architectures Java et les implémenter.\n" +
                                    "\n" +
                                    "Parce qu'elle aime également apprendre et partager, elle est très active dans la communauté. Elle est leader de 2 Java Users Groups en France : le Lyon JUG et Duchess France, et a été élue Java Champion en 2012! Elle est également co-fondatrice de la société coopérative de développeurs passionnés Ninja Squad\n" +
                                    "\n" +
                                    "Elle aime parler Design Patterns, Objet ou Agilité dans des conférences à travers le monde. Vous pouvez lire ses articles techniques sur des frameworks de l'écosystème Java dans la presse et ses interviews sur le blog des Duchesses.\n" +
                                    "\n" +
                                    "Parce qu'elle ne regarde que très rarement la télévision, elle a encore du temps libre pour l'organisation de la conférence Mix-IT, mix de Java et d'Agilité, qu'elle a co-fondé. Vous pouvez également l'entendre au micro du podcast Cast-IT qui aborde ses sujets de prédilection.\n" +
                                    "\n" +
                                    "Et parce que les nuits sont courtes, elle garde un peu de temps pour l'association Avataria dont elle est présidente, qui organise des concerts, festivals ou Linux Party, dans des lieux du patrimoine industriel de sa ville !\n" +
                                    "\n")
                            .setEmail("agnes.crepet@gmail.com"));

            addMember("Gregory", "Alexandre", "Agilite",
                    new Staff().setEmail("g.alexandre@coactiv.fr"));
            //Speaker
            addSpeakers(event);
        }
    }

    private void addInterests() {
        interestRepository.save(new Interest().setName("Agilite"));
        interestRepository.save(new Interest().setName("Java"));
        interestRepository.save(new Interest().setName("Scala"));
    }

    private <T extends Member> T addMember(String firstname, String lastname, String interest, T member) {
        return (T) memberRepository.save(
                member
                        .setId(id--)
                        .setLogin(UUID.randomUUID().toString())
                        .setLastname(lastname)
                        .setFirstname(firstname)
                        .addInterest(interestRepository.findOne(interest))
        );
    }

    private <T extends Session> T addSession(T session, Speaker speaker, Event event, String title, boolean accepted) {
        return (T) sessionRepository.save(
                session
                        .setId(id--)
                        .setTitle(title)
                        .addSpeaker(speaker)
                        .setEvent(event)
                        .setDescription("description of " + title)
                        .setSessionAccepted(accepted)
                        .addVote(new Vote().setId(id--).setSession(session).setValue(false))
                        .addVote(new Vote().setId(id--).setSession(session).setValue(true))
                        .setSummary("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                        .setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "")
                        .setIdeaForNow("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "")
                        .setValid(true)
                        .setLevel(Level.Beginner)
                        .setLang(SessionLanguage.fr)
                        .setGuest(true)
        );
    }

    private void addSpeakers(Event event) {
        Speaker speaker;

        speaker = addMember("Martin", "Odersky", "Scala",
                new Speaker()
                        .addEvent(event)
                        .setSessionType(Format.Keynote)
                        .setShortDescription("Sébastien Blanc is software engineer with 10 years of experience. He works at Red Hat and focus on Open Source libraries for Mobile.")
                        .setEmail("martin.odersky@pipo.com"));
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
        addSession(new Keynote(), speaker, event, "Le scala c'est super bien", true);
        addSession(new Keynote(), speaker, event, "Java bashing", false);
        addSession(new LightningTalk(), speaker, event, "Le scala en 5 minutes", true);

        speaker = addMember("James", "Gosling", "Java",
                new Speaker()
                        .addEvent(event)
                        .setSessionType(Format.Workshop)
                        .setShortDescription("Open Web Developer Advocate at Google • Tools, Performance, Animation, UX • HFR enthusiast • Creator of jQuery UI")
                        .setEmail("james.gosling@pipo.com"));
        addSession(new Workshop(), speaker, event, "Le Java c'est super bien", true);

        speaker = addMember("Jean François", "Zobrist", "Agilite",
                new Speaker()
                        .addEvent(event)
                        .setSessionType(Format.Talk)
                        .setShortDescription("Transdisciplinary engineer. Building software, studying humans, designing interactions, thinking society.")
                        .setEmail("jf.zobrist@pipo.com"));
        ;
        addSession(new Talk(), speaker, event, "Favi l'entreprise libérée", true);
    }

}
