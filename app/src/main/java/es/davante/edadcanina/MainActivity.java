package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.controlador.EdadCaninaControlador;
import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;

public class MainActivity extends AppCompatActivity {

    private EdadCaninaModelo modelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);

        modelo = new EdadCaninaModelo();
        EdadCaninaControlador controlador = new EdadCaninaControlador(modelo);

        binding.setModelo(modelo);
        binding.setControlador(controlador);
    }
}