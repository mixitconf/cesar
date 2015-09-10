package org.mixit.cesar.model.member;

import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.mixit.cesar.model.event.Event;
import org.mixit.cesar.model.security.Role;

@Entity
public class Participant<T extends Participant> extends Member<T> {

    public Participant() {
        ROLES.add(Role.Member);
        ROLES.add(Role.Participant);
    }

}
