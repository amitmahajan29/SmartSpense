package acer.example.com.smartspense;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Acer on 02/02/2019.
 */

public class CustomAdapter extends ArrayAdapter<ExpenseViewer>
{
    Context ctx;
    List<ExpenseViewer> list;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<ExpenseViewer> objects)
    {
        super(context, resource, objects);
        ctx = context;
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.view_expense_layout, null);

        TextView name = view.findViewById(R.id.name);
        TextView note = view.findViewById(R.id.note);
        TextView price = view.findViewById(R.id.price);
        TextView date = view.findViewById(R.id.date);
        TextView category = view.findViewById(R.id.category);

        name.setText(list.get(position).getItemName());
        note.setText(list.get(position).getNote());
        price.setText(list.get(position).getPrice());
        date.setText(list.get(position).getDate());
        category.setText(list.get(position).getCategory());

        return view;
    }
}
