/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.AccesoGet;
import Interprete.Expresiones.Colecciones.ArrayArit;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Colecciones.ListArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.Identificador;
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import Utileria.DumbExpresion;
import Utileria.Retorno;
import Utileria.ValorArit;
import java.util.LinkedList;

/**
 * Clase para manejar la instrucción for que funciona como un foreach
 *
 * @author Jerduar
 */
public class For extends Instruccion {

    /**
     * Identificador de la variable en la que se actualizará el for
     */
    private final String id;

    /**
     * Expresión que contendra el valor que se le asignará a la variable
     */
    private final Expresion e;

    /**
     * constructor de la instrucción for
     *
     * @param id Identificador de la variable para iterar
     * @param e Expresión que contiene la estructura a iterar
     * @param cuerpo Cuerpo del for
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public For(String id, Expresion e, LinkedList<NodoAST> cuerpo, Integer fila, Integer col) {
        super(fila, col, cuerpo);
        this.id = id;
        this.e = e;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {
        Object valor = this.e.Resolver(t);

        if (valor instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "For: hubo un error en la expresión", this.getFila(), this.getColumna());
        }

        Coleccion col = (Coleccion) valor;

        for (int i = 0; i < col.getTamanio(); i++) {
            Object v = col.getValores().get(i);
            TablaSimbolos nueva = new TablaSimbolos(t);
            nueva.IncrementarDisplay();

            if (col instanceof VectorArit) {
                v = new VectorArit(col.getTipo_dato(), (ValorArit)v);
            }else if(col instanceof ListArit){
                v = ((ValorArit)v).getVal();
            }

            //CAMBIO EL VALOR DE LA VARIABLE EN LA TABLA DE SÍMBOLOS
            nueva.GuardarVariable(id, v);

            Object result = this.Recorrer(nueva);
            
            v = nueva.BusarVariable(id);
            
            if(col instanceof VectorArit){
                if(v instanceof ListArit){
                    col = ((VectorArit)col).vectorToList();
                    if(this.e instanceof Identificador){
                        t.GuardarVariable(((Identificador)this.e).getId(), col);
                    }else if(this.e instanceof AccesoGet){
                        AccesoGet get = (AccesoGet)this.e;
                        AccesoAsig asig = new AccesoAsig(get, new DumbExpresion((Coleccion)v, this.getFila(), this.getColumna()));
                        Object res = asig.Ejecutar(t);
                        if(res instanceof ErrorCompi){
                            return res;
                        }
                    }
                }
                col.SetPosicion(i, (Coleccion)v);
            }
            
            //ESTO HACE POSIBLE EL CASTEO DE LAS ESTRUCTURAS Y SU ACTUALIZACIÓN
            if(col instanceof ArrayArit){
                if(v instanceof VectorArit){
                    if(((VectorArit) v).meTransformeALista()){
                        v = ((VectorArit)v).vector_lista;
                    }
                }
                col.SetPosicion(i, (Coleccion)v);
            }
            
            if(col instanceof ListArit){
                col.SetPosicion(i, (Coleccion)v);
            }

            if (result instanceof Break) {
                break;
            }

            if (result instanceof Continue) {
                continue;
            }

            if (result instanceof Retorno) {
                return result;
            }
        }

        return null;
    }

    @Override
    public void dibujar(String padre) {
        String n = NodoAST.getIdNodo("FOR"), m = NodoAST.getIdNodo(this.id);
        Interprete.Interprete.Conectar(padre, n);
        Interprete.Interprete.Conectar(n, m);
        this.DibujarCuerpo(n);
    }

}
