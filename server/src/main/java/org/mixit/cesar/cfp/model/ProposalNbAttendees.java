package org.mixit.cesar.cfp.model;

public enum ProposalNbAttendees {
    BETWEEN_0_AND_10(10),
    BETWEEN_10_AND_40(40),
    BETWEEN_40_AND_100(100),
    MORE_THAN_100(500);

    private int nbMax;

    ProposalNbAttendees(int nbMax) {
        this.nbMax = nbMax;
    }

    public int getNbMax() {
        return nbMax;
    }
}
