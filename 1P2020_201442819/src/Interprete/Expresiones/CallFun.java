/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Instrucciones.DecFuncion;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import Utileria.Retorno;
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

            if (funcion.getLista_param().get(i).TieneValorDefault() && this.param_act.get(i) instanceof ValorDefault) {
                continue;
            } else if (!funcion.getLista_param().get(i).TieneValorDefault() && this.param_act.get(i) instanceof ValorDefault) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "La función " + this.id + " no tienen un valor por defecto para el parametro " + funcion.getLista_param().get(i).getId(), this.getFila(), this.getColumna());
            }

            //OBTENGO EL VALOR DEL PARÁMETRO QUE CORRESPONDE
            Object r = this.param_act.get(i).Resolver(t);

            //VERIFICÓ QUE NO SEA UN ERROR
            if (r instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hubo un error en la evaluación de los parametros", this.getFila(), this.getColumna());
            }

            if (r instanceof Coleccion) {
                r = ((Coleccion) r).copiar();
            }

            //SETEO SU VALOR CON EL PARÁMETRO ACTUAL
            nueva.GuardarVariable(funcion.getLista_param().get(i).getId(), r);
        }

        //EJECUTO LA FUNCION
        nueva.EntreEnFuncion();

        Object result = funcion.Ejecutar(nueva);
        if (result instanceof Retorno) {
            return ((Retorno) result).getValor();
        }
        return null;
    }

    @Override
    public void dibujar(String padre) {
        String x = NodoAST.getIdNodo("CALL_FUN"), y = NodoAST.getIdNodo(this.id), z = NodoAST.getIdNodo("LPARAM_ACT");
        Interprete.Interprete.Conectar(padre, x);
        Interprete.Interprete.Conectar(x, y);
        Interprete.Interprete.Conectar(x, z);
        
        for(Expresion e : this.getParam_act()){
            e.dibujar(z);
        }
    }

    /**
     * Retorna la lista de parámetro actuales
     *
     * @return LinkedList de Expresión
     */
    public LinkedList<Expresion> getParam_act() {
        return param_act;
    }

}
