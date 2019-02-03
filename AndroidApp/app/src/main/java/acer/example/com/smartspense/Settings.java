package acer.example.com.smartspense;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Settings extends AppCompatActivity
{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SeekBar sb;
    TextView tv;
    EditText mail;
    Button btnRating, startNotification, stopNotification;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    private static final int JOB_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sb = findViewById(R.id.sb);
        tv = findViewById(R.id.tv);
        mail = findViewById(R.id.mailid);
        startNotification = (Button) findViewById(R.id.startNotification);
        stopNotification = (Button) findViewById(R.id.stopNotification);

        preferences = getSharedPreferences(Prefs.NOTES_SETTINGS, MODE_PRIVATE);
        final int current = preferences.getInt(Prefs.Current_Budget, 10);
        sb.setMax(100000);
        tv.setText("Your Budget: " + current);
        sb.setProgress(current);

        String s = preferences.getString("Email", "akshay.kotak@somaiya.edu");
        mail.setText(s);

        btnRating = (Button) findViewById(R.id.ratings);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Settings.this, Ratings.class);
                //myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i!=current){
                    i = i / 500;
                    i = i * 500;
                }
                seekBar.setProgress(i);
                tv.setText("Your Budget: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ComponentName componentName = new ComponentName(Settings.this, MJobScheduler.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setPeriodic(5000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);

        jobInfo = builder.build();
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        startNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //jobScheduler.schedule(jobInfo);
                //Toast.makeText(Settings.this, "Job Scheduled!", Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 04);
                calendar.set(Calendar.MINUTE, 43);
                calendar.set(Calendar.SECOND,00);
                Intent i = new Intent(getApplicationContext(), Notification_reciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,i,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            }
        });
        stopNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //jobScheduler.cancel(JOB_ID);
                //Toast.makeText(Settings.this, "Job Cancelled!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btn_save(View v){
        editor = preferences.edit();
        editor.putInt(Prefs.Current_Budget, sb.getProgress());
        String m = String.valueOf(mail.getText());
        editor.putString("Email", m);
        editor.apply();
        finish();
    }
}
