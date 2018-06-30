package com.wael.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wael.mydiary.Database.AppDatabase;

public class MainActivity extends AppCompatActivity implements EntryAdapter.ListItemClickListener{
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private EntryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getsInstance(getApplicationContext());

//        RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_entries);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new EntryAdapter(mDb.entryDao().loadAllEntries(), this, this);
        mRecyclerView.setAdapter(mAdapter);


        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //Navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();



                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setEntries(mDb.entryDao().loadAllEntries());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_search:
                Log.i(this.getClass().getName(), "action_search opened");
                return true;

            case R.id.action_add:
                startActivity(new Intent(MainActivity.this, EntityEditionActivity.class));
                return true;
        }
            return super.onOptionsItemSelected(item);
        }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.d(TAG, "onListItemClick: Index item clicked  = " + clickedItemIndex);
        Intent intent = new Intent(MainActivity.this, EntityEditionActivity.class);
        intent.putExtra(EntityEditionActivity.EXTRA_ENTRY_ID, clickedItemIndex);
        startActivity(intent);
    }
}
