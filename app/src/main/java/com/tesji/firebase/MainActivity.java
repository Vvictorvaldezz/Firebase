package com.tesji.firebase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    EditText nomP, appP, correoP, passwordP;//
    ListView listV_personas;//

    FirebaseDatabase firebaseDatabase;//
    DatabaseReference databaseReference;//

    Persona personaSelected;//
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

        listV_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSelected = (Persona) parent.getItemAtPosition(position);
                nomP.setText(personaSelected.getNombre());
                appP.setText(personaSelected.getApellido());
                correoP.setText(personaSelected.getCorreo());
                passwordP.setText(personaSelected.getPassword());
            }
        });
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("¿Está seguro de agregar el registro?")
                            .setTitle("¡Mensaje de advertencia!");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Persona p =new Persona();
                            p.setUid(UUID.randomUUID().toString());
                            p.setNombre(nombre);
                            p.setApellido(app);
                            p.setCorreo(correo);
                            p.setPassword(password);
                            databaseReference.child("Persona").child(p.getUid()).setValue(p);
                            limpiarCajas();
                            Toast.makeText(MainActivity.this, "¡Acción exitosa!", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "¡Acción cancelada!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                break;
            }

            case R.id.icon_save:{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Está seguro de realizar los cambios?")
                        .setTitle("¡Mensaje de advertencia!");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Persona p = new Persona();
                        p.setUid(personaSelected.getUid());
                        p.setNombre(nomP.getText().toString().trim());
                        p.setApellido(appP.getText().toString().trim());
                        p.setCorreo(correoP.getText().toString().trim());
                        p.setPassword(passwordP.getText().toString().trim());
                        databaseReference.child("Persona").child(p.getUid()).setValue(p);
                        limpiarCajas();
                        Toast.makeText(MainActivity.this, "¡Acción exitosa!", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "¡Acción cancelada!", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }

            case R.id.icon_delete:{

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Está seguro de eliminar el registro?")
                        .setTitle("¡Mensaje de advertencia!");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Persona p = new Persona();
                        p.setUid(personaSelected.getUid());
                        databaseReference.child("Persona").child(p.getUid()).removeValue();
                        limpiarCajas();
                        Toast.makeText(MainActivity.this, "¡Acción exitosa!", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "¡Acción cancelada!", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

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
        nomP.requestFocus();
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