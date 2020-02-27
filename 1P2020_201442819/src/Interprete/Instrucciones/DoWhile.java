/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Expresion;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase que simula el comportamiento de un do while
 *
 * @author Jerduar
 */
public class DoWhile extends Instruccion {

    /**
     * Condición del do while
     */
    private final Expresion cond;

    public DoWhile(LinkedList<NodoAST> cuerpo, Expresion c, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.cond = c;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        boolean b = true;
        do {
            TablaSimbolos nueva = new TablaSimbolos(t);
            nueva.IncrementarDisplay();

            Object result = this.Recorrer(nueva);

            if (result instanceof Break) {
                break;
            } else if (result instanceof Continue) {
                continue;
            }

            Object c = this.cond.Resolver(t);

            if (c instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semántico", "Error en la condición del If", this.getFila(), this.getColumna());
            }

            c = this.getValorBool(c);

            if (c instanceof ErrorCompi) {
                return c;
            }

            //ACTUALIZACIÓN DE LA CONDICIÓN
            b = (Boolean) c;

        } while (b);

        return null;
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
