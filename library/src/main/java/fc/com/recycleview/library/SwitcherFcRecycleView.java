package fc.com.recycleview.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by rjhy on 15-3-6.
 */
public class SwitcherFcRecycleView extends FcRecycleView {

    private View emptyView;
    private View progressView;
    private View errorView;
    private FrameLayout framelayout;

    private int layout_progress = -1;
    private int layout_empty = -1;
    private int layout_error = -1;
    private boolean isInit;

    private SwitcherType switcherType = SwitcherType.CONTENT;

    private enum SwitcherType{
        EMPTY, PROGRESS, ERROR, CONTENT
    }

    public SwitcherFcRecycleView(Context context) {
        this(context, null);
    }

    public SwitcherFcRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(attrs);
    }

    private void initLayout() {
        framelayout = new FrameLayout(getContext());
        ViewGroup.LayoutParams params = getLayoutParams();
        framelayout.setLayoutParams(params);

        if (layout_progress != -1 && progressView == null) {
            progressView = LayoutInflater.from(getContext()).inflate(layout_progress, framelayout, false);
            progressView.setVisibility(View.GONE);
        }

        if (layout_empty != -1 && emptyView == null) {
            emptyView = LayoutInflater.from(getContext()).inflate(layout_empty, framelayout, false);
            emptyView.setVisibility(View.GONE);
        }

        if (layout_error != -1 && errorView == null) {
            errorView = LayoutInflater.from(getContext()).inflate(layout_error, framelayout, false);
            errorView.setVisibility(View.GONE);
        }

        ViewGroup parent = (ViewGroup) getParent();
        int index = parent.indexOfChild(this);
        parent.removeView(this);
        parent.addView(framelayout, index);

        framelayout.addView(this);
        if (emptyView != null) {
            framelayout.addView(emptyView);
        }
        if (progressView != null) {
            framelayout.addView(progressView);
        }
        if (errorView != null) {
            framelayout.addView(errorView);
        }

        parent.invalidate();
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.switcherFcRecycleView);
        layout_progress = a.getResourceId(R.styleable.switcherFcRecycleView_layout_progress, R.layout.switcher_progress);
        layout_empty = a.getResourceId(R.styleable.switcherFcRecycleView_layout_empty, R.layout.switcher_empty);
        layout_error = a.getResourceId(R.styleable.switcherFcRecycleView_layout_error, R.layout.switcher_error);
        a.recycle();

    }

    public void showEmpty() {
        ensureInit();
        if (emptyView != null) {
            hide();
            showView(SwitcherType.EMPTY);
        }
    }

    public void showProgress() {
        ensureInit();
        if (progressView != null) {
            hide();
            showView(SwitcherType.PROGRESS);
        }
    }

    public void showError() {
        ensureInit();
        if (errorView != null) {
            hide();
            showView(SwitcherType.ERROR);
        }
    }

    private synchronized void ensureInit() {
        if (!isInit) {
            initLayout();
            isInit = true;
        }
    }

    public void showContent() {
        ensureInit();
        hide();
        showView(SwitcherType.CONTENT);
    }

    private void setContentShown(SwitcherType switcherType) {
        switch (switcherType) {
            case EMPTY:
                showEmpty();
                break;
            case PROGRESS:
                showProgress();
                break;
            case ERROR:
                showError();
                break;
            case CONTENT:
                showContent();
                break;
        }
    }

    private void showView(SwitcherType switcherType) {
        this.switcherType = switcherType;
        switch (switcherType) {
            case EMPTY:
                emptyView.setVisibility(View.VISIBLE);
                break;
            case PROGRESS:
                progressView.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                errorView.setVisibility(View.VISIBLE);
                break;
            case CONTENT:
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hide() {
        switch (switcherType) {
            case EMPTY:
                emptyView.setVisibility(View.GONE);
                break;
            case PROGRESS:
                progressView.setVisibility(View.GONE);
                break;
            case ERROR:
                errorView.setVisibility(View.GONE);
                break;
            case CONTENT:
                this.setVisibility(View.GONE);
                break;
        }
    }

    public boolean isShowEmpty() {
        return switcherType == SwitcherType.EMPTY;
    }

    public boolean isShowProgress() {
        return switcherType == SwitcherType.PROGRESS;
    }

    public boolean isShowError() {
        return switcherType == SwitcherType.ERROR;
    }

    public boolean isShowContent() {
        return switcherType == SwitcherType.CONTENT;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcelable", superState);
        bundle.putInt("switcher_type", switcherType.ordinal());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        super.onRestoreInstanceState(bundle.getParcelable("parcelable"));
        setContentShown(SwitcherType.values()[bundle.getInt("switcher_type")]);
    }
}
