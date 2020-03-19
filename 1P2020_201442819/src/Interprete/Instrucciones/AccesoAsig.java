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
        if (o instanceof ArrayArit) {
            return this.AccesoSetArray((ArrayArit) o, t);
        } else if (o instanceof MatrixArit) {
            return this.SetMatriz((MatrixArit) o, t);
        } else if (o instanceof VectorArit) {
            return AccesoSetVector((VectorArit) o, t, this.lista_index);
        } else if (o instanceof ListArit) {
            return this.AccesoSetList((ListArit) o, t, this.lista_index);
        }

        throw new UnsupportedOperationException("Todavía no tengo asignación para esa estructura"); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * función de seteo a una matrizArit
     *
     * @param matriz Matriz a la que se va a modificar
     * @param t Tabla de Símbolos
     * @return null o ErrorCompi
     */
    private Object SetMatriz(MatrixArit matriz, TablaSimbolos t) {
        if (this.lista_index.size() != 1 || this.lista_index.getFirst().isDoble()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en los índices", this.getFila(), this.getColumna());
        }

        switch (this.lista_index.getFirst().getTipo()) {
            case SIMPLE:
                return AccesoSetVector(matriz, t, this.lista_index);
            case MATRIX:
                return this.SetMatrizDoble(matriz, t, this.lista_index.getFirst());
            case MATRIX_ROW:
                return this.SetMatrizRow(matriz, t, this.lista_index.getFirst());
            case MATRIX_COL:
                return this.SetMatrizCol(matriz, t, this.lista_index.getFirst());
        }
        return null;
    }

    /**
     * Modificación de una matriz usando un índice [E,E]
     *
     * @param matriz Matriz que se va a modificar
     * @param t Tabla de Símbolos
     * @param index Índice de la forma [E,E]
     * @return
     */
    private Object SetMatrizDoble(MatrixArit matriz, TablaSimbolos t, Indice index) {
        Object i1 = index.getExp().Resolver(t),
                i2 = index.getExp2().Resolver(t);

        if ((i1 instanceof ErrorCompi) || (i2 instanceof ErrorCompi)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en los índices", this.getFila(), this.getColumna());
        }

        if (!(i1 instanceof VectorArit) || !(i2 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los índices deben ser númericos", this.getFila(), this.getColumna());
        }

        VectorArit fila = (VectorArit) i1, col = (VectorArit) i2;

        if (!fila.isNumerico() || !col.isNumerico()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los índices deben ser números", this.getFila(), this.getColumna());
        }

        Integer f = fila.isInteger() ? (Integer) fila.Acceder(0) : ((Double) fila.Acceder(0)).intValue(),
                c = col.isInteger() ? (Integer) col.Acceder(0) : ((Double) col.Acceder(0)).intValue();

        if (f < 1 || f > matriz.getNum_filas() || c < 1 || c > matriz.getNum_columnas()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indices fuera de rango", this.getFila(), this.getColumna());
        }

        Object nuevo_valor = this.valor.Resolver(t);

        if (nuevo_valor instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en la expresón de valor nuevo", this.getFila(), this.getColumna());
        }

        if (!(nuevo_valor instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La expresión debe ser un vector primitivo", this.getFila(), this.getColumna());
        }

        VectorArit v = (VectorArit) nuevo_valor;

        if (v.getTamanio() != 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La expresión debe ser un vector primitivo de tamanio 1", this.getFila(), this.getColumna());
        }

        matriz.SetPosicion(f - 1, c - 1, v);
        return null;
    }

    /**
     * Modificación de matriz de la forma [E,]
     *
     * @param matriz Matriz a modificar
     * @param t Tabla de Símbolos
     * @param index Índice
     * @return null o ErrorCompi
     */
    private Object SetMatrizRow(MatrixArit matriz, TablaSimbolos t, Indice index) {
        Object i1 = index.getExp().Resolver(t);

        if (i1 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en los índices", this.getFila(), this.getColumna());
        }

        if (!(i1 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los índices deben ser númericos", this.getFila(), this.getColumna());
        }

        VectorArit fila = (VectorArit) i1;

        if (!fila.isNumerico()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los índices deben ser números", this.getFila(), this.getColumna());
        }

        Integer f = fila.isInteger() ? (Integer) fila.Acceder(0) : ((Double) fila.Acceder(0)).intValue();

        if (f < 1 || f > matriz.getNum_filas()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indices fuera de rango", this.getFila(), this.getColumna());
        }

        Object nuevo_valor = this.valor.Resolver(t);

        if (nuevo_valor instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en la expresón de valor nuevo", this.getFila(), this.getColumna());
        }

        if (!(nuevo_valor instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La expresión debe ser un vector primitivo", this.getFila(), this.getColumna());
        }

        VectorArit v = (VectorArit) nuevo_valor;

        if (v.getTamanio() != 1 && v.getTamanio() != matriz.getNum_columnas()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La expresión debe ser un vector primitivo de tamanio 1", this.getFila(), this.getColumna());
        }

        matriz.SetRow(f - 1, v);
        return null;
    }

    /**
     * Modificación de una matriz de la forma [,E]
     *
     * @param matriz Matriz a modificar
     * @param t Tabla de Simbolos
     * @param index Índice
     * @return null o ErrorCompi
     */
    private Object SetMatrizCol(MatrixArit matriz, TablaSimbolos t, Indice index) {
        Object i1 = index.getExp().Resolver(t);

        if (i1 instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en los índices", this.getFila(), this.getColumna());
        }

        if (!(i1 instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los índices deben ser númericos", this.getFila(), this.getColumna());
        }

        VectorArit columna = (VectorArit) i1;

        if (!columna.isNumerico()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los índices deben ser números", this.getFila(), this.getColumna());
        }

        Integer c = columna.isInteger() ? (Integer) columna.Acceder(0) : ((Double) columna.Acceder(0)).intValue();

        if (c < 1 || c > matriz.getNum_filas()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indices fuera de rango", this.getFila(), this.getColumna());
        }

        Object nuevo_valor = this.valor.Resolver(t);

        if (nuevo_valor instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Error en la expresón de valor nuevo", this.getFila(), this.getColumna());
        }

        if (!(nuevo_valor instanceof VectorArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La expresión debe ser un vector primitivo", this.getFila(), this.getColumna());
        }

        VectorArit v = (VectorArit) nuevo_valor;

        if (v.getTamanio() != 1 && v.getTamanio() != matriz.getNum_filas()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La expresión debe ser un vector primitivo de tamanio 1", this.getFila(), this.getColumna());
        }

        matriz.SetCol(c - 1, v);
        return null;
    }

    /**
     * Acceso para setear un acceso a un vector
     *
     * @param vector Vector
     * @param t Tabla de simbolos
     * @return null si todo salió correctamente o ErrorCompi de lo contrario
     */
    private Object AccesoSetVector(VectorArit vector, TablaSimbolos t, LinkedList<Indice> lista_indices) {

        //RESVISO QUE LA LISTA DE ÍNDICES NO SEA MAYOR A 1
        if (lista_indices.size() > 1) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "No se puede acceder más de una vez al vector", this.getFila(), this.getColumna());
        }

        //REVISO QUE EL INDICE SEA DE ACCESO SIMPLE []
        if (!lista_indices.getFirst().isSimple()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "En los vectores solo se pueden hacer accesos simples", this.getFila(), this.getColumna());
        }

        Object i = lista_indices.getFirst().getExp().Resolver(t);
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

        Coleccion nuevo_valor = (Coleccion) v;

        if (nuevo_valor.isList()) {
            LinkedList<Object> l = new LinkedList<>();
            for (Object o : vector.getValores()) {
                VectorArit vec = new VectorArit(vector.getTipo_dato(), o);
                l.add(vec);
            }
            ListArit lista = new ListArit(l);
            t.GuardarVariable(this.id.getId(), lista);
            lista.SetPosicion(y - 1, nuevo_valor);
            return null;
        }

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
    private Object AccesoSetList(ListArit lista, TablaSimbolos t, LinkedList<Indice> lista_indices) {

        Coleccion col = lista;

        for (int i = 0; i < lista_indices.size() - 1; i++) {
            if (!lista_indices.get(i).isSimple() && !(col instanceof ListArit)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede acceder por corchete simple", this.getFila(), this.getColumna());
            }

            Object a = lista_indices.get(i).getExp().Resolver(t);

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

        if (!lista_indices.getLast().isSimple() && !(col instanceof ListArit)) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede acceder por corchete simple", this.getFila(), this.getColumna());
        }

        Object a = lista_indices.getLast().getExp().Resolver(t);

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
            if (lista_indices.getLast().isSimple()) {
                if (valor_asig.getTamanio() > 1) {
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "Solo se puede asignar de tamanio 1", this.getFila(), this.getColumna());
                }
                valor_asig = (valor_asig instanceof VectorArit) ? valor_asig : (Coleccion) valor_asig.Acceder(0);
            }
            col.SetPosicion(y - 1, valor_asig.copiar());
        }

        return null;

    }

    private Object AccesoSetArray(ArrayArit array, TablaSimbolos t) {
        if (this.lista_index.size() < array.getCantidadDims()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "AccesoArray: Índices insuficientes", this.getFila(), this.getColumna());
        }

        LinkedList<Integer> lista_index = new LinkedList<>();
        int i = 0;
        for (i = 0; i < this.lista_index.size() && i < array.getCantidadDims(); i++) {
            Object index = this.lista_index.get(i).getExp().Resolver(t);
            if (index instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "AccesoArray: Error en el índice", this.getFila(), this.getColumna());
            }

            Coleccion in = (Coleccion) index;
            if (!in.isVector() || !in.isInteger()) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "AccesoArray: El índice debe ser vector entero", this.getFila(), this.getColumna());
            }

            Integer y = (int) in.Acceder(0);
            if (y < 1 || y > array.getLista_dim().get(i)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "AccesoArray: indice fuera de rango", this.getFila(), this.getColumna());
            }

            lista_index.add(y - 1);
        }

        if (i == this.lista_index.size()) {
            Object val = this.valor.Resolver(t);
            if (val instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "AccesoArray: Error en el valor a asignar", this.getFila(), this.getColumna());
            }
            Coleccion nuevo_valor = ((Coleccion) val).copiar();

            if (nuevo_valor.getTamanio() != 1) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "AccesoArray: Solo se pueden asignar elementos de tamanio 1", this.getFila(), this.getColumna());
            }
            array.SetPosicion(lista_index, nuevo_valor);
            return null;
        }
        LinkedList<Indice> l_in = new LinkedList<>();

        for (int y = i; y < this.lista_index.size(); y++) {
            l_in.add(this.lista_index.get(i));
        }

        Coleccion v = (Coleccion) array.Acceso(lista_index);

        if (v.isVector()) {
            this.AccesoSetVector((VectorArit) v, t, l_in);
            v = (Coleccion) array.Acceso(lista_index);
            array.SetPosicion(lista_index, v);
        } else {
            this.AccesoSetList((ListArit) v, t, l_in);
        }

        return null;
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
