package entities;

import java.util.Set;

public class Position {

    private String name;
    private String initials;
    private Position dad;
    private Integer level;

    public Position(String name, String initials, Position dad, Integer level) {
        this.name = name;
        this.initials = initials;
        this.dad = dad;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getDad() {
        return dad;
    }

    public void setDad(Position dad) {
        this.dad = dad;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Override
    public String toString() {
        return "Position{" +
                "initials='" + initials + '\'' +
                '}';
    }
}
