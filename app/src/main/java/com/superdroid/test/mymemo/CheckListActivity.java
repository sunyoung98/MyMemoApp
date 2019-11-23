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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CheckListActivity extends AppCompatActivity {
    private final String dbName = "MyMemoDB";
    private final String checkListTableName= "CheckList";
    private final String MemoTableName="Memo";
    EditText checkListTitle;
    SQLiteDatabase sampleDB = null;
    ArrayList<HashMap<String, String>> checkLists;
    ListAdapter adapter;
    ListView listview;
    private boolean TitleErrorCheck=false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_check, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.action_memo){
            Intent memoIntent=new Intent(this, MainActivity.class);
            memoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(memoIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("My CheckList");
        TitleErrorCheck=false;
        checkLists = new ArrayList<>();
        listview = (ListView) findViewById(R.id.checkListListView);
        sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        try{
            sampleDB=this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + checkListTableName
                    + " (done VARCHAR(20), title VARCHAR(100), date VARCHAR(20),PRIMARY KEY(title) );");
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + MemoTableName
                    + " (date VARCHAR(20), title VARCHAR(100), document VARCHAR(2000),PRIMARY KEY(title) );");
        }catch(SQLiteException se){
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());
        }
        showList();
    }
    protected void updateDatabase(String title, String done){
        try{
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
            ReadDB.execSQL("update "+checkListTableName+" set done = '"+done+"' where title = '"+title+"';");
            ReadDB.close();
        }catch(SQLiteException se){
            Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    protected void showList(){
        try{
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName,MODE_PRIVATE,null);
            Cursor c= ReadDB.rawQuery("SELECT * FROM "+checkListTableName,null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        String done = c.getString(c.getColumnIndex("done"));
                        String title = c.getString(c.getColumnIndex("title"));
                        String date = c.getString(c.getColumnIndex("date"));
                        HashMap<String, String> c1= new HashMap<>();
                        c1.put("done",done);
                        c1.put("title", title);
                        checkLists.add(c1);
                    }while(c.moveToNext());
                }
            }
            ReadDB.close();
            adapter = new SimpleAdapter(this, checkLists, R.layout.checklist_list_item,
                    new String[]{"done","title"},
                    new int[]{R.id.checkListIsDone, R.id.checkListTitle}){
                @Override
                public View getView (int position, View convertView, ViewGroup parent)
                {
                    View v = super.getView(position, convertView, parent);
                    final Button clickedBtn=(Button)v.findViewById(R.id.checkListIsDone);
                    final TextView clickedtextView=(TextView)v.findViewById(R.id.checkListTitle);
                    clickedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(clickedBtn.getText().toString().equals("미완료")) {
                                clickedBtn.setText("완료");
                                updateDatabase(clickedtextView.getText().toString(), clickedBtn.getText().toString());
                            }
                            else if(clickedBtn.getText().toString().equals("완료")) {
                                clickedBtn.setText("미완료");
                                updateDatabase(clickedtextView.getText().toString(), clickedBtn.getText().toString());
                            }

                        }
                    });
                    return v;
                }
            };
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

    public void addCheckList(View v){
        checkListTitle=findViewById(R.id.checkListTitle);
        if(checkListTitle.getText().toString()!=""){
            CheckList checkList=new CheckList(checkListTitle.getText().toString(), new Date(System.currentTimeMillis()));
            try{
                sampleDB.execSQL("INSERT INTO "+checkListTableName+" (done, title, date) Values ('"+checkList.isDone()+"','"+checkList.getTitle()+
                        "','"+checkList.getDate()+"');");
            }catch(SQLiteException se){
                if(se.getMessage().toString().contains("UNIQUE"))
                    TitleErrorCheck=true;
            }
            if(!TitleErrorCheck) {
                finish();
                startActivity(new Intent(this, CheckListActivity.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"제목이 중복됩니다. 다른 제목으로 선택하세요",Toast.LENGTH_SHORT).show();
                TitleErrorCheck=false;
            }
        }
    }
}
