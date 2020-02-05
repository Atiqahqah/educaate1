package sg.edu.np.educaate1.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.R;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingViewHolder>{
    public Context context;
    public ArrayList<Rating> ratingList;
    public DatabaseReference databaseReference;

    public RatingsAdapter(Context c, ArrayList<Rating> rlist){
        context = c;
        ratingList = rlist;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_reviews,viewGroup,false);
        RatingViewHolder ratingViewHolder = new RatingViewHolder(view);
        return ratingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RatingViewHolder ratingViewHolder, int i) {
        Rating rating = ratingList.get(i);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(rating.getUserID());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                ratingViewHolder.Name.setText(student.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ratingViewHolder.Desc.setText(rating.getDesc());
        ratingViewHolder.Score.setText(String.valueOf(rating.getScore()));
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder{
        public TextView Name;
        public TextView Score;
        public TextView Desc;
        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.ratingNametv);
            Score = itemView.findViewById(R.id.ratingScoretv);
            Desc = itemView.findViewById(R.id.ratingDesctv);
        }
    }

}
