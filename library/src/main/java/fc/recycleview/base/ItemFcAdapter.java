package fc.recycleview.base;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by rjhy on 15-3-4.
 */
public interface ItemFcAdapter<VH extends RecyclerView.ViewHolder> {
    public VH onCreateFcItemViewHolder(ViewGroup parent, int viewType);
    public void onBindFcItemViewHolder(VH holder, int position);
    public int getFcItemPosition();
    public int getFcItemViewType(int position);
}
