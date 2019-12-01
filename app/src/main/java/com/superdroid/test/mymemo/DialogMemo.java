package com.superdroid.test.mymemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DialogMemo extends Activity {
    private final String dbName = "MyMemoDB";
    private final String checkListTableName= "CheckList";
    private final String MemoTableName="Memo";
    String title=null;
    String doc=null;
    SQLiteDatabase sampleDB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_memo);
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        doc=intent.getStringExtra("doc");
        setTitle(title);

    }
    protected void modify(View v){
        Intent intent=new Intent(this,ModifyMemoActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("doc",doc);
        finish();
        startActivity(intent);
    }
    protected void deleteMemo(View v){
        try{
            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            sampleDB.execSQL("DELETE FROM "+MemoTableName+" WHERE title = '"+title+"'");
        }catch(SQLiteException se){
            Toast.makeText(getApplicationContext(), se.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        Intent checkIntent=new Intent(this, MainActivity.class);
        checkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(checkIntent);

    }
    protected void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }
}
