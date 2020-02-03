package sg.edu.np.educaate1.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.R;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingViewHolder>{
    public Context context;
    public ArrayList<Rating> ratingList;

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
    public void onBindViewHolder(@NonNull RatingViewHolder ratingViewHolder, int i) {
        Rating rating = ratingList.get(i);
        //ratingViewHolder.Name.setText(rating.getName());
        ratingViewHolder.Desc.setText(rating.getDesc());
        ratingViewHolder.Score.setText(rating.getScore());
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
