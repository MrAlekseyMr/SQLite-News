package com.example.sqlite_news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        Button btn_regto = (Button) findViewById(R.id.btn_login);
        btn_regto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Registration.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_registration = (Button) findViewById(R.id.btn_register);
        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name_et = (EditText) findViewById(R.id.et_name);
                EditText login_et = (EditText) findViewById(R.id.et_email);
                EditText password_et = (EditText) findViewById(R.id.et_password);
                EditText repassword_et = (EditText) findViewById(R.id.et_repassword);

                name_et.setText(name_et.getText().toString().trim());
                login_et.setText(login_et.getText().toString().trim());
                password_et.setText(password_et.getText().toString().trim());
                repassword_et.setText(repassword_et.getText().toString().trim());
                if(name_et.getText().toString().length() < 2 || name_et.getText().toString().length() > 30)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Имя указано некорретно. Минимум 2 символа!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if(login_et.getText().toString().length() < 2 || login_et.getText().toString().contains(" ") || login_et.getText().toString().length() > 20)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Логин указан некорретно. Минимум 2 символа!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if(password_et.getText().toString().length() < 6 || password_et.getText().toString().contains(" "))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Минимальная длина пароля 6 символов", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if(!password_et.getText().toString().equals(repassword_et.getText().toString()))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_USERS + " where "+DatabaseHelper.COLUMN_LOGIN+" = '"+ login_et.getText().toString()+"'", null);
                int t = userCursor.getCount();
                if(t > 0)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Такой логин уже существует!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.COLUMN_NAME, name_et.getText().toString());
                cv.put(DatabaseHelper.COLUMN_PASSWORD, password_et.getText().toString());
                cv.put(DatabaseHelper.COLUMN_LOGIN, login_et.getText().toString());
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String selected = spinner.getSelectedItem().toString();
                if(selected.equals("Читатель"))
                    cv.put(DatabaseHelper.COLUMN_ROLE, "Ч");
                if(selected.equals("Писатель"))
                    cv.put(DatabaseHelper.COLUMN_ROLE, "П");
                db.insert(DatabaseHelper.TABLE_USERS,null, cv);

                userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_USERS + " where "+DatabaseHelper.COLUMN_LOGIN+" = '"+ login_et.getText().toString()+"'", null);
                if(selected.equals("Читатель"))
                {
                    Intent intent;
                    intent = new Intent(Registration.this, ReaderActivity.class);
                    String id = userCursor.getString(0);
                    intent.putExtra("ID_User",id);
                    startActivity(intent);
                    finishAffinity();
                }
                if(selected.equals("Писатель"))
                {
                    Intent intent;
                    intent = new Intent(Registration.this, PisatelActivity.class);
                    String id = userCursor.getString(0);
                    intent.putExtra("ID_User",id);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.getReadableDatabase();
    }
}