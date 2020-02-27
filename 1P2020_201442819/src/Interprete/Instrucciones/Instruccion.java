/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.Return;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import Utileria.Retorno;
import java.util.LinkedList;

/**
 * Clase abstracta instrucción de la que heredan todas las intrucciones del
 * lenguaje
 *
 * @author Jerduar
 */
public abstract class Instruccion extends NodoAST {

    /**
     * Atributo en el que se guardan el cuerpo de la instrucción si lo tuviera
     */
    private final LinkedList<NodoAST> cuerpo;

    /**
     * Constructor de la clase instrucción que no contiene cuerpo
     *
     * @param fila integer fila en la que se encuentra
     * @param col integer columna en la que se encuentra
     */
    public Instruccion(Integer fila, Integer col) {
        super(fila, col);
        this.cuerpo = null;
    }

    /**
     * Constructor de la clase instrucción que contiene asociada un cuerpo de
     * instrucciones
     *
     * @param fila integer fila en la que se encuentra
     * @param col integer columna en la que se encuentra
     * @param cuerpo LinkedList de NodoAST
     */
    public Instruccion(Integer fila, Integer col, LinkedList<NodoAST> cuerpo) {
        super(fila, col);
        this.cuerpo = cuerpo;
    }

    /**
     * Función abstractar ejecutar que deben implementar todas las instrucciones
     *
     * @param t Tabla de Simbolos que representa el entorno de ejecución
     * @return Object
     */
    public abstract Object Ejecutar(TablaSimbolos t);

    /**
     * Función usada por las instrucciones que tiene un cuerpo asociado para
     * recorrerlo en su ejecución
     *
     * @param t Tabla de Simbolos del entorno de ejecución
     * @return Object
     */
    public Object Recorrer(TablaSimbolos t) {
        if (cuerpo != null) {
            for (NodoAST i : this.cuerpo) {
                if (i instanceof DecFuncion) {
                    continue;
                }

                Object result = i instanceof Instruccion ? ((Instruccion) i).Ejecutar(t) : ((Expresion) i).Resolver(t);
                if (result instanceof Break || result instanceof Continue || result instanceof Retorno) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Retorna el cuerpo de la instrucción, esta función solo será usada en la
     * clase ábol por facilidad
     *
     * @return
     */
    public LinkedList<NodoAST> getCuerpo() {
        return cuerpo;
    }

    /**
     * Devuelve el valor booleano de acuerdo a la estructura que resulte de
     * resolver la condición asociada
     *
     * @param estructura Estructra vector, lista, array o matriz
     * @return Object booleano o error
     */
    protected Object getValorBool(Object estructura) {
        if (estructura instanceof VectorArit) {
            VectorArit vc = (VectorArit) estructura;
            if (!vc.isBool()) {
                return VentanaErrores.getVenErrores().AgregarError("Semántico", "Se esperaba un booleano en la condición", this.getFila(), this.getColumna());
            }
            return (Boolean) vc.getValores().getFirst();
        } else {
            throw new UnsupportedOperationException("Solo puedo manejar vectores en el while");
        }
    }

}
