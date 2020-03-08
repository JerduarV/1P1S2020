/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Instrucciones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.AccesoGet;
import Interprete.Expresiones.Colecciones.Coleccion;
import Interprete.Expresiones.Colecciones.ListArit;
import Interprete.Expresiones.Colecciones.MatrixArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.Identificador;
import Interprete.Expresiones.Indice;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase para setear el valor de un casilla en una estructura
 *
 * @author Jerduar
 */
public class AccesoAsig extends Instruccion {

    /**
     * Lista de índices
     */
    private final LinkedList<Indice> lista_index;

    /**
     * Identificador al que se quiere accesar
     */
    private final Identificador id;

    /**
     * Nuevo valor a asignar
     */
    private final Expresion valor;

    /**
     * Constructor del acceso asignación
     *
     * @param a Acceso a asignación
     * @param e Expresión con nuevo valor
     */
    public AccesoAsig(AccesoGet a, Expresion e) {
        super(a.getFila(), a.getColumna());
        this.id = (Identificador) a.getId();
        this.lista_index = a.getLista_index();
        this.valor = e;
    }

    @Override
    public Object Ejecutar(TablaSimbolos t) {

        Object o = id.Resolver(t);

        //SI NO SE ECONTRÓ LA VARIABLE SE RETORNA EL ERROR
        if (o instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se realizó la asignación", this.getFila(), this.getColumna());
        }

        if (o instanceof MatrixArit) {

        } else if (o instanceof VectorArit) {
            return AccesoSetVector((VectorArit) o, t);
        } else if (o instanceof ListArit) {
            return this.AccesoSetList((ListArit) o, t);
        }

        throw new UnsupportedOperationException("Todavía no tengo asignación para esa estructura"); //To change body of generated methods, choose Tools | Templates.
    }

    private Object SetMatriz(MatrixArit matriz, TablaSimbolos t) {
        if(this.lista_index.size() != 1 || this.lista_index.getFirst().isDoble()){
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en los índices", this.getFila(), this.getColumna());
        }
        
        switch(this.lista_index.getFirst().getTipo()){
            case SIMPLE:
                return AccesoSetVector(matriz, t);
            case MATRIX:
                
        }
        return null;
    }

    /**
     * Acceso para setear un acceso a un vector
     *
     * @param vector Vector
     * @param t Tabla de simbolos
     * @return null si todo salió correctamente o ErrorCompi de lo contrario
     */
    private Object AccesoSetVector(VectorArit vector, TablaSimbolos t) {

        //RESVISO QUE LA LISTA DE ÍNDICES NO SEA MAYOR A 1
        if (this.lista_index.size() > 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede acceder más de una vez al vector", this.getFila(), this.getColumna());
        }

        //REVISO QUE EL INDICE SEA DE ACCESO SIMPLE []
        if (!this.lista_index.getFirst().isSimple()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "En los vectores solo se pueden hacer accesos simples", this.getFila(), this.getColumna());
        }

        Object i = this.lista_index.getFirst().getExp().Resolver(t);
        //REVISO QUE NO HAYA ERRORES AL RESOLVER EL ÍNDICE
        if (i instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hubo un error en los indices", this.getFila(), this.getColumna());
        }

        VectorArit index;

        if (i instanceof VectorArit) {
            index = (VectorArit) i;
        } else {
            throw new UnsupportedOperationException("No he validado que pasa cuando vienen otras cosas diferentes a vectores en el indice -> ACCESO GET");
        }

        if (!index.isInteger()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba un entero en el índice", this.getFila(), this.getColumna());
        }

        Integer y = (Integer) index.Acceder(0);

        if (y < 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
        }

        if (vector instanceof MatrixArit) {
            if (((MatrixArit) vector).getTamanio() < y) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
            }
        }

        Object v = this.valor.Resolver(t);

        //REVISO SI HAY ERRORES EN LA EXPRESIÓN QUE SE QUIERE INGRESAR
        if (v instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en la expresión a asignar", this.getFila(), this.getColumna());
        }

        if (!(v instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede asignar otro vector de uno", this.getFila(), this.getColumna());
        }

        VectorArit nuevo_valor = (VectorArit) v;

        if (nuevo_valor.getTamanio() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede asignar un vector de tamanio 1", this.getFila(), this.getColumna());
        }
        vector.SetPosicion(y - 1, nuevo_valor);

        return null;
    }

    /**
     * Seteo de Lista
     *
     * @param lista Lista a modificar
     * @param t Tabla de Simbolos
     * @return null o ErrorCompi
     */
    private Object AccesoSetList(ListArit lista, TablaSimbolos t) {

        Coleccion col = lista;

        for (int i = 0; i < this.lista_index.size() - 1; i++) {
            if (!this.lista_index.get(i).isSimple() && !(col instanceof ListArit)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede acceder por corchete simple", this.getFila(), this.getColumna());
            }

            Object a = lista_index.get(i).getExp().Resolver(t);

            //REVISO QUE NO HAYA ERRORES AL RESOLVER EL ÍNDICE
            if (a instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hubo un error en los indices", this.getFila(), this.getColumna());
            }

            VectorArit index;

            if (a instanceof VectorArit) {
                index = (VectorArit) a;
            } else {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los indices deben ser vectores enteros", this.getFila(), this.getColumna());
            }

            if (!index.isInteger()) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba un entero en el índice", this.getFila(), this.getColumna());
            }

            Integer y = (Integer) index.Acceder(0);

            if (y < 1) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
            }

            Object estruct = col.Acceder(y - 1);

            if (!(estruct instanceof Coleccion)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba una estructura", this.getFila(), this.getColumna());
            }
            col = (Coleccion) estruct;
        }

        if (!this.lista_index.getLast().isSimple() && !(col instanceof ListArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede acceder por corchete simple", this.getFila(), this.getColumna());
        }

        Object a = lista_index.getLast().getExp().Resolver(t);

        //REVISO QUE NO HAYA ERRORES AL RESOLVER EL ÍNDICE
        if (a instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hubo un error en los indices", this.getFila(), this.getColumna());
        }

        VectorArit index;

        if (a instanceof VectorArit) {
            index = (VectorArit) a;
        } else {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los indices deben ser vectores enteros", this.getFila(), this.getColumna());
        }

        if (!index.isInteger()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba un entero en el índice", this.getFila(), this.getColumna());
        }

        Integer y = (Integer) index.Acceder(0);

        if (y < 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
        }

        Object v = this.valor.Resolver(t);

        //REVISO SI HAY ERRORES EN LA EXPRESIÓN QUE SE QUIERE INGRESAR
        if (v instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en la expresión a asignar", this.getFila(), this.getColumna());
        }

        if (col instanceof VectorArit) {
            if (!(v instanceof VectorArit)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede asignar otro vector de uno", this.getFila(), this.getColumna());
            }

            VectorArit nuevo_valor = (VectorArit) v;

            if (nuevo_valor.getTamanio() != 1) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede asignar un vector de tamanio 1", this.getFila(), this.getColumna());
            }

            col.SetPosicion(y - 1, nuevo_valor);
        } else {
            Coleccion valor_asig = (Coleccion) v;
            if (this.lista_index.getLast().isSimple()) {
                if (valor_asig.getTamanio() > 1) {
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "Solo se puede asignar de tamanio 1", this.getFila(), this.getColumna());
                }
                valor_asig = (valor_asig instanceof VectorArit) ? valor_asig : (Coleccion) valor_asig.Acceder(0);
            }
            col.SetPosicion(y - 1, valor_asig);
        }

        return null;

    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
