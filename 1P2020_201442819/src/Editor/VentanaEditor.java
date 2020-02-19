/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

//import InterpreteGXML.IntepreteGXML;
import Interprete.Interprete;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Jerson Villatoro
 */
public class VentanaEditor extends javax.swing.JFrame {

    /**
     * Creates new form VentanaEditor
     */
    public VentanaEditor() {

        initComponents();

        this.setVisible(true);
    }

    /**
     * Método que da inicio al inteprete
     */
    private void Interpretar() {
        if ("".equals(this.getContenido())) {
            return;
        }
        if (this.dbOpciones.getSelectedIndex() == 1) {
//            InterpreteGXML.IntepreteGXML gmxl = new IntepreteGXML();
//            String path = gmxl.Traducir(this.getContenido());
//            if (path != null) {
//                AbrirTraduccion(path);
//            }

        } else if (this.dbOpciones.getSelectedIndex() == 0) {
            Interprete i = new Interprete();
            i.Interpretar(this.getContenido());
        }

        if (VentanaErrores.getVenErrores().getErrores() > 0) {
            VentanaErrores.getVenErrores().setVisible(true);
        }
    }

    /**
     * Función que devuelve el contenido de la pestaña actual
     *
     * @return Cadena con contenido de la pestaña
     */
    private String getContenido() {
        Pestania actual = (Pestania) tabpane.getSelectedComponent();
        if (actual != null) {
            return actual.getContenido();
        }
        return "";
    }

    /**
     * Metodo para agregar pestañas al tabpane
     */
    private void AgregarPestaña() {
        tabpane.addTab("Nueva", new Pestania(dbOpciones.getSelectedIndex()));
    }

    /**
     * Método para abrir un archivo de GXML O FS
     */
    private void AbrirArchivo() {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Texto (*.fs, *.gxml, *.gdato)", "fs", "gxml", "gdato");
        fc.setFileFilter(filtro);

        int file = fc.showOpenDialog(this);
        if (file == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            Pestania p = new Pestania(f.getPath());
            tabpane.addTab(p.getNombre_pestania(), p);
        }
    }

    private void AbrirTraduccion(String path) {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Texto (*.fs, *.gxml, *.gdato)", "fs", "gxml", "gdato");
        fc.setFileFilter(filtro);

        Pestania p = new Pestania(path);
        tabpane.addTab(p.getNombre_pestania(), p);

    }

    /**
     * Metodo para imprimir en la consola del IDE
     *
     * @param cadena
     */
    public static void ImprimirEnConsoloa(String cadena) {
        taConsola.append(">>>" + cadena + "\n");
    }

    /**
     * Método para guardar un archivo ya existente
     */
    private void GuardarArchivo() {
        Pestania actual = (Pestania) tabpane.getSelectedComponent();
        if (actual != null) {
            actual.SobreEscribirArchivo();
        }
    }

    /**
     * Método para cerrar la pestania actual
     */
    private void CerrarPestania() {
        int pestania_actual = tabpane.getSelectedIndex();
        if (pestania_actual != -1) {
            tabpane.removeTabAt(pestania_actual);
        }
    }

    /**
     * Muestra la ventana de Errores
     */
    private void MostrarErrores() {
        VentanaErrores ven = VentanaErrores.getVenErrores();
        ven.setVisible(true);
    }

    /**
     * Método para limpiar la consola del sistema
     */
    private void LimpiarConsola() {
        taConsola.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btInterpretar = new javax.swing.JButton();
        javax.swing.ImageIcon i = new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icono_play.png")).getImage().getScaledInstance(95, 70, Image.SCALE_DEFAULT));
        tabpane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        taConsola = new javax.swing.JTextArea();
        dbOpciones = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        opcAbrir = new javax.swing.JMenuItem();
        opcGuardar = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        opcAbrirPestania = new javax.swing.JMenuItem();
        opcCerrarPestania = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("1P1S2020");

        btInterpretar.setIcon(i);
        btInterpretar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInterpretarActionPerformed(evt);
            }
        });
        btInterpretar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btInterpretarKeyTyped(evt);
            }
        });

        taConsola.setEditable(false);
        taConsola.setColumns(20);
        taConsola.setFont(new java.awt.Font("DialogInput", 0, 18)); // NOI18N
        taConsola.setLineWrap(true);
        taConsola.setRows(5);
        jScrollPane1.setViewportView(taConsola);

        dbOpciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "JFlex/Cup", "JavaCC" }));

        jMenu1.setText("ARCHIVO");

        opcAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        opcAbrir.setText("ABRIR");
        opcAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcAbrirActionPerformed(evt);
            }
        });
        jMenu1.add(opcAbrir);

        opcGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        opcGuardar.setText("GUARDAR");
        opcGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcGuardarActionPerformed(evt);
            }
        });
        jMenu1.add(opcGuardar);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("GUARDAR COMO");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("PESTAÑAS");

        opcAbrirPestania.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        opcAbrirPestania.setText("ABRIR PESTAÑA");
        opcAbrirPestania.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcAbrirPestaniaActionPerformed(evt);
            }
        });
        jMenu2.add(opcAbrirPestania);

        opcCerrarPestania.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        opcCerrarPestania.setText("CERRAR PESTAÑA");
        opcCerrarPestania.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcCerrarPestaniaActionPerformed(evt);
            }
        });
        jMenu2.add(opcCerrarPestania);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("ERRORES");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("REPORTE");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("UTILIDADES");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Limpiar Consola");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        jMenuItem2.setText("Ejecutar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1199, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btInterpretar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dbOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tabpane, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btInterpretar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(dbOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabpane, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void opcAbrirPestaniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcAbrirPestaniaActionPerformed
        AgregarPestaña();
    }//GEN-LAST:event_opcAbrirPestaniaActionPerformed

    private void opcAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcAbrirActionPerformed
        AbrirArchivo();
    }//GEN-LAST:event_opcAbrirActionPerformed

    private void opcGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcGuardarActionPerformed
        GuardarArchivo();
    }//GEN-LAST:event_opcGuardarActionPerformed

    private void opcCerrarPestaniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcCerrarPestaniaActionPerformed
        CerrarPestania();
    }//GEN-LAST:event_opcCerrarPestaniaActionPerformed

    private void btInterpretarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInterpretarActionPerformed
        Interpretar();
    }//GEN-LAST:event_btInterpretarActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        MostrarErrores();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void btInterpretarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btInterpretarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btInterpretarKeyTyped

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        LimpiarConsola();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Interpretar();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        GuardarComo();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
            java.util.logging.Logger.getLogger(VentanaEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaEditor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btInterpretar;
    private javax.swing.JComboBox<String> dbOpciones;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem opcAbrir;
    private javax.swing.JMenuItem opcAbrirPestania;
    private javax.swing.JMenuItem opcCerrarPestania;
    private javax.swing.JMenuItem opcGuardar;
    public static javax.swing.JTextArea taConsola;
    private javax.swing.JTabbedPane tabpane;
    // End of variables declaration//GEN-END:variables

    private void GuardarComo() {

        Pestania actual = (Pestania) tabpane.getSelectedComponent();
        if (actual == null) {
            return;
        }

        JFileChooser guardar = new JFileChooser();
        guardar.showSaveDialog(null);
        guardar.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        File archivo = guardar.getSelectedFile();
        FileWriter escribir;

        try {
            if (archivo == null) {
                return;
            }
            escribir = new FileWriter(archivo, true);
            escribir.write(actual.getTexto());
            escribir.close();
            actual.setPath(archivo.getPath());

            int i = tabpane.getSelectedIndex();
            tabpane.setTitleAt(i, archivo.getName());
        } catch (IOException ex) {
            //Logger.getLogger(VentanaEditor.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
        }

    }
}
