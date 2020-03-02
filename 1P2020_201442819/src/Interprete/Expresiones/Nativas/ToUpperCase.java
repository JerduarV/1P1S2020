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
public class ToUpperCase extends CallFun{
    
    public ToUpperCase(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("ToUpperCase", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "StringLength: Se esperaba un parámetro string", this.getFila(), this.getColumna());
        }

        Object c = this.getParam_act().getFirst().Resolver(t);

        if (c instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "StringLength: Hubo un error al resolver la expresión", this.getFila(), this.getColumna());
        }

        Coleccion col = (Coleccion) c;

        if (!(col instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "StringLength: Se esperaba un vector string", this.getFila(), this.getColumna());
        }

        VectorArit v = (VectorArit) col;

        if (v.getTamanio() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "StringLength: Se esperaba un vector string de tamaño 1", this.getFila(), this.getColumna());
        }
        
        String cad = v.Acceder(0).toString();
        
        return new VectorArit(TipoPrimitivo.STRING, cad.toUpperCase());
    }
    
    
}
