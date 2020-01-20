package debugit.lesquizerables;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FireBase UI and stuff
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth.AuthStateListener stateListener;
    private static final int RC_SIGN_IN = 1;

    private DrawerLayout drawer;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------------Navigation Drawer---------------------------\\
        android.support.v7.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.open, R.string.close) {


            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                drawer.closeDrawer(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
                drawer.bringToFront();
                invalidateOptionsMenu();
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //---------------------FireBase---------------------------\\
        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        reference = database.getReference();

        providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final Query query = reference.child("users").orderByKey();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        //find user.getUid() in storage and then send data to profile
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot users : dataSnapshot.getChildren()) {
                                    if ((users.getKey().equals(user.getUid()))) {
                                        //
                                    } else {
                                        User user1 = new User();
                                        user1.setName(user.getDisplayName());
                                        user1.setEmail(user.getEmail());
                                        Map<String, Object> userProfile = user1.toMap();
                                        //userProfile.put("name", user.getDisplayName());
                                        //userProfile.put("email", user.getEmail());
                                        reference.child("users").child(user.getUid()).updateChildren(userProfile);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    Toast.makeText(MainActivity.this, "Welcome! You're now signed in " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().
                            setIsSmartLockEnabled(false).
                            setAvailableProviders(providers)
                            .build(), RC_SIGN_IN);
                }
            }
        };
        navigationView.setItemIconTintList(null);
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(stateListener);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.topics:
                fragment = new TopicFragment();
                break;
            case R.id.favorites:
                break;
            case R.id.my_profile:
                fragment = new UserProfile();
                break;
            case R.id.ranking:
                fragment = new RankingFragment();
                break;
            case R.id.friends:
                break;
            case R.id.opponents:
                Intent intentQuiz = new Intent(this, GameActivity.class);
                intentQuiz.putExtra("topic", "general");
                startActivity(intentQuiz);
                break;
            case R.id.log_out:
                signOut();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.contentOfStuff, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


