package io.ipoli.android.challenge.data;

import android.text.TextUtils;

import java.util.Date;

import io.ipoli.android.app.net.RemoteObject;
import io.ipoli.android.app.utils.DateUtils;
import io.ipoli.android.quest.QuestContext;
import io.ipoli.android.quest.data.Quest;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 5/27/16.
 */
public class Challenge extends RealmObject implements RemoteObject<Challenge> {
    @Required
    @PrimaryKey
    private String id;

    @Required
    private String name;

    private String context;

    private String reason;

    private Integer difficulty;

    private Date dueDate;

    @Required
    private Date createdAt;

    @Required
    private Date updatedAt;

    private boolean needsSyncWithRemote;
    private boolean isRemoteObject;

    public Challenge() {
    }

    public Challenge(String name) {
        this.name = name;
        this.context = QuestContext.PERSONAL.name();
        createdAt = DateUtils.nowUTC();
        updatedAt = DateUtils.nowUTC();
        needsSyncWithRemote = true;
        isRemoteObject = false;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void markUpdated() {
        setNeedsSync();
        setUpdatedAt(DateUtils.nowUTC());
    }

    @Override
    public void setNeedsSync() {
        needsSyncWithRemote = true;
    }

    @Override
    public boolean needsSyncWithRemote() {
        return needsSyncWithRemote;
    }

    @Override
    public void setSyncedWithRemote() {
        needsSyncWithRemote = false;
    }

    @Override
    public void setRemoteObject() {
        isRemoteObject = true;
    }

    @Override
    public boolean isRemoteObject() {
        return isRemoteObject;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = DateUtils.getDate(dueDate);;
    }

    public String getContext() {
        return TextUtils.isEmpty(context) ? QuestContext.PERSONAL.name() : context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public static QuestContext getContext(Quest quest) {
        return QuestContext.valueOf(quest.getContext());
    }

    public static void setContext(Challenge challenge, QuestContext context) {
        challenge.setContext(context.name());
    }
}