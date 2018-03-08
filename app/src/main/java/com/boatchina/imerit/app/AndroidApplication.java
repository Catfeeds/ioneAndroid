package com.boatchina.imerit.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import com.boatchina.imerit.app.di.components.ApplicationComponent;
import com.boatchina.imerit.app.di.components.DaggerApplicationComponent;
import com.boatchina.imerit.app.di.modules.ApplicationModule;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by flamingo on 2015/9/17.
 */
public class AndroidApplication extends Application {
    private ApplicationComponent applicationComponent;
    @SuppressLint("StaticFieldLeak")
    private static AndroidApplication mApplication;

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        this.initializeInjector();
        initJpush();
        this.initializeLeakDetection();
        mApplication = this;
        appContext = getApplicationContext();
//        initData();
        FileDownloader.init(appContext);
        ForegroundCallbacks.get(this);
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(appContext);
//        PackageManager packageManager = getPackageManager();
//        // getPackageName()是你当前类的包名，0代表是获取版本信息
//        PackageInfo packInfo = null;
//        try {
//            packInfo = packageManager.getPackageInfo(getPackageName(),0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String version = packInfo.versionName;
//        strategy.setAppVersion()
//        CrashReport.initCrashReport(getApplicationContext(),strategy);
    }



    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Context getAppContext() {
        return appContext;
    }

    public AndroidApplication() {
        mApplication = this;
    }

    public static AndroidApplication getBaseApplication() {
        if (mApplication != null) {
            return mApplication;
        } else {
            mApplication = new AndroidApplication();
            mApplication.onCreate();
            return mApplication;
        }
    }


    //是否联网网络
    public static boolean isHaveInternet(final Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }


    public static class ForegroundCallbacks implements Application.ActivityLifecycleCallbacks {
        public static final long CHECK_DELAY = 500;
        public static final String TAG = ForegroundCallbacks.class.getName();

        public interface Listener {
            void onBecameForeground();

            void onBecameBackground();
        }

        private static ForegroundCallbacks instance;
        private boolean foreground = false, paused = true;
        private Handler handler = new Handler();
        private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
        private Runnable check;

        public static ForegroundCallbacks init(Application application) {
            if (instance == null) {
                instance = new ForegroundCallbacks();
                application.registerActivityLifecycleCallbacks(instance);
            }
            return instance;
        }

        public static ForegroundCallbacks get(Application application) {
            if (instance == null) {
                init(application);
            }
            return instance;
        }

        public static ForegroundCallbacks get(Context ctx) {
            if (instance == null) {
                Context appCtx = ctx.getApplicationContext();
                if (appCtx instanceof Application) {
                    init((Application) appCtx);
                }
                throw new IllegalStateException(
                        "Foreground is not initialised and " +
                                "cannot obtain the Application object");
            }
            return instance;
        }

        public static ForegroundCallbacks get() {
            if (instance == null) {
                throw new IllegalStateException(
                        "Foreground is not initialised - invoke " +
                                "at least once with parameterised init/get");
            }
            return instance;
        }

        public boolean isForeground() {
            return foreground;
        }

        public boolean isBackground() {
            return !foreground;
        }

        public void addListener(Listener listener) {
            listeners.add(listener);
        }

        public void removeListener(Listener listener) {
            listeners.remove(listener);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            paused = false;
            boolean wasBackground = !foreground;
            foreground = true;
            if (check != null)
                handler.removeCallbacks(check);
            if (wasBackground) {
                System.out.println("went foreground");
                for (Listener l : listeners) {
                    try {
                        l.onBecameForeground();
                    } catch (Exception exc) {
                        System.out.println("Listener threw exception!");
                    }
                }
            } else {
                System.out.println("still foreground");
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            paused = true;
            if (check != null)
                handler.removeCallbacks(check);
            handler.postDelayed(check = new Runnable() {
                @Override
                public void run() {
                    if (foreground && paused) {
                        foreground = false;
                        System.out.println("went background");
                        for (Listener l : listeners) {
                            try {
                                l.onBecameBackground();
                            } catch (Exception exc) {
                                System.out.println("Listener threw exception!");
                            }
                        }
                    } else {
                        System.out.println("still foreground");
                    }
                }
            }, CHECK_DELAY);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
