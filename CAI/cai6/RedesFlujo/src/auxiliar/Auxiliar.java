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
            int low = 0;
            int high = usersList.size();
            if (!task.getName().equals("T4")) {
                Random r = new Random();
                int result = r.nextInt(high - low) + low;
                Set<User> aux = new HashSet<>();
                aux.add(usersList.get(result));
                res.put(task, aux);
            } else {
                Random r1 = new Random();
                Random r2 = new Random();
                int result1 = r1.nextInt(high - low) + low;
                int result2 = r2.nextInt(high - low) + low;
                while (result2 == result1) {
                    result2 = r2.nextInt(high - low) + low;
                }
                Set<User> aux = new HashSet<>();
                aux.add(usersList.get(result1));
                aux.add(usersList.get(result2));
                res.put(task, aux);
            }
        }
        return res;
    }


    public static Boolean validateResult(Set<Map<Task, Set<User>>> maps, Map<Task, Set<User>> result, Set<Position> positions) {
        Boolean res = true;

        if (maps.contains(result)) {
            return false;
        }

        Set<User> totalUser = new HashSet<>();

        for (Task task : result.keySet()) {
            totalUser.addAll(result.get(task));
            // RESTRICTION R0
            Set<User> users = result.get(task);
            for (User user : users) {
                Boolean checkUser = validateAllowUserToTask(user, task, positions);
                if (!checkUser) {
                    return false;
                }
            }
        }

        // RESTRICTION R1
        Task t21 = result.keySet().stream().filter(x -> x.getName().equals("T2.1")).findFirst().orElse(null);
        Task t22 = result.keySet().stream().filter(x -> x.getName().equals("T2.2")).findFirst().orElse(null);
        if (t21 != null && t22 != null) {
            User user2 = result.get(t21).stream().findFirst().orElse(null);
            User user3 = result.get(t22).stream().findFirst().orElse(null);
            if (user2.equals(user3) || user2.getPositions().equals(user3.getPositions())) {
                return false;
            }
        } else {
            return false;
        }

        // RESTRICTION R2
        Task t3 = result.keySet().stream().filter(x -> x.getName().equals("T3")).findFirst().orElse(null);
        Task t4 = result.keySet().stream().filter(x -> x.getName().equals("T4")).findFirst().orElse(null);
        if (t3 != null && t4 != null) {
            User user4 = result.get(t3).stream().findFirst().orElse(null);
            User user5 = result.get(t4).stream().collect(Collectors.toList()).get(0);
            User user6 = result.get(t4).stream().collect(Collectors.toList()).get(1);
            Set<String> check = new HashSet<>();
            Boolean checkResult = false;
            check.addAll(user4.getPositions().stream().map(Position::getInitials).collect(Collectors.toSet()));
            check.addAll(user5.getPositions().stream().map(Position::getInitials).collect(Collectors.toSet()));
            check.addAll(user6.getPositions().stream().map(Position::getInitials).collect(Collectors.toSet()));
            if (user5.getPositions().stream().anyMatch(x -> x.getInitials().equals("PS")) && user6.getPositions().stream().anyMatch(x -> x.getInitials().equals("PS"))) {
                checkResult = true;
            } else {
                checkResult = false;
            }
            if (user4.equals(user5) || user4.equals(user6) || user5.equals(user6) || checkResult) {
                return false;
            }
        } else {
            return false;
        }

        // RESTRICTION R3
        User user1 = result.get(t21).stream().findAny().orElse(null);
        Boolean check1 = user1.getName().equals("GTR");
        if (check1) {
            User user2 = result.get(t22).stream().findAny().orElse(null);
            Boolean check2 = !user2.getName().equals("MDS");
            if (check2) {
                return false;
            }
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
                return false;
            } else if (elem.getName().equals("T4") && result.get(elem).size() != 2) {
                return false;
            }
        }
        if (count > 1) {
            return false;
        }

        // RESTRICTION R5
        Integer number = result.values().size();
        if (totalUser.size() != 6) {
            return false;
        }

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

        Set<Position> copy = new HashSet<>(allowPositions);
        for (Position elem : copy) {
            addAllDad(elem, allowPositions);
        }

        for (Position elem : user.getPositions()) {
            if (allowPositions.contains(elem)) {
                res = true;
                break;
            }
        }
        return res;
    }

    public static Set<Position> getAllPositions() {
        Position dg = new Position("Director General", "DG", null, 0);
        Position dr = new Position("Director de Recursos Sanitarias", "DR", dg, 1);
        Position tr = new Position("Personal Técnico de Recursos", "TR", dr, 2);
        Position tc = new Position("Personal Técnico de Compras", "TC", dr, 2);
        Position dm = new Position("Director Médico", "DM", dg, 1);
        Position ps = new Position("Personal Sanitario", "PS", dm, 2);
        Position de = new Position("Director de Gestión Económica", "DE", dg, 1);
        Set<Position> res = new HashSet<>(Arrays.asList(dg, dr, tr, tc, dm, ps, de));
        return res;
    }

    public static Set<Task> getAllTasks(Set<Position> positions) {
        Task t1 = new Task("T1", positions.stream().filter(x -> x.getInitials().equals("DR")).collect(Collectors.toSet()));
        Task t21 = new Task("T2.1", positions.stream().filter(x -> x.getInitials().equals("TR")).collect(Collectors.toSet()));
        Task t22 = new Task("T2.2", positions.stream().filter(x -> x.getInitials().equals("TC")).collect(Collectors.toSet()));
        Task t3 = new Task("T3", positions.stream().filter(x -> x.getInitials().equals("DM")).collect(Collectors.toSet()));
        Task t4 = new Task("T4", positions.stream().filter(x -> x.getInitials().equals("DE") || x.getInitials().equals("PS")).collect(Collectors.toSet()));
        Set<Task> res = new HashSet<>(Arrays.asList(t1, t21, t22, t3, t4));
        return res;
    }

    public static Set<User> getAllUsers(Set<Position> positions) {
        if (positions == null || positions.isEmpty()) {
            positions = getAllPositions();
        }
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
        Set<User> res = new HashSet<>(Arrays.asList(jvg, hyv, pgr, mfe, gtr, lpg, rgb, bjc, mds, hjr, ihp));
        return res;
    }

    public static void runTest(Integer maxIntent, Boolean blindSearch, Boolean print, Boolean printPercent) {
        System.out.println("Prueba iniciada\n");
        Set<Position> positions = getAllPositions();
        Set<User> users = getAllUsers(positions);
        Set<Task> tasks = getAllTasks(positions);
        Set<Map<Task, Set<User>>> result = new HashSet<>();
        Set<Map<Task, Set<User>>> invalid = new HashSet<>();
        int count = 0;
        Boolean stop = true;
        while (stop) {
            Map<Task, Set<User>> aux = Auxiliar.getResult(tasks, users);
            if (Auxiliar.validateResult(result, aux, positions) && !invalid.contains(aux)) {
                System.out.println("\n");
                result.add(aux);
                printResult(aux, String.valueOf(result.size()));
            } else if (print && !invalid.contains(aux)) {
                invalid.add(aux);
                printResult(aux, "NO VALID");
            } else {
                invalid.add(aux);
            }
            if (printPercent) {
                if (blindSearch) {
                    progressPercentage(result.size(), 20);
                } else {
                    progressPercentage(count, maxIntent);
                }
            }
            count = count + 1;
            stop = blindSearch ? result.size() < 20 : count < maxIntent;
        }
        if (result.size() > 0) {
            System.out.println("\nNUMBER OF VALID RESULT: " + result.size() + "\n");
        } else {
            System.out.println("\nNO RESULT");
        }

        System.out.println("\nPrueba finalizada");
    }

    public static void progressPercentage(int done, int total) {
        int size = 5;
        String iconLeftBoundary = "[";
        String iconDone = "=";
        String iconRemain = ".";
        String iconRightBoundary = "]";

        if (done > total) {
            throw new IllegalArgumentException();
        }
        int donePercents = (100 * done) / total;
        int doneLength = size * donePercents / 100;

        StringBuilder bar = new StringBuilder(iconLeftBoundary);
        for (int i = 0; i < size; i++) {
            if (i < doneLength) {
                bar.append(iconDone);
            } else {
                bar.append(iconRemain);
            }
        }
        bar.append(iconRightBoundary);

        System.out.print("\r" + bar + " " + donePercents + "%");

        if (done == total) {
            System.out.print("\n");
        }
    }

    public static void printResult(Map<Task, Set<User>> result, String info) {
        System.out.println("-------------------------------------- RESULT " + info + " --------------------------------------");
        List<Task> taskListOrder = result.keySet().stream().collect(Collectors.toList());
        taskListOrder.sort(Comparator.comparing(Task::getName));
        for (Task elem : taskListOrder) {
            System.out.println("Tarea: " + elem + " usuarios: " + result.get(elem));
        }
        System.out.println("---------------------------------------------------------------------------------------\n");
    }

    public static void testExamplePDF() {
        Set<Position> positions = Auxiliar.getAllPositions();

        Task t1 = new Task("T1", positions.stream().filter(x -> x.getInitials().equals("DR")).collect(Collectors.toSet()));
        Task t21 = new Task("T2.1", positions.stream().filter(x -> x.getInitials().equals("TR")).collect(Collectors.toSet()));
        Task t22 = new Task("T2.2", positions.stream().filter(x -> x.getInitials().equals("TC")).collect(Collectors.toSet()));
        Task t3 = new Task("T3", positions.stream().filter(x -> x.getInitials().equals("DM")).collect(Collectors.toSet()));
        Task t4 = new Task("T4", positions.stream().filter(x -> x.getInitials().equals("DE") || x.getInitials().equals("PS")).collect(Collectors.toSet()));

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

        Map<Task, Set<User>> solution = new HashMap<>();
        solution.put(t1, new HashSet<>(Arrays.asList(jvg)));
        solution.put(t21, new HashSet<>(Arrays.asList(gtr)));
        solution.put(t22, new HashSet<>(Arrays.asList(mds)));
        solution.put(t3, new HashSet<>(Arrays.asList(pgr)));
        solution.put(t4, new HashSet<>(Arrays.asList(mfe, hjr)));

        System.out.println(Auxiliar.validateResult(new HashSet<>(), solution, positions));
    }
}
