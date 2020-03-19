/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.ArrayArit;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Colecciones.ListArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase que maneja el constructor de arreglos
 *
 * @author Jerduar
 */
public class Array extends CallFun {

    /**
     * Constructor de la clase Array que sirve como constructor de arreglos
     * dentro del lenguaje Arit
     *
     * @param p Lista de parámetros actuales
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public Array(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Array", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        if (this.getParam_act().size() != 2) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Array: Se esperaban 2 parámetos", this.getFila(), this.getColumna());
        }

        Object param1 = this.getParam_act().getFirst().Resolver(t),
                param2 = this.getParam_act().get(1).Resolver(t);

        if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Array: Error en los parámetros", this.getFila(), this.getColumna());
        }

        Coleccion datos = (Coleccion) param1,
                dims = (VectorArit) param2;

        if (!(dims.isInteger() && dims.isVector())) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Array: Se esperaba un vector de enteros", this.getFila(), this.getColumna());
        }

        if (!datos.isVector() && !datos.isList()) {
            return VentanaErrores.getVenErrores().AgregarError("Semántico", "Array: Los datos solo pueden ser listas o vectores primitivos", this.getFila(), this.getColumna());
        }

        Integer tam = 1;
        LinkedList<Integer> dimensiones = new LinkedList<>();
        for (Object j : dims.getValores()) {
            Integer h = (Integer) j;
            if (h < 1) {
                return VentanaErrores.getVenErrores().AgregarError("Semántico", "Array: Las dimensiones deben tener un tamanio mayor a 0", this.getFila(), this.getColumna());
            }
            dimensiones.add(h);
            tam *= h;
        }

        LinkedList<Object> valores = new LinkedList<>();

        for (int i = 0; i < tam;) {
            for (int y = 0; i < tam && y < datos.getTamanio(); y++) {
                if (datos.isList()) {
                    LinkedList<Object> l = new LinkedList<>();
                    Coleccion v = ((Coleccion) datos.Acceder(y)).copiar();
                    l.add(((Coleccion) datos.Acceder(y)).copiar());
                    valores.add(v.isList() ? v : new ListArit(l));
                } else {
                    valores.add(new VectorArit(datos.getTipo_dato(), datos.Acceder(y)));
                }
                i++;
            }
        }

        return new ArrayArit(datos.getTipo_dato(), valores, dimensiones);
    }

}
