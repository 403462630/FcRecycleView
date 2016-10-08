package fc.com.fcrecycleview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;

import fc.com.fcrecycleview.adapter.LoadMoreCombinationFcAdapterTest;
import fc.com.recycleview.library.FcRecycleView;
import fc.com.recycleview.library.OnLoadMoreListener;


public class LoadMoreCombinationFcActivity extends ActionBarActivity implements OnLoadMoreListener {

    private FcRecycleView fcRecycleView;
    private LoadMoreCombinationFcAdapterTest adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        fcRecycleView = (FcRecycleView) findViewById(R.id.fc_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        fcRecycleView.setLayoutManager(linearLayoutManager);
        adapter = new LoadMoreCombinationFcAdapterTest(this);

        fcRecycleView.setAdapter(adapter);
        fcRecycleView.setOnLoadMoreListener(this);
    }

    public void addData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0 ; i < 10; i++) {
            list.add("DATA_" + (i+1));
        }
        adapter.addAll(list);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Math.random() < 0.5) {
                    Toast.makeText(LoadMoreCombinationFcActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    fcRecycleView.notifyError();
                } else {
                    addData();
                    fcRecycleView.notifyNormal();
                }
            }
        }, 2000);
    }
}
