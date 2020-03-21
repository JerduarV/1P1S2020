/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.Interprete;
import Interprete.NodoAST;
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
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encunetra
     */
    public Break(Integer fila, Integer col) {
        super(fila, col);
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        return t.getDisplay() > 0 ? this : VentanaErrores.getVenErrores().AgregarError("Semantico", "Break no puede venir fuera de un ciclo", this.getFila(), this.getColumna());
    }

    @Override
    public void dibujar(String padre) {
        Interprete.Conectar(padre, NodoAST.getIdNodo("BREAK"));
    }

}
