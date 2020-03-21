/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para el control de los else un if
 *
 * @author Jerduar
 */
public class Else extends Instruccion {

    /**
     * Clausula if en caso de que sea un else if
     */
    private final Si si;

    /**
     * Constructor de un else if
     *
     * @param fila Fila en laque se encuentra
     * @param col columna en la que se encuentra
     * @param s Else if
     */
    public Else(Si s, Integer fila, Integer col) {
        super(fila, col);
        this.si = s;
    }

    /**
     * Constructor de un else
     *
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     * @param cuerpo Cuerpo del else
     */
    public Else(LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.si = null;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        if (this.si == null) {
            TablaSimbolos nueva = new TablaSimbolos(t);
            return this.Recorrer(nueva);
        } else {
            return this.si.Ejecutar(t);
        }
    }

    @Override
    public void dibujar(String padre) {
        if (this.si != null) {
            this.si.dibujar(padre);
        } else {
            String n = NodoAST.getIdNodo("ELSE");
            Interprete.Interprete.Conectar(padre, n);
            this.DibujarCuerpo(n);
        }
    }

}
