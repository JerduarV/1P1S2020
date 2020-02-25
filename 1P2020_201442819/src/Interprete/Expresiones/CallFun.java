/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Instrucciones.DecFuncion;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para las llamadas a las funciones
 *
 * @author Jerduar
 */
public class CallFun extends Expresion {

    /**
     * Identificador de la fucion
     */
    private final String id;

    /**
     * Lista de parámetros actuales
     */
    private final LinkedList<Expresion> param_act;

    /**
     * Constructor de la llamada a una función
     *
     * @param id Identificador de la función
     * @param p Lista de parámetros actuales
     * @param fila Fila en la que se encuentra
     * @param col Columan en la que se encuentra
     */
    public CallFun(String id, LinkedList<Expresion> p, Integer fila, Integer col) {
        super(fila, col);
        this.id = id;
        this.param_act = p;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        Object f = t.BuscarFuncion(id);
        
        //REVISO QUE LA FUNCIÓN EXISTA
        if (f == null) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No existe la funcion " + this.id, this.getFila(), this.getColumna());
        }

        DecFuncion funcion = (DecFuncion) f;

        //REVISO QUE LOS PARÁMETROS COINCIDAN
        if (funcion.getLista_param().size() != this.param_act.size()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No coincide el número de parámetros", this.getFila(), this.getColumna());
        }

        //CREO UNA TS PARA LA EJECUCIÓN DE LA FUNCIÓN DANDO COMO PADRE LA TABLA GLOBAL
        TablaSimbolos nueva = new TablaSimbolos(t.getGlobal());

        //CARGO LOS PARÁMETROS A LA NUEVA TABLA
        for (int i = 0; i < this.param_act.size(); i++) {
            funcion.getLista_param().get(i).Ejecutar(nueva);

            //OBTENGO EL VALOR DEL PARÁMETRO QUE CORRESPONDE
            Object r = this.param_act.get(i).Resolver(t);
            
            //VERIFICÓ QUE NO SEA UN ERROR
            if (r instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hubo un error en la evaluación de los parametros", this.getFila(), this.getColumna());
            }
            
            //SETEO SU VALOR CON EL PARÁMETRO ACTUAL
            nueva.GuardarVariable(funcion.getLista_param().get(i).getId(), r);
        }
        
        //EJECUTO LA FUNCION
        return funcion.Ejecutar(nueva);
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
