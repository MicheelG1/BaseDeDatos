package edu.ieu.basedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ieu.basedatos.modelo.AdminSqliteOpenHelper;

public class MainActivity extends AppCompatActivity {

    private final AdminSqliteOpenHelper adminDb = new AdminSqliteOpenHelper(this);
    private static final String TABLE_NAME = "articulos";

    private EditText etCodigo;
    private EditText etPrecio;
    private EditText etDescripcion;

    private Button btnAlta;
    private Button btnBuscarPorCodigo;
    private Button btnBaja;
    private Button btnActualizar;
    private Button btnBuscarPorDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCodigo = findViewById(R.id.et_codigo);
        etPrecio = findViewById(R.id.et_precio);
        etDescripcion = findViewById(R.id.et_descripcion);
        btnAlta = findViewById(R.id.btn_alta);
        btnBaja = findViewById(R.id.btn_baja);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnBuscarPorCodigo = findViewById(R.id.btn_buscar_codigo);
        btnBuscarPorDescripcion = findViewById(R.id.btn_buscar_descripcion);

        btnAlta.setOnClickListener( view -> {
            alta();
        });

        btnBuscarPorCodigo.setOnClickListener( view -> {
            buscarPorCodigo();
        });

        btnBaja.setOnClickListener( view ->
            bajaArticulo()
        ); //Lamda expression

        btnActualizar.setOnClickListener( view ->
            actualizarArticulo()
        ); //Cuando no tiene llaves se llama expreción Lamda, block

        btnBuscarPorDescripcion.setOnClickListener( view -> {
            buscarPorDescripcion();
        });
    }

    private void buscarPorDescripcion() {
        String descripcion = etDescripcion.getText().toString();
        if (descripcion.length() > 0) {
            SQLiteDatabase db = adminDb.getWritableDatabase();
            String where = "descripcion = ? ";
            String[] objectArray = { descripcion }; //Valores del WHERE
            Cursor cursor = db.query(
                    TABLE_NAME,
                    null,
                    where, //Condición del where
                    objectArray, //Valores de la condición
                    null,
                    null,
                    null
            );
            if (cursor.moveToNext()) {
                @SuppressLint("Range")
                Double codigo = cursor.getDouble( cursor.getColumnIndex( "codigo" ) );
                @SuppressLint("Range")
                Double precio = cursor.getDouble( cursor.getColumnIndex( "precio" ) );
                etPrecio.setText( precio.toString() );
                etCodigo.setText( codigo.toString() );
            } else {
                Toast.makeText(this, "La descripción " + descripcion + " no existe en la base de datos", Toast.LENGTH_LONG).show();
            }
        }else Toast.makeText(this, "Descripción no debe ser vacio", Toast.LENGTH_LONG).show();
    }

    private void actualizarArticulo() {
        String codigo = etCodigo.getText().toString();
        Double precio = Double.parseDouble( etPrecio.getText().toString() );
        String descripcion = etDescripcion.getText().toString();

        SQLiteDatabase db = adminDb.getWritableDatabase();
        if (codigo.length() > 0 ) {
            ContentValues content = new ContentValues();
            //content.put("codigo", codigo);
            content.put("precio", precio);
            content.put("descripcion", descripcion);
            String where = " codigo =? ";
            String[] whereparams = {codigo};

            int actualizados = db.update(TABLE_NAME, content, where, whereparams);
            if (actualizados > 0) {
                Toast.makeText(this, "Actualizado el articulo con código " + codigo + " correctamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText( this, "Código no encontrado en la base de datos", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText( this, "Código no debe ser vacio", Toast.LENGTH_LONG).show();
        }
    }

    private void bajaArticulo() {
        String codigo = etCodigo.getText().toString();
        if (codigo.length() > 0) {
            SQLiteDatabase db = adminDb.getWritableDatabase();
            String where = "codigo = ? ";
            String[] objectArray = { codigo }; //Valores del WHERE
            int registrosBorrados = db.delete(
                    TABLE_NAME,
                    where, //Condición del where
                    objectArray //Valores de la condición
            );
            if (registrosBorrados > 0) {
                Toast.makeText(this, "Articulo con código " + codigo + " borrado de la base de datos", Toast.LENGTH_LONG).show();
                etCodigo.setText("");
            } else {
                Toast.makeText(this, "Código" + codigo + " no existe en la base de datos", Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(this, "Código no debe ser vacio", Toast.LENGTH_LONG).show();
    }

    private void buscarPorCodigo() {
        String codigo = etCodigo.getText().toString();
        if (codigo.length() > 0) {
            SQLiteDatabase db = adminDb.getWritableDatabase();
            String where = "codigo = ? ";
            String[] objectArray = { codigo }; //Valores del WHERE
            Cursor cursor = db.query(
                    TABLE_NAME,
                    null,
                    where, //Condición del where
                    objectArray, //Valores de la condición
                    null,
                    null,
                    null
            );
            if (cursor.moveToNext()) {
                @SuppressLint("Range")
                String descripcion = cursor.getString( cursor.getColumnIndex( "descripcion" ) );
                @SuppressLint("Range")
                Double precio = cursor.getDouble( cursor.getColumnIndex( "precio" ) );
                etPrecio.setText( precio.toString() );
                etDescripcion.setText( descripcion.toString() );
            } else {
                Toast.makeText(this, "Código " + codigo + " no existe en la base de datos", Toast.LENGTH_LONG).show();
            }
        }else Toast.makeText(this, "Código no debe ser vacio", Toast.LENGTH_LONG).show();
    }

    private void alta() {
        String codigo = etCodigo.getText().toString();
        Double precio = Double.parseDouble( etPrecio.getText().toString() );
        String descripcion = etDescripcion.getText().toString();
        SQLiteDatabase db = adminDb.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("codigo", codigo);
        content.put("precio", precio);
        content.put("descripcion", descripcion);

        db.insert(TABLE_NAME, null, content);
        Toast.makeText( this,"Insertado el código " + codigo + " en la base de datos", Toast.LENGTH_LONG).show();
    }
}