package es.davante.edadcanina.controlador;

import es.davante.edadcanina.modelo.EdadCaninaModelo;

public class EdadCaninaControlador {

    private final EdadCaninaModelo modelo;

    public EdadCaninaControlador(EdadCaninaModelo modelo) {
        this.modelo = modelo;
    }

    public void calcular() {
        modelo.calcular();
    }

    public void limpiar() {
        modelo.limpiar();
    }
}