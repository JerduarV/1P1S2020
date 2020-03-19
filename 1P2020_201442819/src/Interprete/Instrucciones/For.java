/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import Utileria.Retorno;
import java.util.LinkedList;

/**
 * Clase para manejar la instrucción for que funciona como un foreach
 *
 * @author Jerduar
 */
public class For extends Instruccion {

    /**
     * Identificador de la variable en la que se actualizará el for
     */
    private final String id;

    /**
     * Expresión que contendra el valor que se le asignará a la variable
     */
    private final Expresion e;

    /**
     * constructor de la instrucción for
     *
     * @param id Identificador de la variable para iterar
     * @param e Expresión que contiene la estructura a iterar
     * @param cuerpo Cuerpo del for
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public For(String id, Expresion e, LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.id = id;
        this.e = e;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        Object valor = this.e.Resolver(t);

        if (valor instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "For: hubo un error en la expresión", this.getFila(), this.getColumna());
        }

        Coleccion col = (Coleccion) valor;

        for (int y = 0; y < col.getTamanio(); y++) {
            Object v = col.Acceder(y);
            TablaSimbolos nueva = new TablaSimbolos(t);
            nueva.IncrementarDisplay();

            if (col instanceof VectorArit) {
                v = new VectorArit(col.getTipo_dato(), v);
            }

            nueva.GuardarVariable(id, v);
            Object result = this.Recorrer(nueva);
            col.getValores().set(y, nueva.BusarVariable(id));
            if (result instanceof Break) {
                break;
            }

            if (result instanceof Continue) {
                continue;
            }

            if (result instanceof Retorno) {
                return result;
            }
        }

        return null;
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
