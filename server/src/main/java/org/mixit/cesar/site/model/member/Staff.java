package org.mixit.cesar.site.model.member;

import javax.persistence.Entity;

import org.mixit.cesar.security.model.Role;

@Entity
public class Staff extends Member<Staff> {

    public Staff() {
        ROLES.add(Role.ADMIN);
        ROLES.add(Role.MEMBER);
        ROLES.add(Role.SPONSOR);
        ROLES.add(Role.SPEAKER);
    }

}
