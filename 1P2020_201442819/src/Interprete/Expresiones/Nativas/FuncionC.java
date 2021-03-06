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
import Interprete.Expresiones.Colecciones.MatrixArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import Utileria.ValorArit;
import java.util.LinkedList;

/**
 * Clase de una llamada a la función C que concatena varios elementos dentro del
 * lenguaje
 *
 * @author Jerduar
 */
public class FuncionC extends CallFun {

    /**
     * Lista de elementos a concatenar
     */
    private final LinkedList<Expresion> Lista;

    /**
     * Constructor de la clase FuncionC que devuelve un llamado a la función
     * nativa C
     *
     * @param l
     * @param fila
     * @param col
     */
    public FuncionC(LinkedList<Expresion> l, Integer fila, Integer col) {
        super("C", l, fila, col);
        this.Lista = l;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {

        if (this.Lista.isEmpty()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La función C espera al menos un parámetro", this.getFila(), this.getColumna());
        }

        LinkedList<Coleccion> lo = new LinkedList<>();

        for (Expresion e : this.Lista) {
            Object o = e.Resolver(t);
            if (o instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "C: Hubo un erro resolviendo las expresiones", this.getFila(), this.getColumna());
            }

            if (o instanceof MatrixArit || o instanceof ArrayArit) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "C: Solo acepta list y vectores primitivos", this.getFila(), this.getColumna());
            }
            lo.add((Coleccion) o);
        }

        TipoPrimitivo tipo_result = getTipoPredominante(lo);

        if (tipo_result != TipoPrimitivo.LIST) {
            LinkedList<Object> lista = new LinkedList<>();
            for (Object a : lo) {
                LlenarNuevoVector(lista, (VectorArit) a, tipo_result);
            }
            return new VectorArit(tipo_result, lista);
        } else {
            return crearList(lo);
        }
    }

    /**
     * Retorna una nueva lista, se usa cuando en los parámetros de la función C
     * viene una lista
     *
     * @param lista
     * @return
     */
    private ListArit crearList(LinkedList<Coleccion> lista) {
        LinkedList<Object> valores = new LinkedList<>();
        for (Coleccion o : lista) {
            if (o.isList()) {
                ListArit la = (ListArit) o;
                for (Object ob : la.getValores()) {
                    //System.out.println(ob);
                    valores.add(((ValorArit)ob).getVal());
                }
            } else {
                valores.add(o);
            }
        }
        return new ListArit(valores);
    }

    /**
     * Función que retorna el tipo de dato que predomina en la estructura
     *
     * @param l Lista de objetos
     * @return Tipo de dato que tendrá el resultado
     */
    private TipoPrimitivo getTipoPredominante(LinkedList<Coleccion> l) {
        if (l.size() == 1) {
            return l.get(0).getTipo_dato();
        } else {
            TipoPrimitivo t = l.get(0).getTipo_dato();
            for (int i = 1; i < l.size(); i++) {
                t = (l.get(i).getTipo_dato().ordinal() < t.ordinal()) ? l.get(i).getTipo_dato() : t;
            }
            return t;
        }
    }

    /**
     * Casta el vector de acuerdo al tipo predominante
     *
     * @param v Vector
     * @param t Tipo predominante
     * @return VectorArit
     */
    private VectorArit CasteoVectores(VectorArit v, TipoPrimitivo t) {
        switch (t) {
            case STRING:
                return VectorToString(v);
            case NUMERIC:
                return VectorToDouble(v);
            case INTEGER:
                return VectorToInt(v);
            default:
                return v;
        }
    }

    /**
     * Devuelte un nuevo vector pero con valores de cadena
     *
     * @param v Vector
     * @return VectorArit
     */
    private VectorArit VectorToString(VectorArit v) {
        LinkedList<Object> l = new LinkedList<>();
        for (int i = 0; i < v.getTamanio(); i++) {
            l.add(v.Acceder(i).toString());
        }
        return new VectorArit(TipoPrimitivo.STRING, l);
    }

    /**
     * Devuelve un nuevo vector pero con valore doble
     *
     * @param v Vector
     * @return VectorArit
     */
    private VectorArit VectorToDouble(VectorArit v) {
        LinkedList<Object> l = new LinkedList<>();
        for (int i = 0; i < v.getTamanio(); i++) {
            Object o = v.Acceder(i);
            if (o instanceof Double) {
                l.add(o);
            } else {
                l.add((o instanceof Integer) ? ((Integer) o).doubleValue() : (((Boolean) o) ? 1.0 : 0.0));
            }
        }
        return new VectorArit(TipoPrimitivo.NUMERIC, l);
    }

    /**
     * Devuelve un nuevo vector pero con valores enteros, este casteo solo se
     * usa cuando hay valores booleanos con integer
     *
     * @param v Vector
     * @return
     */
    private VectorArit VectorToInt(VectorArit v) {
        LinkedList<Object> l = new LinkedList<>();
        for (int i = 0; i < v.getTamanio(); i++) {
            Object o = v.Acceder(i);
            if (o instanceof Integer) {
                l.add(o);
            } else {
                l.add((Boolean) o ? 1 : 0);
            }
        }
        return new VectorArit(TipoPrimitivo.INTEGER, l);
    }

    /**
     * Llena la lista que tendra el vector
     *
     * @param l Lista de valores que tendrá
     * @param v Vector
     * @param t Tipo predominante
     */
    private void LlenarNuevoVector(LinkedList<Object> l, VectorArit v, TipoPrimitivo t) {
        v = CasteoVectores(v, t);
        for (Object o : v.getValores()) {
            l.add(((ValorArit)o).getVal());
        }
    }

}
