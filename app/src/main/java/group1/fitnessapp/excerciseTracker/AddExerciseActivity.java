package group1.fitnessapp.excerciseTracker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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
                //AsyncTask<String, Void, ArrayList<Exercise>> task = new ExerciseAsyncTask().execute(query);
                //Toast.makeText(AddExerciseActivity.this, "Searching...Please Wait", Toast.LENGTH_LONG).show();
                AsyncTask<String, Void, ArrayList<Exercise>> task = new ExerciseAsyncTask();
                if(isNetworkConnected()) {
                    task.execute(query);
                    try {
                        searchView.clearFocus();
                        list.clear();
                        list.addAll(task.get());
                        if(list.isEmpty()) {
                           showDialogBox("No Results Found!");
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    adapter.setSearchOperation(list);
                } else {
                    showDialogBox("No Internet Connection Detected!");
                }
                /**
                try {
                    Toast.makeText(AddExerciseActivity.this, "Searching...Please Wait", Toast.LENGTH_LONG).show();
                    searchView.clearFocus();
                    list.clear();
                    list.addAll(task.get());
                } catch(Exception e) {
                    e.printStackTrace();
                }
                adapter.setSearchOperation(list);
                 **/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void showDialogBox(String message) {
        CustomDialogBoxActivity customDialog = new CustomDialogBoxActivity();
        customDialog.setDialogText(message);
        customDialog.setCustomTitle("Error");
        customDialog.show(getSupportFragmentManager(), "Error");
    }

    public void showDialogBox(String message, String title) {
        CustomDialogBoxActivity customDialog = new CustomDialogBoxActivity();
        customDialog.setDialogText(message);
        customDialog.setCustomTitle(title);
        customDialog.show(getSupportFragmentManager(), title);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
