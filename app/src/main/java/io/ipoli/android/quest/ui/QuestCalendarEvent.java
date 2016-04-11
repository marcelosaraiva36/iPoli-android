package io.ipoli.android.quest.ui;

import android.text.TextUtils;

import io.ipoli.android.Constants;
import io.ipoli.android.app.ui.calendar.CalendarEvent;
import io.ipoli.android.quest.data.Quest;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 2/17/16.
 */
public class QuestCalendarEvent implements CalendarEvent {

    private final String name;
    private final int duration;
    private final int backgroundColor;
    private final Quest quest;
    private int startTime;

    public QuestCalendarEvent(Quest quest) {
        this.quest = quest;
        this.name = quest.getName();
        this.duration = Math.max(Constants.QUEST_CALENDAR_EVENT_MIN_DURATION, quest.getDuration());
        this.backgroundColor = Quest.getContext(quest).backgroundColor;
        this.startTime = quest.getStartMinute();
    }

    @Override
    public int getStartMinute() {
        return startTime;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setStartMinute(int startMinute) {
        this.startTime = startMinute;
    }

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public String getName() {
        return name;
    }

    public Quest getQuest() {
        return quest;
    }

    public boolean isRecurrent() {
        return quest.getRecurrentQuest() != null && !TextUtils.isEmpty(quest.getRecurrentQuest().getRecurrence().getRrule());
    }
}
