package com.example.sqlite_news;

import androidx.appcompat.app.AppCompatActivity;

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

public class HomeActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        Button btn_regto = (Button) findViewById(R.id.btn_reg);
        btn_regto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(HomeActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login = (EditText) findViewById(R.id.et_email);
                EditText password = (EditText) findViewById(R.id.et_password);
                userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_USERS + " where "+DatabaseHelper.COLUMN_LOGIN+" ='"+login.getText().toString()+"' and "+DatabaseHelper.COLUMN_PASSWORD+" = '"+password.getText().toString()+"'", null);
                if(userCursor.getCount() <= 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Такой пользователь не найден", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                userCursor.moveToFirst();
                if(userCursor.getString(4).toString().equals("П")) {
                    Intent intent = new Intent(HomeActivity.this, PisatelActivity.class);
                    intent.putExtra("ID_User", userCursor.getString(0));
                    startActivity(intent);
                    finishAffinity();
                }
                if(userCursor.getString(4).toString().equals("Ч")) {
                    Intent intent = new Intent(HomeActivity.this, ReaderActivity.class);
                    intent.putExtra("ID_User", userCursor.getString(0));
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });
    }
}