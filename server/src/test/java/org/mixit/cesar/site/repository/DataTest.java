package org.mixit.cesar.site.repository;


import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.google.common.base.Optional;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import com.ninja_squad.dbsetup.operation.Operation;
import org.mixit.cesar.site.model.session.Format;

public class DataTest {

    public static final Operation DELETE_ALL = deleteAllFrom(
            "ArticleComment", "Article", "SessionComment", "Session_Interest", "Session_Member", "Vote", "Session",
            "SharedLink", "MemberEvent", "Member_Interest", "Account_Authority", "Authority", "Account", "Member",
            "Event", "Interest"
            );

    public static Operation INSERT_EVENT = Operations.sequenceOf(
            insertInto("Event")
                    .withGeneratedValue("id", ValueGenerators.sequence())
                    .columns("year", "current")
                    .values("2014", false)
                    .values("2015", false)
                    .values("2016", true)
                    .build()
    );

    public static Operation INSERT_INTEREST = Operations.sequenceOf(
            insertInto("Interest")
                    .columns("id","name")
                    .values(-10,"Agilite")
                    .values(-11, "Java")
                    .build()
    );

    public static Operation INSERT_MEMBER = Operations.sequenceOf(
            insertInto("Member")
                    .withGeneratedValue("id", ValueGenerators.sequence())
                    .columns("DTYPE", "FIRSTNAME", "LASTNAME", "LOGIN", "NBCONSULTS", "PUBLICPROFILE", "EMAIL")
                    .values("Staff", "Guillaume", "EHRET", "guillaume", 1, "true", "guillaume@dev-mind.fr")
                    .build()
    );

    public static Operation INSERT_SESSION = Operations.sequenceOf(
            INSERT_EVENT,
            INSERT_INTEREST,
            INSERT_MEMBER,
            insertInto("Session")
                    .withGeneratedValue("id", ValueGenerators.sequence())
                    .columns("DTYPE", "format", "Event_ID", "title", "DESCRIPTION", "FEEDBACK", "GUEST", "NBCONSULTS", "VALID")
                    .values("Talk", Format.Talk, 1, "My session", "My session", FALSE, FALSE, 0, TRUE)
                    .values("Workshop", Format.Workshop, 1, "My workshop", "My workshop", FALSE, FALSE, 0, TRUE)
                    .build(),
            insertInto("Session_Interest")
                    .columns("SESSION_ID", "INTERESTS_ID")
                    .values(1, -10)
                    .values(2, -10)
                    .build(),
            insertInto("Session_Member")
                    .columns("SESSIONS_ID", "SPEAKERS_ID")
                    .values(1, 1)
                    .values(2, 1)
                    .build()
    );
}
