package fc.recycleview.base;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by rjhy on 15-3-5.
 */
public abstract class CombinationAdapter extends RecyclerView.Adapter {

    private RecyclerView.Adapter adapter;

    public CombinationAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(new CombinationDataObserver());
    }

    public RecyclerView.Adapter getWrapAdapter() {
        return adapter;
    }

    private class CombinationDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemMoved(fromPosition, toPosition);
        }
    }
}
