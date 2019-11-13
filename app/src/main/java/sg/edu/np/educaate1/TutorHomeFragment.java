package sg.edu.np.educaate1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorHomeFragment extends Fragment {


    public TutorHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutor_home, container, false);
    }

    public void onClick (View v) {

        Intent intent=new Intent(getActivity(),BookingList.class);
        startActivity(intent);
    }
}
