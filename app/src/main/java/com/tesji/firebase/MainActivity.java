package com.tesji.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tesji.firebase.model.Persona;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText nomP, appP, correoP, passwordP;
    ListView listV_personas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP = findViewById(R.id.textInput_Nombre);
        appP =findViewById(R.id.textInput_Apellido);
        correoP =findViewById(R.id.textInput_Correo);
        passwordP=findViewById(R.id.textInput_Contraseña);
        listV_personas=findViewById(R.id.datos_personas);
        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String nombre =nomP.getText().toString();
        String app = appP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if (nombre.equals("") || app.equals("") || correo.equals("") || password.equals("")){
                    validacion();
                }
                else {
                    Persona p =new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(app);
                    p.setCorreo(correo);
                    p.setPassword(password);
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);

                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }

            case R.id.icon_save:{
                Toast.makeText(this, "Guardar", Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.icon_delete:{
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiarCajas() {
        nomP.setText("");
        appP.setText("");
        correoP.setText("");
        passwordP.setText("");
    }

    private void validacion() {
        String nombre =nomP.getText().toString();
        String app = appP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();

        if (nombre.equals("")){
            nomP.setError("Requerido");
        }
        else if (app.equals("")){
            appP.setError("Requerido");
        }
        else if (correo.equals("")){
            correoP.setError("Requerido");
        }
        else if (password.equals("")){
            passwordP.setError("Requerido");
        }
    }
}