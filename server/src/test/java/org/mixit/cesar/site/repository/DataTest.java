package org.mixit.cesar.site.repository;


import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;

import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import com.ninja_squad.dbsetup.operation.Operation;

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
            insertInto("Interest").columns("name").values("Agilite").values("Java").build()
    );

    public static Operation INSERT_MEMBER = Operations.sequenceOf(
            insertInto("Member")
                    .withGeneratedValue("id", ValueGenerators.sequence())
                    .columns("DTYPE", "FIRSTNAME", "LASTNAME", "LOGIN", "NBCONSULTS", "PUBLICPROFILE", "EMAIL")
                    .values("Staff", "Guillaume", "EHRET", "guillaume", 1, "true", "guillaume@dev-mind.fr")
                    .build()
    );
}
