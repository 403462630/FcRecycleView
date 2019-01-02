package fc.recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import fc.com.recycleview.library.R;
import fc.recycleview.base.ItemFcAdapter;
import fc.recycleview.base.ItemNotifyAdapter;
import fc.recycleview.base.ItemScrollAdapter;

/**
 * Created by rjhy on 15-3-4.
 */
public class LoadMoreRecycleView extends RecyclerView {

    private boolean flag;

    private ItemNotifyAdapter itemNotifyAdapter;
    private LoadMoreCombinationAdapter fcAdapter;
    private ItemScrollAdapter itemScrollAdapter;

    private OnScrollListener mOnScrollListener;
    private String emptyText;

    @LayoutRes
    private  int dragRes = 0;
    @LayoutRes
    private int emptyRes = 0;
    @LayoutRes
    private int errorRes = 0;
    @LayoutRes
    private int loadingRes = 0;
    @LayoutRes
    private int loadedAllRes = 0;
    @LayoutRes
    private int normalRes = 0;
    private boolean isIdleLoading = false;
    private int lastLoadingItem = 0;

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
        Adapter adapter = getAdapter();
        if (adapter != null) {
            if (adapter instanceof LoadMoreCombinationAdapter) {
                ((LoadMoreCombinationAdapter)adapter).setEmptyText(emptyText);
            }
        }
    }

    public LoadMoreRecycleView(Context context) {
        this(context, null);
    }

    public LoadMoreRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(onScrollListener);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadMoreRecycleView);
        dragRes = typedArray.getResourceId(R.styleable.LoadMoreRecycleView_load_more_layout_drag, 0);
        emptyRes = typedArray.getResourceId(R.styleable.LoadMoreRecycleView_load_more_layout_empty, 0);
        errorRes = typedArray.getResourceId(R.styleable.LoadMoreRecycleView_load_more_layout_error, 0);
        loadedAllRes = typedArray.getResourceId(R.styleable.LoadMoreRecycleView_load_more_layout_loaded_all, 0);
        loadingRes = typedArray.getResourceId(R.styleable.LoadMoreRecycleView_load_more_layout_loading, 0);
        normalRes = typedArray.getResourceId(R.styleable.LoadMoreRecycleView_load_more_layout_normal, 0);
        isIdleLoading = typedArray.getBoolean(R.styleable.LoadMoreRecycleView_load_more_idle_loading, false);
        lastLoadingItem = typedArray.getInt(R.styleable.LoadMoreRecycleView_load_more_last_loading_item, 0);
        typedArray.recycle();
    }

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }
            if (flag) {
                if (itemScrollAdapter != null) {
                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    itemScrollAdapter.scroll(layoutManager, newState);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
            if (!isIdleLoading) {
                if (flag) {
                    if (itemScrollAdapter != null) {
                        boolean scrollFlag = true;
                        LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof LinearLayoutManager) {
                            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                            if (orientation == LinearLayoutManager.VERTICAL) {
                                scrollFlag = dy > 0;
                            } else {
                                scrollFlag = dx > 0;
                            }
                        }
                        if (scrollFlag) {
                            itemScrollAdapter.scroll(layoutManager, recyclerView.getScrollState());
                        }
                    }
                }
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
            if (adapter instanceof LoadMoreCombinationAdapter) {
                initLoadMoreCombinationAdapter((LoadMoreCombinationAdapter) adapter);
            }
            super.setAdapter(adapter);
        } else {
            fcAdapter = new LoadMoreCombinationAdapter<>(getContext(), adapter);
            initLoadMoreCombinationAdapter(fcAdapter);
            super.setAdapter(fcAdapter);
        }

        if (getAdapter() instanceof ItemScrollAdapter) {
            itemScrollAdapter = (ItemScrollAdapter) getAdapter();
        }

        if (getAdapter() instanceof ItemNotifyAdapter) {
            itemNotifyAdapter = (ItemNotifyAdapter) getAdapter();
        }
    }

    private void initLoadMoreCombinationAdapter(LoadMoreCombinationAdapter adapter) {
        adapter.setLastLoadingItem(lastLoadingItem);
        adapter.setIdleLoading(isIdleLoading);
        if (emptyRes != 0) {
            adapter.setEmptyRes(emptyRes);
        }
        if (errorRes != 0) {
            adapter.setErrorRes(errorRes);
        }
        if (loadingRes != 0) {
            adapter.setLoadingRes(loadingRes);
        }
        if (dragRes != 0) {
            adapter.setDragRes(dragRes);
        }
        if (loadedAllRes != 0) {
            adapter.setLoadedAllRes(loadedAllRes);
        }
        if (normalRes != 0) {
            adapter.setNormalRes(normalRes);
        }
        setEmptyText(emptyText);
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
            if (adapter instanceof LoadMoreCombinationAdapter) {
                ((LoadMoreCombinationAdapter)adapter).setOnLoadMoreListener(listener);
            }
        }
    }
}
