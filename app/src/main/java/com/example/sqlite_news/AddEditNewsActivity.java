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
import android.widget.TextView;
import android.widget.Toast;

public class AddEditNewsActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    String id;
    String authors;
    String id_user;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_news);
        Bundle arguments = getIntent().getExtras();

        id = "";
        try {
            id = arguments.get("id").toString();
        }
        catch (Exception e) {
            id = null;
        }

        id_user = "";
        try {
            id_user = arguments.get("id_user").toString();
        }
        catch (Exception e) {
            id_user = null;
        }

        Button otmena = (Button) findViewById(R.id.otmena);
        Button save_btn = (Button) findViewById(R.id.save_btn);
        Button delete = (Button) findViewById(R.id.btn_delete);
        Button nazad = (Button) findViewById(R.id.nazad);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        EditText zagolovok = (EditText) findViewById(R.id.zagolovok);
        EditText textNews = (EditText) findViewById(R.id.textNews);
        if(id_user != null) {
            if (id != null) {
                TextView rezhim = (TextView) findViewById(R.id.rezhim);
                rezhim.setText("Изменение ");

                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NEWS + " where " + DatabaseHelper.COLUMN_IDNEWS + "=?", new String[]{String.valueOf(id)}, null);
                userCursor.moveToFirst();
                zagolovok.setText(userCursor.getString(1));

                textNews.setText(userCursor.getString(2));

                TextView author = (TextView) findViewById(R.id.author);
                author.setText("Автор: " + userCursor.getString(3));
                authors = userCursor.getString(3);
            } else {
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_USERS + " where " + DatabaseHelper.COLUMN_IDUSER + "=?", new String[]{String.valueOf(id_user)}, null);
                userCursor.moveToFirst();
                authors = userCursor.getString(1);

                TextView rezhim = (TextView) findViewById(R.id.rezhim);
                rezhim.setText("Добавление ");

                TextView author = (TextView) findViewById(R.id.author);
                author.setVisibility(View.GONE);

                Button del_btn = (Button) findViewById(R.id.btn_delete);
                del_btn.setVisibility(View.GONE);
            }
        }
        else {
            TextView rezhim = (TextView) findViewById(R.id.rezhim);
            rezhim.setText("Просмотр ");
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NEWS + " where " + DatabaseHelper.COLUMN_IDNEWS + "=?", new String[]{String.valueOf(id)}, null);
            userCursor.moveToFirst();
            zagolovok.setText(userCursor.getString(1));
            textNews.setText(userCursor.getString(2));
            TextView author = (TextView) findViewById(R.id.author);
            author.setText("Автор: " + userCursor.getString(3));
            zagolovok.setKeyListener(null);
            textNews.setKeyListener(null);
            nazad.setVisibility(View.VISIBLE);
            otmena.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            save_btn.setVisibility(View.GONE);
        }

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        otmena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(DatabaseHelper.TABLE_NEWS, DatabaseHelper.COLUMN_IDNEWS+"=?", new String[]{String.valueOf(id)});
                Toast toast = Toast.makeText(getApplicationContext(), "Новость удалена", Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(AddEditNewsActivity.this, PisatelActivity.class);
                intent.putExtra("ID_User", id_user);
                startActivity(intent);
                finishAffinity();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zagolovok.setText(zagolovok.getText().toString().trim());
                textNews.setText(textNews.getText().toString().trim());
                if(zagolovok.getText().toString().length() < 2) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Заголовок указан некорректно", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if(textNews.getText().toString().length() < 10) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Текст новости указан некорректно. Минимум 20 символов", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if(id == null){
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.COLUMN_POSTEDBYUSERID, Integer.parseInt(id_user));
                    cv.put(DatabaseHelper.COLUMN_POSTEDBYUSERNAME, authors);
                    cv.put(DatabaseHelper.COLUMN_ZAGOLOVOK, zagolovok.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_TEXTNEWS, textNews.getText().toString());
                    db.insert(DatabaseHelper.TABLE_NEWS, null, cv);

                    Toast toast = Toast.makeText(getApplicationContext(), "Новость добавлена!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.COLUMN_POSTEDBYUSERID, Integer.parseInt(id_user));
                    cv.put(DatabaseHelper.COLUMN_POSTEDBYUSERNAME, authors);
                    cv.put(DatabaseHelper.COLUMN_ZAGOLOVOK, zagolovok.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_TEXTNEWS, textNews.getText().toString());
                    db.update(DatabaseHelper.TABLE_NEWS, cv, DatabaseHelper.COLUMN_IDNEWS + "=" + Integer.parseInt(id), null);

                    Toast toast = Toast.makeText(getApplicationContext(), "Новость обновлена!", Toast.LENGTH_LONG);
                    toast.show();
                }
                Intent intent = new Intent(AddEditNewsActivity.this, PisatelActivity.class);
                intent.putExtra("ID_User", id_user);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}