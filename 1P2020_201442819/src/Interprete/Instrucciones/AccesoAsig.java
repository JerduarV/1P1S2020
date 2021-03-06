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
import Interprete.NodoAST;
import TablaSimbolos.TablaSimbolos;
import Utileria.ValorArit;
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

    /**
     * Constructor del acceso asignación
     *
     * @param lista_index Lista de índices
     * @param id Idnetificador de la variable a acceder
     * @param valor Nuevo valor a asignar
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public AccesoAsig(LinkedList<Indice> lista_index, Identificador id, Expresion valor, Integer fila, Integer col) {
        super(fila, col);
        this.lista_index = lista_index;
        this.id = id;
        this.valor = valor;
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
            return AccesoSetVector((VectorArit) o, t, this.lista_index, false);
        } else if (o instanceof ListArit) {
            return this.AccesoSetList((ListArit) o, t, this.lista_index);
        }

        return null;
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
                return AccesoSetVector(matriz, t, this.lista_index, false);
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
    private Object AccesoSetVector(VectorArit vector, TablaSimbolos t, LinkedList<Indice> lista_indices, boolean estoyEnArray) {

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
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "Se esperaba un entero", this.getFila(), this.getColumna());
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

        //COMENTE ESTO PARA QUE NO VALIDARA QUE EL NUEVO VALOR FUERA UN VECTOR
//        if (!(v instanceof VectorArit)) {
//            return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede asignar otro vector de uno", this.getFila(), this.getColumna());
//        }
        Coleccion nuevo_valor = (Coleccion) v;

        //ESTA VALIDACIÓN CONVIERTE EL VECTOR EN LISTA EN CASO DE QUE EL NUEVO VALOR
        //SEA UNA LISTA
        if (nuevo_valor.isList()) {
            
            //SE VALIDA QUE SOLO TENGA UN ELEMENTO
            if (nuevo_valor.getTamanio() > 1) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Solo se pueden asignar de tamanio 1", this.getFila(), this.getColumna());
            }

            ListArit lista = vector.vectorToList();
            lista.SetPosicion(y - 1, ((Coleccion) nuevo_valor.Acceder(0)).copiar());
            
            //ESTO VALIDA QUE EL SETEO NO HAYA SIDO LLAMADA DESDE UN ARRAY
            if (!estoyEnArray) {
                t.GuardarVariable(this.id.getId(), lista);
            } else {
                return lista;
            }
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
        Integer h = -1;
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
            h = y;
            if (y < 1) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Indice fuera de rango", this.getFila(), this.getColumna());
            }

            Object estruct = col.Acceder(y - 1);

            if (lista_indices.get(i).isSimple()) {
                estruct = new ListArit((ValorArit) col.getValores().get(y - 1));
            }

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
            if (!(v instanceof VectorArit || v instanceof ListArit)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "A un vector solo se le puede asignar otro vector de uno", this.getFila(), this.getColumna());
            }

            Coleccion nuevo_valor = (Coleccion) v;

            //ESTA VALIDACIÓN CONVIERTE EL VECTOR EN LISTA EN CASO DE QUE EL NUEVO VALOR
            //SEA UNA LISTA
            if (nuevo_valor.isList()) {

                //SE VALIDA QUE SOLO TENGA UN ELEMENTO
                if (nuevo_valor.getTamanio() > 1) {
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "Solo se pueden asignar de tamanio 1", this.getFila(), this.getColumna());
                }

                ListArit li = ((VectorArit) col).vectorToList();
                lista.SetPosicion(h - 1, li);
                col = li;
                col.SetPosicion(y - 1, ((Coleccion)nuevo_valor.Acceder(0)).copiar());
                return null;
            }

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

    /**
     * Seteo de un array
     *
     * @param array arreglo a setear
     * @param t Tabla de símbolos
     * @return Error compi o null
     */
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
            l_in.add(this.lista_index.get(y));
        }

        Coleccion v = (Coleccion) array.Acceso(lista_index);

        if (v.isVector()) {
            this.AccesoSetVector((VectorArit) v, t, l_in, true);
            v = (Coleccion) array.Acceso(lista_index);
            //System.out.println("t:" + v.getTipo_dato());
            array.SetPosicion(lista_index, ((VectorArit) v).meTransformeALista() ? ((VectorArit) v).vector_lista : v);
        } else if (v.isList()) {
            this.AccesoSetList((ListArit) v, t, l_in);
        }
        return null;
    }

    @Override
    public void dibujar(String padre) {
        String x = NodoAST.getIdNodo("ACC_ASIG");
        this.id.dibujar(x);
        Interprete.Interprete.Conectar(padre, x);
        String z = NodoAST.getIdNodo("LINDICE");
        Interprete.Interprete.Conectar(x, z);
        for (Indice i : this.lista_index) {
            i.Dibujar(z);
        }
        this.valor.dibujar(x);
    }

}
