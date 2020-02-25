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
 * Clase while que funciona como un ciclo mientras en el lenguaje
 *
 * @author Jerduar
 */
public class While extends Instruccion {

    /**
     * Condición a la que esta sujeta la ejecución del ciclo
     */
    private final Expresion condicion;

    /**
     * Constructor de la clase While
     *
     * @param e Expresión condicional
     * @param cuerpo Lista de instrucciones asociadas
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public While(Expresion e, LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.condicion = e;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        Object c = this.condicion.Resolver(t);

        if (c instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Error en la condición del If", this.getFila(), this.getColumna());
        }

        Object c2 = this.getValorBool(c);

        if (c2 instanceof ErrorCompi) {
            return c2;
        }

        boolean b = (Boolean) c2;

        while (b) {
            TablaSimbolos nueva = new TablaSimbolos(t);
            nueva.IncrementarDisplay();
            Object result = this.Recorrer(nueva);
            
            if(result instanceof Break){
                break;
            }

            c = this.condicion.Resolver(t);

            if (c instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semántico", "Error en la condición del If", this.getFila(), this.getColumna());
            }

            c2 = this.getValorBool(c);

            if (c2 instanceof ErrorCompi) {
                return c2;
            }
            
            //ACTUALIZACIÓN DE LA CONDICIÓN
            b = (Boolean) c2;
        }

        return null;
    }

    /**
     * Devuelve el valor booleano de acuerdo a la estructura que resulte de
     * resolver la condición asociada
     *
     * @param estructura Estructra vector, lista, array o matriz
     * @return Object booleano o error
     */
    private Object getValorBool(Object estructura) {
        if (estructura instanceof VectorArit) {
            VectorArit vc = (VectorArit) estructura;
            if (!vc.isBool()) {
                return VentanaErrores.getVenErrores().AgregarError("Semántico", "Se esperaba un booleano en la condición del if", this.getFila(), this.getColumna());
            }
            return (Boolean) vc.getValores().getFirst();
        } else {
            throw new UnsupportedOperationException("Solo puedo manejar vectores en el while");
        }
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
