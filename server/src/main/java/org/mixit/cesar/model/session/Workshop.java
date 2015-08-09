package org.mixit.cesar.model.session;

import javax.persistence.Entity;

@Entity
public class Workshop extends Session<Workshop> {

    public Workshop() {
        super();
        format = Format.Workshop;
    }

}
