package group1.fitnessapp.excerciseTracker;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import group1.fitnessapp.R;

public class AddExerciseActivity extends AppCompatActivity {

    private int reqCode;
    private RecyclerView recyclerView;
    private SearchListAdapter adapter;
    private Toolbar toolbar;
    private ArrayList<Exercise> list = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        setTitle("Add Exercise");
        reqCode = (int) getIntent().getSerializableExtra("reqCode");
        recyclerView = (RecyclerView) findViewById(R.id.exerciseResults);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchListAdapter(this, list);
        //list.add(new Exercise("Test", "Test Description"));
        recyclerView.setAdapter(adapter);
        searchView = (SearchView) findViewById(R.id.addExerciseSearchView);
        //toolbar = (Toolbar) findViewById(R.id.search_exercise_toolbar);
        //setSupportActionBar(toolbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                //System.out.println("Accessed this method!");
                AsyncTask<String, Void, ArrayList<Exercise>> task = new ExerciseAsyncTask().execute(query);
                try {
                    searchView.clearFocus();
                    list.clear();
                    list.addAll(task.get());
                } catch(Exception e) {
                    e.printStackTrace();
                }
                adapter.setSearchOperation(list);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



}
