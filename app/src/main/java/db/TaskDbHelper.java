package db;

/**
 * Created by Turnyur (Umah Chukwudi Williams) on 13/04/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL,"+
                TaskContract.TaskEntry.COL_TASK_SUBJECT + " TEXT NOT NULL,"+
                TaskContract.TaskEntry.COL_TASK_BODY + " TEXT NOT NULL,"+
                TaskContract.TaskEntry.COL_TASK_DATE + " TEXT NOT NULL,"+
                TaskContract.TaskEntry.COL_TASK_TIME + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}