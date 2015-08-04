package org.mixit.cesar.model.session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

@Entity
public class Talk extends Session<Talk> {

    public Talk() {
        super();
        format = Format.Talk;
    }

}
