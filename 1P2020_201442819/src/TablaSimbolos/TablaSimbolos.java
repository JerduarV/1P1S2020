/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import java.util.Hashtable;

/**
 * Clase para el manejo de los simbolos del lenguaje
 * @author Jerduar
 */
public class TablaSimbolos extends Hashtable{
    
    /**
     * Tabla de Simbolos padre, es nula cuando el padre es la tabla global
     */
    private final TablaSimbolos padre;

    /**
     * Constructor de la tabla de simbolos que recibe como parámetro a su tabla
     * padre
     * @param padre TablaSimbolos superior 
     */
    public TablaSimbolos(TablaSimbolos padre) {
        this.padre = padre;
    }
    
    
}
