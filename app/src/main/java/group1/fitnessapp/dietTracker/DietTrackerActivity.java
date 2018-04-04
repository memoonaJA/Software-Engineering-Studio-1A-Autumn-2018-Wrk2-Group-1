package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import group1.fitnessapp.R;

public class DietTrackerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FoodListAdapter adapt = null;
    private ArrayList<Food> foodArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fab for adding food
        // TODO MAKE THIS FUNCTION
        FloatingActionButton addFood = (FloatingActionButton) findViewById(R.id.fab);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddFood();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        generateFakeFoodData();

        adapt =  new FoodListAdapter(this, foodArrayList);
        ListView ls = (ListView) findViewById(R.id.ls_diet_food);
        ls.setAdapter(adapt);
    }

    private void addItem(Food food) {
        foodArrayList.add(food);
        adapt.notifyDataSetChanged();
    }

    private void launchAddFood() {
        Intent intent = new Intent(this, DietAddFoodActivity.class);
        startActivity(intent);
    }

    private void generateFakeFoodData() {
        foodArrayList.add(new Food("Test food", "Test description", 200, null));
        foodArrayList.add(foodArrayList.get(0));
        foodArrayList.add(foodArrayList.get(0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String add_name = data.getStringExtra("SELECTED_FOOD_NAME");
                String add_sub_txt = data.getStringExtra("SELECTED_FOOD_SUB_TXT");
                //int add_calories = data.getIntExtra("SELECTED_FOOD_CALORIES");
                addItem(new Food(add_name, add_sub_txt, 666, null));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.diet_tracker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dietTracker) {
            // A new instance of this activity shouldn't be made again
        } else if (id == R.id.nav_excerciseTracker) {

        } else if (id == R.id.nav_stepTracker) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
