package io.dcloud.H58E83894.callback;

import android.view.View;

public interface OnAdapterListener<T> {

    void onClick(View view, int position, T t);

    void onClick(View view, int position);
}
