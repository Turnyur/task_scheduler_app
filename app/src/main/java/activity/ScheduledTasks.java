package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.turnyur.taskscheduler.R;
import db.TaskContract;
import db.TaskDbHelper;

/**
 * Created by Turnyur (Umah Chukwudi Williams) on 12/04/2017.
 */

public class ScheduledTasks extends Fragment {
    int id_from_bundle;
    private TaskDbHelper mHelper;
    private String title_from_bundle;

    public ScheduledTasks() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This was used in getting the bundles passed from incoming/calling Fragments
        Bundle bundle = this.getArguments();
        if (bundle != null) {
           // id_from_bundle = Integer.parseInt(bundle.getString("Subject"));
            title_from_bundle= (String) bundle.get("title");

        }
        mHelper = new TaskDbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scheduled_task_fragment, container, false);
       final TextView title_view = (TextView) rootView.findViewById(R.id.task_view_title);
        TextView subj_view = (TextView) rootView.findViewById(R.id.real_t_subject);
        TextView date_view = (TextView) rootView.findViewById(R.id.real_due_date);
        final TextView time_view = (TextView) rootView.findViewById(R.id.real_due_time);
        TextView n_body_view = (TextView) rootView.findViewById(R.id.real_note_body);
        FloatingActionButton delete_btn= (FloatingActionButton) rootView.findViewById(R.id.delete_task);
        FloatingActionButton edit_btn= (FloatingActionButton) rootView.findViewById(R.id.edit_task);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new EditTaskFragment();

                Bundle bundle=new Bundle();
                bundle.putString("title",title_view.getText().toString());
               fragment.setArguments(bundle);
                replaceExistingFragment("Edit Task",fragment);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog=new AlertDialog.Builder(getActivity()).setTitle("Delete Task").
                        setMessage("Do you want to delete Task?").
                setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                                new String[] { String.valueOf(title_from_bundle) });
                        db.close();
                        Toast.makeText(getActivity(),"Task Deleted Successfully",Toast.LENGTH_SHORT).show();
                        String title = "Scheduled Task";
                        Fragment fragment = new HomeFragment();
                        replaceExistingFragment( title, fragment);


                    }
                }).setNegativeButton("Cancel",null).create();
                dialog.show();


            }
        });



        //Database Activities were done here
        readOneDataFromDB(title_view, subj_view, date_view, time_view, n_body_view);


        // Inflate the layout for this fragment
        return rootView;
    }
    private void replaceExistingFragment( String title, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            //Custorm Fragment switching animation was implemented here
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.container_body, fragment)
                    .addToBackStack(title).commit();
            //This was used to remotely change MainActivity Toolbar title
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.getSupportActionBar().setTitle(title);
        }
    }
    private void readOneDataFromDB(TextView title_view, TextView subj_view,
                                   TextView date_view, TextView time_view, TextView n_body_view) {
        if(!(title_from_bundle.equals("Task Scheduler 1.0"))) {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            // Getting single DataObject

            Cursor cursor = db.query(TaskContract.TaskEntry.TABLE, new String[]{TaskContract.TaskEntry._ID,
                            TaskContract.TaskEntry.COL_TASK_TITLE,
                            TaskContract.TaskEntry.COL_TASK_SUBJECT,
                            TaskContract.TaskEntry.COL_TASK_BODY,
                            TaskContract.TaskEntry.COL_TASK_DATE,
                            TaskContract.TaskEntry.COL_TASK_TIME
                    }, TaskContract.TaskEntry.COL_TASK_TITLE + "=?",
                    new String[]{String.valueOf(title_from_bundle)
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
