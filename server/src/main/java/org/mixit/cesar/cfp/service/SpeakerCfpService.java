package org.mixit.cesar.cfp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpeakerCfpService {

    /**
     * Check the required fields. A user can save a partial proposal and complete it later
     */
    public boolean check(){
        return true;
    }

    public void save(){

    }
}
