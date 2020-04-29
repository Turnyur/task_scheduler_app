package notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;

import db.TaskContract;

public class OnAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received wake up from alarm manager.");

        String rowTitle = intent.getExtras().getString(TaskContract.TaskEntry.COL_TASK_TITLE);
        long rowId = intent.getExtras().getLong("mId");

        WakeReminderIntentService.acquireStaticLock(context);

        Intent i = new Intent(context, ReminderService.class);
        i.putExtra(TaskContract.TaskEntry.COL_TASK_TITLE, rowTitle);
        i.putExtra("mId", rowId);

        context.startService(i);

    }
}
