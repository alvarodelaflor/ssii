package entities;

import java.util.Set;

public class Task {
    private String name;
    private Set<Position> gradesAllow;

    public Task(String name, Set<Position> gradesAllow) {
        this.name = name;
        this.gradesAllow = gradesAllow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Position> getGradesAllow() {
        return gradesAllow;
    }

    public void setGradesAllow(Set<Position> gradesAllow) {
        this.gradesAllow = gradesAllow;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                '}';
    }
}
