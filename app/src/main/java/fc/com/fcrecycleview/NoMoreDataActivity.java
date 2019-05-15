package fc.com.fcrecycleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fc.com.fcrecycleview.adapter.LoadMoreCombinationFcAdapterTest;
import fc.recycleview.LoadMoreRecycleView;
import fc.recycleview.OnLoadMoreListener;

public class NoMoreDataActivity extends AppCompatActivity implements OnLoadMoreListener {
    private static final String TAG = "NoMoreDataActivity";
    private LoadMoreCombinationFcAdapterTest adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_more_data);

        adapter = new LoadMoreCombinationFcAdapterTest(this);
        LoadMoreRecycleView recycleView = findViewById(R.id.fc_recycle_view);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        recycleView.setOnLoadMoreListener(this);
//        recycleView.setShowNoMoreTipsOnlyOnePage(true);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add("模拟的数据_" + (i + 1));
        }
        adapter.addAll(data);
        recycleView.notifyLoadedAll();
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "===onLoadMore");
    }
}
