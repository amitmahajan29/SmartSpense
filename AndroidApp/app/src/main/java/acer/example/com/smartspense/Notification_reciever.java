package acer.example.com.smartspense;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Acer on 03/02/2019.
 */

public class Notification_reciever extends BroadcastReceiver
{
    boolean billReminder;
    JsonArrayRequest jsonArrayRequest;
    String url = "http://192.168.43.125:3000/viewExpense";
    @Override
    public void onReceive(Context context, Intent intent)
    {


        final RequestQueue queue = Volley.newRequestQueue(context);
        queue.start();
        HashMap<String, String> hashMap = new HashMap<>();
        String userName = "Amit Mahajan";
        hashMap.put("userName",userName);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                int count = 0;
                while(count < response.length())
                {
                    try
                    {
                        JSONObject jsonObject = response.getJSONObject(count);
                        String recievedDate = jsonObject.getString("date");
                        String recievedCategory = jsonObject.getString("category");
                        String dard = "Date = "+recievedDate+"\n"+"Category = "+recievedCategory;
                        if(recievedCategory.equals("Bill"))
                        {
                            int pastMonth = Integer.parseInt(recievedCategory.substring(0,2));
                            int pastDate = Integer.parseInt(recievedCategory.substring(3,5));
                            int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            int currentDate = Integer.parseInt(formatter.format(date).substring(0,2));

                            if(pastMonth+1 == currentMonth && pastDate == currentDate)
                                billReminder = true;
                        }
                        count++;
                    }
                    catch (JSONException e)
                    {
                        //Toast.makeText(ViewExpenses.this,"Inside JSONException", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(ViewExpenses.this, "Inside ErrorListener", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonArrayRequest);


        if(billReminder)
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Intent repeatingIntent = new Intent(context, Repeating_activity.class);
            repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Title").setContentText("Text").setAutoCancel(true);
            notificationManager.notify(100, builder.build());
        }
    }
}
