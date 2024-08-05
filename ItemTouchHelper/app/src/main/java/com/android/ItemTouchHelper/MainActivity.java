package com.android.ItemTouchHelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.android.ItemTouchHelper.helper.DividerItemDecoration;
import com.android.ItemTouchHelper.helper.ItemTouchHelper;
import com.android.ItemTouchHelper.view.MRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MRecyclerView mRecyclerView;
    Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mAdapter.updateData(createTestDatas());

        HelperCallback callback = new HelperCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private List<Model> createTestDatas() {
        List<Model> result = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Model testModel = new Model(i, ":Item Swipe Action Button Container Width");
            if (i == 1) {
                testModel = new Model(i, "Item Swipe with Action container width and no spring");
            }
            if (i == 2) {
                testModel = new Model(i, "Item Swipe with RecyclerView Width");
            }
            result.add(testModel);
        }
        return result;
    }
}
