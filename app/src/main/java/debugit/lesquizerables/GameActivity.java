package debugit.lesquizerables;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class GameActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private Button buttonA, buttonB, buttonC, buttonD;
    private TextView questionView;
    private TextView timerView;

    public int currentQuestionIndex;
    private ArrayList<Question> questions;
    private String topic;
    private int points;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    public GameActivity() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        Intent intent = getIntent();
        topic = intent.getStringExtra("topic");

        //Initiate FireBase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        this.initialize();
    }

    public void initialize() {
        buttonA = findViewById(R.id.button1);
        buttonB = findViewById(R.id.button2);
        buttonC = findViewById(R.id.button3);
        buttonD = findViewById(R.id.button4);
        questionView = findViewById(R.id.question);
        timerView = findViewById(R.id.timer);
        Firebase.setAndroidContext(this);

        DatabaseReference fb = reference.child("questions").child("topics").child(topic);

        fb.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                questions = new ArrayList<>();
                for (DataSnapshot q : snapshot.getChildren()) {
                    questions.add(q.getValue(Question.class));
                }
                currentQuestionIndex = 0;
                displayQuestion(currentQuestionIndex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not used
            }
        });
    }

    public void onClick(View view) {

        final Button button = findViewById(view.getId());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("questions").child("topics").child(topic).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (button.getText().toString().equals(questions.get(currentQuestionIndex).getCorrectAnswer())) {
                                        Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                                        points += 53;
                                        advance();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
                                        advance();
                                    }
                                } else {
                                    System.out.println("DataSnapshot NULL");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                databaseError.toException();
                            }
                        });
            }
        });
    }

    private void advance() {

        if (questions.get(currentQuestionIndex).isCreditAlreadyGiven() && currentQuestionIndex == questions.size() - 1) {
            System.out.println(points + " Points");
            final DatabaseReference tempReference = reference.child("users");
            tempReference.addListenerForSingleValueEvent(new ValueEventListener() {
                User user = new User();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot q : dataSnapshot.getChildren()) {
                        if (firebaseUser.getUid().equals(q.getKey())) {

                            user = q.getValue(User.class);
                            System.out.println(user.getName());
                            System.out.println(user.getPoints() + " Points2");
                            user.setPoints(points);
                            tempReference.child(q.getKey()).updateChildren(user.toMap());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            finish();
        }

        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size();
        questions.get(currentQuestionIndex).setCreditAlreadyGiven(true);
        displayQuestion(currentQuestionIndex);

    }

    public void displayQuestion(int index) {
        questionView.setText(questions.get(index).getQuestionText());
        buttonA.setText(questions.get(index).getA());
        buttonB.setText(questions.get(index).getB());
        buttonC.setText(questions.get(index).getC());
        buttonD.setText(questions.get(index).getD());
    }

    void timer() {
        new CountDownTimer(10000, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d sec",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timerView.setText(text);
            }

            @Override
            public void onFinish() {
                advance();
            }
        }.start();
    }
}
