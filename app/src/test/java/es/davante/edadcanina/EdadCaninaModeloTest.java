package es.davante.edadcanina.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EdadCaninaModeloTest {

    private EdadCaninaModelo modelo;

    @Before
    public void preparar() {
        modelo = new EdadCaninaModelo();
    }

    private Validacion calcularCon(String texto) {
        modelo.setEntrada(texto);
        return modelo.calcular();
    }

    @Test
    public void entradaVacia_daErrorVacio() {
        assertEquals(Validacion.VACIO, calcularCon(""));
        assertFalse(modelo.isHayResultado());
    }

    @Test
    public void soloEspacios_daErrorVacio() {
        assertEquals(Validacion.VACIO, calcularCon("   "));
    }

    @Test
    public void textoNoNumerico_daErrorNoNumero() {
        assertEquals(Validacion.NO_NUMERO, calcularCon("abc"));
        assertEquals(Validacion.NO_NUMERO, calcularCon("3.5"));
    }

    @Test
    public void numeroDemasiadoGrande_daErrorNoNumero() {
        assertEquals(Validacion.NO_NUMERO, calcularCon("99999999999"));
    }

    @Test
    public void cero_daErrorDeRango() {
        assertEquals(Validacion.FUERA_DE_RANGO, calcularCon("0"));
    }

    @Test
    public void treintaYUno_daErrorDeRango() {
        assertEquals(Validacion.FUERA_DE_RANGO, calcularCon("31"));
    }

    @Test
    public void negativo_daErrorDeRango() {
        assertEquals(Validacion.FUERA_DE_RANGO, calcularCon("-4"));
    }

    @Test
    public void seis_da42() {
        assertEquals(Validacion.OK, calcularCon("6"));
        assertTrue(modelo.isHayResultado());
        assertEquals(42, modelo.getEdadHumana());
    }

    @Test
    public void limitesDelRango_sonValidos() {
        assertEquals(Validacion.OK, calcularCon("1"));
        assertEquals(7, modelo.getEdadHumana());
        assertEquals(Validacion.OK, calcularCon("30"));
        assertEquals(210, modelo.getEdadHumana());
    }

    @Test
    public void unErrorBorraElResultadoAnterior() {
        calcularCon("6");
        calcularCon("");
        assertFalse(modelo.isHayResultado());
        assertEquals(Validacion.VACIO, modelo.getError());
    }

    @Test
    public void cambiarLaEntradaHaceCaducarElResultado() {
        calcularCon("6");
        modelo.setEntrada("7");
        assertFalse(modelo.isHayResultado());
        assertEquals(Validacion.OK, modelo.getError());
    }
}