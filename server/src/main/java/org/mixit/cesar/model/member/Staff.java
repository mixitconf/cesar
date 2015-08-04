package org.mixit.cesar.model.member;

import javax.persistence.Entity;

import org.mixit.cesar.model.security.Role;

@Entity
public class Staff extends Member<Staff> {

    public Staff() {
        ROLES.add(Role.Admin);
        ROLES.add(Role.Member);
        ROLES.add(Role.Participant);
        ROLES.add(Role.Sponsor);
        ROLES.add(Role.Speaker);
    }

}
