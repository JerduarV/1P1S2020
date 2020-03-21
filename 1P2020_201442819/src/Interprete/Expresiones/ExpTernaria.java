/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;

/**
 * Clase para manejar el operador ternario
 *
 * @author Jerduar
 */
public class ExpTernaria extends Expresion {

    /**
     * Condición, expresión verdadera y expresión falsa
     */
    private final Expresion cond, exp1, exp2;

    /**
     * Constructor del operador ternario
     *
     * @param c Condición de la expresión ternaria
     * @param e1 Expresion en caso de que la condición sea verdadero
     * @param e2 Expresión en caso de que la condición sea false
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public ExpTernaria(Expresion c, Expresion e1, Expresion e2, Integer fila, Integer col) {
        super(fila, col);
        this.cond = c;
        this.exp1 = e1;
        this.exp2 = e2;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        Object r = this.cond.Resolver(t);

        if (r instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en la condición del operador ternario", this.getFila(), this.getColumna());
        }

        if (!(r instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Ternario: Se espera un vector booleano", this.getFila(), this.getColumna());
        }

        VectorArit condicion = (VectorArit) r;

        if (!condicion.isBool()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Ternario: Debe ser un booleano", this.getFila(), this.getColumna());
        }
        
        boolean b = (Boolean)condicion.Acceder(0);
        
        return b ? this.exp1.Resolver(t) : this.exp2.Resolver(t);
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo("?");
        Interprete.Interprete.Conectar(padre, n);
        this.cond.dibujar(n);
        this.exp1.dibujar(n);
        this.exp2.dibujar(n);
    }

}
