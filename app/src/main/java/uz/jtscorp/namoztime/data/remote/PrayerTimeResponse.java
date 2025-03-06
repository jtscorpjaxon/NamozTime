package uz.jtscorp.namoztime.data.remote;

import java.util.List;

public class PrayerTimeResponse {
    private int code;
    private String status;
    private List<PrayerTimeData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PrayerTimeData> getData() {
        return data;
    }

    public void setData(List<PrayerTimeData> data) {
        this.data = data;
    }
} 