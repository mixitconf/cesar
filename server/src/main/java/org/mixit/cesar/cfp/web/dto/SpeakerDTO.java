package org.mixit.cesar.cfp.web.dto;

import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.utils.HashUtil;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 09/03/16.
 */
public class SpeakerDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String hash;

    public static SpeakerDTO convert(Member member) {
        return new SpeakerDTO()
                .setId(member.getId())
                .setFirstname(member.getFirstname())
                .setLastname(member.getLastname())
                .setHash(HashUtil.md5Hex(member.getEmail()));
    }

    public SpeakerDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public SpeakerDTO setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public SpeakerDTO setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public SpeakerDTO setHash(String hash) {
        this.hash = hash;
        return this;
    }
}
