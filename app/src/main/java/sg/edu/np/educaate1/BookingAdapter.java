package sg.edu.np.educaate1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {
    Context c;
    int layout;
    ArrayList<Booking> data;

    public BookingAdapter(Context c,int i,ArrayList<Booking>data)
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

        Booking s = data.get(position);
        TextView datetime = item.findViewById(R.id.dateTimeTxt);
        datetime.setText(s.getDate()+", "+s.getTime());
        TextView price=item.findViewById(R.id.priceTxt);
        price.setText("$"+s.getPrice());
        TextView subj=item.findViewById(R.id.subjTxt);
        subj.setText(s.getSubject());
        TextView location=item.findViewById(R.id.locationTxt);
        location.setText(s.getLocation());
        TextView name=item.findViewById(R.id.nameTxt);
        name.setText(s.getName());

        return item;
    }
}
