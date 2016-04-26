package com.example.neeraj.spotifystreamer.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Neeraj on 25-04-2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String databaseName="favourite";
    public static final int databaseVersion=1;
   public static final String tableName="movieDetails";
    public static final String movieTitle="movieTitle";
    public static final String posterPath="posterPath";
    public static final String overView="overview";
    public static final String releaseDate="releaseDate";
    public static final String id="id";
    public static final String voteAverage="voteAverage";
    public DataBaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +tableName+"("+movieTitle+" VARCHAR,"+posterPath+" VARCHAR,"+overView+" VARCHAR," +
                releaseDate +" VARCHAR,"+id +" INTEGER PRIMARY KEY,"+voteAverage+" REAL);");
        Log.v("sf","sf");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +tableName);
        onCreate( db );
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}
