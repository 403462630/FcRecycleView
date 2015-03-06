package fc.com.recycleview.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by rjhy on 15-3-4.
 */
public interface ItemFcAdapter<VH extends RecyclerView.ViewHolder> {
    public VH onCreateItemFcViewHolder(ViewGroup parent, int viewType);
    public void onBindItemFcViewHolder(VH holder, int position);
    public int getItemFcPosition();
    public int getItemFcViewType(int position);
}
