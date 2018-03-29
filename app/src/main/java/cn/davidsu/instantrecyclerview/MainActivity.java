package cn.davidsu.instantrecyclerview;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import cn.davidsu.instantrecyclerview.data.AnalogData;
import cn.davidsu.library.itemdecoration.SpaceItemDecoration;
import cn.davidsu.library.listener.Listener;
import cn.davidsu.library.loadMore.AbsLoadingMoreView;
import cn.davidsu.library.loadMore.DefaultLoadMoreView;
import cn.davidsu.library.util.ViewUtil;

public class MainActivity extends AppCompatActivity implements Listener.OnItemClickListener, Listener.LoadMoreListener {

    private RecyclerView rvMain;
    private MainListAdapter mMainListAdapter;
    private View mLoadingView;
    private View mHeaderView;

    private static int MAX_SIZE = 35;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        initListView();

        mMainListAdapter.addHeaderView(mHeaderView);

        loadData(true);
    }

    private void bindViews() {
        rvMain = findViewById(R.id.rv_main);
        mLoadingView = getView(R.layout.view_loading);
        mHeaderView = getView(R.layout.view_header);
    }


    private void initListView() {
        int space = ViewUtil.dp2px(this, 1);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.addItemDecoration(new SpaceItemDecoration(space, space, space, 0));
        rvMain.setVerticalScrollBarEnabled(false);
        mMainListAdapter = createAdapter();
        rvMain.setAdapter(mMainListAdapter);
    }

    /**
     * 创建并配置Adapter
     * @return
     */
    private MainListAdapter createAdapter() {
        mMainListAdapter = new MainListAdapter();
        mMainListAdapter.setLoadingView(mLoadingView)
                .setLoadingMoreView(new DefaultLoadMoreView(rvMain))
                .preLoadMoreNum(5)
                .setOnItemClickListener(this)
                .setOnLoadMoreListener(rvMain, false, this);
        return mMainListAdapter;
    }


    private void loadData(final boolean isFirstPage) {
        if (isFirstPage) {
            mMainListAdapter.showLoading();
        }
        rvMain.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstPage) {
                    mMainListAdapter.setDataList(AnalogData.getPageData(10));
                } else {
                    mMainListAdapter.addData(AnalogData.getPageData(5));
                }

                mMainListAdapter.loadMoreFinish();
            }
        }, 1500);
    }


    private View getView(@LayoutRes int layoutId) {
        return LayoutInflater.from(this).inflate(layoutId, null, false);
    }

    @Override
    public void onItemClick(Object data, View view, int position, int viewType) {
        if (viewType == MainListAdapter.VIEW_TYPE_1) {
            Toast.makeText(this, "Type1 位置：" + position, Toast.LENGTH_SHORT).show();
        } else if (viewType == MainListAdapter.VIEW_TYPE_2) {
            Toast.makeText(this, "Type2 位置：" + position, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void loadMore() {
        if (mMainListAdapter.getDataList().size() > MAX_SIZE) {
            mMainListAdapter.setLoadMoreState(AbsLoadingMoreView.STATE_END);
        } else {
            loadData(false);
        }
    }
}
