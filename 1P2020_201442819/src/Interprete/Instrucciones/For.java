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

        Coleccion ColeccionAIterar = (Coleccion) valor;

        for (int i = 0; i < ColeccionAIterar.getTamanio(); i++) {
            
            Object iterador = ColeccionAIterar.getValores().get(i);
            
            TablaSimbolos nueva = new TablaSimbolos(t);
            nueva.IncrementarDisplay();

            if (ColeccionAIterar instanceof VectorArit) {
                iterador = new VectorArit(ColeccionAIterar.getTipo_dato(), (ValorArit)iterador);
            }else if(ColeccionAIterar instanceof ListArit){
                iterador = ((ValorArit)iterador).getVal();
            }

            //CAMBIO EL VALOR DE LA VARIABLE EN LA TABLA DE SÍMBOLOS
            nueva.GuardarVariable(id, iterador);

            Object result = this.Recorrer(nueva);
            
            iterador = nueva.BusarVariable(id);
            
            if(ColeccionAIterar instanceof VectorArit){
                if(iterador instanceof ListArit){
                    ColeccionAIterar = ((VectorArit)ColeccionAIterar).vectorToList();
                    if(this.e instanceof Identificador){
                        t.GuardarVariable(((Identificador)this.e).getId(), ColeccionAIterar);
                    }else if(this.e instanceof AccesoGet){
                        AccesoGet get = (AccesoGet)this.e;
                        AccesoAsig asig = new AccesoAsig(get, new DumbExpresion(ColeccionAIterar, this.getFila(), this.getColumna()));
                        Object res = asig.Ejecutar(t);
                        if(res instanceof ErrorCompi){
                            return res;
                        }
                        ColeccionAIterar = (Coleccion)this.e.Resolver(t);
                    }
                    iterador = ((Coleccion)((Coleccion)iterador).Acceder(0)).copiar();
                }
                ColeccionAIterar.SetPosicion(i, (Coleccion)iterador);
            }
            
            //ESTO HACE POSIBLE EL CASTEO DE LAS ESTRUCTURAS Y SU ACTUALIZACIÓN
            if(ColeccionAIterar instanceof ArrayArit){
                if(iterador instanceof VectorArit){
                    if(((VectorArit) iterador).meTransformeALista()){
                        iterador = ((VectorArit)iterador).vector_lista;
                    }
                }
                ColeccionAIterar.SetPosicion(i, (Coleccion)iterador);
            }
            
            if(ColeccionAIterar instanceof ListArit){
                ColeccionAIterar.SetPosicion(i, (Coleccion)iterador);
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
