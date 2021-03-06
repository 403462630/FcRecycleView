package fc.recycleview;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import fc.com.recycleview.library.R;
import fc.recycleview.base.BaseItemCombinationAdapter;
import fc.recycleview.base.ItemNotifyAdapter;
import fc.recycleview.base.ItemScrollAdapter;

/**
 * Created by rjhy on 15-3-4.
 */
public class LoadMoreCombinationAdapter<T> extends BaseItemCombinationAdapter
        implements ItemScrollAdapter, ItemNotifyAdapter {
    public static final int EMPTY_ITEM_TYPE = -3001;
    public static final int ERROR_ITEM_TYPE = -3002;
    public static final int LOADING_ITEM_TYPE = -3003;
    public static final int NORMAL_ITEM_TYPE = -3004;
    public static final int LOADED_ALL_ITEM_TYPE = -3005;
    public static final int DRAGE_ITEM_TYPE = -3006;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean hasDrag = true;
    private boolean isIdleLoading = false;
    private int lastLoadingItem = 0;

    private String emptyText;
    private String errorText;
    private String loadingText;
    private String dragText;
    private String loadedAllText;
    private String normalText;
    private Handler handler = new Handler();
    private WeakReference<RecyclerView> recyclerViewRef;
    /**
     * 数据小于一屏的情况下是否显示"没有更多"的提示
     */
    private boolean showNoMoreTipsOnlyOnePage = false;

    @LayoutRes
    private  int dragRes = R.layout.load_more_drag;
    @LayoutRes
    private int emptyRes = R.layout.load_more_empty;
    @LayoutRes
    private int errorRes = R.layout.load_more_error;
    @LayoutRes
    private int loadingRes = R.layout.load_more_loading;
    @LayoutRes
    private int loadedAllRes = R.layout.load_more_loaded_all;
    @LayoutRes
    private int normalRes = R.layout.load_more_normal;

    public void setShowNoMoreTipsOnlyOnePage(boolean showNoMoreTipsOnlyOnePage) {
        this.showNoMoreTipsOnlyOnePage = showNoMoreTipsOnlyOnePage;
    }

    public void setIdleLoading(boolean idleLoading) {
        isIdleLoading = idleLoading;
    }

    public void setLastLoadingItem(int lastLoadingItem) {
        this.lastLoadingItem = lastLoadingItem;
    }

    public final void setDragRes(@LayoutRes int dragRes) {
        this.dragRes = dragRes;
        hasDrag = (dragRes != loadingRes) && dragRes != 0;
    }

    public final void setEmptyRes(@LayoutRes int emptyRes) {
        this.emptyRes = emptyRes;
    }

    public final void setErrorRes(@LayoutRes int errorRes) {
        this.errorRes = errorRes;
    }

    public final void setLoadingRes(@LayoutRes int loadingRes) {
        this.loadingRes = loadingRes;
    }

    public final void setLoadedAllRes(@LayoutRes int loadedAllRes) {
        this.loadedAllRes = loadedAllRes;
    }

    public final void setNormalRes(@LayoutRes int normalRes) {
        this.normalRes = normalRes;
    }

    public final void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public final void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public final void setDragText(String dragText) {
        this.dragText = dragText;
    }

    public final void setLoadedAllText(String loadedAllText) {
        this.loadedAllText = loadedAllText;
    }

    public final void setNormalText(String normalText) {
        this.normalText = normalText;
    }

    public final void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    private RecyclerView getRecyclerView() {
        if (recyclerViewRef == null) {
            return null;
        }
        return recyclerViewRef.get();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void setLoadItemType(LoadItemType loadType) {
        if (loadType == LoadItemType.LOADING && !hasDrag && isDragging()) {
            this.loadType = loadType;
            return;
        }
        if (this.loadType != loadType) {
            this.loadType = loadType;
            Log.i("TAG", "LoadItemType: " + loadType.name());
            notifyItemChanged(getFcItemPosition());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        notifyItemChanged(getFcItemPosition());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }
    }

    @Override
    public int getFcItemViewType(int position) {
        if (loadType == LoadItemType.ERROR) {
            return ERROR_ITEM_TYPE;
        } else if (loadType == LoadItemType.LOADING) {
            return LOADING_ITEM_TYPE;
        } else if (loadType == LoadItemType.DRAGE) {
            return DRAGE_ITEM_TYPE;
        } else {
            if (loadType == LoadItemType.LOADED_ALL) {
                if (getRealCount() == 0) {
                    return EMPTY_ITEM_TYPE;
                } else {
                    return LOADED_ALL_ITEM_TYPE;
                }
            } else {
                return NORMAL_ITEM_TYPE;
            }
        }
    }

    private LoadItemType loadType = LoadItemType.NO_LOADING;

    @Override
    public void notifyError() {
        setLoadItemType(LoadItemType.ERROR);
    }

    @Override
    public void notifyLoading() {
        setLoadItemType(LoadItemType.LOADING);
    }

    private View.OnLayoutChangeListener loadAllOnLayoutChangeListener = new View.OnLayoutChangeListener() {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            RecyclerView recyclerView = getRecyclerView();
            if (v == recyclerView && recyclerView != null) {
                v.removeOnLayoutChangeListener(loadAllOnLayoutChangeListener);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int itemCount = getFcItemPosition() + 1;
                    // 当数据大于一屏的时候显示"没有更多的"提示
                    boolean showLoadedAllItem = visibleItemCount != itemCount;

                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(getFcItemPosition());
                    if (viewHolder == null || viewHolder.getItemViewType() != LOADED_ALL_ITEM_TYPE) {
                        return;
                    }
                    viewHolder.itemView.setVisibility(showLoadedAllItem ? View.VISIBLE : View.GONE);
                }
            }
        }
    };

    @Override
    public void notifyLoadedAll() {
        setLoadItemType(LoadItemType.LOADED_ALL);
    }

    @Override
    public void notifyNormal() {
        setLoadItemType(LoadItemType.NO_LOADING);
    }

    @Override
    public void notifyDragged() {
        setLoadItemType(LoadItemType.DRAGE);
    }

    public static enum LoadItemType {
        LOADING, DRAGE ,NO_LOADING, LOADED_ALL, ERROR
    }

    public boolean isLoading() {
        return loadType == LoadItemType.LOADING;
    }

    public boolean isLoadedAll() {
        return loadType == LoadItemType.LOADED_ALL;
    }

    public boolean isError() {
        return loadType == LoadItemType.ERROR;
    }

    public boolean isDragging() {
        return loadType == LoadItemType.DRAGE;
    }

    public LoadMoreCombinationAdapter(Context context, RecyclerView.Adapter adapter) {
        super(adapter);
        this.context = context;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof ItemViewHolder) {

        } else {
            getWrapAdapter().onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof ItemViewHolder) {

        } else {
            getWrapAdapter().onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (holder instanceof ItemViewHolder) {
            return super.onFailedToRecycleView(holder);
        } else {
            return getWrapAdapter().onFailedToRecycleView(holder);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof ItemViewHolder) {
            super.onViewRecycled(holder);
        } else {
            getWrapAdapter().onViewRecycled(holder);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerViewRef = new WeakReference<>(recyclerView);
        getWrapAdapter().onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        getWrapAdapter().onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFcItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DRAGE_ITEM_TYPE:
                return new ItemViewHolder(
                        LayoutInflater.from(context).inflate(dragRes, parent, false), viewType);
            case LOADING_ITEM_TYPE:
                return new ItemViewHolder(
                        LayoutInflater.from(context).inflate(loadingRes, parent, false), viewType);
            case ERROR_ITEM_TYPE:
                return new ItemViewHolder(
                        LayoutInflater.from(context).inflate(errorRes, parent, false), viewType);
            case LOADED_ALL_ITEM_TYPE:
                return new ItemViewHolder(
                        LayoutInflater.from(context).inflate(loadedAllRes, parent, false), viewType);
            case EMPTY_ITEM_TYPE:
                return new ItemViewHolder(
                        LayoutInflater.from(context).inflate(emptyRes, parent, false), viewType);
            case NORMAL_ITEM_TYPE:
            default:
                return new ItemViewHolder(
                        LayoutInflater.from(context).inflate(normalRes, parent, false), viewType);
        }
    }

    @Override
    public void onBindFcItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder)holder;
        if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams()).setFullSpan(true);
        }
        String content = null;
//        Log.i("TAG", "viewHolder.viewType: " + viewHolder.viewType);
        switch (viewHolder.viewType) {
            case DRAGE_ITEM_TYPE:
                content = dragText;
                break;
            case LOADING_ITEM_TYPE:
                content = loadingText;
                break;
            case ERROR_ITEM_TYPE:
                content = errorText;
                if (viewHolder.actionView != null) {
                    viewHolder.actionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadMore();
                        }
                    });
                } else {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadMore();
                        }
                    });
                }
                break;
            case LOADED_ALL_ITEM_TYPE:
                content = loadedAllText;
                viewHolder.itemView.setVisibility(View.VISIBLE);
                if (!showNoMoreTipsOnlyOnePage) {
                    RecyclerView recyclerView = getRecyclerView();
                    if (recyclerView != null) {
                        recyclerView.addOnLayoutChangeListener(loadAllOnLayoutChangeListener);
                    }
                }
                break;
            case EMPTY_ITEM_TYPE:
                content = emptyText;
                break;
            case NORMAL_ITEM_TYPE:
                content = normalText;
                if (viewHolder.actionView != null) {
                    viewHolder.actionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadMore();
                        }
                    });
                } else {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadMore();
                        }
                    });
                }
                break;
        }
        if (viewHolder.contentView != null && content != null) {
            viewHolder.contentView.setText(content);
        }
    }

    private void loadMore() {
        if (!isLoading() && !isLoadedAll() && onLoadMoreListener != null) {
            setLoadItemType(LoadItemType.LOADING);
            onLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void scroll(RecyclerView.LayoutManager layoutManager, int state) {
        if (!isIdleLoading || state == RecyclerView.SCROLL_STATE_IDLE) {
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//                int visibleItemCount = linearLayoutManager.getChildCount();
//                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//                boolean flag = getFcItemPosition() >= firstVisibleItem && getFcItemPosition() - lastLoadingItem < (firstVisibleItem + visibleItemCount);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                boolean flag = lastVisibleItem + lastLoadingItem >= getFcItemPosition();
//                Log.i("TAG", "lastVisibleItem: " + lastVisibleItem + ", lastLoadingItem: " + lastLoadingItem + ", FcItemPosition: " + getFcItemPosition());
                if (flag) {
                    // 一定要post，不然在recycleView 的 OnScrollListener 的 onScrolled 回调中调用 这个scroll方法会报错，下面也是如此
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadMore();
                        }
                    });
                } else if (state != RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isDragging() && !isLoading() && !isLoadedAll() && onLoadMoreListener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setLoadItemType(LoadItemType.DRAGE);
                            }
                        });
                    }
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastSpanPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
//                Log.i("TAG", "lastSpanPositions: " + lastSpanPositions[0] + ", " + lastSpanPositions[1]);
                boolean flag = (lastSpanPositions[1] + lastLoadingItem >= getFcItemPosition()) || (lastSpanPositions[0] + lastLoadingItem >= getFcItemPosition()) ;
                if (flag) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadMore();
                        }
                    });
                } else if (state != RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isDragging() && !isLoading() && !isLoadedAll() && onLoadMoreListener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setLoadItemType(LoadItemType.DRAGE);
                            }
                        });
                    }
                }
            }
        } else if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            if (!isDragging() && !isLoading() && !isLoadedAll() && onLoadMoreListener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setLoadItemType(LoadItemType.DRAGE);
                    }
                });
            }
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        int viewType;
        TextView contentView;
        View actionView;
        public ItemViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            contentView = (TextView) itemView.findViewById(R.id.tv_content);
            actionView = itemView.findViewById(R.id.do_action_view);
        }
    }
}
