package org.mixit.cesar.repository;


import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;

import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import com.ninja_squad.dbsetup.operation.Operation;

public class DataTest {

    public static final Operation DELETE_ALL = deleteAllFrom(
            "SESSION_COMMENT", "SESSION_INTERESTS", "SESSION_SPEAKERS", "VOTE", "SESSION",
            "SHARED_LINK", "MEMBER_EVENTS", "MEMBER_INTERESTS", "MEMBER",
            "EVENT", "INTEREST"
            );

    public static Operation INSERT_EVENT = Operations.sequenceOf(
            insertInto("EVENT")
                    .withGeneratedValue("id", ValueGenerators.sequence())
                    .columns("year", "current")
                    .values("2014", false)
                    .values("2015", false)
                    .values("2016", true)
                    .build()
    );

    public static Operation INSERT_INTEREST = Operations.sequenceOf(
            insertInto("INTEREST").columns("name").values("Agilite").values("Java").build()
    );
}
