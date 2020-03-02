/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 *
 * @author Jerduar
 */
public class Round extends CallFun{
    
    public Round(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Round", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Trunk: Se esperaba un parámetro numérico", this.getFila(), this.getColumna());
        }

        Object c = this.getParam_act().getFirst().Resolver(t);

        if (c instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Trunk: Hubo un error al resolver la expresión", this.getFila(), this.getColumna());
        }

        Coleccion col = (Coleccion) c;

        if (!(col instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Trunk: Se esperaba un vector numérico", this.getFila(), this.getColumna());
        }

        VectorArit v = (VectorArit) col;

        if (v.getTamanio() != 1 || !v.isDouble()) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Trunk: Se esperaba un vector numérico de tamanio 1", this.getFila(), this.getColumna());
        }
        
        Double d = (Double)v.Acceder(0);
        return new VectorArit(TipoPrimitivo.INTEGER, ((Long)Math.round(d)).intValue());
    }
}
