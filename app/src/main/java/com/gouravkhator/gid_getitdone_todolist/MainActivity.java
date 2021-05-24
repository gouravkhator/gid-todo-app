package com.gouravkhator.gid_getitdone_todolist;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gouravkhator.gid_getitdone_todolist.fragments.AllTasksFragment;
import com.gouravkhator.gid_getitdone_todolist.fragments.DoneFragment;
import com.gouravkhator.gid_getitdone_todolist.fragments.TodoFragment;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private Toolbar toolbar;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.mainToolbar);
        title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        setTitle("");
        loadFragment(new TodoFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.modeTransition:
                // code for dark mode programmatically
                Toast.makeText(this, "This feature is not available now !! It will be available soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.aboutUs:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.todoItem:
                fragment=new TodoFragment();
                title.setText("My To-Dos");
                break;
            case R.id.doneItem:
                fragment=new DoneFragment();
                title.setText("Completed Tasks");
                break;
            case R.id.allItem:
                fragment=new AllTasksFragment();
                title.setText("All Tasks");
                break;
        }
        return loadFragment(fragment);
    }
}
