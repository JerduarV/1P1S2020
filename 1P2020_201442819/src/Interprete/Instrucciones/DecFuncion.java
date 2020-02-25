/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para menejar la declaración de funciones
 *
 * @author Jerduar
 */
public class DecFuncion extends Instruccion {

    /**
     * Nombre de la función
     */
    private final String id;

    /**
     * Lista de parámatros de la función
     */
    private final LinkedList<Declaracion> lista_param;

    /**
     * Constructor de un Declaración de Función
     *
     * @param id Identificador de la función
     * @param lista_param lista de parámetros
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     * @param cuerpo Cuerpo de la función
     */
    public DecFuncion(String id, LinkedList<Declaracion> lista_param, LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.id = id;
        this.lista_param = lista_param;
    }

    /**
     * Método que guarda la función en la tabla de simbolos
     *
     * @param global Tabla de símbolos global donde se deben guardar todas las
     * funciones
     */
    public void GuardarFuncion(TablaSimbolos global) {
        global.GuardarFuncion(this.id, this);
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        return this.Recorrer(t);
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Retorna la lista de parámetros
     *
     * @return LinkedList de Declaraciones
     */
    public LinkedList<Declaracion> getLista_param() {
        return lista_param;
    }

}
