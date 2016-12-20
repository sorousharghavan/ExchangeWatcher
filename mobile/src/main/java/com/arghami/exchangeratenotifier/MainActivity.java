package com.arghami.exchangeratenotifier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final SQLManager sqlManager = new SQLManager(MainActivity.this);
        try{sqlManager.createDB();}catch (Exception e){e.printStackTrace();}
        final EditText sym = (EditText) findViewById(R.id.sym);
        String smbl = sqlManager.readSym();
        if (smbl != null && !smbl.equals("Error")){
            sym.setText(smbl);
        }
        final Button sve = (Button) findViewById(R.id.btn);
        sve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sqlManager.updateSymb(sym.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                startService(new Intent(MainActivity.this, ExchangeService.class));
                Toast.makeText(MainActivity.this, "Watcher started.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
