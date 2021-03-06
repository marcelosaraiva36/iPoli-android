package io.ipoli.android.quest.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ipoli.android.Constants;
import io.ipoli.android.R;
import io.ipoli.android.app.activities.BaseActivity;
import io.ipoli.android.app.events.CalendarDayChangedEvent;
import io.ipoli.android.app.events.EventSource;
import io.ipoli.android.app.events.ScreenShownEvent;
import io.ipoli.android.app.ui.EmptyStateRecyclerView;
import io.ipoli.android.quest.adapters.AgendaAdapter;
import io.ipoli.android.quest.data.Quest;
import io.ipoli.android.quest.persistence.QuestPersistenceService;
import io.ipoli.android.quest.viewmodels.AgendaViewModel;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 12/30/16.
 */

public class AgendaActivity extends BaseActivity implements CalendarView.OnDateChangeListener {

    @Inject
    QuestPersistenceService questPersistenceService;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.agenda_calendar)
    CalendarView calendar;

    @BindView(R.id.agenda_list_container)
    ViewGroup questListContainer;

    @BindView(R.id.agenda_list)
    EmptyStateRecyclerView questList;

    @BindView(R.id.agenda_journey_text)
    TextView journeyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agenda);
        ButterKnife.bind(this);
        appComponent().inject(this);

        long selectedDate = getIntent().getLongExtra(Constants.CURRENT_SELECTED_DAY_EXTRA_KEY, 0);
        if (selectedDate == 0) {
            finish();
            return;
        }

        eventBus.post(new ScreenShownEvent(EventSource.AGENDA_CALENDAR));

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        questList.setLayoutManager(layoutManager);
        questList.setHasFixedSize(true);

        questList.setEmptyView(questListContainer, R.string.empty_agenda_text, R.drawable.ic_calendar_blank_grey_24dp);

        calendar.setDate(selectedDate, true, true);
        showQuestsForDate(new LocalDate(selectedDate));
        calendar.setOnDateChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onClose();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        onClose();
    }

    private void onClose() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_bottom);
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private void showQuestsForDate(LocalDate date) {
        eventBus.post(new CalendarDayChangedEvent(date, CalendarDayChangedEvent.Source.AGENDA_CALENDAR));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(getToolbarText(date)), Locale.getDefault());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(simpleDateFormat.format(date.toDate()));
        }
        String dayNumberSuffix = getDayNumberSuffix(date.getDayOfMonth());
        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.agenda_daily_journey_format, dayNumberSuffix));
        journeyText.setText(getString(R.string.agenda_daily_journey, dateFormat.format(date.toDate())));
        questPersistenceService.findAllNonAllDayForDate(date, quests -> {
            List<AgendaViewModel> vms = new ArrayList<>();
            for (Quest quest : quests) {
                vms.add(new AgendaViewModel(this, quest));
            }
            questList.setAdapter(new AgendaAdapter(this, eventBus, vms));
        });
    }

    private int getToolbarText(LocalDate date) {
        if (date.isEqual(new LocalDate().minusDays(1))) {
            return R.string.yesterday_calendar_format;
        }
        if (date.isEqual(new LocalDate())) {
            return R.string.today_calendar_format;
        }
        if (date.isEqual(new LocalDate().plusDays(1))) {
            return R.string.tomorrow_calendar_format;
        }
        return R.string.agenda_calendar_format;
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        showQuestsForDate(new LocalDate(year, month + 1, dayOfMonth));
    }
}
