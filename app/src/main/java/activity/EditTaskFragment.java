package activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import com.turnyur.taskscheduler.R;
import db.TaskContract;
import db.TaskDbHelper;
import notify.ReminderManager;


public class EditTaskFragment extends Fragment {
    private String title_from_bundle;
    private TaskDbHelper mHelper;
    private TextInputLayout e_view_title;
    private TextInputLayout e_view_subj, e_view_n_body;
    Button edit_date, edit_time;
    FloatingActionButton fab_edited_save, fab_cancel_edit;
    private int mYear, mHour, mMonth, mDay, mMinute;
    private int globe_year, globe_month, globe_day;
    private int globe_hour, globe_minute;
    private String globe_title;
    private Calendar calender;


    public static String[] month_name = {
            "Jan", "Feb",
            "Mar", "Apr",
            "May", "Jun",
            "Jul", "Aug",
            "Sep", "Oct",
            "Nov", "Dec"
    };
    private String reslt = "";

    public EditTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // id_from_bundle = Integer.parseInt(bundle.getString("Subject"));
            title_from_bundle = (String) bundle.get("title");

        }
        mHelper = new TaskDbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.edit_task_fragment, container, false);
        e_view_title = (TextInputLayout) rootView.findViewById(R.id.edit_title);
        e_view_subj = (TextInputLayout) rootView.findViewById(R.id.edit_subj);
        e_view_n_body = (TextInputLayout) rootView.findViewById(R.id.edit_n_body);
        edit_date = (Button) rootView.findViewById(R.id.edit_rem_date);
        edit_time = (Button) rootView.findViewById(R.id.edit_rem_time);
        fab_edited_save = (FloatingActionButton) rootView.findViewById(R.id.edited_task_save);
        fab_cancel_edit = (FloatingActionButton) rootView.findViewById(R.id.cancel_edit_task);

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                globe_year = year;
                                globe_month = monthOfYear;
                                globe_day = dayOfMonth;
                                edit_date.setText(month_name[monthOfYear] + "-" + dayOfMonth + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        edit_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String day_night;
                                if (hourOfDay > 12) {
                                    day_night = "PM";
                                } else if (hourOfDay == 12) {
                                    day_night = "PM";
                                    // hourOfDay=0;
                                } else day_night = "AM";


                                edit_time.setText(processTime(hourOfDay) + ":" + minute + " " + day_night);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        fab_edited_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender=Calendar.getInstance();
                globe_title = e_view_title.getEditText().getText().toString(); //Used in assigning Global Title accessible by all
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, e_view_title.getEditText().getText().toString());
                values.put(TaskContract.TaskEntry.COL_TASK_SUBJECT, e_view_subj.getEditText().getText().toString());
                values.put(TaskContract.TaskEntry.COL_TASK_BODY, e_view_title.getEditText().getText().toString());
                values.put(TaskContract.TaskEntry.COL_TASK_DATE, edit_date.getText().toString());
                values.put(TaskContract.TaskEntry.COL_TASK_TIME, edit_time.getText().toString());

                // updating row
               long rowReId= db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                        new String[]{String.valueOf(title_from_bundle)});
                Toast.makeText(getActivity(), "Task Successfully Updated", Toast.LENGTH_SHORT).show();

                calender.set(Calendar.MONTH, globe_month);
                calender.set(Calendar.YEAR, globe_year);
                calender.set(Calendar.DAY_OF_MONTH, globe_day);

                calender.set(Calendar.HOUR_OF_DAY, globe_hour);
                calender.set(Calendar.MINUTE, globe_minute);
                calender.set(Calendar.SECOND, 0);
                //   calender.set(Calendar.AM_PM,Calendar.AM);

               new ReminderManager(getActivity()).setReminder(globe_title,rowReId, calender);


                Fragment fragment = new HomeFragment();
                replaceExistingFragment("Scheduled Tasks", fragment);
            }
        });
        fab_cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Operation Cancelled", Toast.LENGTH_SHORT).show();
                Fragment fragment = new HomeFragment();
                replaceExistingFragment("Scheduled Tasks", fragment);
            }
        });

//e_view_title.getEditText().setText(title_from_bundle);

        readOneDataFromDB(e_view_title.getEditText(), e_view_subj.getEditText(),
                edit_date, edit_time, e_view_n_body.getEditText());

        // Inflate the layout for this fragment
        return rootView;
    }


    private void replaceExistingFragment(String title, Fragment fragment) {
        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            //Custorm Fragment switching animation was implemented here
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.slide_down, R.anim.slide_up);
            fragmentTransaction.replace(R.id.container_body, fragment)
                    .addToBackStack(title).commit();
            //This was used to remotely change MainActivity Toolbar title
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.getSupportActionBar().setTitle(title);
        }
    }

    private String processTime(int m) {
// This function was used in converting the gotten time into readable format we can show to an average person....
        if (m <= 12) {
            switch (m) {
                case 0:
                    reslt = "00";
                case 1:
                    transformHour(m);
                case 2:
                    transformHour(m);
                case 3:
                    transformHour(m);
                case 4:
                    transformHour(m);
                case 5:
                    transformHour(m);
                case 6:
                    transformHour(m);
                case 7:
                    transformHour(m);
                case 8:
                    transformHour(m);
                case 9:
                    transformHour(m);

                default:
                    reslt = m + "";
            }
        } else {
            switch (m) {
                case 13:
                    m = 1;
                    break;
                case 14:
                    m = 2;
                    break;
                case 15:
                    m = 3;
                    break;
                case 16:
                    m = 4;
                    break;
                case 17:
                    m = 5;
                    break;
                case 18:
                    m = 6;
                    break;
                case 19:
                    m = 7;
                    break;
                case 20:
                    m = 8;
                    break;
                case 21:
                    m = 9;
                    break;
                case 22:
                    m = 10;
                    break;
                case 23:
                    m = 11;
                    break;

                default:
                    reslt = m + "";
            }


            if (m == 24) {
                reslt = "00";
            } else reslt = m + "";
        }


        return reslt;
    }

    private void transformHour(int m) {
        reslt = m + "";
        reslt = "0" + reslt;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void readOneDataFromDB(EditText title_view, EditText subj_view, Button date_view, Button time_view, EditText n_body_view) {
        if (!(title_from_bundle.equals("Task Scheduler 1.0"))) {
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

            db.close();
        }
    }
}
