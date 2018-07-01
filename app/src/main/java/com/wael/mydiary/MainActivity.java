package com.wael.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.wael.mydiary.Database.AppDatabase;
import com.wael.mydiary.Database.User;

public class MainActivity extends AppCompatActivity implements EntryAdapter.ListItemClickListener{
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private EntryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private AppDatabase mDb;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account = GoogleSignIn.getLastSignedInAccount(this);


        mDb = AppDatabase.getsInstance(getApplicationContext());

        //        RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_entries);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EntryAdapter(null, this, this);
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
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.Log_out:
                                signOut();
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(account != null) {
            updateUI();
            User user = new User(account.getId());
            mDb.userDao().insertUser(user);
        }
        else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void updateUI() {
//        account.getId();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView text_user_name = (TextView)headerView.findViewById(R.id.text_user_name);
        TextView text_user_email = (TextView)headerView.findViewById(R.id.text_user_email);
        ImageView image_user_profile_picture = (ImageView)headerView.findViewById(R.id.image_user_profile_picture);

        text_user_name.setText(account.getDisplayName());
        text_user_email.setText(account.getEmail());
        Glide.with(navigationView).load(account.getPhotoUrl().toString()).into(image_user_profile_picture);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setEntries(mDb.entryDao().loadAllEntriesByClient(account.getId()));
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
    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
