package com.turnyur.taskscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import activity.MainActivity;
import db.TaskContract;
import db.TaskDbHelper;

public class ViewDueTask extends AppCompatActivity {



    TextView title_view;
    TextView subj_view;
    TextView date_view;
    TextView time_view;
    TextView n_body_view;
    FloatingActionButton delete_btn;
    FloatingActionButton edit_btn;
    String entry_title;
    private TaskDbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_due_task);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);


        mHelper = new TaskDbHelper(this);
        title_view = (TextView) findViewById(R.id.task_view_title);
        subj_view = (TextView)findViewById(R.id.real_t_subject);
        date_view = (TextView) findViewById(R.id.real_due_date);
        time_view = (TextView)findViewById(R.id.real_due_time);
        n_body_view = (TextView)findViewById(R.id.real_note_body);
        delete_btn= (FloatingActionButton)findViewById(R.id.delete_task);
        edit_btn= (FloatingActionButton)findViewById(R.id.edit_task);
        entry_title= getIntent().getExtras().getString("title");
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog=new AlertDialog.Builder(ViewDueTask.this).setTitle(entry_title).
                        setMessage("Task automatically detected to be due today, "+date_view.getText()
                                +" at " + time_view.getText()).
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                                        new String[] { String.valueOf(entry_title) });
                                db.close();
                                Toast.makeText(ViewDueTask.this,"Task Deleted",Toast.LENGTH_SHORT).show();
                               Intent i=new Intent(ViewDueTask.this, MainActivity.class);
                                startActivity(i);


                            }
                        }).setNegativeButton("Cancel",null).create();
                dialog.show();


            }
        });

        //Database Activities were done here
        readOneDataFromDB(title_view, subj_view, date_view, time_view, n_body_view);

        toolbar.setTitle("Task: "+entry_title);
        setSupportActionBar(toolbar);

    }



    private void readOneDataFromDB(TextView title_view, TextView subj_view,
                                   TextView date_view, TextView time_view, TextView n_body_view) {
        
            SQLiteDatabase db = mHelper.getReadableDatabase();
            // Getting single DataObject

            Cursor cursor = db.query(TaskContract.TaskEntry.TABLE, new String[]{TaskContract.TaskEntry._ID,
                            TaskContract.TaskEntry.COL_TASK_TITLE,
                            TaskContract.TaskEntry.COL_TASK_SUBJECT,
                            TaskContract.TaskEntry.COL_TASK_BODY,
                            TaskContract.TaskEntry.COL_TASK_DATE,
                            TaskContract.TaskEntry.COL_TASK_TIME
                    }, TaskContract.TaskEntry.COL_TASK_TITLE + "=?",
                    new String[]{String.valueOf(entry_title)
                                /* 1 was added just to correct the position gotten from bundle*/},
                    null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();
            title_view.setText(cursor.getString(1));
            subj_view.setText(cursor.getString(2));
            n_body_view.setText(cursor.getString(3));
            date_view.setText(cursor.getString(4));
            time_view.setText(cursor.getString(5));
        
    }
}
