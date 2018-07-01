package com.wael.mydiary;

import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.wael.mydiary.Database.AppDatabase;
import com.wael.mydiary.Database.Entry;

public class EntityEditionActivity extends AppCompatActivity {
    private static final String TAG = "EntityEditionActivity";
    public static final String EXTRA_ENTRY_ID = "extraTaskId";
    private static final int DEFAULT_ENTRY_ID = -1;
    private boolean mUpdate=false;


    private AppDatabase mDb;
    private int myEntryId = DEFAULT_ENTRY_ID;
    private Entry entry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entity_edition);
        mDb = AppDatabase.getsInstance(getApplicationContext());
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_entry_edition);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        mUpdate = (intent != null )&& (intent.hasExtra(EXTRA_ENTRY_ID));
        if (mUpdate) {
            myEntryId = intent.getIntExtra(EXTRA_ENTRY_ID, DEFAULT_ENTRY_ID);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    entry = mDb.entryDao().loadEntryById(account.getId(), myEntryId);
                    populateUI(entry);
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entity_edition,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (mUpdate) {
                        mDb.entryDao().deleteEntry(entry);
                }
                finish();

                return true;

            case R.id.action_favorite:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateData(){
        EditText etText = (EditText)findViewById(R.id.text_entry_edition);
        EditText etTitle = (EditText)findViewById(R.id.edit_title_entry_edition);

        String text = etText.getText().toString();
        String title = etTitle.getText().toString();
        if (mUpdate) {
            if ((text.length() != 0) || (title.length() != 0)){
                entry.setText(text);
                entry.setTitle(title);

                mDb.entryDao().updateEntry(entry);
            }
            else
                mDb.entryDao().deleteEntry(entry);

        }else {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            entry = new Entry(0, title, text ,account.getId());
            if ((text.length() != 0) || (title.length() != 0))
                mDb.entryDao().insertEntry(entry);
        }
        finish();
    }
    private void populateUI(Entry entry) {
        if (entry == null) {
            return;
        }
        TextView title = (TextView) findViewById(R.id.edit_title_entry_edition);
        TextView text = (TextView) findViewById(R.id.text_entry_edition);
//        TextView day = (TextView) findViewById(R.id.edit_date_entry_edition);
//        TextView dayNumber = (TextView) findViewById(R.id.day_number_entry_item);
        title.setText(entry.getTitle());
        text.setText(entry.getText());
    }
    @Override
    protected void onPause() {
        super.onPause();
        updateData();
    }
}
