/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas;

import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.Colecciones.ListArit;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.util.LinkedList;

/**
 * Clase de una llamada a la función C que concatena varios elementos dentro del
 * lenguaje
 *
 * @author Jerduar
 */
public class FuncionC extends Expresion {

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
        super(fila, col);
        this.Lista = l;
    }

    @Override
    public Object Resolver(TablaSimbolos t) {

        if (this.Lista.isEmpty()) {
            return VentanaErrores.getVenErrores().AgregarError("Semantico", "La función C espera al menos un parámetro", this.getFila(), this.getColumna());
        }

        LinkedList<Object> lo = new LinkedList<>();

        for (Expresion e : this.Lista) {
            Object o = e.Resolver(t);
            if (o instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "C: Hubo un erro resolviendo las expresiones", this.getFila(), this.getColumna());
            }
            lo.add(o);
        }

        TipoPrimitivo tipo_result = getTipoPredominante(lo);

        if (tipo_result != TipoPrimitivo.LIST) {
            LinkedList<Object> lista = new LinkedList<>();
            for(Object a : lo){
                LlenarNuevoVector(lista, (VectorArit)a, tipo_result);
            }
            return new VectorArit(tipo_result, lista);
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dibujar() {
        //node46["CallC" exp]
        //for(hijos){hijo.dibujar(nodo46)}
        //nodo45->nodo46
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Función que retorna el tipo de dato que predomina en la estructura
     *
     * @param l Lista de objetos
     * @return Tipo de dato que tendrá el resultado
     */
    private TipoPrimitivo getTipoPredominante(LinkedList<Object> l) {

        for (Object o : l) {
            if (o instanceof ListArit) {
                return TipoPrimitivo.LIST;
            }
        }

        if (l.size() == 1) {
            return ((VectorArit) l.get(0)).getTipo_dato();
        } else {
            TipoPrimitivo t = ((VectorArit) l.get(0)).getTipo_dato();
            for (int i = 1; i < l.size(); i++) {
                t = (((VectorArit) l.get(i)).getTipo_dato().ordinal() < t.ordinal()) ? ((VectorArit) l.get(i)).getTipo_dato() : t;
            }
            return t;
        }
    }

    private VectorArit CasteoVectores(VectorArit v, TipoPrimitivo t) {
        switch (t) {
            case STRING:
                return VectorToString(v);
            case DOUBLE:
                return VectorToDouble(v);
            case INTEGER:
                return VectorToInt(v);
            default:
                return v;
        }
    }

    private VectorArit VectorToString(VectorArit v) {
        LinkedList<Object> l = new LinkedList<>();
        for (Object o : v.getValores()) {
            l.add(o.toString());
        }
        return new VectorArit(TipoPrimitivo.STRING, l);
    }

    private VectorArit VectorToDouble(VectorArit v) {
        LinkedList<Object> l = new LinkedList<>();
        for (Object o : v.getValores()) {
            if (o instanceof Double) {
                l.add(o);
            } else {
                l.add((o instanceof Integer) ? ((Integer) o).doubleValue() : (((Boolean) o) ? 1.0 : 0.0));
            }
        }
        return new VectorArit(TipoPrimitivo.DOUBLE, l);
    }

    private VectorArit VectorToInt(VectorArit v) {
        LinkedList<Object> l = new LinkedList<>();
        for (Object o : v.getValores()) {
            if(o instanceof Integer){
                l.add(o);
            }else{
                l.add((Boolean)o ? 1 : 0);
            }
        }
        return new VectorArit(TipoPrimitivo.INTEGER, l);
    }
    
    private void LlenarNuevoVector(LinkedList<Object> l, VectorArit v, TipoPrimitivo t){
        v = CasteoVectores(v, t);
        for(Object o : v.getValores()){
            l.add(o);
        }
    }

}
