package fc.com.recycleview.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import fc.com.recycleview.library.adapter.ItemFcAdapter;
import fc.com.recycleview.library.adapter.ItemScrollAdapter;
import fc.com.recycleview.library.adapter.LoadMoreCombinationFcAdapter;

/**
 * Created by rjhy on 15-3-4.
 */
public class FcRecycleView extends RecyclerView {

    private LoadMoreCombinationFcAdapter fcAdapter;
    private ItemScrollAdapter itemScrollAdapter;

    private OnScrollListener mOnScrollListener;

    private boolean loading;

    public FcRecycleView(Context context) {
        this(context, null);
    }

    public FcRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(onScrollListener);
    }

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
            }

            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
            if (itemScrollAdapter != null) {
                itemScrollAdapter.scroll(pastVisiblesItems, visibleItemCount, totalItemCount);
            }
        }
    };

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        if (this.onScrollListener == onScrollListener) {
            super.setOnScrollListener(onScrollListener);
        } else {
            mOnScrollListener = onScrollListener;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof ItemFcAdapter) {
            super.setAdapter(adapter);
        } else {
            fcAdapter = new LoadMoreCombinationFcAdapter<>(getContext(), adapter);
            super.setAdapter(fcAdapter);
        }

        if (getAdapter() instanceof ItemScrollAdapter) {
            itemScrollAdapter = (ItemScrollAdapter) getAdapter();
        }
    }

    @Override
    public Adapter getAdapter() {
        if (fcAdapter != null) {
            return fcAdapter;
        } else {
            return super.getAdapter();
        }
    }
}
