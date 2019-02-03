package acer.example.com.smartspense;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ViewExpenses extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView lv;
    ArrayList<ExpenseViewer> expenseViewerArrayList = new ArrayList<>();
    String url = "http://192.168.43.125:3000/viewExpense";
    JsonArrayRequest jsonArrayRequest;
    TextView name, date, price, category, note;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //addData();

        date = (TextView) findViewById(R.id.date);
        name = (TextView) findViewById(R.id.name);
        category = (TextView) findViewById(R.id.category);
        note = (TextView) findViewById(R.id.note);
        price = (TextView) findViewById(R.id.price);


        lv = findViewById(R.id.expenseListView);
        adapter = new CustomAdapter(ViewExpenses.this, R.layout.view_expense_layout, expenseViewerArrayList);
        lv.setAdapter(adapter);

        final RequestQueue queue = Volley.newRequestQueue(ViewExpenses.this);
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
                        String dard = "Name = "+recievedName+"\n"+"Date = "+recievedDate+"\n"+"Category = "+recievedCategory+"\nNote = "+recievedNote+"\nPrice = "+recievedPrice;
                        ExpenseViewer expenseViewer = new ExpenseViewer(recievedName, recievedPrice, recievedCategory, recievedNote, recievedDate);
                        //Toast.makeText(ViewExpenses.this, dard, Toast.LENGTH_LONG).show();
                        expenseViewerArrayList.add(expenseViewer);
                        count++;
                        adapter.notifyDataSetChanged();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(ViewExpenses.this,"Inside JSONException", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(ViewExpenses.this, "Inside ErrorListener", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void addData() {
        for (int i = 0; i < 2; i++) {
            ExpenseViewer e = new ExpenseViewer("Item " + i, "Price " + i, "Category " + i, "Note " + i, "Date " + i);
            expenseViewerArrayList.add(e);
        }
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
        getMenuInflater().inflate(R.menu.view_expenses, menu);
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
            Intent i = new Intent();
            i.setClass(ViewExpenses.this, Settings.class);
            i.putExtra("msg", "Settings Opened");
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addExpense) {
            Intent myIntent = new Intent(ViewExpenses.this, AddExpense.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        } else if (id == R.id.viewExpense) {
            Toast.makeText(ViewExpenses.this, "You are already on same page!", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.viewCharts)
        {
            Intent myIntent = new Intent(ViewExpenses.this, Chart.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
