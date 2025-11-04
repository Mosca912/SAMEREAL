/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CONEXIONES.Conexiones;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Facuymayriver
 */
public class ModElimCargo1 extends javax.swing.JDialog {

    static int band;

    Connection con = Conexiones.Conexion();

    //Metodos para editar
    public class ModeloEditablePorFila extends DefaultTableModel {

        private final int editableRow = -1;
        private final int band;

        public ModeloEditablePorFila(Object[] columnNames, int rowCount, int band) {
            super(columnNames, rowCount);
            this.band = band;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            // Solo permitir editar la columna 1 (índice 1) y solo si band == 1
            return band == 1 && column == 1;
        }

    }
    //Metodos para editar
    ModeloEditablePorFila tabla1 = new ModeloEditablePorFila(new String[]{"Nº", "Cargo", "Area"}, 0, band);

    public ModElimCargo1(int band, JFrame ventanaPrincipal) {
        super(ventanaPrincipal, true);
        initComponents();
        this.setLocationRelativeTo(null);
        ModElimCargo1.band = band;

        //Tabla
        ModeloEditablePorFila tabla2 = new ModeloEditablePorFila(new String[]{"Nº", "Cargo", "Area"}, 0, band);
        //Tabla
        tablamostrar.setModel(tabla2);
        try {
            CLASES.Empleados.MostrarCargo(con, tabla2);
            if (tablamostrar.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún Producto.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e);
        }

        if (band == 0) {
            titulo.setText("Eliminar");
            Leyenda.setText("Seleccione una opcion que desea eliminar");
            Boton.setText("Eliminar");
        } else if (band == 1) {
            titulo.setText("Modificar");
            Leyenda.setText("Seleccione una opcion que desea modificar");
            Boton.setText("Modificar");
        }

        tablamostrar.setRowHeight(30);
        tablamostrar.getTableHeader().setReorderingAllowed(false);
        tablamostrar.getTableHeader().setResizingAllowed(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        Leyenda = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablamostrar = new javax.swing.JTable();
        Cancelar = new javax.swing.JButton();
        Boton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Opciones");
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        titulo.setText("Titulo");

        Leyenda.setText("jLabel1");

        tablamostrar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablamostrar);

        Cancelar.setBackground(new java.awt.Color(52, 170, 121));
        Cancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Cancelar.setForeground(new java.awt.Color(255, 255, 255));
        Cancelar.setText("Cancelar");
        Cancelar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarActionPerformed(evt);
            }
        });

        Boton.setBackground(new java.awt.Color(52, 170, 121));
        Boton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Boton.setForeground(new java.awt.Color(255, 255, 255));
        Boton.setText("Boton");
        Boton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Boton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Leyenda, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo)
                .addGap(29, 29, 29)
                .addComponent(Leyenda)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Boton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_CancelarActionPerformed

    private void BotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonActionPerformed
        if (ModElimCargo1.this.band == 0) {
            int filaSeleccionada = tablamostrar.getSelectedRow();
            if (filaSeleccionada != -1) {
                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Deseás Eliminar?",
                        "Confirmar acción",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (opcion == JOptionPane.OK_OPTION) {
                    int fila = tablamostrar.getSelectedRow();
                    int cod = Integer.parseInt(tablamostrar.getValueAt(fila, 0).toString());
                    try {
                        int veri = CLASES.Empleados.Validacion(con, cod);
                        int borrado = 1;
                        switch (veri) {
                            case 1:
                                try {
                                    CLASES.Empleados.EliminarCargo(con, cod, borrado);
                                    JOptionPane.showMessageDialog(null, "Eliminao");
                                    tabla1.setRowCount(0);
                                    CLASES.Empleados.MostrarCargo(con, tabla1);
                                    tablamostrar.setModel(tabla1);
                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(null, "ERROR" + ex);
                                }
                                break;
                            case 0:
                                try {
                                    CLASES.Empleados.EliminarCargo(con, cod, borrado);
                                    JOptionPane.showMessageDialog(null, "Eliminao");
                                    tabla1.setRowCount(0);
                                    CLASES.Empleados.MostrarCargo(con, tabla1);
                                    tablamostrar.setModel(tabla1);
                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(null, "ERROR" + ex);
                                }
                                break;
                            case 2:
                                break;
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "ERROR" + ex);
                    }
                } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                    // El usuario eligió "Cancelar" o cerró la ventana
                    System.out.println("Cancelado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila primero.");
            }
        } else if (ModElimCargo1.this.band == 1) {

            for (int i = 0; i < tablamostrar.getRowCount(); i++) {
                boolean filaCompleta = true;
                // Recorremos todas las columnas
                for (int j = 1; j < tablamostrar.getColumnCount(); j++) {
                    Object valor = tablamostrar.getValueAt(i, j);

                    if (valor == null || valor.toString().trim().isEmpty()) {
                        filaCompleta = false;
                        break;
                    }
                }

                if (!filaCompleta) {
                    JOptionPane.showMessageDialog(null, "Hay filas nuevas con campos vacíos. Completá todos los datos antes de guardar.");
                    return; // Cancela el proceso de guardado
                }

            }

            for (int i = 0; i < tablamostrar.getRowCount(); i++) {
                // Obtener los datos actualizados desde la tabla
                int cod = Integer.parseInt(tablamostrar.getValueAt(i, 0).toString());
                String Zon = tablamostrar.getValueAt(i, 1).toString();
                try {
                    CLASES.Empleados.ActualizarCargo(con, cod, Zon);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR" + ex);
                }
            }

            JOptionPane.showMessageDialog(null, "Actualizao");
            try {
                tabla1.setRowCount(0);
                CLASES.Empleados.MostrarCargo(con, tabla1);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR1" + ex);
            }
        }
    }//GEN-LAST:event_BotonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ModElimCargo1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModElimCargo1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModElimCargo1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModElimCargo1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModElimCargo1(0, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton;
    private javax.swing.JButton Cancelar;
    private javax.swing.JLabel Leyenda;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablamostrar;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
