package activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import com.turnyur.taskscheduler.R;
import db.TaskContract;
import db.TaskDbHelper;
import notify.ReminderManager;

public class TasksFragment extends Fragment {

    public static final String TAG = "Added Successfully";
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button rem_date_btn, rem_time_btn;
    private int mHour;
    private int mMinute;
    private FloatingActionButton fab_task_save_btn;
    private TextInputLayout inTitle, inSubject, inBody;
    private TaskDbHelper mHelper;
    String reslt = "";
    public static String[] month_name = {
            "Jan", "Feb",
            "Mar", "Apr",
            "May", "Jun",
            "Jul", "Aug",
            "Sep", "Oct",
            "Nov", "Dec"
    };
    private int globe_year, globe_month,globe_day;
    private int globe_hour,globe_minute;
    private String globe_title;
    private Calendar calender;




    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new TaskDbHelper(getActivity());
        // Create a new service client and bind our activity to this service

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        //Getting veach view were are going to change at runtime
        fab_task_save_btn = (FloatingActionButton) rootView.findViewById(R.id.add_task_fab);
        inTitle = (TextInputLayout) rootView.findViewById(R.id.t_title);
        inSubject = (TextInputLayout) rootView.findViewById(R.id.t_subject);
        inBody = (TextInputLayout) rootView.findViewById(R.id.t_body);
        rem_date_btn = (Button) rootView.findViewById(R.id.rem_date);
        rem_time_btn = (Button) rootView.findViewById(R.id.rem_time);


        //Setting listeners
        rem_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                globe_year=year;
                                globe_month=monthOfYear;
                                globe_day=dayOfMonth;
                                rem_date_btn.setText(month_name[monthOfYear] + "-" + dayOfMonth + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        rem_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                calender=c;
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                globe_hour=hourOfDay;
                                globe_minute=minute;
                                String day_night;
//This Code was used in fixing time to AM or PM
                                if (hourOfDay > 12) {
                                    day_night = "PM";
                                } else if (hourOfDay == 12) {
                                    day_night = "PM";
                                    // hourOfDay=0;
                                } else day_night = "AM";

                                rem_time_btn.setText(processTime(hourOfDay) + ":" + minute + " " + day_night);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        fab_task_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//Processing of Data before insertion into database
                String loc_inTitle = String.valueOf(inTitle.getEditText().getText());
               globe_title=loc_inTitle; //Used in assigning Global Title accessible by all
                String loc_inSubject = String.valueOf(inSubject.getEditText().getText());
                String loc_inBody = String.valueOf(inBody.getEditText().getText());
                String loc_inDate = String.valueOf(rem_date_btn.getText());
                String loc_inTime = String.valueOf(rem_time_btn.getText());
                if (!(loc_inTitle.equals("") || loc_inSubject.equals("") || loc_inBody.equals(""))) {
                    Toast.makeText(getActivity(), "Task:" + inTitle.getEditText().getText() +
                            " Added Succesfully", Toast.LENGTH_SHORT).show();

                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TaskContract.TaskEntry.COL_TASK_TITLE, loc_inTitle);
                    values.put(TaskContract.TaskEntry.COL_TASK_SUBJECT, loc_inSubject);
                    values.put(TaskContract.TaskEntry.COL_TASK_BODY, loc_inBody);
                    values.put(TaskContract.TaskEntry.COL_TASK_DATE, loc_inDate);
                    values.put(TaskContract.TaskEntry.COL_TASK_TIME, loc_inTime);

               long rowReID=  db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    Log.d(TAG, "TASK TO ADD" + loc_inBody);
                    db.close();















                    //Setting Up the notification segment of the code
                    //This includes the AlarmManager class




                    // Create a new calendar set to the date chosen
                    // we set the time to midnight (i.e. the first minute of that day)

                  //  Calendar calendar = Calendar.getInstance();

                    calender.set(Calendar.MONTH, globe_month);
                    calender.set(Calendar.YEAR, globe_year);
                    calender.set(Calendar.DAY_OF_MONTH, globe_day);

                    calender.set(Calendar.HOUR_OF_DAY, globe_hour);
                    calender.set(Calendar.MINUTE, globe_minute);
                    calender.set(Calendar.SECOND, 0);
                 //   calender.set(Calendar.AM_PM,Calendar.AM);

                    new ReminderManager(getActivity()).setReminder(globe_title,rowReID,calender);








                    //Transfer to HomeFragment, so as to view newly inserted data
                    Fragment fragment = new HomeFragment();
                    if (fragment != null) {

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        //Custorm Fragment switching animation was implemented here
                        fragmentTransaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit, R.anim.enter, R.anim.exit);
                        fragmentTransaction.replace(R.id.container_body, fragment)
                                .addToBackStack("Scheduled Tasks").commit();

                        //This was used to remotely change MainActivity Toolbar title
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.getSupportActionBar().setTitle("Scheduled Tasks");
                    }

                } else {
                    Toast.makeText(getActivity(), "Some fields are Empty", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {

        super.onDetach();
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

}