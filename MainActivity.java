package com.example.prac1_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    WordDBHelper mHelper;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edit);
        mHelper = new WordDBHelper(this);
    }


    public void mClick(View view){
        SQLiteDatabase db;
        ContentValues row;

        switch(view.getId()){
            case R.id.insert:
                db = mHelper.getWritableDatabase();
                row = new ContentValues();
                row.put("eng","boy");
                row.put("han", "머스마");
                db.insert("dic",null,row);
                //SQL문장으로 insert
                db.execSQL("Insert into dic values (null,'girl', '가시나')");
                mHelper.close();
                editText.setText("Insert Success!");
                break;
            case R.id.delete:
                int num;
                db = mHelper.getWritableDatabase();
                num=db.delete("dic", null, null);//("dic", "eng=? and han =?", new String[]{"girl","가시나"}); <-eng에 girl, han에 가시나인 데이터를 삭제
                //db.execSQL("Delete From dic Where eng = 'boy');
                editText.setText(num+" Datas delete complete!");
                mHelper.close();
                break;
            case R.id.update:
                db = mHelper.getWritableDatabase();
                row = new ContentValues();
                row.put("han","소녀");
                db.update("dic",row, "eng=?",new String[]{"girl"});// "eng=gril", null도 괜찮다.
                db.execSQL("Update dic set han='소년' where eng = 'boy'");
                editText.setText("Update Complete");
                break;
            case R.id.select:
                db = mHelper.getReadableDatabase();
                String result="";
                Cursor cursor = db.rawQuery("Select eng, han From dic",null);
                while(cursor.moveToNext()){
                    String eng = cursor.getString(0);
                    String han = cursor.getString(1);
                    result += eng+" = "+han+"\n";
                }
                if(result.length()==0){
                    editText.setText("Empty set");
                }else{
                    editText.setText(result);
                }
                cursor.close();
                mHelper.close();
                break;
        }
    }

    static class WordDBHelper extends SQLiteOpenHelper{

        public WordDBHelper(Context context) {
            super(context, "Engword.db", null,1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create table dic (_id INTEGER primary key autoincrement, eng text, han text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("Drop table if exists dic");
            onCreate(db);
        }
    }
}