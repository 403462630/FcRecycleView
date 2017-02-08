package fc.recycleview.base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by rjhy on 15-3-5.
 */
public interface ItemScrollAdapter {
    public void scroll(RecyclerView.LayoutManager layoutManager, int newState);
}
