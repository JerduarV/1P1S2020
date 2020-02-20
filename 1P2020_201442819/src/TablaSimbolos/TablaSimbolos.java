/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import TablaSimbolos.Simbolo.ROL;
import java.util.Hashtable;

/**
 * Clase para el manejo de los simbolos del lenguaje
 *
 * @author Jerduar
 */
public class TablaSimbolos extends Hashtable {

    /**
     * Tabla de Simbolos padre, es nula cuando el padre es la tabla global
     */
    private final TablaSimbolos padre;

    /**
     * Constructor de la tabla de simbolos que recibe como parámetro a su tabla
     * padre
     *
     * @param padre TablaSimbolos superior
     */
    public TablaSimbolos(TablaSimbolos padre) {
        this.padre = padre;
    }

    /**
     * Guarda la variable o le cambia el valor en la tabla de simbolos
     * @param nombre
     * @param valor 
     */
    public void GuardarVariable(String nombre, Object valor) {
        int key = this.getHashCode(nombre, ROL.VARIABLE);
        this.put(key, valor);
    }

    /**
     * Retorna la llave con la que se guardará o buscará el simbolo en la tabla
     *
     * @param nombre de la variable o función
     * @param r rol del símbolo
     * @return entero llave
     */
    private int getHashCode(String nombre, ROL r) {
        return (r + "$$$" + nombre).hashCode();
    }

}
