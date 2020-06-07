import auxiliar.Auxiliar;

public class Main {
    public static void main(String[] args) {
        Integer maxIntent = 10000000;
        Boolean blindSearch = false;
        Boolean printTrace = false;
        Boolean printPercent = true;

        Auxiliar.runTest(maxIntent, blindSearch, printTrace, printPercent);
    }
}
