package io.ipoli.android.challenge.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.joda.time.LocalDate;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.ipoli.android.Constants;
import io.ipoli.android.app.App;
import io.ipoli.android.app.utils.IntentUtils;
import io.ipoli.android.app.utils.LocalStorage;
import io.ipoli.android.app.utils.Time;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 2/1/16.
 */
public class ScheduleDailyChallengeReminderReceiver extends BroadcastReceiver {

    public static final String ACTION_SCHEDULE_DAILY_CHALLENGE_REMINDER = "io.ipoli.android.intent.action.SCHEDULE_DAILY_CHALLENGE_REMINDER";

    @Inject
    LocalStorage localStorage;

    @Override
    public void onReceive(Context context, Intent intent) {
        App.getAppComponent(context).inject(this);
        PendingIntent repeatingIntent = IntentUtils.getBroadcastPendingIntent(context, getDailyChallengeReminderIntent());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int startMinute = localStorage.readInt(Constants.KEY_DAILY_CHALLENGE_REMINDER_START_MINUTE, Constants.DEFAULT_DAILY_CHALLENGE_REMINDER_START_MINUTE);
        long firstTriggerMillis = LocalDate.now().toDateTimeAtStartOfDay().getMillis() + Time.of(startMinute).toMillisAfterMidnight();
        if (timeIsInThePast(firstTriggerMillis)) {
            firstTriggerMillis += TimeUnit.DAYS.toMillis(1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstTriggerMillis,
                AlarmManager.INTERVAL_DAY, repeatingIntent);
    }

    private boolean timeIsInThePast(long firstTriggerMillis) {
        return firstTriggerMillis < System.currentTimeMillis();
    }

    private Intent getDailyChallengeReminderIntent() {
        return new Intent(DailyChallengeReminderReceiver.ACTION_SHOW_DAILY_CHALLENGE_REMINDER);
    }
}
