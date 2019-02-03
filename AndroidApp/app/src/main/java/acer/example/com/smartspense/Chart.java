package acer.example.com.smartspense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Chart extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    PieChart pieChart;
    ArrayList<PieEntry> expenseViewerArrayList = new ArrayList<>();
    String url = "http://192.168.43.125:3000/viewExpense";
    JsonArrayRequest jsonArrayRequest;
    int totalSum;
    int foodSum, lifestyleSum, billSum, medicalSum, clothesSum, entertainmentSum, giftSum;
    ArrayList<PieEntry> pieEntries = new ArrayList<>();
    boolean proceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final RequestQueue queue = Volley.newRequestQueue(Chart.this);
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
                        String recievedName = jsonObject.getString("item_name");
                        String recievedDate = jsonObject.getString("date");
                        String recievedCategory = jsonObject.getString("category");
                        String recievedNote = jsonObject.getString("note");
                        String recievedPrice = jsonObject.getString("price");

                        if(recievedCategory.equals("Food"))
                            foodSum += Integer.parseInt(recievedPrice);
                        else if(recievedCategory.equals("Lifestyle"))
                            lifestyleSum += Integer.parseInt(recievedPrice);
                        else if(recievedCategory.equals("Bill"))
                            billSum += Integer.parseInt(recievedPrice);
                        else if(recievedCategory.equals("Medical"))
                            medicalSum += Integer.parseInt(recievedPrice);
                        else if(recievedCategory.equals("Clothes"))
                            clothesSum += Integer.parseInt(recievedPrice);
                        else if(recievedCategory.equals("Entertainment"))
                            entertainmentSum += Integer.parseInt(recievedPrice);
                        else if(recievedCategory.equals("Gift"))
                            giftSum += Integer.parseInt(recievedPrice);

                        String dard = "Name = "+recievedName+"\n"+"Date = "+recievedDate+"\n"+"Category = "+recievedCategory+"\nNote = "+recievedNote+"\nPrice = "+recievedPrice;
                        //Toast.makeText(Chart.this, dard, Toast.LENGTH_LONG).show();

                        totalSum += Integer.parseInt(recievedPrice);
                        count++;
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(Chart.this,"Inside JSONException", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                if(count==response.length())
                {
                    /*pieEntries.add(new PieEntry(23500/1024, "Dard"));
                    pieEntries.add(new PieEntry(8f,"Trial 1"));
                    pieEntries.add(new PieEntry(14f,"Trial 2"));
                    pieEntries.add(new PieEntry(23f,"Trial 3"));*/
                    //Toast.makeText(Chart.this, "Total = "+totalSum, Toast.LENGTH_SHORT).show();
                    float dard = 6f;
                    if(foodSum!=0)
                    {
                        dard = (float)(foodSum*100/totalSum);
                        pieEntries.add(new PieEntry(dard, "Food"));
                    }
                    /*else
                    {
                        pieEntries.add(new PieEntry(0f, "Food"));
                    }*/
                    if(lifestyleSum!=0)
                    {
                        dard = (float)(lifestyleSum*100/totalSum);
                        pieEntries.add(new PieEntry(dard, "Lifestyle"));
                    }
                    /*else
                    {
                        pieEntries.add(new PieEntry(0f, "Lifestyle"));
                    }*/
                    if(billSum!=0)
                    {
                        dard = (float)(billSum*100/totalSum);
                        pieEntries.add(new PieEntry(dard, "Bill"));
                    }
                    /*else
                    {
                        pieEntries.add(new PieEntry(0f, "Bill"));
                    }*/
                    if(medicalSum!=0)
                    {
                        dard = (float)(medicalSum*100/totalSum);
                        pieEntries.add(new PieEntry(dard, "Medical"));
                    }
                    /*else
                    {
                        pieEntries.add(new PieEntry(0f, "Medical"));
                    }*/
                    if(clothesSum!=0)
                    {
                        dard = (float)(clothesSum*100/totalSum);
                        pieEntries.add(new PieEntry(dard, "Clothes"));
                    }
                    /*else
                    {
                        pieEntries.add(new PieEntry(0f, "Clothes"));
                    }*/
                    if(entertainmentSum!=0)
                    {
                        dard = (float)(entertainmentSum*100/totalSum);
                        pieEntries.add(new PieEntry(dard, "Entertainment"));
                    }
                    /*else
                    {
                        pieEntries.add(new PieEntry(0f, "Entertainment"));
                    }*/
                    if(giftSum!=0)
                    {
                        dard = (float)(giftSum*100/totalSum);
                        pieEntries.add(new PieEntry(dard, "Gift"));
                    }
                    /*else
                    {
                        pieEntries.add(new PieEntry(0f, "Gift"));
                    }*/

                    pieChart = findViewById(R.id.chart);
                    pieChart.setUsePercentValues(true);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(5,10,5,5);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleRadius(30f);
                    pieChart.setHoleColor(Color.rgb(255,165,0));
                    pieChart.setTransparentCircleRadius(61f);
                    pieChart.setCenterTextColor(Color.BLACK);
                    pieChart.setEntryLabelColor(Color.BLACK);

                    PieDataSet pieDataSet = new PieDataSet(pieEntries,"Categories");
                    pieDataSet.setSliceSpace(3f);
                    pieDataSet.setColor(Color.YELLOW);
                    pieDataSet.notifyDataSetChanged();

                    PieData data = new PieData(pieDataSet);
                    data.setValueTextSize(20f);
                    data.setValueTextColor(Color.BLACK);

                    pieChart.setData(data);
                    pieChart.notifyDataSetChanged();
                    pieChart.invalidate();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(Chart.this, "Inside ErrorListener", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(Chart.this, Settings.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addExpense) 
        {
            Intent myIntent = new Intent(Chart.this, AddExpense.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }
        else if (id == R.id.viewExpense)
        {
            Intent myIntent = new Intent(Chart.this, ViewExpenses.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }
        else if (id == R.id.viewCharts)
        {
            Toast.makeText(this, "Already on same page!", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
