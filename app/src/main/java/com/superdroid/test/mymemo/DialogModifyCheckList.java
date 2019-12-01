package com.superdroid.test.mymemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DialogModifyCheckList extends Activity {

    String preTitle="";
    private final String dbName = "MyMemoDB";
    private final String checkListTableName= "CheckList";
    private final String MemoTableName="Memo";
    String title=null;
    TextView titleText;
    SQLiteDatabase sampleDB = null;
    boolean errorcheck=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_modify_check_list);
        Intent intent=getIntent();
        preTitle=intent.getStringExtra("title");
        titleText=(TextView)findViewById(R.id.titleText);
        setTitle("체크리스트 수정");
    }

    protected void modify(View v){
        String title=titleText.getText().toString();
        if(!title.equals("")) {
            try {
                sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                sampleDB.execSQL("UPDATE " + checkListTableName + " set title = '" + title + "' where title='" + preTitle + "';");
            } catch (SQLiteException se) {
                Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_SHORT).show();
                if (se.getMessage().toString().contains("UNIQUE")) {
                    errorcheck = true;
                }

            }
            sampleDB.close();

            if (errorcheck)
                Toast.makeText(getApplicationContext(), "이름이 중복됩니다. 다른 이름으로 변경하세요.", Toast.LENGTH_SHORT).show();
            else {
                Intent checkIntent = new Intent(this, CheckListActivity.class);
                checkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(checkIntent);
            }
        }else{
            Toast.makeText(getApplicationContext(),"내용이 입력되지 않았습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    protected void exit(View v){
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }
}
