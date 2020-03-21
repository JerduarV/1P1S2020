/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Editor.VentanaErrores;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;

/**
 * Clase para manejar las invocaciones a variables contenidas en tabla de simbolos
 * @author Jerduar
 */
public class Identificador extends Expresion{
    
    /**
     * Nombre de la variable
     */
    private final String id;

    /**
     * Constructor de la clase identificador
     * @param id Identificador string
     * @param fila fila en la que se encuentra
     * @param col columan en la que se encuentra
     */
    public Identificador(String id, Integer fila, Integer col) {
        super(fila, col);
        this.id = id;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        Object result = t.BusarVariable(this.id);
        return result == null ? VentanaErrores.getVenErrores().AgregarError("Semantico", "No se encuentra la variable " + this.id, this.getFila(), this.getColumna()) : result;
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo(this.id);
        Interprete.Interprete.Conectar(padre, n);
    }

    public String getId() {
        return id;
    }
    
}
