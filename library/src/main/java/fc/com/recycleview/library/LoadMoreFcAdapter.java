package fc.com.recycleview.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import fc.com.recycleview.library.base.BaseItemFcAdapter;
import fc.com.recycleview.library.base.ItemNotifyAdapter;
import fc.com.recycleview.library.base.ItemScrollAdapter;

/**
 * Created by rjhy on 15-3-5.
 */
public abstract class LoadMoreFcAdapter extends BaseItemFcAdapter implements ItemScrollAdapter, ItemNotifyAdapter {

    private Context context;
    private OnLoadMoreListener onLoadMoreListener;

    private void setLoadItemType(LoadItemType loadType) {
        this.loadType = loadType;
        notifyItemChanged(getFcItemPosition());
    }

    private LoadItemType loadType = LoadItemType.NO_LOADING;

    public static enum LoadItemType{
        LOADING, NO_LOADING, LOADED_ALL, ERROR
    }

    protected void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void notifyError() {
        setLoadItemType(LoadItemType.ERROR);
    }

    @Override
    public void notifyLoading() {
        setLoadItemType(LoadItemType.LOADING);
    }

    @Override
    public void notifyLoadedAll() {
        setLoadItemType(LoadItemType.LOADED_ALL);
    }

    @Override
    public void notifyNormal() {
        setLoadItemType(LoadItemType.NO_LOADING);
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

    public LoadMoreFcAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getFcItemPosition() {
        return getCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateFcItemViewHolder(ViewGroup parent, int viewType) {
        return new LoadMoreViewHolder(LayoutInflater.from(context).inflate(R.layout.load_more, parent, false));
    }

    @Override
    public void onBindFcItemViewHolder(RecyclerView.ViewHolder holder, int position) {
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
    public void scroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getFcItemPosition() >= firstVisibleItem && getFcItemPosition() < (firstVisibleItem + visibleItemCount)) {
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
}
