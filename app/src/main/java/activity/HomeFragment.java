package activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.MyRecyclerViewAdapter;
import com.turnyur.taskscheduler.R;
import db.TaskContract;
import db.TaskDbHelper;
import model.DataObject;


public class HomeFragment extends Fragment {
    private static TaskDbHelper mHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";

   public static String[] month_name = {
            "January", "February",
            "March", "April",
            "May", "June",
            "July", "August",
            "September", "October",
            "November", "December"
    };
    public static long task_due_bundle=-1;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHelper = new TaskDbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mRecyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(HomeFragment.getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        };
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Toast.makeText(getActivity(), "Card View clicked!", Toast.LENGTH_SHORT);
            }

            @Override
            public void onDrawerItemSelected(View view, int position) {
                TextView title_v = (TextView) view.findViewById(R.id.task_title);
                String bundled_title = title_v.getText().toString();

                displayView(position, bundled_title);
            }

        });
    }


    private void displayView(int position, String mstring) {

        String title = "Scheduled Task";
        Fragment fragment = new ScheduledTasks();
        replaceExistingFragment(mstring, title, fragment);
    }

    private void replaceExistingFragment(String mstring, String title, Fragment fragment) {
        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            //Custorm Fragment switching animation was implemented here
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.flip, R.anim.slide_down, R.anim.slide_up);
            fragmentTransaction.replace(R.id.container_body, fragment)
                    .addToBackStack(title).commit();
            //This was used to pass message to the called fragment
            Bundle bundle = new Bundle();
            bundle.putString("title",/* position*/ mstring);
            fragment.setArguments(bundle);
            //This was used to remotely change MainActivity Toolbar title
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.getSupportActionBar().setTitle(title);
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


    private static ArrayList<DataObject> getDataSet() {
        //Communication with the SQLite database was done with the code below
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COL_TASK_SUBJECT,
                        TaskContract.TaskEntry.COL_TASK_BODY,
                        TaskContract.TaskEntry.COL_TASK_DATE,
                        TaskContract.TaskEntry.COL_TASK_TIME
                },
                null, null, null, null, null);
        //Date instance obtained
        final Calendar d_c = Calendar.getInstance();
        int mYear = d_c.get(Calendar.YEAR);
        int mMonth = d_c.get(Calendar.MONTH);
        int mDay = d_c.get(Calendar.DAY_OF_MONTH);
//Time Instance obtained
        final Calendar t_c = Calendar.getInstance();
        int mHour = t_c.get(Calendar.HOUR_OF_DAY);
        int mMinute = t_c.get(Calendar.MINUTE);


        //Dummy Text was used to populate the recyclerview atleast to show one list in the HomeFragment recyclerview
        String Title = "Task Scheduler 1.0";
        String Subject = "Welcome to Task Scheduler";
        String Body = "You have no Task Scheduled yet";
        String Date = mDay + "-" + month_name[mMonth] + "-" + mYear;
        String Time = mHour + ":" + mMinute;

        ArrayList results = new ArrayList<DataObject>();
        if ((cursor.getCount() < 1)) {
            for (int index = 0; index < 1; index++) {
                DataObject obj = new DataObject(Title, Subject, Body, "Today's Date: " + Date,
                        "Current Time: " + Time, index);
                results.add(index, obj);
            }

        } else {

            while (cursor.moveToNext()) {
                int data_id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String subj = cursor.getString(2);
                String body = cursor.getString(3);
                String date = cursor.getString(4);
                String time = cursor.getString(5);
                DataObject obj2 = new DataObject(title, subj, body, date, time, data_id);
                results.add(obj2);
            }
        }
        return results;
    }
}