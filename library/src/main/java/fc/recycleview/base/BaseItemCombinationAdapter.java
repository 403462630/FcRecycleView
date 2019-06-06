package fc.recycleview.base;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by rjhy on 15-3-5.
 */
public abstract class BaseItemCombinationAdapter<T> extends CombinationAdapter implements ItemFcAdapter {

    public static final int FC_ITEM_TYPE = -2001;

    public BaseItemCombinationAdapter(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public final int getItemViewType(int position) {
        if (getFcItemPosition() == position) {
            return getFcItemViewType(position);
        } else {
            if (position > getFcItemPosition()) {
                position = position - 1;
            }
            return getWrapAdapter().getItemViewType(position);
        }
    }

    @Override
    public int getFcItemPosition() {
        return getRealCount();
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == getItemViewType(getFcItemPosition())) {
            return onCreateFcItemViewHolder(parent, viewType);
        } else {
            return getWrapAdapter().onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (getItemViewType(position) == getFcItemViewType(position)) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            if (position > getFcItemPosition()){
                position = position - 1;
            }
            getWrapAdapter().onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == getFcItemViewType(position)) {
            onBindFcItemViewHolder(holder, position);
        } else {
            if (position > getFcItemPosition()){
                position = position - 1;
            }
            getWrapAdapter().onBindViewHolder(holder, position);
        }
    }

    public int getRealCount() {
        return getWrapAdapter().getItemCount();
    }

    @Override
    public final int getItemCount() {
        return getRealCount() + 1;
    }

    @Override
    public int getFcItemViewType(int position) {
        return FC_ITEM_TYPE;
    }
}
