/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Expresion;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;

/**
 * Clase declaración que se usará únicamente en los paramaetros formales de la
 * declaración de funciones
 *
 * @author Jerduar
 */
public class Declaracion extends Instruccion {

    /**
     * Nombre del parametro
     */
    private final String id;

    /**
     * Valor por defecto
     */
    private final Expresion exp;

    /**
     * Declaracion de parametro sin valor por defecto
     *
     * @param id Identificador del parámetro
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public Declaracion(String id, Integer fila, Integer col) {
        super(fila, col);
        this.id = id;
        this.exp = null;
    }

    /**
     * Declaración de parámetro con valor por defecto
     *
     * @param id Identificador del parámetro
     * @param exp Expresión que le da el valor defecto
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public Declaracion(String id, Expresion exp, Integer fila, Integer col) {
        super(fila, col);
        this.id = id;
        this.exp = exp;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        Object valor_defecto = "null";
        if (exp != null) {
            valor_defecto = this.exp.Resolver(t);
            if (valor_defecto instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Sementico", "Error en la expresión por defecto", this.getFila(), this.getColumna());
            }
        }

        return t.GuardarParametro(this.id, valor_defecto) ? null : VentanaErrores.getVenErrores().AgregarError("Semantico", "Ya existe el parametro " + this.id, this.getFila(), this.getColumna());
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo("PARAMF"), m = NodoAST.getIdNodo(this.id);
        Interprete.Interprete.Conectar(padre, n);
        Interprete.Interprete.Conectar(n, m);
        if(this.exp != null){
            this.exp.dibujar(n);
        }
    }

    /**
     * Retorna el identificador del parámetro
     *
     * @return Identificador del parámetro
     */
    public String getId() {
        return id;
    }

    /**
     * Devualeve si este parámetro tiene un valor por defecto
     *
     * @return
     */
    public boolean TieneValorDefault() {
        return this.exp != null;
    }

}
