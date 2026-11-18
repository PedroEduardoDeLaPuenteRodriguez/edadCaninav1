package es.davante.edadcanina.modelo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import es.davante.edadcanina.BR;

public class EdadCaninaModelo extends BaseObservable {

    public static final int FACTOR_CANINO = 7;
    public static final int EDAD_MINIMA = 1;
    public static final int EDAD_MAXIMA = 30;

    private String entrada = "";
    private Integer edadHumana = null;
    private Validacion error = Validacion.OK;

    @Bindable
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
        notifyPropertyChanged(BR.entrada);

        setEdadHumana(null);
        setError(Validacion.OK);
    }

    @Bindable
    public boolean isHayResultado() {
        return edadHumana != null;
    }

    @Bindable
    public int getEdadHumana() {
        return edadHumana == null ? 0 : edadHumana;
    }

    @Bindable
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

    public void limpiar() {
        setEntrada("");
        setEdadHumana(null);
        setError(Validacion.OK);
    }

    private Validacion rechazar(Validacion motivo) {
        setEdadHumana(null);
        setError(motivo);
        return motivo;
    }

    private void setEdadHumana(Integer nueva) {
        if ((edadHumana == null && nueva == null)
                || (edadHumana != null && edadHumana.equals(nueva))) {
            return;
        }
        edadHumana = nueva;
        notifyPropertyChanged(BR.edadHumana);
        notifyPropertyChanged(BR.hayResultado);
    }

    private void setError(Validacion nuevo) {
        if (error == nuevo) {
            return;
        }
        error = nuevo;
        notifyPropertyChanged(BR.error);
    }
}