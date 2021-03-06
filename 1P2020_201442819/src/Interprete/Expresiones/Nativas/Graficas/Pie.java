/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete.Expresiones.Nativas.Graficas;

import Editor.VentanaEditor;
import Editor.VentanaErrores;
import Interprete.ErrorCompi;
import Interprete.Expresiones.CallFun;
import Interprete.Expresiones.Colecciones.VectorArit;
import Interprete.Expresiones.Expresion;
import Interprete.Expresiones.TipoPrimitivo;
import TablaSimbolos.TablaSimbolos;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Clase para el manejo de la función nativa pie que contruye una gráfica de
 * pastel
 *
 * @author Jerduar
 */
public class Pie extends CallFun {

    /**
     * Contador estático usado para nombrar las gráficas según se vayan
     * generando
     */
    private static int i = 0;

    /**
     * Constructor de una llamade la función nativa Pie que contruye una gráfica
     * circular en el lenguaje arit
     *
     * @param p Lista de parámetros actuales
     * @param fila Fila en la que se encuentra
     * @param col Columna en la que se encuentra
     */
    public Pie(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Pie", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        try {
            if (this.getParam_act().size() != 3) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Pie: Se esperaban 3 parámetros", this.getFila(), this.getColumna());
            }
            Object param1 = this.getParam_act().getFirst().Resolver(t),
                    param2 = this.getParam_act().get(1).Resolver(t),
                    param3 = this.getParam_act().get(2).Resolver(t);
            if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi || param3 instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Pie: Error en los parámetros", this.getFila(), this.getColumna());
            }
            if (!(param1 instanceof VectorArit && param2 instanceof VectorArit && param3 instanceof VectorArit)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Pie: Se esperaban 3 vectores primitivos", this.getFila(), this.getColumna());
            }
            VectorArit datos = (VectorArit) param1,
                    lb = (VectorArit) param2,
                    titulo = (VectorArit) param3;
            if (!datos.isNumerico() || !(lb.isString() && titulo.isString())) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Pie: Se esperaban un vector numerico y dos strings", this.getFila(), this.getColumna());
            }
            LinkedList<Object> label = new LinkedList<>();
            label.addAll(lb.getValores());
            if (lb.getTamanio() < datos.getTamanio()) {
                VentanaErrores.getVenErrores().AgregarError("Warning", "Pie: Hacen falta etiquetas para los datos", this.getFila(), this.getColumna());
                for (int y = 0; y < datos.getTamanio() - lb.getTamanio(); y++) {
                    label.add("Desconocido" + y);
                }
            }
            String nombre = titulo.Acceder(0).toString();
            DefaultPieDataset dataset = new DefaultPieDataset();
            for (int i = 0; i < label.size() && i < datos.getValores().size(); i++) {
                Double dato = datos.isInteger() ? ((Integer) datos.Acceder(i)).doubleValue() : (Double) datos.Acceder(i);
                if(dato < 0){
                    return VentanaErrores.getVenErrores().AgregarError("Semantico", "Pie: Los datos no pueden ser negativos", this.getFila(), this.getColumna());
                }
                dataset.setValue(label.get(i).toString(), dato);
            }
            
            
            JFreeChart pieChar = ChartFactory.createPieChart(nombre, dataset, true, true, false);
            int width = 640, height = 480;
            File pie = new File("Graficas\\Pie_" + nombre + i + ".jpeg");
            ChartUtilities.saveChartAsJPEG(pie, pieChar, width, height);
            VentanaEditor.InsertarGrafica("Graficas\\Pie_" + nombre + i + ".jpeg", nombre);
            i++;

        } catch (IOException ex) {
            Logger.getLogger(Pie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new VectorArit(TipoPrimitivo.STRING, "null");
    }

}
