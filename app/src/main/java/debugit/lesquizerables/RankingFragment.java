package debugit.lesquizerables;

import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RankingFragment extends Fragment {

    private ListView rankingList;
    private ArrayList<String> rankings = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rankings_layout, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        rankingList = getView().findViewById(R.id.ranking_list);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_1, rankings);


        DatabaseReference reference1 = reference.child("users");
        reference1.orderByChild("points").limitToLast(50).addListenerForSingleValueEvent(new ValueEventListener() {
            User user = new User();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot q : dataSnapshot.getChildren()) {

                    user = q.getValue(User.class);
                    assert user != null;
                    System.out.println("Name: " + user.getName() + " Points: " + user.getPoints());
                    rankings.add("Name: " + user.getName() + " Points: " + user.getPoints());
                }
                Collections.reverse(rankings);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rankingList.setAdapter(arrayAdapter);
    }

}
