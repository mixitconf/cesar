package org.mixit.cesar.site.model.session;

import javax.persistence.Entity;

@Entity
public class Random extends Session<Random> {

    public Random() {
        super();
        format = Format.Random;
    }

}
