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
    public static final String ENCABEZADO = "Archivos que contienen > " + COMANDO_GREP[1];
    public static final String SEPARADOR = "----------------------------";
    public static final String TERMINO_LS = "LS terminó con código: ";
    public static final String TERMINO_GREP = "GREP terminó con código: ";

    public static void main(String[] args) {
        Process procesoLS = null;
        Process procesoGrep = null;

        try {
            procesoLS = Runtime.getRuntime().exec(COMANDO_LS);
            procesoGrep = Runtime.getRuntime().exec(COMANDO_GREP);

            BufferedReader salidaLS = new BufferedReader(
                    new InputStreamReader(procesoLS.getInputStream()));

            OutputStream entradaGrep = procesoGrep.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(entradaGrep));

            String linea;
            while ((linea = salidaLS.readLine()) != null) {
                writer.println(linea);
            }

            writer.flush();
            writer.close();
            salidaLS.close();

            BufferedReader salidaGrep = new BufferedReader(
                    new InputStreamReader(procesoGrep.getInputStream()));

            System.out.println(ENCABEZADO);
            System.out.println(SEPARADOR);

            while ((linea = salidaGrep.readLine()) != null) {
                System.out.println(linea);
            }

            salidaGrep.close();

            int exitValLS = procesoLS.waitFor();
            int exitValGrep = procesoGrep.waitFor();

            if (exitValLS == 0 && exitValGrep == 0) {
                System.exit(0);
            } else {
                System.out.println(MSG_ERROR);
                System.exit(1);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(MSG_ERROR);
            System.exit(1);
        } finally {
            if (procesoLS != null)
                procesoLS.destroy();
            if (procesoGrep != null)
                procesoGrep.destroy();
        }
    }
}