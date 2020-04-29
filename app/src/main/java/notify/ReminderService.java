package notify;

/**
 * Created by Turnyur Siy on 17/04/2017.
 */

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.turnyur.taskscheduler.R;
import com.turnyur.taskscheduler.ViewDueTask;
import db.TaskContract;

public class ReminderService extends WakeReminderIntentService {

    public ReminderService() {
        super("ReminderService");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    void doReminderWork(Intent intent) {
        Log.d("ReminderService", "Doing work.");
        String rowTitle = intent.getExtras().getString(TaskContract.TaskEntry.COL_TASK_TITLE);
        int rowId=(int)intent.getExtras().getLong("mId");


        Intent i = new Intent(this, ViewDueTask.class);
        i.putExtra(TaskContract.TaskEntry.COL_TASK_TITLE,rowTitle);
        i.putExtra("Db_ID",rowId);

// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, 0);

         NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


         NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this)
                .setContentTitle("Scheduled Task is due").setSmallIcon(R.drawable.ic_tasks).setContentIntent(pIntent)
                .setContentText(rowTitle);
        //add sound
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(uri);
        //vibrate
        long[] v = {500,1000};
        notificationBuilder.setVibrate(v);
        notificationManager.notify(rowId, notificationBuilder.build());



    }
}
