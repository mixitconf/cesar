package org.mixit.cesar.site.model.session;

import javax.persistence.Entity;

@Entity
public class Keynote extends Session<Keynote> {

    public Keynote() {
        super();
        format = Format.Keynote;
    }

}
