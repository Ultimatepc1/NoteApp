package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

public class Texteditoractivity extends AppCompatActivity {
    EditText currentnote;
    Button addbutton;
    Intent frompage;
    MenuItem pageswitcher;
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.addanote:
                if(pageswitcher.getTitle().toString().equals("Cancel Adding")) {
                    pageswitcher.setTitle("Add a note");
                    finish();
                    break;
                }
                else if(pageswitcher.getTitle().toString().equals("Cancel Editing")) {
                    pageswitcher.setTitle("Add a note");
                    finish();
                    break;
                }
                return true;
            default:
                Toast.makeText(this, "Cannot Select", Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        pageswitcher=(MenuItem)menu.getItem(0);
        if(frompage.getIntExtra("notevalue",0)==-1){
            pageswitcher.setTitle("Cancel Adding");
        }else{
            pageswitcher.setTitle("Cancel Editing");
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texteditoractivity);
        currentnote=(EditText)findViewById(R.id.currentnote);
        addbutton=(Button)findViewById(R.id.add);
        frompage=getIntent();
        if(frompage.getIntExtra("notevalue",0)==-1){
            addbutton.setText("Add note");
            currentnote.setText("");
            currentnote.setHint("Add your note here");
        }else{
            addbutton.setText("Edit note");
            currentnote.setText(MainActivity.notes.get(frompage.getIntExtra("notevalue",0)));
            currentnote.setHint("Edit your note here");
        }
        //his code for edittext change
        /*currentnote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(frompage.getIntExtra("notevalue",0),String.valueOf(charSequence));
                try{
                    MainActivity.notestore.edit().putString("notes",ObjectSerializer.serialize((Serializable) MainActivity.notes)).apply();
                    MainActivity.noteadapter.notifyDataSetChanged();
                }catch (Exception e){
                    Log.i("Edit Note  error",""+e);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
         */
    }
    public void edithandler(View view){
        if(frompage.getIntExtra("notevalue",0)==-1){
            MainActivity.notes.add(String.valueOf(currentnote.getText()));
            try{
                MainActivity.notestore.edit().putString("notes",ObjectSerializer.serialize((Serializable) MainActivity.notes)).apply();
                MainActivity.noteadapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Successfully Added", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
            }catch (Exception e){
                Log.i("Add Note  error",""+e);
                Toast.makeText(this, "Something went wrong while adding notes", Toast.LENGTH_SHORT).show();
            }
        }else{
            MainActivity.notes.set(frompage.getIntExtra("notevalue",0),String.valueOf(currentnote.getText()));
            try{
                MainActivity.notestore.edit().putString("notes",ObjectSerializer.serialize((Serializable) MainActivity.notes)).apply();
                MainActivity.noteadapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Successfully Edited", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
            }catch (Exception e){
                Log.i("Edit Note  error",""+e);
                Toast.makeText(this, "Something went wrong while editing notes", Toast.LENGTH_SHORT).show();
            }
        }
    }
}