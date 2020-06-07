package entities;

import java.util.Set;

public class User {
    private String name;
    private Set<Position> positions;

    public User(String name, Set<Position> positions) {
        this.name = name;
        this.positions = positions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", positions=" + positions +
                '}';
    }
}
