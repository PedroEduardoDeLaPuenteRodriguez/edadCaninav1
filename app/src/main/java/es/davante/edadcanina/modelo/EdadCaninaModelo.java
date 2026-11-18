package es.davante.edadcanina.modelo;

public class EdadCaninaModelo {

    public static final int FACTOR_CANINO = 7;
    public static final int EDAD_MINIMA = 1;
    public static final int EDAD_MAXIMA = 30;

    private String entrada = "";
    private Integer edadHumana = null;
    private Validacion error = Validacion.OK;

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String nueva) {
        if (nueva == null) {
            nueva = "";
        }
        if (nueva.equals(entrada)) {
            return;
        }
        entrada = nueva;
        edadHumana = null;
        error = Validacion.OK;
    }

    public boolean isHayResultado() {
        return edadHumana != null;
    }

    public int getEdadHumana() {
        return edadHumana == null ? 0 : edadHumana;
    }

    public Validacion getError() {
        return error;
    }

    public boolean haCalculado() {
        return edadHumana != null || error != Validacion.OK;
    }

    public Validacion calcular() {
        String texto = entrada.trim();

        if (texto.isEmpty()) {
            return rechazar(Validacion.VACIO);
        }

        int edad;
        try {
            edad = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return rechazar(Validacion.NO_NUMERO);
        }

        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            return rechazar(Validacion.FUERA_DE_RANGO);
        }

        setError(Validacion.OK);
        setEdadHumana(edad * FACTOR_CANINO);
        return Validacion.OK;
    }

    private Validacion rechazar(Validacion motivo) {
        setEdadHumana(null);
        setError(motivo);
        return motivo;
    }

    private void setEdadHumana(Integer nueva) {
        edadHumana = nueva;
    }

    private void setError(Validacion nuevo) {
        error = nuevo;
    }
}