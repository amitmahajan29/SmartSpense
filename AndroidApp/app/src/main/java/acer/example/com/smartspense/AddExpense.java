package acer.example.com.smartspense;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddExpense extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    EditText name;
    EditText priceOfItem;
    EditText description;
    public Spinner category;
    public Spinner paymentMode;
    EditText dateOfPayment;
    List<String> categoryName = new ArrayList<String>();
    List<Integer> categoryImage = new ArrayList<Integer>();
    List<String> paymentModeList = new ArrayList<String>();
    final Calendar myCalendar = Calendar.getInstance();
    Button addExpense;
    final String add_url = "http://192.168.43.125:3000/addExpense";
    JsonObjectRequest js;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences(Prefs.NOTES_SETTINGS, MODE_PRIVATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        name = (EditText) findViewById(R.id.name);
        priceOfItem = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.extraDescription);
        dateOfPayment = (EditText) findViewById(R.id.date);
        category = (Spinner) findViewById(R.id.category);
        paymentMode = (Spinner) findViewById(R.id.paymentMode);
        addExpense = (Button) findViewById(R.id.btnAddExpense);

        addToSpinners();
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(categoryName,AddExpense.this,categoryImage);
        category.setAdapter(spinnerAdapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddExpense.this,R.layout.support_simple_spinner_dropdown_item, paymentModeList);
        paymentMode.setAdapter(adapter);
        final RequestQueue queue = Volley.newRequestQueue(AddExpense.this);
        queue.start();
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String itemName = name.getText().toString();
                String price = priceOfItem.getText().toString();
                int priceOfItem = Integer.parseInt(price);
                String extraNote = description.getText().toString();
                String modeOfPayment = paymentMode.getSelectedItem().toString();
                String itemCategory = (String) category.getSelectedItem();
                String payment_date = dateOfPayment.getText().toString();

                ExpenseData ed = new ExpenseData(itemName, payment_date, itemCategory, modeOfPayment, extraNote, priceOfItem);

        /*Toast.makeText(AddExpense.this, itemName, Toast.LENGTH_SHORT).show();
        Toast.makeText(AddExpense.this, price, Toast.LENGTH_SHORT).show();
        Toast.makeText(AddExpense.this, extraNote, Toast.LENGTH_SHORT).show();
        Toast.makeText(AddExpense.this, modeOfPayment, Toast.LENGTH_SHORT).show();
        Toast.makeText(AddExpense.this, itemCategory, Toast.LENGTH_SHORT).show();
        Toast.makeText(AddExpense.this, payment_date, Toast.LENGTH_SHORT).show();*/

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("itemName",itemName);
                hashMap.put("extraNote",extraNote);
                hashMap.put("price",price);
                hashMap.put("modeOfPayment",modeOfPayment);
                hashMap.put("paymentDate",payment_date);
                hashMap.put("itemCategory",itemCategory);
                String s = preferences.getString("Email","akshay.kotak@somaiya.edu");
                hashMap.put("emailAddress",s);
                hashMap.put("budget",String.valueOf(preferences.getInt(Prefs.Current_Budget, 10)));
                js = new JsonObjectRequest(Request.Method.POST, add_url, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            String display = "You have "+response.get("message").toString()+" rupees left!";
                            Toast.makeText(AddExpense.this, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(AddExpense.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(js);            }
        });

         final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateOfPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(AddExpense.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateOfPayment.setText(sdf.format(myCalendar.getTime()));
    }

    private void addToSpinners()
    {
        categoryName.add("Food");
        categoryName.add("Lifestyle");
        categoryName.add("Bill");
        categoryName.add("Medical");
        categoryName.add("Clothes");
        categoryName.add("Entertainment");
        categoryName.add("Gift");

        categoryImage.add(R.drawable.food);
        categoryImage.add(R.drawable.lifestyle);
        categoryImage.add(R.drawable.bill);
        categoryImage.add(R.drawable.medical);
        categoryImage.add(R.drawable.clothes);
        categoryImage.add(R.drawable.entertainment);
        categoryImage.add(R.drawable.gift);

        paymentModeList.add("Cash");
        paymentModeList.add("Net Banking");
        paymentModeList.add("Other");
        paymentModeList.add("Cheque");
    }

    @Override
    public void onBackPressed()
    {
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
        getMenuInflater().inflate(R.menu.add_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) //There was inbuilt settings button, we just linked it with the SettingsActivity.java file.
        {
            Intent i = new Intent();
            i.setClass(AddExpense.this,Settings.class);
            i.putExtra("msg","Settings Opened");
            startActivity(i);
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
            Toast.makeText(AddExpense.this, "You are already on same page!", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.viewExpense)
        {
            Intent myIntent = new Intent(AddExpense.this, ViewExpenses.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }
        else if (id == R.id.viewCharts)
        {
            Intent myIntent = new Intent(AddExpense.this, Chart.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class ExpenseData
    {
        String itemName, payment_date,itemCategory, modeOfPayment, extraNote;
        int price;
        ExpenseData(String itemName, String payment_date, String itemCategory, String modeOfPayment, String extraNote, int price)
        {
            this.itemName = itemName;
            this.payment_date = payment_date;
            this.itemCategory = itemCategory;
            this.modeOfPayment = modeOfPayment;
            this.extraNote = extraNote;
            this.price = price;
        }
    }
}

