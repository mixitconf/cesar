package org.mixit.cesar.model.session;

import javax.persistence.Entity;

@Entity
public class Keynote extends Session<Keynote> {

    public Keynote() {
        super();
        format = Format.Keynote;
    }

}
