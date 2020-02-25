/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import TablaSimbolos.TablaSimbolos;

/**
 * Clase que simula el comportamiento de un break
 *
 * @author Jerduar
 */
public class Break extends Instruccion {

    /**
     * Constructor de una instrucción break
     *
     * @param fila
     * @param col
     */
    public Break(Integer fila, Integer col) {
        super(fila, col);
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        return t.getDisplay() > 0 ? this : VentanaErrores.getVenErrores().AgregarError("Semantico", "Break no puede venir fuera de un ciclo", this.getFila(), this.getColumna());
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
