package acer.example.com.smartspense;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ratings extends AppCompatActivity
{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button btnSaveRatings;
    EditText etFood, etLifestyle, etBill, etMedical, etClothes, etEntertainment, etGift;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        btnSaveRatings = (Button) findViewById(R.id.saveRatings);
        etFood = (EditText) findViewById(R.id.etFood);
        etLifestyle = (EditText) findViewById(R.id.etLifestyle);
        etBill = (EditText) findViewById(R.id.etBill);
        etMedical = (EditText) findViewById(R.id.etMedical);
        etClothes = (EditText) findViewById(R.id.etClothes);
        etEntertainment = (EditText) findViewById(R.id.etEntertainment);
        etGift = (EditText) findViewById(R.id.etGift);
        preferences = getSharedPreferences(Prefs.NOTES_SETTINGS, MODE_PRIVATE);

        etFood.setText(String.valueOf(preferences.getInt("foodPercent",10)));
        etLifestyle.setText(String.valueOf(preferences.getInt("lifestylePercent",10)));
        etBill.setText(String.valueOf(preferences.getInt("billPercent",10)));
        etMedical.setText(String.valueOf(preferences.getInt("medicalPercent",10)));
        etClothes.setText(String.valueOf(preferences.getInt("clothesPercent",10)));
        etEntertainment.setText(String.valueOf(preferences.getInt("entertainmentPercent",10)));
        etGift.setText(String.valueOf(preferences.getInt("giftPercent",10)));

        btnSaveRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(Ratings.this, "Food rating :"+etFood.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Ratings.this, "Lifestyle rating :"+etLifestyle.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Ratings.this, "Bill rating :"+etBill.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Ratings.this, "Medical rating :"+etMedical.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Ratings.this, "Clothes rating :"+etClothes.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Ratings.this, "Entertainment rating :"+etEntertainment.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Ratings.this, "Gift rating :"+etGift.getText().toString(), Toast.LENGTH_SHORT).show();

                editor = preferences.edit();
                editor.putInt("foodPercent", Integer.parseInt(etFood.getText().toString()));
                editor.putInt("lifestylePercent", Integer.parseInt(etLifestyle.getText().toString()));
                editor.putInt("billPercent", Integer.parseInt(etBill.getText().toString()));
                editor.putInt("medicalPercent", Integer.parseInt(etMedical.getText().toString()));
                editor.putInt("clothesPercent", Integer.parseInt(etClothes.getText().toString()));
                editor.putInt("entertainmentPercent", Integer.parseInt(etEntertainment.getText().toString()));
                editor.putInt("giftPercent", Integer.parseInt(etGift.getText().toString()));
                editor.apply();
                finish();
            }
        });
    }
}
