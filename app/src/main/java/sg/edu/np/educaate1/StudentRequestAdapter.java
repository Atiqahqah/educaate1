package sg.edu.np.educaate1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Tutor;

public class StudentRequestAdapter extends ArrayAdapter<Tutor> {
    Context c;
    int layout;
    ArrayList<Tutor> data;

    public StudentRequestAdapter(Context c,int i,ArrayList<Tutor>data)
    {
        super(c, i, data);
        this.c = c;
        this.layout = i;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View item = convertView;
        if(item == null)
        {
            item = LayoutInflater.from(c)
                    .inflate(layout, parent, false);
        }

        Tutor t = data.get(position);
        TextView name=item.findViewById(R.id.tutorProfileAgetv);
        name.setText(t.getName());

        return item;
    }
}
