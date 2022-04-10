package com.example.sqlite_news;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int SCHEMA = 5;

    static final String TABLE_USERS = "Users";
    public static final String COLUMN_IDUSER = "_idUsers";
    public static final String COLUMN_NAME = "NameUser";
    public static final String COLUMN_LOGIN = "Login";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_ROLE = "Role";

    static final String TABLE_NEWS = "News";
    public static final String COLUMN_IDNEWS = "_id";
    public static final String COLUMN_ZAGOLOVOK = "Zagolovok";
    public static final String COLUMN_TEXTNEWS = "TextNews";
    public static final String COLUMN_POSTEDBYUSERNAME = "PostedByUserName";
    public static final String COLUMN_POSTEDBYUSERID = "PostedByID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" +
                COLUMN_IDUSER + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT, "
                + COLUMN_LOGIN + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_ROLE + " TEXT)");

        db.execSQL("INSERT INTO "+ TABLE_USERS +" ("
                + COLUMN_NAME + ", "
                + COLUMN_LOGIN  + ", "
                + COLUMN_PASSWORD + ", "
                + COLUMN_ROLE + ") VALUES ('Пользователь Юзер', 'user1', 'password', 'Ч');");

        db.execSQL("INSERT INTO "+ TABLE_USERS +" ("
                + COLUMN_NAME + ", "
                + COLUMN_LOGIN  + ", "
                + COLUMN_PASSWORD + ", "
                + COLUMN_ROLE + ") VALUES ('Писатель Один', 'pisatel1', 'password', 'П');");


        db.execSQL("CREATE TABLE News (" +
                COLUMN_IDNEWS + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ZAGOLOVOK + " TEXT, "
                + COLUMN_TEXTNEWS + " TEXT, "
                + COLUMN_POSTEDBYUSERNAME + " TEXT, "
                + COLUMN_POSTEDBYUSERID + " INTEGER)");

        db.execSQL("INSERT INTO "+ TABLE_NEWS +" ("
                + COLUMN_ZAGOLOVOK + ", "
                + COLUMN_TEXTNEWS  + ", "
                + COLUMN_POSTEDBYUSERNAME + ", "
                + COLUMN_POSTEDBYUSERID + ") VALUES ('Новость №1', '08.04.2022 произошла эвакуация всех корпусов РЭУ им. Г. В. Плеханова', 'pisatel1', 2);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NEWS);
        onCreate(db);
    }
}
