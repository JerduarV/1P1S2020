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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Clase para el manejo de la llamada a la función nativa barplot que genera la
 * gráfica de barras
 *
 * @author Jerduar
 */
public class Barplot extends CallFun {

    private static int i = 0;

    public Barplot(LinkedList<Expresion> p, Integer fila, Integer col) {
        super("Barplot", p, fila, col);
    }

    @Override
    public Object Resolver(TablaSimbolos t) {
        try {
            if (this.getParam_act().size() != 5) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Barras: Se esperaban 3 parámetros", this.getFila(), this.getColumna());
            }
            Object param1 = this.getParam_act().getFirst().Resolver(t),
                    param2 = this.getParam_act().get(1).Resolver(t),
                    param3 = this.getParam_act().get(2).Resolver(t),
                    param4 = this.getParam_act().get(3).Resolver(t),
                    param5 = this.getParam_act().get(4).Resolver(t);
            if (param1 instanceof ErrorCompi || param2 instanceof ErrorCompi || param3 instanceof ErrorCompi || param4 instanceof ErrorCompi || param5 instanceof ErrorCompi) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Barras: Error en los parámetros", this.getFila(), this.getColumna());
            }
            if (!(param1 instanceof VectorArit && param2 instanceof VectorArit && param3 instanceof VectorArit && param4 instanceof VectorArit || param5 instanceof VectorArit)) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Barras: Se esperaban 3 vectores primitivos", this.getFila(), this.getColumna());
            }
            VectorArit datos = (VectorArit) param1,
                    lbx = (VectorArit) param2,
                    lby = (VectorArit) param3,
                    titulo = (VectorArit) param4,
                    lbs = (VectorArit) param5;
            if (!datos.isNumerico() || !(lbx.isString() && titulo.isString() && lby.isString() && lbs.isString())) {
                return VentanaErrores.getVenErrores().AgregarError("Semantico", "Barras: Se esperaban un vector numerico y cuatro strings", this.getFila(), this.getColumna());
            }
            LinkedList<Object> label = new LinkedList<>();
            label.addAll(lbs.getValores());
            if (lbs.getTamanio() < datos.getTamanio()) {
                VentanaErrores.getVenErrores().AgregarError("Warning", "Barras: Hacen falta etiquetas para los datos", this.getFila(), this.getColumna());
                for (int y = 0; y < datos.getTamanio() - lbs.getTamanio(); y++) {
                    label.add("Desconocido" + y);
                }
            }
            String nombre = titulo.Acceder(0).toString();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int y = 0; y < label.size() && y < datos.getValores().size(); y++) {
                dataset.addValue(datos.isInteger() ? ((Integer) datos.Acceder(y)).intValue() : (Double) datos.Acceder(y), label.get(y).toString(), "");
            }
            JFreeChart barChart = ChartFactory.createBarChart(nombre, lbx.Acceder(0).toString(), lby.Acceder(0).toString(), dataset, PlotOrientation.VERTICAL, true, true, true);
            int width = 640, height = 480;
            File BarChart = new File("Graficas\\" + nombre + i + ".jpeg");
            ChartUtilities.saveChartAsJPEG(BarChart, barChart, width, height);
            VentanaEditor.InsertarGrafica("Graficas\\" + nombre + i + ".jpeg", nombre);
            i++;
        } catch (IOException ex) {
            Logger.getLogger(Barplot.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new VectorArit(TipoPrimitivo.STRING, "null");

    }

}
