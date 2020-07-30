package com.example.app18sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText
    texId,
    texNombreProducto,
    texPrecioNetoProducto,
    texInteresProducto,
    texPrecioFinalProducto;

    private String id, nombreProducto, procioNetoProducto, interesProducto, precioFinalProducto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getProcioNetoProducto() {
        return procioNetoProducto;
    }

    public void setProcioNetoProducto(String procioNetoProducto) {
        this.procioNetoProducto = procioNetoProducto;
    }

    public String getInteresProducto() {
        return interesProducto;
    }

    public void setInteresProducto(String interesProducto) {
        this.interesProducto = interesProducto;
    }

    public String getPrecioFinalProducto() {
        return precioFinalProducto;
    }

    public void setPrecioFinalProducto(String precioFinalProducto) {
        this.precioFinalProducto = precioFinalProducto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texId=(EditText)findViewById(R.id.texId);
        texNombreProducto = (EditText)findViewById(R.id.texNombreProducto);
        texPrecioNetoProducto = (EditText)findViewById(R.id.texPrecioNetoProducto);
        texInteresProducto = (EditText)findViewById(R.id.texInteresProducto);
        texPrecioFinalProducto = (EditText)findViewById(R.id.texPrecioFinalProducto);


    }

    public void setTexs() {
        setId(texId.getText().toString());
        setNombreProducto(texNombreProducto.getText().toString());
        setProcioNetoProducto(texPrecioNetoProducto.getText().toString());
        setInteresProducto(texInteresProducto.getText().toString());
        setPrecioFinalProducto(texPrecioFinalProducto.getText().toString());
    }

    public void vaciarDatos() {
        setId("");
        setNombreProducto("");
        setProcioNetoProducto("");
        setInteresProducto("");
        setPrecioFinalProducto("");

        texId.setText("");
        texNombreProducto.setText("");
        texPrecioNetoProducto.setText("");
        texInteresProducto.setText("");
        texPrecioFinalProducto.setText("");
    }

    public double getcalcularPrecioFinal(){
        setTexs();
        double precioFinal = 0;
        try {

            double precioNeto = Integer.parseInt(getProcioNetoProducto());
            double interes = Integer.parseInt(getInteresProducto());

            precioFinal = precioNeto + interes;

        }catch (NumberFormatException e ){
            Toast.makeText(this, "Error al Registrar datos", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
        }
        return precioFinal;
    }



    public void Registrar (View view){

        AdminSQLite adminSQLite = new AdminSQLite(this, "DataBaseSQLite", null,1);
        SQLiteDatabase sqLiteDatabase = adminSQLite.getWritableDatabase();

        setTexs();

        if(getId() != "" & getNombreProducto() != "" & getProcioNetoProducto() != "" & getInteresProducto() != ""){

            try {
                ContentValues registros = new ContentValues();

                registros.put("id", getId());
                registros.put("nombre_producto", getNombreProducto());
                registros.put("precio_neto_producto", getProcioNetoProducto());
                registros.put("interes_productos", getInteresProducto());
                registros.put("procio_final_producto", getcalcularPrecioFinal());

                sqLiteDatabase.insert("name_tabla1", null, registros);

                Toast.makeText(this, "Datos Registrados Correctamente", Toast.LENGTH_LONG).show();

                vaciarDatos();
            }catch (SQLiteException e){
                Toast.makeText(this, "Error al Registrar datos", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();

            }finally {
                sqLiteDatabase.close();
            }

        }else{
            if(getId() != "" & getNombreProducto() != "" & getProcioNetoProducto() != "" & getInteresProducto() != ""){
                Toast.makeText(this, "Debes de llenar todos los campos", Toast.LENGTH_SHORT).show();
            }
            if(getId() != ""){
                Toast.makeText(this, "Debes de llenar el campo Id", Toast.LENGTH_LONG).show();
            }
            if(getNombreProducto() != ""){
                Toast.makeText(this, "Debes de llenar el campo Nombre Producto", Toast.LENGTH_LONG).show();
            }
            if(getProcioNetoProducto() != ""){
                Toast.makeText(this, "Debes de llenar el campo Procio Neto del Producto", Toast.LENGTH_LONG).show();
            }
            if(getInteresProducto() != ""){
                Toast.makeText(this, "Debes de llenar el campo Interes del Producto", Toast.LENGTH_LONG).show();
            }
        }

    }



    public void Buscar(View view){
        AdminSQLite adminSQLite = new AdminSQLite(this, "DataBaseSQLite", null, 1);
        SQLiteDatabase sqLiteDatabase = adminSQLite.getWritableDatabase();

        setTexs();

        if(getId() != ""){
          try {
              Cursor fila = sqLiteDatabase.rawQuery("SELECT nombre_producto, precio_neto_producto, interes_productos, " +
                                                        "procio_final_producto" + " FROM name_tabla1 WHERE id =" + getId(), null);

              if(fila.moveToFirst()){
                  texNombreProducto.setText(fila.getString(0));
                  texPrecioNetoProducto.setText(fila.getString(1));
                  texInteresProducto.setText(fila.getString(2));
                  texPrecioFinalProducto.setText(fila.getString(3));

                  Toast.makeText(this, "Producto seleccionado correctamente", Toast.LENGTH_LONG).show();
              }else{
                  Toast.makeText(this, "Producto no registrado", Toast.LENGTH_LONG).show();
              }
          }catch (SQLiteException e){
              Toast.makeText(this, "Error al Buscar datos", Toast.LENGTH_LONG).show();
              Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();

          }finally {
              sqLiteDatabase.close();
          }


        }else{
            Toast.makeText(this, "Debes de llenar el campo Id", Toast.LENGTH_LONG).show();
        }
    }



    public void Eliminar(View view){
        AdminSQLite adminSQLite = new AdminSQLite(this, "DataBaseSQLite", null, 1);
        SQLiteDatabase sqLiteDatabase = adminSQLite.getWritableDatabase();

        setTexs();

        if(getId() != ""){
            try {

                int cantidad = sqLiteDatabase.delete("name_tabla1","id=" + getId(), null);

                if (cantidad > 0){
                    Toast.makeText(this, "Producto Eliminado correctamente", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "Producto no registrado", Toast.LENGTH_LONG).show();
                }

                vaciarDatos();
            }catch (SQLiteException e){
                Toast.makeText(this, "Error al Eliminar datos", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();

            }finally {
                sqLiteDatabase.close();
            }
        }else{
            Toast.makeText(this, "Debes de llenar el campo Id", Toast.LENGTH_LONG).show();
         }
    }



    public void Modificar(View view){
        AdminSQLite adminSQLite = new AdminSQLite(this, "DataBaseSQLite", null, 1);
        SQLiteDatabase sqLiteDatabase = adminSQLite.getWritableDatabase();

        setTexs();

        if(getId() != "" & getNombreProducto() != "" & getProcioNetoProducto() != "" & getInteresProducto() != ""){
            try {

                ContentValues registros = new ContentValues();

                registros.put("id", getId());
                registros.put("nombre_producto", getNombreProducto());
                registros.put("precio_neto_producto", getProcioNetoProducto());
                registros.put("interes_productos", getInteresProducto());
                registros.put("procio_final_producto", getcalcularPrecioFinal());

                int cantidad = sqLiteDatabase.update("name_tabla1", registros, "id=" + getId(), null);

                if (cantidad > 0){
                    Toast.makeText(this, "Producto Modificado correctamente", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "Producto no registrado", Toast.LENGTH_LONG).show();
                }

                vaciarDatos();

            }catch (SQLiteException e){
                Toast.makeText(this, "Error al Modificar datos", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();

            }finally {
                sqLiteDatabase.close();
            }
        }else{
            if(getId() != "" & getNombreProducto() != "" & getProcioNetoProducto() != "" & getInteresProducto() != ""){
                Toast.makeText(this, "Debes de llenar todos los campos", Toast.LENGTH_SHORT).show();
            }
            if(getId() != ""){
                Toast.makeText(this, "Debes de llenar el campo Id", Toast.LENGTH_LONG).show();
            }
            if(getNombreProducto() != ""){
                Toast.makeText(this, "Debes de llenar el campo Nombre Producto", Toast.LENGTH_LONG).show();
            }
            if(getProcioNetoProducto() != ""){
                Toast.makeText(this, "Debes de llenar el campo Procio Neto del Producto", Toast.LENGTH_LONG).show();
            }
            if(getInteresProducto() != ""){
                Toast.makeText(this, "Debes de llenar el campo Interes del Producto", Toast.LENGTH_LONG).show();
            }
        }
    }
}