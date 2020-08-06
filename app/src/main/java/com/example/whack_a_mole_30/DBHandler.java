package com.example.whack_a_mole_30;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static String TAG = "DBHANDLER";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "WhackAMole.db";
    public static final String TABLE_USER = "Users";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_LEVEL = "Level";
    public static final String COLUMN_SCORE = "Score";



    public DBHandler(Context ctx, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(ctx,DATABASE_NAME,factory,DATABASE_VERSION);
    }

    private static SharedPreferences getPrefs(Context ctx){
        return ctx.getSharedPreferences("Shared",Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG,"Creating database");
        String CREATE_TABLE = "CREATE TABLE "+TABLE_USER + "(" + COLUMN_USERNAME + " TEXT,"+ COLUMN_PASSWORD + " TEXT,"+
                COLUMN_LEVEL + " TEXT,"+ COLUMN_SCORE +" TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        onCreate(db);
    }

    public void createAccount(String username, String password,Context ctx){
        SQLiteDatabase db = this.getWritableDatabase();
        //check if user exists
        Log.v(TAG,"New user creation with"+username+":"+password);
        String query = "SELECT * FROM "+ TABLE_USER + " WHERE " + COLUMN_USERNAME + "=\""+username + "\"";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            Log.v(TAG,"Account found, cancelling creation");
            cursor.close();
            Toast.makeText(ctx,"Account exists!",Toast.LENGTH_SHORT).show();
        }
        else {
            Log.v(TAG,"No data found");
            for (int i = 0; i < 10; i++) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_USERNAME, username);
                values.put(COLUMN_PASSWORD, password);
                values.put(COLUMN_LEVEL, Integer.toString(i+1));
                values.put(COLUMN_SCORE, "0");
                db.insert(TABLE_USER,null,values);
                Log.v(TAG,"Adding data for database"+":Level"+(i+1)+" Score = 0"+" Username ="+username+" Password ="+password);
            }
            db.close();
            Toast.makeText(ctx,"Account created!",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean loginAccount(String username,String password,Context ctx){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v(TAG,"Logging in with:"+username);
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + "=\""+username +"\"";
        Cursor cursor = db.rawQuery(query,null  );
        if(cursor.moveToFirst()){
            //check if password is correct
            String dbpassword = cursor.getString(1);
            if(dbpassword.equals(password)){
                //retrieve account data and store into shared pref
                SharedPreferences.Editor editor = getPrefs(ctx).edit();
                editor.putString("Username",username);
                editor.putString("Password",password);
                for(int i = 0; i < cursor.getCount(); i++){
                    editor.putInt("Level "+(i+1),Integer.parseInt(cursor.getString(3)));
                    cursor.moveToNext();
                }
                editor.apply();
                return true;
            }
            else{
                Log.v(TAG,"Passwords do not match!");
                Toast.makeText(ctx,"Incorrect password!",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Log.v(TAG,"No data found!");
            Toast.makeText(ctx,"Account does not exist",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public ArrayList<Integer> getScores(Context ctx){
        Log.v(TAG,"getting scores...");
        ArrayList<Integer>scores = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            int score = getPrefs(ctx).getInt("Level "+(i+1),0);
            scores.add(score);
            Log.v(TAG,"level "+(i+1)+":"+score);
        }
        return scores;
    }

    public int getScore(Context ctx,int level){
        return getPrefs(ctx).getInt("Level "+level,0);
    }
    public void saveScore(int level, int score,Context ctx){
        //save into db and update
        SQLiteDatabase db = getWritableDatabase();
        String username = getPrefs(ctx).getString("Username","Null");
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SCORE,score);
        String query = "UPDATE " + TABLE_USER + " SET "
                + COLUMN_SCORE + " = " + score + " WHERE "
                + COLUMN_USERNAME + " = \"" + username + "\"" + " AND "
                + COLUMN_LEVEL + " = \"" + level + "\"";
        db.execSQL(query);
        db.close();
        //update
        updateData(ctx);
    }

    public void updateData(Context ctx) {
        SQLiteDatabase db = getWritableDatabase();
        SharedPreferences.Editor editor = getPrefs(ctx).edit();
        String username = getPrefs(ctx).getString("Username","Null");
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + "=\"" + username + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                editor.putInt("Level " + (i+1), Integer.parseInt(cursor.getString(3)));
                cursor.moveToNext();

            }
            editor.apply();

        }
    }


}
