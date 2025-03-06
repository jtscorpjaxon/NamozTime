package uz.jtscorp.namoztime.di;

import android.app.Service;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ServiceScoped;
import uz.jtscorp.namoztime.service.PrayerTimeService;

@Module
@InstallIn(ServiceComponent.class)
public class ServiceModule {

    @Provides
    @ServiceScoped
    Context provideServiceContext(@ApplicationContext Context context) {
        return context;
    }

    @Provides
    @ServiceScoped
    PrayerTimeService providePrayerTimeService() {
        return new PrayerTimeService();
    }
} 