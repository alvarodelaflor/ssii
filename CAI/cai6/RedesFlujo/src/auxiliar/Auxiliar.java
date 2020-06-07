package auxiliar;

import entities.Position;
import entities.Task;
import entities.User;

import java.util.*;
import java.util.stream.Collectors;

public class Auxiliar {

    public static Map<Task, Set<User>> getResult(Set<Task> tasks, Set<User> users) {
        Map<Task, Set<User>> res = new HashMap<>();
        List<User> usersList = users.stream().collect(Collectors.toList());
        for (Task task : tasks) {
            if (!task.getName().equals("T4")) {
                Random r = new Random();
                int low = 0;
                int high = usersList.size() - 1;
                int result = r.nextInt(high-low) + low;
                Set<User> aux = new HashSet<>();
                aux.add(usersList.get(result));
                res.put(task, aux);
            } else {
                Random r1 = new Random();
                Random r2 = new Random();
                int low = 0;
                int high = usersList.size() - 1;
                int result1 = r1.nextInt(high-low) + low;
                int result2 = r2.nextInt(high-low) + low;
                Set<User> aux = new HashSet<>();
                aux.add(usersList.get(result1));
                aux.add(usersList.get(result2));
                res.put(task, aux);
            }
        }
        return res;
    }


    public static Boolean validateResult(Map<Task, Set<User>> result, Set<Position> positions){
        Boolean res = true;

        for (Task task : result.keySet()) {
            // RESTRICTION R0
            User user = result.get(task).stream().findFirst().orElse(null);
            Boolean checkUser = validateAllowUserToTask(user, task, positions);
            if (!checkUser) {
                res = false;
                break;
            }
        }

        // RESTRICTION R1
        Task t21 = result.keySet().stream().filter(x -> x.getName().equals("T2.1")).findFirst().orElse(null);
        Task t22 = result.keySet().stream().filter(x -> x.getName().equals("T2.2")).findFirst().orElse(null);
        if (t21 != null && t22 != null) {
            User user1 = result.get(t21).stream().findFirst().orElse(null);
            User user2 = result.get(t22).stream().findFirst().orElse(null);
            if (user1.equals(user2)) {
                res = false;
            }
        } else {
            res = false;
        }

        // RESTRICTION R2
        Task t3 = result.keySet().stream().filter(x -> x.getName().equals("T3")).findFirst().orElse(null);
        Task t4 = result.keySet().stream().filter(x -> x.getName().equals("T4")).findFirst().orElse(null);
        if (t3 != null && t4 != null) {
            User user3 = result.get(t3).stream().findFirst().orElse(null);
            User user4 = result.get(t4).stream().findFirst().orElse(null);
            Boolean check = false;
            for (Position elem1 : user3.getPositions()) {
                if (user4.getPositions().contains(elem1)) {
                    check = true;
                }
            }
            if (user3.equals(user4) || check) {
                res = false;
            }
        } else {
            res = false;
        }

        // RESTRICTION R3
        Boolean check1 = result.get(t21).equals("GTR");
        Boolean check2 = !result.get(t22).equals("MDS");
        if (check1 && check2) {
            res = false;
        }

        int count = 0;
        Set<Task> auxTask = result.keySet();
        for (Task elem : auxTask) {
            // RESTRICTION R4
            if (result.get(elem).stream().findFirst().orElse(null).getName().equals("JVG")) {
                count = count + 1;
            }

            // RESTRICTION SIZE
            if (!elem.getName().equals("T4") && result.get(elem).size() > 1) {
                res = false;
            } else if (elem.getName().equals("T4") && result.get(elem).size() != 2) {
                res = false;
            }
        }
        if (count > 1) {
            res = false;
        }

        // RESTRICTION R5
        // TODO

        return res;
    }

    private static void addAllDad(Position position, Set<Position> acum) {
        if (position.getDad() != null) {
            acum.add(position.getDad());
            addAllDad(position.getDad(), acum);
        }
    }

    private static Boolean validateAllowUserToTask(User user, Task task, Set<Position> positions) {
        Boolean res = false;
        Set<Position> allowPositions = new HashSet<>();
        allowPositions.addAll(task.getGradesAllow());
        Set<Position> positionsAuxUser = new HashSet<>();
        positionsAuxUser.addAll(user.getPositions());

        Set<Position> copy = new HashSet<>(positionsAuxUser);
        for (Position elem : copy) {
            addAllDad(elem, allowPositions);
        }

        for (Position elem : positionsAuxUser) {
            if (allowPositions.contains(elem)) {
                res = true;
                break;
            }
        }
        return res;
    }

    public static Set<Position> getAllPositions() {
        Set<Position> res = new HashSet<>();
        Position dg = new Position("Director General", "DG", null, 0);
        Position dr = new Position("Director de Recursos Sanitarias", "DR", dg, 1);
        Position tr = new Position("Personal Técnico de Recursos", "TR", dr, 2);
        Position tc = new Position("Personal Técnico de Compras", "TC", dr, 2);
        Position dm = new Position("Director Médico", "DM", dg, 1);
        Position ps = new Position("Personal Sanitario", "PS", dm, 2);
        Position de = new Position("Director de Gestión Económica", "DE", dg, 1);
        res.add(dg);
        res.add(dr);
        res.add(tr);
        res.add(tc);
        res.add(dm);
        res.add(ps);
        res.add(de);
        return res;
    }

    public static Set<Task> getAllTasks(Set<Position> positions) {
        Set<Task> res = new HashSet<>();
        Task t1 = new Task("T1", positions.stream().filter(x -> x.getInitials().equals("DR")).collect(Collectors.toSet()));
        Task t21 = new Task("T2.1", positions.stream().filter(x -> x.getInitials().equals("TR")).collect(Collectors.toSet()));
        Task t22 = new Task("T2.2", positions.stream().filter(x -> x.getInitials().equals("TC")).collect(Collectors.toSet()));
        Task t3 = new Task("T3", positions.stream().filter(x -> x.getInitials().equals("DM")).collect(Collectors.toSet()));
        Task t4 = new Task("T4", positions.stream().filter(x -> x.getInitials().equals("DE") || x.getInitials().equals("PS")).collect(Collectors.toSet()));
        res.add(t1);
        res.add(t21);
        res.add(t22);
        res.add(t3);
        res.add(t4);
        return res;
    }

    public static Set<User> getAllUsers(Set<Position> positions) {
        if (positions == null || positions.isEmpty()) {
            positions = getAllPositions();
        }
        Set<User> res = new HashSet<>();
        User jvg = new User("JVG", positions.stream().filter(x -> x.getInitials().equals("DG")).collect(Collectors.toSet()));
        User hyv = new User("HYV", positions.stream().filter(x -> x.getInitials().equals("DR") || x.getInitials().equals("TR")).collect(Collectors.toSet()));
        User pgr = new User("PGR", positions.stream().filter(x -> x.getInitials().equals("DM") || x.getInitials().equals("PS")).collect(Collectors.toSet()));
        User mfe = new User("MFE", positions.stream().filter(x -> x.getInitials().equals("DE")).collect(Collectors.toSet()));
        User gtr = new User("GTR", positions.stream().filter(x -> x.getInitials().equals("TR")).collect(Collectors.toSet()));
        User lpg = new User("LPG", positions.stream().filter(x -> x.getInitials().equals("TR") || x.getInitials().equals("TC")).collect(Collectors.toSet()));
        User rgb = new User("RGB", positions.stream().filter(x -> x.getInitials().equals("TR") || x.getInitials().equals("TC")).collect(Collectors.toSet()));
        User bjc = new User("BJC", positions.stream().filter(x -> x.getInitials().equals("TR")).collect(Collectors.toSet()));
        User mds = new User("MDS", positions.stream().filter(x -> x.getInitials().equals("TC")).collect(Collectors.toSet()));
        User hjr = new User("HJR", positions.stream().filter(x -> x.getInitials().equals("PS")).collect(Collectors.toSet()));
        User ihp = new User("IHP", positions.stream().filter(x -> x.getInitials().equals("PS")).collect(Collectors.toSet()));
        res.add(jvg);
        res.add(hyv);
        res.add(pgr);
        res.add(mfe);
        res.add(gtr);
        res.add(lpg);
        res.add(rgb);
        res.add(bjc);
        res.add(mds);
        res.add(hjr);
        res.add(ihp);
        return res;
    }

    public static void runTest(Integer maxIntent, Boolean print) {
        Set<Position> positions = getAllPositions();
        Set<User> users = getAllUsers(positions);
        Set<Task> tasks = getAllTasks(positions);
        Set<Map<Task, Set<User>>> result = new HashSet<>();
        int count = 0;
        while (count < maxIntent && result.size() < 20) {
            Map<Task, Set<User>> aux = Auxiliar.getResult(tasks, users);
            if (Auxiliar.validateResult(aux, positions)) {
                result.add(aux);
            } else if (print){
                printResult(aux);
            }
            count = count + 1;
        }
        if (result.size()>0) {
            System.out.println("NUMBER OF VALID RESULT: " + result.size() + "\n");
            result.stream().forEach(x -> printResult(x));
        } else {
            System.out.println("NO RESULT");
        }
    }

    public static void printResult(Map<Task, Set<User>> result) {
        System.out.println("--------------RESULT--------------");
        for (Task elem : result.keySet()) {
            System.out.println("Tarea: " + elem + " usuarios: " + result.get(elem));
        }
        System.out.println("--------------RESULT--------------");
    }
}
