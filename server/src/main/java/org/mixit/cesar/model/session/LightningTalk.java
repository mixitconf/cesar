package org.mixit.cesar.model.session;

import javax.persistence.Entity;

@Entity
public class LightningTalk extends Session<LightningTalk> {

    public LightningTalk() {
        super();
        // A Lightning Talk is always validated
        valid = true;
        format = Format.LightningTalk;
    }

}
