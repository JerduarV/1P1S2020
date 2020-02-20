/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Colecciones;

import Interprete.Expresiones.TipoPrimitivo;
import java.util.LinkedList;

/**
 * Clase vector del lenguaje
 * @author Jerduar
 */
public class VectorArit {
    
    /**
     * Tipo de dato que contiene el vector
     */
    private final TipoPrimitivo tipo_dato;
    
    /**
     * Lista de valores que contiene el vector
     */
    private final LinkedList<Object> valores;

    /**
     * Constructor de la clase vector
     * @param tipo_dato Tipo de dato primitivo que contiene
     * @param valores LinkedList de valores
     */
    public VectorArit(TipoPrimitivo tipo_dato, LinkedList<Object> valores) {
        this.tipo_dato = tipo_dato;
        this.valores = valores;
    }

    /**
     * Retorna el tipo de dato que contiene en su lista
     * @return 
     */
    public TipoPrimitivo getTipo_dato() {
        return tipo_dato;
    }

    /**
     * Retorna la lista de valores del vector
     * @return 
     */
    public LinkedList<Object> getValores() {
        return valores;
    }
    
    /**
     * Retorna el tamanio de la coleccion
     * @return 
     */
    public int getTamanio(){
        return this.valores.size();
    }
    
    /**
     * Función para acceder a un valor del vector
     * @param index índice entero
     * @return Object valor
     */
    public Object Acceder(Integer index){
        return this.valores.get(index);
    }
    
    
}
