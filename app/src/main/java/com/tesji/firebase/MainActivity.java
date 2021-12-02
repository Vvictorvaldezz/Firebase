package com.tesji.firebase;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tesji.firebase.model.Persona;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

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

        listarDatos();
    }

    private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPerson.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    Persona p = objSnapshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapterPersona= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listPerson);
                    listV_personas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase= FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
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