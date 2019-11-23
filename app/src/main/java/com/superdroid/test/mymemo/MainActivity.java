package com.superdroid.test.mymemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String dbName = "MyMemoDB";
    private final String checkListTableName= "CheckList";
    private final String MemoTableName="Memo";
    SQLiteDatabase sampleDB = null;
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> Lists;
    ListView listview;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_memo, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id= item.getItemId();
       if(id==R.id.action_checklist){
            Intent checkIntent=new Intent(this, CheckListActivity.class);
            checkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(checkIntent);
        }
       if(id==R.id.action_add){
           Intent addIntent=new Intent(this, AddMemoActivity.class);
           addIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(addIntent);
       }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("My Memo");
        Lists=new ArrayList<>();
        listview=(ListView)findViewById(R.id.memoListView);
        try{
            sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + checkListTableName
                    + " (done VARCHAR(20), title VARCHAR(100), date VARCHAR(20), PRIMARY KEY(title) );");
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + MemoTableName
                    + " (date VARCHAR(20), title VARCHAR(100), document VARCHAR(2000), PRIMARY KEY(title) );");
        }catch(SQLiteException se){
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
        }

        showList();

    }
    public String returnIndex(String titletemp){
        for(int i=0; i<Lists.size(); i++){
            HashMap<String,String> hashMap = Lists.get(i);
            if(hashMap.get("title")==titletemp)
                return hashMap.get("doc");
        }
        return "";
    }
    public void TitleClick(View v){
        Button btn=(Button)v;
        String titletemp=btn.getText().toString();
        String doctemp=returnIndex(titletemp);
        Intent readIntent=new Intent(this, ReadMemoActivity.class);
        readIntent.putExtra("title",titletemp);
        readIntent.putExtra("doc",doctemp);
        startActivity(readIntent);

    }
    protected void showList(){
        try{
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
            Cursor c= ReadDB.rawQuery("SELECT * FROM "+MemoTableName,null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        String date = c.getString(c.getColumnIndex("date"));
                        String title = c.getString(c.getColumnIndex("title"));
                        String document = c.getString(c.getColumnIndex("document"));
                        HashMap<String, String> c1= new HashMap<>();
                        c1.put("title",title);
                        c1.put("doc", document);
                        Lists.add(c1);
                    }while(c.moveToNext());
                }
            }
            ReadDB.close();
            adapter = new SimpleAdapter(this, Lists, R.layout.memo_list_item,
                    new String[]{"title","doc"},
                    new int[]{R.id.listMemoTitle, R.id.listMemoTitle2});
            listview.setAdapter(adapter);
        } catch(SQLiteException se){
            Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }

}
