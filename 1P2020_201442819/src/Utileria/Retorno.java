/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utileria;

/**
 * Clase que se usa en la Expresión retorno para sacar el resultado
 *
 * @author Jerduar
 */
public class Retorno {

    /**
     * Valor del retorno
     */
    private final Object valor;

    /**
     * Constructor de la clase
     * @param valor valor que tiene el retorno
     */
    public Retorno(Object valor) {
        this.valor = valor;
    }

    /**
     * Obtiene el valor del retorno
     * @return Objec con el valor
     */
    public Object getValor() {
        return valor;
    }

}
