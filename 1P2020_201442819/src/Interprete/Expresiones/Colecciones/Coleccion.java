/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import java.util.LinkedList;

/**
 * Clase abstracta de la que heredan todas las estructuras que existen el
 * lenguaje
 *
 * @author Jerduar
 */
public abstract class Coleccion {

    /**
     * Lista de valores que contiene el vector
     */
    private final LinkedList<Object> valores;

    /**
     * Tipo de dato que contiene o es la estructura
     */
    private TipoPrimitivo tipo_dato;

    public Coleccion(TipoPrimitivo t, LinkedList<Object> v) {
        this.valores = v;
        this.tipo_dato = t;
    }

    /**
     * Devuelve el tipo de dato que contienen;
     *
     * @return Cadena con el tipo de dato
     */
    public final String Typeof() {
        return this.getTipo_dato().toString();
    }

    public int getTamanio() {
        return this.valores.size();
    }

    /**
     * Función para acceder a un valor del vector
     *
     * @param index índice entero
     * @return Object valor
     */
    public Object Acceder(Integer index) {
        return this.valores.get(index);
    }

    /**
     * Retorna una copia de la colección
     *
     * @return Nueva coleacción con una copia de sus elementos
     */
    public abstract Coleccion copiar();

    /**
     * Retorna la lista de valores
     *
     * @return Lista de valores
     */
    public LinkedList<Object> getValores() {
        return valores;
    }

    /**
     * Retorna el tipo de dato que contiene en su lista
     *
     * @return Tipo de valores que contiene el vector
     */
    public final TipoPrimitivo getTipo_dato() {
        return tipo_dato;
    }

    public final void setTipo_dato(TipoPrimitivo tipo_dato) {
        this.tipo_dato = tipo_dato;
    }

    /**
     * Determina si la instancia actual es una lista
     *
     * @return valor booleano
     */
    public final boolean isList() {
        return this instanceof ListArit;
    }

    /**
     * Determina si la instancia actual es un VectorArit
     *
     * @return valor booleano
     */
    public final boolean isVector() {
        return this instanceof VectorArit;
    }

}
