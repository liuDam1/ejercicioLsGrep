package es.etg.psp.lsgrep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Main {
    public static final String MSG_ERROR = "Se ha producido un error al ejecutar el comando";
    public static final String[] COMANDO_LS = { "ls" };
    public static final String[] COMANDO_GREP = { "grep", "a" };
    public static final String SALTO_LINEA = "\n";

    public static void main(String[] args) {
        try {
            String salidaLs = ejecutarComando(COMANDO_LS);
            String salidaGrep = ejecutarComando(COMANDO_GREP, salidaLs);
            System.out.println(salidaGrep);
            System.exit(0);
        } catch (IOException e) {
            System.out.println(MSG_ERROR);
            System.exit(34);
        }
    }

    private static String leerSalida(Process proceso) throws IOException {
        StringBuilder resultado = new StringBuilder();
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                resultado.append(linea).append(SALTO_LINEA);
            }
        }
        return resultado.toString();
    }

    private static String ejecutarComando(String[] comando, String entrada) throws IOException {
        Process proceso = Runtime.getRuntime().exec(comando);

        if (entrada != null) {
            try (OutputStream output = proceso.getOutputStream();
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(output))) {
                pw.print(entrada);
                pw.flush();
            }
        }

        return leerSalida(proceso);
    }

    private static String ejecutarComando(String[] comando) throws IOException {
        return ejecutarComando(comando, null);
    }
}