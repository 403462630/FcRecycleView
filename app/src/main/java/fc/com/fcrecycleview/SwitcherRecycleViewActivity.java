package fc.com.fcrecycleview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import fc.com.fcrecycleview.adapter.LoadMoreFcAdapterTest;
import fc.com.recycleview.library.OnLoadMoreListener;
import fc.com.recycleview.library.SwitcherFcRecycleView;


public class SwitcherRecycleViewActivity extends ActionBarActivity implements OnLoadMoreListener {

    private SwitcherFcRecycleView fcRecycleView;
    private LoadMoreFcAdapterTest adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher_test);
        fcRecycleView = (SwitcherFcRecycleView) findViewById(R.id.fc_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        fcRecycleView.setLayoutManager(linearLayoutManager);
        adapter = new LoadMoreFcAdapterTest(this);
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
                    Toast.makeText(SwitcherRecycleViewActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    fcRecycleView.notifyError();
                } else {
                    addData();
                }
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_progress) {
            fcRecycleView.showProgress();
            return true;
        } else if (id == R.id.action_empty) {
            fcRecycleView.showEmpty();
            return true;
        } else if (id == R.id.action_error) {
            fcRecycleView.showError();
            return true;
        } else if (id == R.id.action_content) {
            fcRecycleView.showContent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
