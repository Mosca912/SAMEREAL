/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Dump {

    private static final String RUTA_MYSQLDUMP = "C:\\laragon\\bin\\mysql\\mysql-8.0.30-winx64\\bin\\mysqldump.exe";
    // 2. Nombre de la base de datos a respaldar
    private static final String DB_NAME = "samerealpro";
    // 3. Credenciales de la BD
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // Dejar vacío si no tienes contraseña
    private static final String RUTA_SALIDA_DUMP = "C:\\db\\" + DB_NAME + ".sql";

    public static void realizarDump() {
// --- 1. Construcción del Comando ---

        // Crear el comando base: "C:\ruta\mysqldump.exe" -u user nombre_db
        // Las comillas dobles (") son necesarias si la ruta tiene espacios.
        StringBuilder sbComando = new StringBuilder();
        sbComando.append("\"").append(RUTA_MYSQLDUMP).append("\""); // Ruta del ejecutable
        sbComando.append(" -u ").append(DB_USER);                    // Usuario

        // Agregar la contraseña solo si no está vacía (formato: -pPASSWORD)
        if (DB_PASS != null && !DB_PASS.isEmpty()) {
            sbComando.append(" -p").append(DB_PASS);
        }

        sbComando.append(" ").append(DB_NAME); // Nombre de la BD
        sbComando.append(" > ").append(RUTA_SALIDA_DUMP); // Redirección de la salida al archivo

        String comandoFinal = sbComando.toString();
        System.out.println("Comando a ejecutar: " + comandoFinal);

        String[] comandoArray = {
            "cmd.exe",
            "/c",
            comandoFinal // Tu string completo con el "> C:\ruta\archivo.sql"
        };

        // --- 2. Ejecución y Manejo de Errores ---
        try {
            // Ejecutar el comando
            Process proceso = Runtime.getRuntime().exec(comandoArray);

            // Leer y mostrar el Stream de Error (clave para el código 6)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proceso.getErrorStream()));
            String lineaError;

            System.err.println("\n--- Mensajes de Error de mysqldump ---");
            boolean hayError = false;
            while ((lineaError = errorReader.readLine()) != null) {
                System.err.println(lineaError); // Esto mostrará "Access denied..."
                hayError = true;
            }
            System.err.println("----------------------------------------");

            // Esperar a que el proceso termine y obtener el código de salida
            int exitCode = proceso.waitFor();

            if (exitCode == 0) {
                System.out.println("✅ Dump de la base de datos completado con éxito.");
                System.out.println("   Archivo guardado en: " + RUTA_SALIDA_DUMP);
            } else {
                System.err.println("❌ ERROR: El proceso mysqldump falló.");
                System.err.println("   Código de salida: " + exitCode + ". Revise las credenciales y el nombre de la BD.");
            }

        } catch (IOException e) {
            // Error de Java si no encuentra la ruta de mysqldump (ej: CreateProcess error=2)
            System.err.println("\n❌ ERROR IO: No se pudo ejecutar el programa.");
            System.err.println("   Verifique que la RUTA_MYSQLDUMP sea correcta: " + RUTA_MYSQLDUMP);
        } catch (InterruptedException e) {
            System.err.println("❌ ERROR: El proceso fue interrumpido.");
        }
    }
}
