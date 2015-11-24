package org.mixit.cesar.site.model.planning;

public enum Room {

    S1("Gosling"), //used by mixit13, mixit14, mixit15
    S2("Lovelace"), //used by mixit13, mixit14, mixit15
    S3("Nonaka"), //used by mixit13, mixit14, mixit15
    S4("Dijkstra"), //used by mixit13, mixit14, mixit15
    S5("Turing"), //used by mixit13, mixit15
    A1("Grand Amphi"), //used by mixit14, mixit15
    A2("Petit Amphi"); //used by mixit14, mixit15

    private String name;

    private Room(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
