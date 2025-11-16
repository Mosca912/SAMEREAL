/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CLASES.Movimientos;
import CONEXIONES.Conexiones;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Historial extends javax.swing.JDialog{

    //Metodos para editar
    public class ModeloEditablePorFila extends DefaultTableModel {

        private int editableRow = -1;

        public ModeloEditablePorFila(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return false; // Nunca editar la columna ID
            }
            Object cod = getValueAt(row, 0);

            // Si es una fila nueva → editable
            if (cod == null || cod.toString().trim().isEmpty()) {
                return true;
            }

            // Si estamos en modo edición manual → solo esa fila
            return row == editableRow;
        }

        public void setEditableRow(int row) {
            this.editableRow = row;
            fireTableRowsUpdated(row, row);
        }

        public void bloquearEdicion() {
            this.editableRow = -1;
            fireTableDataChanged();
        }
    }
    //Metodos para editar

    //Tabla
    ModeloEditablePorFila tabla1 = new ModeloEditablePorFila(new String[]{"Nº", "Tripulacion", "Victor", "Fecha Relevado", "Movimientos"}, 0) {
        private final int editableRow = -1;
    };
    //Tabla

    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int id, band, mesActual, indice;
    String veri, fechaFormateada;

    public Historial(JFrame ventanaPrincipal) {
        super(ventanaPrincipal,true);
        initComponents();
        
        String rutaIcono = "/IMAGENES/iconosame.png";

        try {
            // Cargar la imagen desde los recursos del proyecto (la forma recomendada)
            Image icono = new ImageIcon(getClass().getResource(rutaIcono)).getImage();
            this.setIconImage(icono);

        } catch (Exception e) {
            System.err.println("Error al cargar el ícono: " + e.getMessage());
        }
        
        Tabla.setRowHeight(30);
        Tabla.setModel(tabla1);
        Tabla.getTableHeader().setReorderingAllowed(false);
        this.setLocationRelativeTo(null);
        mesActual = LocalDate.now().getMonthValue();
        titulo.setText("Historial de movimientos - mes "+mesActual);
        try {
            CLASES.Movimientos.MostrarHist(con, tabla1, Tabla, mesActual);
        } catch (SQLException ex) {
            Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
        }

        Tabla.getColumnModel().getColumn(0).setMinWidth(70);
        Tabla.getColumnModel().getColumn(0).setMaxWidth(70);

        Tabla.getColumnModel().getColumn(1).setMinWidth(280);
        Tabla.getColumnModel().getColumn(1).setMaxWidth(280);

        Tabla.getColumnModel().getColumn(2).setMinWidth(60);
        Tabla.getColumnModel().getColumn(2).setMaxWidth(60);

        Tabla.getColumnModel().getColumn(4).setMinWidth(100);
        Tabla.getColumnModel().getColumn(4).setMaxWidth(100);
        
        Tabla.getTableHeader().setResizingAllowed(false);
        
        Tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble click
                if (e.getClickCount() == 2 && Tabla.getSelectedRow() != -1) {
                    int fila = Tabla.getSelectedRow();
                    Object id = tabla1.getValueAt(fila, 0);
                    Object trip= tabla1.getValueAt(fila, 1);
                    Object vic= tabla1.getValueAt(fila, 2);
                    Object fec= tabla1.getValueAt(fila, 3);
                    int idOf = Integer.parseInt(id.toString());
                    String tripOf= String.valueOf(trip);
                    String vicOf= String.valueOf(vic);
                    String fecOf= String.valueOf(fec);
                    String direccion = "C:\\SAME\\Relevos\\Relevo _ id " + idOf + "_ fecha " + fecOf + "_ saliente " + tripOf + " _ victor " + vicOf + ".pdf";
                    Movimientos.abrirPDF(direccion);       
                }
            }
        });
        
        int ventanaTheme = CLASES.MenuClass.VentanaOpcThemeRet();
        if (ventanaTheme == 0) {
        } else if (ventanaTheme ==1){
            Color colorPersonalizado = new Color(44, 44, 53);
            Fondo.setBackground(colorPersonalizado);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        Salir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        mes = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Historial");
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Fondo.setBackground(new java.awt.Color(255, 255, 255));
        Fondo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        Tabla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(Tabla);

        Salir.setBackground(new java.awt.Color(78, 247, 177));
        Salir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Salir.setText("Salir");
        Salir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(52, 170, 121));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        titulo.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setText("Historial de Movimientos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        mes.setBackground(new java.awt.Color(78, 247, 177));
        mes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        mes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Opciones", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));
        mes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Mes"));
        mes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addComponent(Salir, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mes, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Salir, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mes))
                .addContainerGap())
        );

        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_SalirActionPerformed

    private void mesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mesActionPerformed
        indice = mes.getSelectedIndex();
        if (indice != 0) {
            try {
                tabla1.setRowCount(0);
                CLASES.Movimientos.MostrarHist(con, tabla1, Tabla, indice);
                titulo.setText("Historial de movimientos - mes "+indice);
            } catch (SQLException ex) {
                Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                tabla1.setRowCount(0);
                CLASES.Movimientos.MostrarHist(con, tabla1, Tabla, mesActual);
                titulo.setText("Historial de movimientos - mes "+mesActual);
            } catch (SQLException ex) {
                Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_mesActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Historial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Historial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Historial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Historial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Historial(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    private javax.swing.JButton Salir;
    private javax.swing.JTable Tabla;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> mes;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
