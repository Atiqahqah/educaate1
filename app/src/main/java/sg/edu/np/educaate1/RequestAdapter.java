package sg.edu.np.educaate1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Student;

public class RequestAdapter extends ArrayAdapter<Student> {
    Context c;
    int layout;
    ArrayList<Student> data;

    public RequestAdapter(Context c,int i,ArrayList<Student>data)
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

        Student s = data.get(position);
        TextView name=item.findViewById(R.id.sSummaryName);
        name.setText(s.getName());

        return item;
    }
}
