package com.superdroid.test.mymemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        titleTextView=(TextView)findViewById(R.id.MemoTitle);
        docTextView=(TextView)findViewById(R.id.MemoDocument);
        timeTextView=(TextView)findViewById(R.id.MemoTime);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String doc=intent.getStringExtra("doc");
        titleTextView.setText(title);
        docTextView.setText(doc);
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
        sampleDB.close();
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void backBtnClick(View v){
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_readmemo, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        if(id==R.id.action_modify){
            String title=titleTextView.getText().toString();
            String doc=docTextView.getText().toString();
            Intent intent= new Intent(this, ModifyMemoActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("doc", doc);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(id==R.id.action_delete){
            String title=titleTextView.getText().toString();
            try {
                sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                sampleDB.execSQL("DELETE FROM "+MemoTableName+" WHERE title = '"+title+"'");
            }catch(SQLiteException se){
                Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("", se.getMessage());
            }
            sampleDB.close();
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

}
