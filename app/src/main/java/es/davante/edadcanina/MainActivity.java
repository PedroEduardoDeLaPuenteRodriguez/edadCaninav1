package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final EdadCaninaModelo modelo = new EdadCaninaModelo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnCalc.setOnClickListener(view -> calcular());
    }

    private void calcular() {
        modelo.setEntrada(binding.ageInput.getText().toString());
        Validacion validacion = modelo.calcular();

        if (validacion == Validacion.OK) {
            binding.ageInput.setError(null);
            binding.resultText.setText(getString(R.string.tu_edad_format, modelo.getEdadHumana()));
        } else {
            binding.ageInput.setError(textoDeError(validacion));
            binding.ageInput.requestFocus();
            binding.resultText.setText("");
        }
    }

    private String textoDeError(Validacion validacion) {
        switch (validacion) {
            case VACIO:
                return getString(R.string.error_vacio);
            case NO_NUMERO:
                return getString(R.string.error_no_numero);
            default:
                return getString(R.string.error_rango,
                        EdadCaninaModelo.EDAD_MINIMA, EdadCaninaModelo.EDAD_MAXIMA);
        }
    }
}