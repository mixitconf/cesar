package org.mixit.cesar.site.model.session;

import javax.persistence.Entity;

@Entity
public class Talk extends Session<Talk> {

    public Talk() {
        super();
        format = Format.Talk;
    }

}
