package com.superdroid.test.mymemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddMemoActivity extends AppCompatActivity {
    private EditText MemoTitle, MemoDocument;
    private final String dbName = "MyMemoDB";
    private final String checkListTableName= "CheckList";
    private final String MemoTableName="Memo";
    private boolean TitleErrorCheck=false;
    SQLiteDatabase sampleDB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        TitleErrorCheck=false;
        try{
            sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + checkListTableName
                    + " (done VARCHAR(20), title VARCHAR(100), date VARCHAR(20),PRIMARY KEY(title));");
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + MemoTableName
                    + " (date VARCHAR(20), title VARCHAR(100), document VARCHAR(2000),PRIMARY KEY(title) );");
        }catch(SQLiteException se){
                Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());
        }
    }
    private long time= 0;
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void OnClick(View v){
        int id= v.getId();
        if(id==R.id.saveBtn) {
            long now = System.currentTimeMillis();
            MemoTitle = (EditText) findViewById(R.id.MemoTitle);
            MemoDocument = (EditText) findViewById(R.id.MemoDocument);
            if (MemoTitle.getText().toString() != "" && MemoDocument.getText().toString() != "") {
                MemoData memoData = new MemoData(new Date(now), MemoTitle.getText().toString(), MemoDocument.getText().toString());
                try {
                    sampleDB.execSQL("INSERT INTO " + MemoTableName + " (date, title, document) Values ('" + memoData.getDate() + "','" + memoData.getTitle() +
                            "','" + memoData.getDocument() + "');");
                } catch (SQLiteException se) {
                    if (se.getMessage().toString().contains("UNIQUE"))
                        TitleErrorCheck = true;
                }
                if (!TitleErrorCheck) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "제목이 중복됩니다. 다른 제목으로 선택하세요", Toast.LENGTH_SHORT).show();
                    TitleErrorCheck = false;
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"제목 혹은 내용이 없습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
