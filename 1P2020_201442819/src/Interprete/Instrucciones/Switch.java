/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import Utileria.Retorno;
import java.util.LinkedList;

/**
 * Clase para manejar el comportamiento de un switch
 *
 * @author Jerduar
 */
public class Switch extends Instruccion {

    /**
     * Expresión que se usará para comparar con los casos
     */
    private final Expresion e;

    /**
     * Lista de casos de contiene el switch
     */
    private final LinkedList<Case> lista_casos;

    /**
     * Defecto en caso de que no haga match con ningún caso
     */
    private final DefaultSw defecto;

    /**
     * Constructor sin defecto
     *
     * @param e Expresión de control del switch
     * @param l Lista de casos
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public Switch(Expresion e, LinkedList<Case> l, Integer fila, Integer col) {
        super(fila, col);
        this.e = e;
        this.lista_casos = l;
        this.defecto = null;
    }

    /**
     * Constructor con defecto
     *
     * @param e Expresión de control del switch
     * @param lista_casos Lista de casos
     * @param defecto Defecto
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public Switch(Expresion e, LinkedList<Case> lista_casos, DefaultSw defecto, Integer fila, Integer col) {
        super(fila, col);
        this.e = e;
        this.lista_casos = lista_casos;
        this.defecto = defecto;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        TablaSimbolos nueva = new TablaSimbolos(t);
        nueva.IncrementarDisplay();

        Object val = this.e.Resolver(t);

        if (val instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Switch: Error en la expresión de control", this.getFila(), this.getColumna());
        }

        if (!(val instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Switch: Se esperaba una expresión primitiva", this.getFila(), this.getColumna());
        }

        VectorArit a = (VectorArit) val;
        boolean bandera = false, vinoBreak = false;

        for (Case caso : this.lista_casos) {
            if (bandera) {
                Object r = caso.Ejecutar(nueva);
                if (r instanceof Break) {
                    vinoBreak = true;
                    break;
                } else if (r instanceof Retorno) {
                    return r;
                }
                continue;
            }

            Object c = caso.getE().Resolver(nueva);

            if (c instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Caso: Error en la expresión de control", caso.getFila(), caso.getColumna());
            }

            if (!(c instanceof VectorArit)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Caso: Se esperaba una expresión primitiva", caso.getFila(), caso.getColumna());
            }

            VectorArit b = (VectorArit) c;
            boolean valor;
            if(a.isNumerico() && b.isNumerico()){
                valor = (a.isInteger() ? (Integer)a.Acceder(0) : (Double)a.Acceder(0)) == (b.isInteger() ? (Integer)b.Acceder(0) : (Double)b.Acceder(0));
            }else{
                valor = a.Acceder(0).equals(b.Acceder(0));
            }
            if (valor) {
                Object r = caso.Ejecutar(nueva);
                bandera = true;
                if (r instanceof Break) {
                    vinoBreak = true;
                    break;
                } else if (r instanceof Retorno) {
                    return r;
                }
            }
        }

        if (!bandera || (bandera && !vinoBreak)) {
            return this.defecto == null ? null : this.defecto.Ejecutar(nueva);
        }

        return null;
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo("SWITCH");
        this.e.dibujar(n);
        String m = NodoAST.getIdNodo("LCASOS");
        Interprete.Interprete.Conectar(padre, n);
        Interprete.Interprete.Conectar(n, m);
        for(Case c: this.lista_casos){
            c.dibujar(m);
        }
        if(this.defecto != null){
            this.defecto.dibujar(m);
        }
    }

}
