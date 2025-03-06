package uz.jtscorp.namoztime.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;

import uz.jtscorp.namoztime.util.AlarmScheduler;
import uz.jtscorp.namoztime.util.SharedPreferencesHelper;

public class Dialogs {
    public static void showTimePicker(Context context) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, selectedHour, selectedMinute) -> {
            SharedPreferencesHelper.saveTime(context, selectedHour, selectedMinute);
            AlarmScheduler.scheduleSilentMode(context, selectedHour, selectedMinute);
            Toast.makeText(context, "Tanlangan vaqt: " + selectedHour + ":" + String.format("%02d", selectedMinute), Toast.LENGTH_LONG).show();
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
