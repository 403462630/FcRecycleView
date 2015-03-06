package fc.com.recycleview.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by rjhy on 15-3-5.
 */
public abstract class BaseItemFcAdapter extends RecyclerView.Adapter implements ItemFcAdapter {
    public static final int FC_ITEM_TYPE = -2000;

    @Override
    public final int getItemViewType(int position) {
        if (position == getItemFcPosition()) {
            return getItemFcViewType(position);
        } else {
            if (position > getItemFcPosition()) {
                position  = position - 1;
            }
            return getMainViewType(position);
        }
    }

    public int getMainViewType(int position) {
        return 0;
    }

    @Override
    public int getItemFcViewType(int position) {
        return FC_ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        return getCount() + 1;
    }

    public abstract int getCount();

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == getItemViewType(getItemFcPosition())) {
            return onCreateItemFcViewHolder(parent, viewType);
        } else {
            return onCreateMainViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == getItemFcViewType(position)) {
            onBindItemFcViewHolder(holder, position);
        } else {
            if (position > getItemFcPosition()) {
                onBindMainViewHolder(holder, position - 1);
            } else {
                onBindMainViewHolder(holder, position);
            }
        }
    }

    public abstract void onBindMainViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup parent, int viewType);

}
