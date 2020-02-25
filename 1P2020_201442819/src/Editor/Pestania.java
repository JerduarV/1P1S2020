/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import org.apache.commons.io.FilenameUtils;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author Jerson Villatoro
 */
public class Pestania extends javax.swing.JPanel {

    /**
     * Variable que guarda la dirección del archivo que se abre en la pestaña en
     * caso de no tener archivo asociado el valor de la variable es ""
     */
    private String path;

    /**
     * Variable en la que se almacenara la extension del archivo
     */
    private String extension;

    /**
     * Variable en la que se guarda el nombre de la pestaña y corresponde al
     * nombre del archivo. Por defecto se le coloca el valor de Nueva
     */
    private String nombre_pestania;

    /**
     * Contruye una nueva Pestania
     *
     * @param path dirección del archivo con el que se crea
     */
    public Pestania(String path) {
        this.path = path;
        this.extension = FilenameUtils.getExtension(path);
        this.nombre_pestania = "Nueva";
        initComponents();
        configurarEditor();
    }

    /**
     * Devuelve una nueva instacia de la pestania y configura el editor según el
     * índice que le envían
     *
     * @param i
     */
    public Pestania(Integer i) {
        this.path = "";
        this.extension = FilenameUtils.getExtension(path);
        this.nombre_pestania = "Nueva";
        initComponents();
        configurarEditor();
        this.actualizarNumeroColumna();
    }

    /**
     * Función que devuelve el contenido en el área de texto de la pestaña
     *
     * @return Cadena con el contenido de la caja de texto
     */
    public String getContenido() {
        return rsta.getText();
    }

    /**
     * Función que lee el archivo y retorna su contenido
     *
     * @param path dirección del archivo a leer
     * @return
     */
    private String LeerArchivo(String path) {
        if ("".equals(path)) {
            return "";
        }
        try {
            String cad1 = "";
            String cad2 = "";
            File file = new File(path);
            FileReader f = new FileReader(file);
            this.extension = FilenameUtils.getExtension(cad2);
            BufferedReader br = new BufferedReader(f);
            this.nombre_pestania = new File(path).getName();
            while ((cad1 = br.readLine()) != null) {
                cad2 += cad1 + "\n";
            }
            br.close();
            return cad2;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Pestania.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Pestania.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Método que sobreescribe el archivo que se encuentra abierto en la pestaña
     * actual
     */
    public void SobreEscribirArchivo() {
        File archivo = new File(this.path);
        BufferedWriter bw;
        if (archivo.exists()) {
            try {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(rsta.getText());
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Pestania.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método que se usa para Inciar la ejecución usando la techa F5
     *
     * @param evt
     */
    private void jTextEntradaKeyReleased(java.awt.event.KeyEvent evt) {
        actualizarNumeroColumna();
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            ejecutar();
        }
    }

    /**
     * Método para iniciar la Ejecucion del codigo
     */
    private void ejecutar() {
        //System.out.println("Aqui tengo que ejecutar XD");
    }

    /**
     * Metodo para mostrar el número de Fila y Columna actual
     */
    private void actualizarNumeroColumna() {
        try {
            int caretPos = rsta.getCaretPosition();
            int offset = Utilities.getRowStart(rsta, caretPos);
            int colNum = caretPos - offset + 1;
            int fila = rsta.getCaretLineNumber() + 1;
            lb_col_fila.setText("FILA : " + fila + " | COLUMNA: " + colNum);
        } catch (BadLocationException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Método que configura el Editor de Texto
     */
    private void configurarEditor() {
        
        rsta = new RSyntaxTextArea(10, 100);

        rsta.setCodeFoldingEnabled(true);
        rsta.setCurrentLineHighlightColor(new Color(227, 242, 253, 200));
        rsta.setFadeCurrentLineHighlight(true);
        rsta.setBorder(BorderFactory.createEmptyBorder());
        rsta.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextEntradaKeyReleased(evt);
            }
        });

        RTextScrollPane rtsp = new RTextScrollPane(rsta);
        rsta.setText(LeerArchivo(path));
        rtsp.setViewportBorder(BorderFactory.createEmptyBorder());
        rtsp.setSize(500, 500);
        Principal.add(rtsp);

        CambiarEstilo(rsta, new Font("Verdana", Font.PLAIN, 17));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Principal = new javax.swing.JPanel();
        lb_col_fila = new javax.swing.JLabel();

        Principal.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, 1148, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lb_col_fila, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Principal, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_col_fila, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Principal;
    private javax.swing.JLabel lb_col_fila;
    // End of variables declaration//GEN-END:variables
    private RSyntaxTextArea rsta;

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the nombre_pestania
     */
    public String getNombre_pestania() {
        return nombre_pestania;
    }

    /**
     * @param nombre_pestania the nombre_pestania to set
     */
    public void setNombre_pestania(String nombre_pestania) {
        this.nombre_pestania = nombre_pestania;
    }

    /**
     * CAmbia el estilo del área de texto
     * @param t área de textos
     * @param f fuente que se utiliza
     */
    private void CambiarEstilo(RSyntaxTextArea t, Font f) {
        SyntaxScheme ss = t.getSyntaxScheme();
        ss = (SyntaxScheme) ss.clone();
        for (int i = 0; i < ss.getStyleCount(); i++) {
            if (ss.getStyle(i) != null) {
                ss.getStyle(i).font = f;
            }
        }
        t.setSyntaxScheme(ss);
        t.setFont(f);
    }
    /**
     * Devuel ve el texto que contiene el área de texto
     * @return 
     */
    public String getTexto() {
        return this.rsta.getText();
    }
}