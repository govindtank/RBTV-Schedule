package de.metzgore.rbtvschedule.weeklyschedule;

import android.util.Log;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.metzgore.rbtvschedule.R;
import de.metzgore.rbtvschedule.data.RBTVScheduleApi;
import de.metzgore.rbtvschedule.data.Schedule;
import de.metzgore.rbtvschedule.util.Injector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyFragmentPresenter implements WeeklyScheduleContract.UserActionListener {

    private static final String TAG = WeeklyScheduleContract.class.getSimpleName();

    private WeeklyScheduleContract.View mView;

    WeeklyFragmentPresenter(WeeklyScheduleContract.View view) {
        mView = view;
    }

    @Override
    public void loadWeeklySchedule() {
        mView.hideSnackbar();
        mView.showRefreshIndicator(true);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        RBTVScheduleApi rbtvScheduleApi = Injector.provideRBTVScheduleApi();
        Call<Schedule> call = rbtvScheduleApi.weeklySchedule();

        call.enqueue(new Callback<Schedule>() {
            @Override
            public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                Log.d(TAG, "received response for weekly schedule");
                Log.d(TAG, "response code: " + response.code());

                Log.d(TAG, "cache response: " + response.raw().cacheResponse());
                Log.d(TAG, "network response: " + response.raw().networkResponse());

                mView.showEmptyView(false);

                if (response.isSuccessful()) {
                    mView.showEmptyView(false);
                    Log.d(TAG, response.body().toString());
                    mView.showSchedule(response.body());
                } else
                    mView.showRetrySnackbar(R.string.error_message_schedule_general);
            }

            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {
                Log.d(TAG, "did not receive response for weekly schedule: " + t.getMessage());
                Log.d(TAG, call.request().toString());
                mView.showEmptyView(true);

                if (t instanceof IOException) {
                    //device is offline
                    mView.showRetrySnackbar(R.string.error_message_device_offline);
                } else {
                    //other errors
                    mView.showRetrySnackbar(R.string.error_message_schedule_general);
                }
            }
        });

        mView.showRefreshIndicator(false);
    }
}