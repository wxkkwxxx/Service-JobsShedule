package com.wxk.myservice;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * Created by Administrator on 2017/4/8
 */

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService{

    private final int jobWakeuUpId = 1;

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务 轮训 看MessageService是否存在
        boolean messageServiceAlive = isServiceAlive(this, MessageService.class.getName());
        if(!messageServiceAlive){

            startService(new Intent(this, MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启轮训
        JobInfo.Builder jobBuilder = new JobInfo.Builder(jobWakeuUpId, new ComponentName(this, JobWakeUpService.class));
        jobBuilder.setPeriodic(5000);
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobBuilder.build());
        return START_STICKY;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    private boolean isServiceAlive(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
