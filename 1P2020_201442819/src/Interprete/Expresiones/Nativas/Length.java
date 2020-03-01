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
 * Clase para realizar la función nativa Lenght que retorna el tamanio de las
 * estructuras
 *
 * @author Jerduar
 */
public class Length extends CallFun {

    public Length(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("lenght", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba un parámetro", this.getFila(), this.getColumna());
        }
        
        Object re = this.getParam_act().getFirst().Resolver(t);
        
        if(re instanceof ErrorCompi){
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La expresión resulto en un error", this.getFila(), this.getColumna());
        }
        
        Integer lenght = ((Coleccion)re).getTamanio();
        return new VectorArit(TipoPrimitivo.INTEGER, lenght);
    }

}
