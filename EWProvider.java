package com.example.prac1_2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class EWProvider extends ContentProvider {
    static final Uri CONTENT_URI = Uri.parse("content://yc.ac.kr/word");
    static final int ALLWORD = 1;
    static final int ONEWORD = 2;
    static UriMatcher Matcher =null;
    {
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI("yc.ac.kr","word", ALLWORD);
        Matcher.addURI("yc.ac.kr","word/*",ONEWORD);
    }

    SQLiteDatabase db;


    public EWProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        /*
        int count=0;
        switch (Matcher.match(uri)){
            case ALLWORD:
                count = db.delete("dic", selection, selectionArgs);
                break;
            case ONEWORD:
                String where;
                where = " eng = '"+uri.getPathSegments().get(1)+"'";
                if(TextUtils.isEmpty(selection)==false){
                    where += " AND "+ selection;
                }
                count = db.delete("dic", selection, selectionArgs);
                break;
        }
        return count;
        */
        String sql = "Delete From dic";
        if(Matcher.match(uri)== ONEWORD){
            sql += " Where eng = '"+uri.getPathSegments().get(1)+"'";
        }
        db.execSQL(sql);
        return 1;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        //throw new UnsupportedOperationException("Not yet implemented");
        if(Matcher.match(uri)==ALLWORD){
            return "vnd.kr.ac.yc.cursor.item/word";
        }
        if(Matcher.match(uri)==ONEWORD){
            return "vnd.kr.ac.yc.cursor.dir/word";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        //throw new UnsupportedOperationException("Not yet implemented");

        long row = db.insert("dic", null, values);
        if(row>0){
            Uri notiuri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(notiuri, null);
            return notiuri;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        MainActivity.WordDBHelper helper = new MainActivity.WordDBHelper(getContext());
        db = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //throw new UnsupportedOperationException("Not yet implemented");
        String sql;
        sql = "Select eng, han From dic ";
        if(Matcher.match(uri)==ONEWORD){
            sql +=" Where eng = '"+ uri.getPathSegments().get(1)+"'";
        }
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        int count=0;
        switch (Matcher.match(uri)){
            case ALLWORD:
                count = db.update("dic", values,selection, selectionArgs);
                break;
            case ONEWORD:
                String where;
                where = " eng = '"+uri.getPathSegments().get(1)+"'";
                if(TextUtils.isEmpty(selection)==false){
                    where += " AND "+ selection;
                }
                count = db.update("dic", values,selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
