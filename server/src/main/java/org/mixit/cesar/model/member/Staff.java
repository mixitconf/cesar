package org.mixit.cesar.model.member;

import javax.persistence.Entity;

import org.mixit.cesar.model.security.Role;

@Entity
public class Staff extends Member<Staff> {

    public Staff() {
        ROLES.add(Role.ADMIN);
        ROLES.add(Role.MEMBER);
        ROLES.add(Role.SPONSOR);
        ROLES.add(Role.SPEAKER);
    }

}
