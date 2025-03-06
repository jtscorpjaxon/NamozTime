package uz.jtscorp.namoztime.data.remote;

import java.util.Date;

public class PrayerTimeData {
    private Timings timings;
    private Date date;

    public Timings getTimings() {
        return timings;
    }

    public void setTimings(Timings timings) {
        this.timings = timings;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
} 