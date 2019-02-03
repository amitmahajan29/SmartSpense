package acer.example.com.smartspense;

import android.os.AsyncTask;

/**
 * Created by Acer on 03/02/2019.
 */

public class MJobExecuter extends AsyncTask<Void, Void, String>
{
    @Override
    protected String doInBackground(Void... voids)
    {
        return "Background task running!";
    }
}
