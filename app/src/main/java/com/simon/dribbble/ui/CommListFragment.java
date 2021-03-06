package com.simon.dribbble.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.simon.agiledevelop.log.LLog;
import com.simon.agiledevelop.mvpframe.BaseFragment;
import com.simon.agiledevelop.recycler.adapter.RecycledAdapter;
import com.simon.agiledevelop.recycler.listeners.OnItemClickListener;
import com.simon.agiledevelop.state.StateView;
import com.simon.agiledevelop.utils.App;
import com.simon.agiledevelop.utils.ToastHelper;
import com.simon.dribbble.R;
import com.simon.dribbble.data.Api;
import com.simon.dribbble.util.DialogHelp;
import com.simon.dribbble.widget.loadingdia.SpotsDialog;

import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/1/9
 * @email hanzx1024@gmail.com
 */

public abstract class CommListFragment<P extends CommListPresenter, A extends RecycledAdapter>
        extends BaseFragment<P> implements CommListContract.View {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private SpotsDialog mLoadingDialog;

    protected A mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setColorSchemeResources(R.color.purple_500, R.color.blue_500, R.color
                .orange_500, R.color.pink_500);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.xrlv_list);
        LinearLayoutManager rlm = new LinearLayoutManager(getActivity());
        rlm.setRecycleChildrenOnDetach(true);
        mRecyclerView.setLayoutManager(rlm);

        if (mAdapter == null) {
            mAdapter = getListAdapter();
//            mRecyclerView.setRecycledViewPool(mAdapter.g);
            mAdapter.openAnimation(RecycledAdapter.SCALEIN);
            mAdapter.setLoadMoreEnable(isLoadMoreEnabled());
        }

    }

    @Override
    protected StateView getLoadingView(View view) {
        return (StateView) view.findViewById(R.id.stateView_list);
    }

    @Override
    protected void initEventAndData() {

        mRefreshLayout.setRefreshing(isRefreshing());
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });


        if (isLoadMoreEnabled()) {
            mAdapter.setOnLoadMoreListener(new RecycledAdapter.LoadMoreListener() {
                @Override
                public void onLoadMore() {
                    moreData();
                }
            });
        }

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            protected void onItemClick(RecycledAdapter adapter, RecyclerView recyclerView, View
                    view, int position) {
                itemClick(view, position);
            }
        });
    }

    @Override
    public void showList(List lists) {
        LLog.d("加载数据: " + lists.size());
        showContent();
        hideDialog();

        mAdapter.setNewData(lists);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void refreshComments(List lists) {
        mRefreshLayout.setRefreshing(false);

        if (!lists.isEmpty()) {
            mAdapter.setNewData(lists);
        } else {
            ToastHelper.showLongToast(App.INSTANCE, "刷新失败");
        }
    }

    @Override
    public void moreComments(List lists) {
        mAdapter.loadComplete();

        if (!lists.isEmpty()) {
            mAdapter.appendData(lists);
        } else {
            mAdapter.showNOMoreView();
        }
    }

    @Override
    public void showLoading(int action, String msg) {
        if (Api.EVENT_BEGIN == action) {
            showDialog();
        }
    }

    @Override
    public void onEmpty(String msg) {
        hideDialog();

        showEmtry(msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showLongToast(App.INSTANCE, "都说没有数据了，还点~");
            }
        });
    }

    @Override
    public void onFailed(final int action, String msg) {
        hideDialog();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry(action);
            }
        };
        if (Api.EVENT_BEGIN == action && msg.contains("网络")) {
            showNetworkError(msg, onClickListener);
        } else {
            showError(msg, onClickListener);
        }
    }

    @Override
    public void onCompleted(int action) {

    }

    private void showDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogHelp.getLoadingDialog(getActivity(), "正在加载...");
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void hideDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 是否支持自动加载更多，默认不支持，子类可以重写此方法进行更改
     *
     * @return
     */
    protected boolean isLoadMoreEnabled() {
        return true;
    }

    /**
     * 一进入页面是否刷新，默认不刷新，子类可以重写此方法进行更改
     *
     * @return
     */
    protected boolean isRefreshing() {
        return false;
    }


    protected void refreshData() {

    }

    protected void moreData() {

    }

    protected void retry(int action) {

    }

    protected abstract A getListAdapter();

    protected abstract void itemClick(View view, int position);

}
