package com.superdroid.test.mymemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ReadMemoActivity extends AppCompatActivity {

    private final String dbName = "MyMemoDB";
    private final String MemoTableName="Memo";
    SQLiteDatabase sampleDB = null;
    TextView titleTextView, docTextView,timeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_memo);
        titleTextView=(TextView)findViewById(R.id.MemoTitle);
        docTextView=(TextView)findViewById(R.id.MemoDocument);
        timeTextView=(TextView)findViewById(R.id.MemoTime);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String doc=intent.getStringExtra("doc");
        titleTextView.setText(title);
        docTextView.setText(doc);
        sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        try{
            sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
            Cursor cursor=sampleDB.rawQuery("SELECT date from "+MemoTableName+" where title='"+title+"'",null);
            while(cursor.moveToNext()){
                String date=cursor.getString(0);
                timeTextView.setText(date);
            }
        }catch(SQLiteException se){
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());
        }
    }
    protected void backBtnClick(View v){
        onBackPressed();
    }

}
