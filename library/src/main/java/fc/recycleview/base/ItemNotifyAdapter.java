package fc.recycleview.base;

/**
 * Created by rjhy on 15-3-4.
 */
public interface ItemNotifyAdapter {
    public void notifyError();

    public void notifyLoading();

    public void notifyLoadedAll();

    public void notifyNormal();
}