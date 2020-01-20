package debugit.lesquizerables;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserProfile extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ArrayList<User> users;

    private TextView nameTextView;
    private TextView titleTextView;
    private TextView locationTextView;
    private Spinner rankTextView;
    private TextView pointsTextView;
    private TextView nºGamesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nameTextView = getView().findViewById(R.id.nameText);
        titleTextView = getView().findViewById(R.id.titleText);
        locationTextView = getView().findViewById(R.id.locationText);
        rankTextView = getView().findViewById(R.id.rankText);
        pointsTextView = getView().findViewById(R.id.pointsText);
        nºGamesTextView = getView().findViewById(R.id.numberGamesText);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("users");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();
                for (DataSnapshot q : dataSnapshot.getChildren()) {
                    if (firebaseUser.getUid().equals(q.getKey())) {
                        user = q.getValue(User.class);
                    }
                }
                System.out.println(user.getName());
                System.out.println(user.getPoints() + "");
                System.out.println(user.getNumberOfGames() + "");
                nameTextView.setText(user.getName());
                pointsTextView.setText(user.getPoints() + "");
                nºGamesTextView.setText(user.getNumberOfGames() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
