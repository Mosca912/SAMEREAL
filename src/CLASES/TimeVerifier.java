package CLASES;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JFormattedTextField;

public class TimeVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        JFormattedTextField tf = (JFormattedTextField) input;
        String text = tf.getText().trim();

        // Si el campo está vacío, puede que quieras permitirlo o no.
        if (text.isEmpty() || text.equals("00:00:00")) {
            return true; // Asumimos que vacío o 00:00:00 es válido
        }

        try {
            // Dividir la cadena en partes: HH, MM, SS
            String[] parts = text.split(":");
            if (parts.length != 3) {
                return false; // El formato debe ser HH:MM:SS
            }
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            // Lógica de Validación de Rangos:
            boolean isValid = true;
            String mensaje = "";

            if (hours < 0 || hours > 23) {
                isValid = false;
                mensaje = "Las horas deben estar entre 00 y 23.";
            } else if (minutes < 0 || minutes > 59) {
                isValid = false;
                mensaje = "Los minutos deben estar entre 00 y 59.";
            } else if (seconds < 0 || seconds > 59) {
                isValid = false;
                mensaje = "Los segundos deben estar entre 00 y 59.";
            }

            if (!isValid) {
                // Muestra un mensaje al usuario
                JOptionPane.showMessageDialog(input, "Tiempo inválido: " + mensaje, "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }

            return isValid;

        } catch (NumberFormatException e) {
            // Esto no debería pasar si el MaskFormatter funciona, pero es buena práctica
            JOptionPane.showMessageDialog(input, "Formato numérico incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
