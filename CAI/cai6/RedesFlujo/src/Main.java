import auxiliar.Auxiliar;

public class Main {
    public static void main(String[] args) {
        Integer maxIntent = 4500000; // Número de intentos máximos a probar (imprimir todas las soluciones)
        Boolean blindSearch = false; // Probar hasta encontrar 20 soluciones correctas
        Boolean printTrace = false; // Mostrar también los casos incorrectos
        Boolean printPercent = true; // Mostrar porcentaje de avance del script

        Auxiliar.runTest(maxIntent, blindSearch, printTrace, printPercent);

//        Auxiliar.testExamplePDF(); // Comprobar ejemplo del enunciado
    }
}
