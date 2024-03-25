package ru.nsu.fit.geodrilling;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.DefaultXYZDataset;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;

import javax.swing.*;


@SpringBootApplication
public class GeodrillingApplication {


		public static void main(String[] args) {
            SpringApplication.run(GeodrillingApplication.class, args);

}}
