package fc.com.recycleview.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import fc.com.recycleview.library.base.ItemFcAdapter;
import fc.com.recycleview.library.base.ItemNotifyAdapter;
import fc.com.recycleview.library.base.ItemScrollAdapter;

/**
 * Created by rjhy on 15-3-4.
 */
public class FcRecycleView extends RecyclerView {

    private boolean flag;

    private ItemNotifyAdapter itemNotifyAdapter;
    private LoadMoreCombinationFcAdapter fcAdapter;
    private ItemScrollAdapter itemScrollAdapter;

    private OnScrollListener mOnScrollListener;

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

            if (flag && newState == SCROLL_STATE_IDLE) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                if (itemScrollAdapter != null) {
                    itemScrollAdapter.scroll(pastVisiblesItems, visibleItemCount, totalItemCount);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                flag = true;
            }
        }, 1000);
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

        if (getAdapter() instanceof ItemNotifyAdapter) {
            itemNotifyAdapter = (ItemNotifyAdapter) getAdapter();
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

    public void notifyError() {
        if (itemNotifyAdapter != null) {
            itemNotifyAdapter.notifyError();
        }
    }


    public void notifyLoadding() {
        if (itemNotifyAdapter != null) {
            itemNotifyAdapter.notifyLoading();
        }
    }


    public void notifyLoadedAll() {
        if (itemNotifyAdapter != null) {
            itemNotifyAdapter.notifyLoadedAll();
        }
    }


    public void notifyNormal() {
        if (itemNotifyAdapter != null) {
            itemNotifyAdapter.notifyNormal();
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        Adapter adapter = getAdapter();
        if (adapter != null) {
            if (adapter instanceof LoadMoreCombinationFcAdapter) {
                ((LoadMoreCombinationFcAdapter)adapter).setOnLoadMoreListener(listener);
            } else if (adapter instanceof LoadMoreFcAdapter) {
                ((LoadMoreFcAdapter)adapter).setOnLoadMoreListener(listener);
            }
        }
    }
}
