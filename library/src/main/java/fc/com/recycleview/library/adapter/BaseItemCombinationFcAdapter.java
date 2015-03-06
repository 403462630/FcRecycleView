package fc.com.recycleview.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by rjhy on 15-3-5.
 */
public abstract class BaseItemCombinationFcAdapter<T> extends CombinationFcAdapter implements ItemFcAdapter {

    public static final int FC_ITEM_TYPE = -2001;

    public BaseItemCombinationFcAdapter(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public final int getItemViewType(int position) {
        if (getItemFcPosition() == position) {
            return getItemFcViewType(position);
        } else {
            if (position > getItemFcPosition()) {
                position = position - 1;
            }
            return getWrapAdapter().getItemViewType(position);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == getItemViewType(getItemFcPosition())) {
            return onCreateItemFcViewHolder(parent, viewType);
        } else {
            return getWrapAdapter().onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == getItemFcViewType(position)) {
            onBindItemFcViewHolder(holder, position);
        } else {
            if (position > getItemFcPosition()){
                position = position - 1;
            }
            getWrapAdapter().onBindViewHolder(holder, position);
        }
    }

    public int getCount() {
        return getWrapAdapter().getItemCount();
    }

    @Override
    public final int getItemCount() {
        return getCount() + 1;
    }

    @Override
    public int getItemFcViewType(int position) {
        return FC_ITEM_TYPE;
    }
}
