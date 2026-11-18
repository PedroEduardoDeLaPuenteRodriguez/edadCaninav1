package es.davante.edadcanina.vista;

import android.content.Context;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import es.davante.edadcanina.R;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

public final class AdaptadoresVista {

    private AdaptadoresVista() {
    }

    @BindingAdapter("errorValidacion")
    public static void setErrorValidacion(EditText campo, Validacion validacion) {
        if (validacion == null || validacion == Validacion.OK) {
            campo.setError(null);
            return;
        }

        Context contexto = campo.getContext();
        String mensaje;
        switch (validacion) {
            case VACIO:
                mensaje = contexto.getString(R.string.error_vacio);
                break;
            case NO_NUMERO:
                mensaje = contexto.getString(R.string.error_no_numero);
                break;
            default:
                mensaje = contexto.getString(R.string.error_rango,
                        EdadCaninaModelo.EDAD_MINIMA, EdadCaninaModelo.EDAD_MAXIMA);
                break;
        }
        campo.setError(mensaje);
        campo.requestFocus();
    }
}