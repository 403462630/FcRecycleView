package fc.com.recycleview.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import fc.com.recycleview.library.R;

/**
 * Created by rjhy on 15-3-4.
 */
public class LoadMoreCombinationFcAdapter<T> extends BaseItemCombinationFcAdapter implements ItemScrollAdapter {

    private Context context;
    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoadItemType(LoadItemType loadType) {
        this.loadType = loadType;
        notifyItemChanged(getItemFcPosition());
    }

    private LoadItemType loadType = LoadItemType.NO_LOADING;

    public static enum LoadItemType{
        LOADING, NO_LOADING, LOADED_ALL, ERROR
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

    public LoadMoreCombinationFcAdapter(Context context, RecyclerView.Adapter adapter) {
        super(adapter);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemFcViewHolder(ViewGroup parent, int viewType) {
        return new LoadMoreViewHolder(LayoutInflater.from(context).inflate(R.layout.load_more, parent, false));
    }

    @Override
    public void onBindItemFcViewHolder(RecyclerView.ViewHolder holder, int position) {
        LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
        switch (loadType) {
            case LOADING:
                loadMoreViewHolder.progressBar.setVisibility(View.VISIBLE);
                loadMoreViewHolder.contentView.setVisibility(View.VISIBLE);
                loadMoreViewHolder.contentView.setText("数据正在加载中");
                break;
            case LOADED_ALL:
                loadMoreViewHolder.progressBar.setVisibility(View.GONE);
                loadMoreViewHolder.contentView.setVisibility(View.VISIBLE);
                loadMoreViewHolder.contentView.setText("已加载完所有数据");
                break;
            case NO_LOADING:
            case ERROR:
                loadMoreViewHolder.progressBar.setVisibility(View.GONE);
                loadMoreViewHolder.contentView.setVisibility(View.VISIBLE);
                loadMoreViewHolder.contentView.setText("点击加载更多");
                loadMoreViewHolder.loadMoreContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLoadMoreListener != null && !isLoading() && !isLoadedAll()) {
                            setLoadItemType(LoadItemType.LOADING);
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                });
                break;
            default:
                loadMoreViewHolder.progressBar.setVisibility(View.GONE);
                loadMoreViewHolder.contentView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemFcPosition() {
        return getCount();
    }

    @Override
    public void scroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getItemFcPosition() >= firstVisibleItem && getItemFcPosition() <= (firstVisibleItem + visibleItemCount)) {
            if (!isLoading() && !isLoadedAll() && onLoadMoreListener != null) {
                setLoadItemType(LoadItemType.LOADING);
                onLoadMoreListener.onLoadMore();
            }
        }
    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder{

        LinearLayout loadMoreContainer;
        ProgressBar progressBar;
        TextView contentView;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            loadMoreContainer = (LinearLayout) itemView.findViewById(R.id.ll_load_more_container);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb_loadding);
            contentView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public static interface OnLoadMoreListener {
        public void onLoadMore();
    }
}
