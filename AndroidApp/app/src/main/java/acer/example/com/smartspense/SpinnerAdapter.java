package acer.example.com.smartspense;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 02/02/2019.
 */

public class SpinnerAdapter extends BaseAdapter
{
    private List<String> categoryName;
    private List<Integer> categoryImage;
    private LayoutInflater inflater;
    private Activity activity;

    public SpinnerAdapter(List<String> categoryName, Activity activity, List<Integer> categoryImage)
    {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return categoryName.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = inflater.inflate(R.layout.spinner_item, null);
        }
        TextView categoryNameTv = (TextView) view.findViewById(R.id.categoryName);
        ImageView categoryImageIv = (ImageView) view.findViewById(R.id.categoryImage);
        categoryNameTv.setText(categoryName.get(i));
        categoryImageIv.setImageResource(categoryImage.get(i));
        return view;
    }


}
