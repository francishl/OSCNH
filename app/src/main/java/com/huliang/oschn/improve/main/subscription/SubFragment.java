package com.huliang.oschn.improve.main.subscription;

import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.base.fragments.BaseGeneralRecyclerFragment;
import com.huliang.oschn.improve.bean.SubBean;

import java.lang.reflect.Type;

/**
 * DynamicTabFragment tab 对应子页面
 * <p/>
 * Created by huliang on 4/5/17.
 */
public class SubFragment extends BaseGeneralRecyclerFragment<SubBean> {

    @Override
    protected BaseRecyclerAdapter<SubBean> getRecyclerAdapter() {
        return null;
    }

    @Override
    protected Type getType() {
        return null;
    }
}
