/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import java.util.LinkedList;

/**
 *
 * @author Jerduar
 */
public abstract class Coleccion {

    /**
     * Lista de valores que contiene el vector
     */
    private final LinkedList<Object> valores;

    public Coleccion(LinkedList<Object> v) {
        this.valores = v;
    }

    /**
     * Devuelve el tipo de dato que contienen;
     *
     * @return Cadena con el tipo de dato
     */
    public abstract String Typeof();

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

}
