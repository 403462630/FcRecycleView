package fc.com.fcrecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fc.com.fcrecycleview.R;
import fc.com.recycleview.library.LoadMoreFcAdapter;

/**
 * Created by rjhy on 15-3-6.
 */
public class LoadMoreFcAdapterTest extends LoadMoreFcAdapter {

    private List<String> data = new ArrayList<>();
    private Context context;

    public LoadMoreFcAdapterTest(Context context) {
        super(context);
        this.context = context;
    }
    
    public void addAll(List<String> list) {
        data.addAll(list);
        notifyItemRangeInserted(data.size() - list.size(), list.size());
        if (list.size() == 0) {
            notifyLoadedAll();
        } else {
            notifyNormal();
        }
    }

    public void reset(List<String> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void onBindMainViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MainViewHolder)holder).textView.setText(data.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_item, parent, false));
    }

    static class MainViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MainViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_text);
        }
    }
}
