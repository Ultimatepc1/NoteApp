package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences notestore;
    static ListView noteview;
    static ArrayAdapter<String> noteadapter;
    static ArrayList<String> notes;
    static MenuItem pageswitcher;
    Intent editor;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.addanote:
                if(pageswitcher.getTitle().toString().equals("Add a note")) {
                    toeditor(noteview, -1);
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
        pageswitcher.setTitle("Add a note");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notestore=this.getSharedPreferences("com.example.noteapp", Context.MODE_PRIVATE);
        noteview=(ListView)findViewById(R.id.noteview);
        try{
        notes=(ArrayList<String>)ObjectSerializer.deserialize(notestore.getString("notes",ObjectSerializer.serialize((Serializable)new ArrayList<String>())));
            //sharedPreferences.edit().putString("friends",ObjectSerializer.serialize((Serializable) list)).apply();
        }catch (Exception e){
            Log.i("Note retrieveng error",""+e);
            Toast.makeText(this, "Something went wrong while fetching notes", Toast.LENGTH_SHORT).show();
        }
        if(notes.size()==0){
            notes.add("Example note");
            try{
                notestore.edit().putString("notes",ObjectSerializer.serialize((Serializable) notes)).apply();
            }catch (Exception e){
                Log.i("First Note  error",""+e);
                Toast.makeText(this, "Something went wrong while adding notes", Toast.LENGTH_SHORT).show();
            }
        }
        noteadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,notes);
        noteview.setAdapter(noteadapter);
        noteview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toeditor(view,i);
            }
        });
        noteview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertcode(view,i);
                return false;
            }
        });
    }
    public void toeditor(View view,int i){
        editor=new Intent(this,Texteditoractivity.class);
        editor.putExtra("notevalue",i);
        startActivity(editor);
    }
    public int editdataadapter(){
        try{
            notestore.edit().putString("notes",ObjectSerializer.serialize((Serializable) notes)).apply();
            noteadapter.notifyDataSetChanged();
            return 1;
        }catch (Exception e){
            Log.i("Edit Note  error",""+e);
            Toast.makeText(this, "Something went wrong while adding notes", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
    public void alertcode(View view,int i){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Delete Note")
                .setMessage("Do you want to delete"+notes.get(i)+" note")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notes.remove(i);
                        int k=editdataadapter();
                        if(k==1)
                            Toast.makeText(MainActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Deletion Cncelled", Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .show();
    }
}