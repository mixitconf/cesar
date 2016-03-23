package org.mixit.cesar.site.model.planning;

public enum Room {

    Amphi1("Grand Amphi", 500),
    Amphi2("Petit Amphi", 200),
    Salle1("Gosling", 110),
    Salle2("Eich", 110),
    Salle3("Nonaka", 30),
    Salle4("Dijkstra", 30),
    Salle5("Turing", 30),
    Salle6("Lovelace", 30),
    Salle7("Mezzanine", 50);

    private String name;
    private int capacity;

    private Room(String name, int capacity) {
        this.capacity = capacity;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
}
