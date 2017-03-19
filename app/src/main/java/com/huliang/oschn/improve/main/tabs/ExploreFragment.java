package com.huliang.oschn.improve.main.tabs;

import android.view.View;
import android.widget.Toast;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.fragments.BaseTitleFragment;

/**
 * Created by huliang on 17/3/19.
 */
public class ExploreFragment extends BaseTitleFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    protected int getTitleRes() {
        return R.string.main_tab_name_explore;
    }

    @Override
    protected int getIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected View.OnClickListener getIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "btn_search_normal", Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }
}
