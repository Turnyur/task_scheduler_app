package db;

/**
 * Created by Turnyur (Umah Chukwudi Williams) on 13/04/2017.
 */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "task_scheduler";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_SUBJECT = "subject";
        public static final String COL_TASK_BODY = "body";
        public static final String COL_TASK_DATE = "date";
        public static final String COL_TASK_TIME = "time";
    }
}

