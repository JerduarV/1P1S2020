/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para la instruccion si
 *
 * @author Jerduar
 */
public class Si extends Instruccion {

    /**
     * Condición que determinará si se ejecuta el cuerpo del if
     */
    private final Expresion condicion;

    /**
     * Else asociado el if
     */
    private final Else Sino;

    /**
     * Constructor de la clase si
     *
     * @param cond Condición a la que está sujeta su ejecución
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     * @param cuerpo cuerpo del if
     */
    public Si(Expresion cond, LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.condicion = cond;
        this.Sino = null;
    }

    /**
     * Constructor de If que si tiene un else
     *
     * @param condicion
     * @param Sino
     * @param fila
     * @param col
     * @param cuerpo
     */
    public Si(Expresion condicion, LinkedList<NodoAST> cuerpo, Else Sino, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.condicion = condicion;
        this.Sino = Sino;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        Object c = this.condicion.Resolver(t);

        if (c instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Error en la condición del If", this.getFila(), this.getColumna());
        }

        if (c instanceof VectorArit) {
            VectorArit vc = (VectorArit) c;
            if (!vc.isBool()) {
                return VentanaErrores.getVenErrores().AgregarError("Semántico", "Se esperaba un booleano en la condición del if", this.getFila(), this.getColumna());
            }

            boolean b = (Boolean) vc.getValores().getFirst();

            TablaSimbolos nuevo = new TablaSimbolos(t);

            return b ? this.Recorrer(nuevo) : (this.Sino != null ? this.Sino.Ejecutar(t) : null);
        }

        throw new UnsupportedOperationException("No puedo manejar otras estructuras en el if");
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
