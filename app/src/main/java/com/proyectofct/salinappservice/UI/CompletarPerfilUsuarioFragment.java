package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectofct.salinappservice.Clases.Clientes.Cliente;
import com.proyectofct.salinappservice.Clases.Clientes.Direcciones;
import com.proyectofct.salinappservice.Clases.Clientes.DireccionesClientes;
import com.proyectofct.salinappservice.Controladores.ClienteController;
import com.proyectofct.salinappservice.Modelos.ClienteDB;
import com.proyectofct.salinappservice.R;

public class CompletarPerfilUsuarioFragment extends Fragment {
    private EditText edtNombre;
    private EditText edtApellidos;
    private EditText edtCalle;
    private EditText edtNúmero;
    private EditText edtProvincia;
    private EditText edtLocalidad;
    private EditText edtCP;

    private Button btCompletarInformación;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_completar_perfil_usuario, container, false);

        edtNombre = (EditText) vista.findViewById(R.id.edtNombreUsuario);
        edtApellidos = (EditText) vista.findViewById(R.id.edtApellidosUsuario);
        edtCalle = (EditText) vista.findViewById(R.id.edtCalleUsuario);
        edtNúmero = (EditText) vista.findViewById(R.id.edtNúmeroUsuario);
        edtProvincia = (EditText) vista.findViewById(R.id.edtProvinciaUsuario);
        edtLocalidad = (EditText) vista.findViewById(R.id.edtLocalidadUsuario);
        edtCP = (EditText) vista.findViewById(R.id.edtCPUsuario);

        btCompletarInformación = (Button) vista.findViewById(R.id.btCompletraInformaciónUsuario);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        DireccionesClientes direccionesObtenidas = ClienteController.obtenerDireccionesCliente();
        if (direccionesObtenidas != null){
            Toast.makeText(getActivity(), "Ya existe un dirección asignada a este usuario", Toast.LENGTH_LONG).show();
            btCompletarInformación.setEnabled(false);
        }

        btCompletarInformación.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DireccionesClientes direccionesClientes = completarInformaciónUsuario();
                Log.i("DireccionesClientes", "DireccionesClientes -> " + direccionesClientes.toString());
                if (direccionesClientes != null){
                    boolean insertadoOk = ClienteController.insertarDireccionesClientes(direccionesClientes);
                    if (insertadoOk){
                        Toast.makeText(getActivity(), "Información registrada correctamente", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(vista).navigate(R.id.nav_fragment_carrito);
                    }else {
                        Log.i("", "Error al insertar las direcciones de clientes");
                    }
                }else {
                    Log.i("", "Las direcciones de clientes valen null");
                }
            }
        });
        return vista;
    }

    public DireccionesClientes completarInformaciónUsuario(){
        if (String.valueOf(edtNombre.getText()).isEmpty() || String.valueOf(edtApellidos.getText()).isEmpty() || String.valueOf(edtCalle.getText()).isEmpty() || String.valueOf(edtNúmero.getText()).isEmpty() || String.valueOf(edtProvincia.getText()).isEmpty() || String.valueOf(edtLocalidad.getText()).isEmpty() || String.valueOf(edtCP.getText()).isEmpty()){
            Toast.makeText(getActivity(), "COMPLETA TODOS LOS CAMPOS", Toast.LENGTH_LONG).show();
            return null;
        }else {
            int idDireccion = ClienteDB.obtenerNuevoIDDireccion();
            String direccionTexto = "C/" + String.valueOf(edtCalle.getText()) + ", Nº" + String.valueOf(edtNúmero.getText()) + ", " + String.valueOf(edtProvincia.getText()) + ", " + String.valueOf(edtLocalidad.getText()) + ", CP: " + String.valueOf(edtCP.getText());
            Direcciones direccion = new Direcciones(idDireccion, direccionTexto);

            int idCliente = ClienteDB.obtenerNuevoIdCliente();
            String datosUsuario = String.valueOf(edtNombre.getText()) + " " + String.valueOf(edtApellidos.getText());
            Cliente cliente = new Cliente(idCliente, firebaseAuth.getCurrentUser().getEmail(), "contraseña", datosUsuario);

            int idDireccionesClientes = ClienteDB.obtenerNuevoIDDireccionCliente();
            DireccionesClientes direccionesClientes = new DireccionesClientes(idDireccionesClientes, direccion, cliente);
            return direccionesClientes;
        }
    }
}