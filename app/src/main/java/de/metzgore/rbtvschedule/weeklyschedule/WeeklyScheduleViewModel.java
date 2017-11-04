package de.metzgore.rbtvschedule.weeklyschedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import de.metzgore.rbtvschedule.data.Resource;
import de.metzgore.rbtvschedule.data.WeeklySchedule;
import de.metzgore.rbtvschedule.shared.IScheduleViewModel;
import de.metzgore.rbtvschedule.shared.ScheduleRepository;

public class WeeklyScheduleViewModel extends ViewModel implements IScheduleViewModel<WeeklySchedule> {

    private final MutableLiveData<Boolean> refresh = new MutableLiveData<>();
    private final LiveData<Resource<WeeklySchedule>> schedule;

    @Inject
    public WeeklyScheduleViewModel(ScheduleRepository scheduleRepo) {
        schedule = Transformations.switchMap(refresh, forceFromNetwork -> {
            //TODO lambda
            return scheduleRepo.loadWeeklySchedule(forceFromNetwork);
        });
    }

    @Override
    public LiveData<Resource<WeeklySchedule>> getSchedule() {
        return schedule;
    }

    @Override
    public void loadSchedule(boolean forceFromNetwork) {
        refresh.setValue(forceFromNetwork);
    }
}
