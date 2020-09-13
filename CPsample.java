package com.example.callwordcp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    static final String WORDURI = "content://yc.ac.kr/word";
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
    }

    public void mClick(View v){
        ContentResolver resolver = getContentResolver();
        switch (v.getId()){
            case R.id.readall:
                Cursor cursor = resolver.query(Uri.parse(WORDURI),null,null,null,null);
                String result = "";
                while(cursor.moveToNext()){
                    String eng = cursor.getString(0);
                    String han = cursor.getString(1);
                    result += (eng + " = "+han + "\n");
                    if(result.length()==0){
                        editText.setText("Empty Set");
                    }else{
                        editText.setText(result);
                    }
                }
                cursor.close();
                break;
            case R.id.readone:
                Cursor cursor1 = resolver.query(Uri.parse(WORDURI+"/boy"),null,null,null,null);
                String result1 = "";
                if(cursor1.moveToNext()==true){
                    String eng = cursor1.getString(0);
                    String han = cursor1.getString(1);
                    result1 += (eng + " = "+han + "\n");
                }
                if(result1.length()==0){
                    editText.setText("Empty Set");
                }else{
                    editText.setText(result1);
                }
                cursor1.close();
                break;
            case R.id.insert:
                ContentValues row = new ContentValues();
                row.put("eng", "school");
                row.put("han", "학교");
                resolver.insert(Uri.parse(WORDURI), row);
                editText.setText("Insert Success!");
                break;
            case R.id.update:
                ContentValues row1 = new ContentValues();
                row1.put("han", "핵교");
                resolver.update(Uri.parse(WORDURI+"/school"), row1,null,null);
                editText.setText("Update Success!");
                break;
            case R.id.delete:
                resolver.delete(Uri.parse(WORDURI), null,null);
                editText.setText("Delete Success!");
                break;
        }
    }
}