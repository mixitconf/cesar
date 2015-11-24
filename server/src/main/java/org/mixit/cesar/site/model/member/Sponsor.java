package org.mixit.cesar.site.model.member;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.mixit.cesar.security.model.Role;

/**
 * A Mix-IT sponsor, giving a fucking load of money to buy better sandwiches than the ones given at WSN Paris.
 */
@Entity
public class Sponsor extends Member<Sponsor> {

    public Sponsor() {
        ROLES.add(Role.MEMBER);
        ROLES.add(Role.SPONSOR);
    }

    public enum Level{
      GOLD, SILVER, BRONZE
    }

    @NotNull
    @Size(max = 250)
    public String logoUrl;

    @Enumerated(EnumType.STRING)
    public Level level;

    public String getLogoUrl() {
        return logoUrl;
    }

    public Sponsor setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    public Sponsor setLevel(Level level) {
        this.level = level;
        return this;
    }
}
