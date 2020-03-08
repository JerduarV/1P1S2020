/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.ListArit;
import Interprete.Expresiones.Colecciones.MatrixArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase que maneja los accesos a las estructuras para recuperar un valor
 *
 * @author Jerduar
 */
public class AccesoGet extends Expresion {

    /**
     * Identificador de la variable a accesar
     */
    private final Expresion id;

    /**
     * Lista de indices para acceder a la estructura
     */
    private final LinkedList<Indice> lista_index;

    /**
     * Constructor de un acceso para obtener un valor de la estructura
     *
     * @param id Identificador de la variable
     * @param l Lista de índices
     */
    public AccesoGet(Expresion id, LinkedList<Indice> l) {
        super(id.getFila(), id.getColumna());
        this.id = id;
        this.lista_index = l;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        Object o = id.Resolver(t);

        //SI NO SE ECONTRÓ LA VARIABLE SE RETORNA EL ERROR
        if (o instanceof ErrorCompi) {
            return o;
        }

        for (Indice i : this.lista_index) {
            if (o instanceof MatrixArit) {
                o = this.AccesoMatriz((MatrixArit) o, t, i);
            } else if (o instanceof VectorArit) {
                o = AccesoGetVector((VectorArit) o, t, i);
            } else if (o instanceof ListArit) {
                o = this.AccesoLista((ListArit) o, t, i);
            } else {
                return o;
            }
        }
        return o;
    }

    /**
     * Retorna el Id
     *
     * @return String id
     */
    public Expresion getId() {
        return id;
    }

    /**
     * Retorna la lista de indices
     *
     * @return LinkedList de indices
     */
    public LinkedList<Indice> getLista_index() {
        return lista_index;
    }

    /**
     * Acceso a un vector primitivo
     *
     * @param vector Vector a acceder
     * @param t Tabla de símbolos
     * @return Vector con resultado
     */
    private Object AccesoGetVector(VectorArit vector, TablaSimbolos t, Indice indice) {

        if (!indice.isSimple()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "En los vectores solo se pueden hacer accesos simples", this.getFila(), this.getColumna());
        }

        Object i = indice.getExp().Resolver(t);

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

        LinkedList<Object> l = new LinkedList<>();
        Integer y = (Integer) index.Acceder(0);

        if (y < 1 || y > vector.getTamanio()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
        }

        l.add(vector.Acceder(y - 1));
        return new VectorArit(vector.getTipo_dato(), l);
    }

    /**
     * Acceso a una lista
     *
     * @param lista Lista a la que se va a acceder
     * @param t Tabla de símbolos
     * @return Objeto colección o ErrorCompi
     */
    private Object AccesoLista(ListArit lista, TablaSimbolos t, Indice indice) {


        if (!(indice.isSimple() || indice.isDoble())) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "El índice no corresponde a una lista", this.getFila(), this.getColumna());
        }

        Object in = indice.getExp().Resolver(t);

        if (in instanceof ErrorCompi) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Hubo un error en los indices", this.getFila(), this.getColumna());
        }

        VectorArit index = (VectorArit) in;

        if (in instanceof VectorArit) {
            index = (VectorArit) index;
        } else {
            throw new UnsupportedOperationException("No he validado que pasa cuando vienen otras cosas diferentes a vectores en el indice -> ACCESO GET");
        }

        if (!index.isInteger()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba un entero en el índice", this.getFila(), this.getColumna());
        }

        Integer y = (Integer) index.Acceder(0);

        if (y < 1 || y > lista.getTamanio()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
        }

        //SI ES UN ACCESO SIMPLE RETORNA LO QUE CONTIENE EL POSICIÓN Y DENTRO DE UNA NUEVA LISTA
        if (indice.isSimple()) {
            LinkedList<Object> l = new LinkedList<>();
            l.add(lista.Acceder(y - 1));
            return new ListArit(l);
        } else {
            //SI ES UN ACCESO DOBLE DEVUELVE LO QUE SEA QUE HAYA EN LA POSICIÓN Y
            return lista.Acceder(y - 1);
        }
    }

    /**
     * Acceso a una matriz Arit
     *
     * @param matriz Matriz a la que se realiza el acceso
     * @param t Tabla de Símbolos
     * @param index Índice que se realizará
     * @return Colección o ErrorCompi
     */
    private Object AccesoMatriz(MatrixArit matriz, TablaSimbolos t, Indice index) {
        if (index.isDoble()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Los corchetes dobles no son premitidos en los accesos a matrices", this.getFila(), this.getColumna());
        }

        switch (index.getTipo()) {
            case MATRIX:
                return AccesoMatrizDoble(matriz, t, index);
            case MATRIX_COL:
                return AccesoMatrizCol(matriz, t, index);
            case MATRIX_ROW:
                return AccesoMatrizRow(matriz, t, index);
            default:
                return AccesoGetVector(matriz, t, index);
        }
    }

    /**
     * Acceso a una matriz usando un índice [E,E]
     *
     * @param matriz Matriz a la que se accederá
     * @param t Tabla de Símbolos
     * @param index Indice
     * @return Objeto Colección o ErrorCompi
     */
    private Object AccesoMatrizDoble(MatrixArit matriz, TablaSimbolos t, Indice index) {
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

        return new VectorArit(matriz.getTipo_dato(), matriz.AccederMatrix(f - 1, c - 1));
    }

    /**
     * Acceso a una matriz por fila
     *
     * @param matriz Matriz a la que se accederá
     * @param t Tabla de Símbolos
     * @param index Índice que se usará
     * @return Objeto colección o ErrorCompi
     */
    private Object AccesoMatrizRow(MatrixArit matriz, TablaSimbolos t, Indice index) {
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
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indices fuera de ranto", this.getFila(), this.getColumna());
        }

        LinkedList<Object> lista = new LinkedList<>();

        for (int y = 0; y < matriz.getNum_columnas(); y++) {
            lista.add(matriz.AccederMatrix(f - 1, y));
        }

        return new VectorArit(matriz.getTipo_dato(), lista);
    }

    /**
     * Acceso a la matriz por columna
     *
     * @param matriz Matriz a la que se accederá
     * @param t Tabla de Símbolos
     * @param index Índice que se usará
     * @return Objeto colección o ErrorCompi
     */
    private Object AccesoMatrizCol(MatrixArit matriz, TablaSimbolos t, Indice index) {
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
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indices fuera de ranto", this.getFila(), this.getColumna());
        }

        LinkedList<Object> lista = new LinkedList<>();

        for (int y = 0; y < matriz.getNum_filas(); y++) {
            lista.add(matriz.AccederMatrix(y, c - 1));
        }

        return new VectorArit(matriz.getTipo_dato(), lista);
    }

    @Override
    public void dibujar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
