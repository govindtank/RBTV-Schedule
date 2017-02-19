package de.metzgore.rbtvschedule.weeklyschedule;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.format.DateUtils;

import java.util.Date;

import de.metzgore.rbtvschedule.dailyschedule.DailyScheduleFragment;
import de.metzgore.rbtvschedule.data.Schedule;

import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;

public class WeeklyScheduleAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Schedule mSchedule;

    public WeeklyScheduleAdapter(Context context, FragmentManager mgr) {
        super(mgr);
        mContext = context;
        mSchedule = new Schedule();
    }

    @Override
    public int getCount() {
        return mSchedule.getWeeklySchedule().size();
    }

    @Override
    public Fragment getItem(int position) {
        Date selectedDate = (Date) mSchedule.getWeeklySchedule().keySet().toArray()[position];
        return DailyScheduleFragment.newInstance(mSchedule.getWeeklySchedule().get(selectedDate));
    }

    @Override
    public String getPageTitle(int position) {
        Date dateOfSchedule = (Date) mSchedule.getWeeklySchedule().keySet().toArray()[position];
        return DateUtils.formatDateTime(mContext, dateOfSchedule.getTime(), FORMAT_SHOW_WEEKDAY|FORMAT_SHOW_DATE);
    }

    public void setSchedule(Schedule schedule) {
        mSchedule = schedule;
        notifyDataSetChanged();
    }
}