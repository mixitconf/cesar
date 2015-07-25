package org.mixit.cesar;

import java.util.UUID;
import javax.annotation.PostConstruct;

import org.mixit.cesar.model.Member;
import org.mixit.cesar.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Initialize data if db is empty
 */
@Component
public class CesarInitializer {
    @Autowired
    private MemberRepository memberRepository;

    @PostConstruct
    public void init(){
        if(memberRepository.count()==0){
            memberRepository.save(new Member().setLogin(UUID.randomUUID().toString()).setLastname("Odersky").setFirstname("Martin"));
            memberRepository.save(new Member().setLogin(UUID.randomUUID().toString()).setLastname("Gosling").setFirstname("James"));
        }
    }
}
