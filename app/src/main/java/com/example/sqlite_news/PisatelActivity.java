package com.example.sqlite_news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class PisatelActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pisatel);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        try {
            Bundle arguments = getIntent().getExtras();
            id = arguments.get("ID_User").toString();
            db = databaseHelper.getReadableDatabase();
            userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_USERS + " where "+DatabaseHelper.COLUMN_IDUSER+"=?", new String[]{String.valueOf(id)}, null);
            userCursor.moveToFirst();
            TextView ima_user = (TextView) findViewById(R.id.ima_user);
            ima_user.setText(userCursor.getString(1));
            TextView login = (TextView) findViewById(R.id.login);
            login.setText(userCursor.getString(2));

            userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_NEWS +" where "+DatabaseHelper.COLUMN_POSTEDBYUSERID+"=?",new String[]{String.valueOf(id)}, null);
            String[] headers = new String[] {DatabaseHelper.COLUMN_ZAGOLOVOK, DatabaseHelper.COLUMN_POSTEDBYUSERNAME};
            userAdapter = new SimpleCursorAdapter(this,
                    R.layout.my_list_item,
                    userCursor,
                    headers,
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0);
            ListView list = (ListView) findViewById(R.id.list);
            list.setAdapter(userAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id_id) {
                    Intent intent = new Intent(getApplicationContext(), AddEditNewsActivity.class);
                    intent.putExtra("id", id_id);
                    intent.putExtra("id_user", id);
                    startActivity(intent);
                }
            });

            Button createnews = (Button) findViewById(R.id.createnews);
            createnews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AddEditNewsActivity.class);
                    intent.putExtra("id_user", id);
                    startActivity(intent);
                }
            });

            Button exit_btn = (Button) findViewById(R.id.exit_btn);
            exit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            });
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}