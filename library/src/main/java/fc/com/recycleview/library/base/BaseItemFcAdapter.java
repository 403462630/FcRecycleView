package fc.com.recycleview.library.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by rjhy on 15-3-5.
 */
public abstract class BaseItemFcAdapter extends RecyclerView.Adapter implements ItemFcAdapter {
    public static final int FC_ITEM_TYPE = -2000;

    @Override
    public final int getItemViewType(int position) {
        if (position == getFcItemPosition()) {
            return getFcItemViewType(position);
        } else {
            if (position > getFcItemPosition()) {
                position  = position - 1;
            }
            return getMainViewType(position);
        }
    }

    public int getMainViewType(int position) {
        return 0;
    }

    @Override
    public int getFcItemViewType(int position) {
        return FC_ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        return getCount() + 1;
    }

    @Override
    public int getFcItemPosition() {
        return getCount();
    }

    public abstract int getCount();

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == getItemViewType(getFcItemPosition())) {
            return onCreateFcItemViewHolder(parent, viewType);
        } else {
            return onCreateMainViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == getFcItemViewType(position)) {
            onBindFcItemViewHolder(holder, position);
        } else {
            if (position > getFcItemPosition()) {
                onBindMainViewHolder(holder, position - 1);
            } else {
                onBindMainViewHolder(holder, position);
            }
        }
    }

    public abstract void onBindMainViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup parent, int viewType);

}
