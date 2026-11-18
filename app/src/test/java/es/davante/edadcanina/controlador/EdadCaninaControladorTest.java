package es.davante.edadcanina.controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

public class EdadCaninaControladorTest {

    private EdadCaninaModelo modelo;
    private EdadCaninaControlador controlador;

    @Before
    public void preparar() {
        modelo = new EdadCaninaModelo();
        controlador = new EdadCaninaControlador(modelo);
    }

    @Test
    public void calcular_conEntradaValida_dejaElResultadoEnElModelo() {
        modelo.setEntrada("6");
        controlador.calcular();
        assertEquals(42, modelo.getEdadHumana());
    }

    @Test
    public void calcular_conEntradaInvalida_dejaElErrorEnElModelo() {
        modelo.setEntrada("31");
        controlador.calcular();
        assertEquals(Validacion.FUERA_DE_RANGO, modelo.getError());
    }

    @Test
    public void limpiar_borraEntradaResultadoYError() {
        modelo.setEntrada("6");
        controlador.calcular();
        controlador.limpiar();
        assertEquals("", modelo.getEntrada());
        assertFalse(modelo.isHayResultado());
        assertEquals(Validacion.OK, modelo.getError());
    }

    @Test
    public void limpiar_tambienQuitaUnErrorConLaEntradaYaVacia() {
        modelo.setEntrada("");
        controlador.calcular();
        assertEquals(Validacion.VACIO, modelo.getError());
        controlador.limpiar();
        assertEquals(Validacion.OK, modelo.getError());
        assertFalse(modelo.haCalculado());
    }
}