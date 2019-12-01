package com.superdroid.test.mymemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class ModifyMemoActivity extends AppCompatActivity {
    private EditText MemoTitle, MemoDocument;
    private final String dbName = "MyMemoDB";
    private final String checkListTableName= "CheckList";
    private final String MemoTableName="Memo";
    private boolean TitleErrorCheck=false;
    private String originTitle="";
    SQLiteDatabase sampleDB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent= getIntent();
        String title=intent.getStringExtra("title");
        originTitle=title;
        String doc=intent.getStringExtra("doc");
        MemoTitle = (EditText) findViewById(R.id.MemoTitle);
        MemoDocument = (EditText) findViewById(R.id.MemoDocument);
        MemoTitle.setText(title);
        MemoDocument.setText(doc);
        TitleErrorCheck=false;
    }
    private long time= 0;
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
    protected void OnClick(View v){
        int id= v.getId();
        if(id==R.id.saveBtn) {
            long now = System.currentTimeMillis();
            if (!(MemoTitle.getText().toString().equals("")) && !(MemoDocument.getText().toString().equals(""))) {
                MemoData memoData = new MemoData(new Date(now), MemoTitle.getText().toString(), MemoDocument.getText().toString());
                try {
                    sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
                    sampleDB.execSQL("DELETE FROM "+MemoTableName+" WHERE title = '"+originTitle+"'");
                    sampleDB.execSQL("INSERT INTO " + MemoTableName + " (date, title, document) Values ('" + memoData.getDate() + "','" + memoData.getTitle() +
                            "','" + memoData.getDocument() + "');");
                } catch (SQLiteException se) {
                    if (se.getMessage().toString().contains("UNIQUE"))
                        TitleErrorCheck = true;
                }
                sampleDB.close();
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
