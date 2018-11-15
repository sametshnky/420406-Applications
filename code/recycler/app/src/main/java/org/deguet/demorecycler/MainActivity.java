package org.deguet.demorecycler;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * TODO
 * https://stackoverflow.com/questions/44454797/pull-to-refresh-recyclerview-android
 *
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 * https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
 *
 * https://developer.android.com/training/snackbar/showing
 *
 */


public class MainActivity extends AppCompatActivity {

    List<Truc> trucs = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();
        initSwipe();
        triggerSnackbar();
    }

    private void triggerSnackbar() {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.coord),
                "pof pof ", Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setAction("action A", new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Tada", Toast.LENGTH_LONG).show();
            }
        });
        mySnackbar.show();
    }

    private void initRecycler() {
        mRecyclerView = findViewById(R.id.rv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        // specify an adapter (see also next example)

        mAdapter = new JorisAdapter(trucs);
        mRecyclerView.setAdapter(mAdapter);
        changeList();
    }

    private void initSwipe() {
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void changeList(){
        Random r = new Random();
        trucs.clear();
        for (int i = 0 ; i < 30 ; i++) {
            Truc truc = new Truc();
            truc.a = "test"+r.nextInt(99);
            trucs.add(truc);
        }
        mAdapter.notifyDataSetChanged();
    }
}
