package cn.davidsu.library;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.davidsu.library.listener.Listener;
import cn.davidsu.library.loadMore.AbsLoadingMoreView;
import cn.davidsu.library.loadMore.DefaultLoadMoreView;
import cn.davidsu.library.support.EnumStatusView;
import cn.davidsu.library.support.NoOpViewHolder;
import cn.davidsu.library.util.ListUtil;
import cn.davidsu.library.util.RVUtil;
import cn.davidsu.library.util.ViewTypeUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cn.davidsu.library.BaseItemType.VIEW_TYPE_FOOTER;
import static cn.davidsu.library.BaseItemType.VIEW_TYPE_HEADER;
import static cn.davidsu.library.BaseItemType.VIEW_TYPE_LOAD_MORE;
import static cn.davidsu.library.BaseItemType.VIEW_TYPE_STATUS;
import static cn.davidsu.library.support.EnumStatusView.STATUS_VIEW_DATA_EMPTY;
import static cn.davidsu.library.support.EnumStatusView.STATUS_VIEW_LOADING;
import static cn.davidsu.library.support.EnumStatusView.STATUS_VIEW_LOAD_FAILED;

/**
 * Created by cxzheng on 2018/2/3.
 * <p>
 * <h3>名词解释:</h3>
 * <ul>
 * <li><em>StatusView:</em> 状态View,包括加载中View、加载数据为空View、加载失败View.</li>
 * <li><em>DataPosition:</em> 数据中的位置，与下面LayoutPosition的区别是LayoutPosition指的是布局上的位置，
 * 而DataPosition只考虑数据，不考虑header、footer、statusView等.</li>
 * <li><em>LayoutPosition:</em> 布局上的位置，包括了header、footer、statusView等.</li>
 * </ul>
 * <p>
 * <p>
 * <h3>支持的功能：</h3>
 * <ul>
 * <p>
 * <li>1.支持数据的全量更新刷新{@link #setDataList(List)},和增量更新刷新{@link #addData(List)}，并提供index固定位置增量 </li>
 * <p>
 * <li>2.支持header、footer的增删改，并可以指定index，提高了增删改的效率和性能
 * {@link #addHeaderView(View)}{@link #removeHeaderView(View)}{@link #removeAllHeaderView()}，{@link #addFooterView(View)}{@link #removeFooterView(View)}{@link #removeAllFooterView()}</li>
 * <p>
 * <li>3.支持多itemType列表方便拓展新的类型{@link #addItemType(int, ItemViewDelegate)},
 * 支持了当只有一种itemType时可以不用在原始数据上多包一层，当有多种itemType时需在数据model中指定itemType</li>
 * <p>
 * <li>4.支持添加loading view{@link #setLoadingView(View)}、failed view{@link #setLoadFailedView(View)}、dataEmpty view{@link #setDataEmptyView(View)}，
 * 展示时调用{@link #showLoading()}{@link #showError()}{@link #showDataEmpty()},解决原先这些状态View导致过度绘制的问题</li>
 * <p>
 * <li>5.支持设置item点击事件{@link #setOnItemClickListener(Listener.OnItemClickListener)}
 * 和长按事件{@link #setOnItemLongClickListener(Listener.OnItemLongClickListener)},避免item复用时重复设置事件</li>
 * <p>
 * <li>6.支持上拉加载更多，可通过{@link #isOpenLoadMore(boolean)}来关闭上拉加载更多功能。loadMore自动加载,可通过{@link #preLoadMoreNum(int)}来设置发起laodMore的时机
 * loadMoreView默认使用{@link DefaultLoadMoreView},可继承自{@link AbsLoadingMoreView}来实现自定义的LoadMoreView，
 * 并通过{@link #setLoadingMoreView(AbsLoadingMoreView)}来替换</li>
 * <p>
 * <li>7.展示statusView时默认隐藏header和footer，可通过{@link #isHideHeaderWhenStatusView(boolean)}和{@link #isHideFooterWhenStatusView(boolean)}来控制</li>
 * </ul>
 */

public class SuperBaseAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    private Context context;
    @NonNull
    protected List<T> mDataList = new ArrayList<>();

    @NonNull
    private DelegateScheduler mDelegateScheduler = new DelegateScheduler();

    @Nullable
    private LinearLayout mHeaderContainer;
    @Nullable
    private LinearLayout mFooterContainer;
    @Nullable
    private FrameLayout mStatusViewContainer;
    @Nullable
    private AbsLoadingMoreView mLoadingMoreView;

    @Nullable
    private View mLoadingView;
    @Nullable
    private View mDataEmptyView;
    @Nullable
    private View mLoadFailedView;

    private boolean isOpenStatusView = true;//是否启用statusView
    private boolean isOpenLoadMore = true;//是否开启加载更多
    private boolean isHideHeaderWhenStatusView = true;//是否在展示statusView的时候隐藏header
    private boolean isHideFooterWhenStatusView = true;//是否在展示statusView的时候隐藏footer
    private int mPreLoadMoreNum = 3;//提前几个item开始加载更多,默认3
    private boolean isRealTimeLoadMore = false;//loadMore监听有两种模式 true:滚动实时监听  false:滚动状态Changed时才监听

    @Nullable
    private Listener.OnItemClickListener mItemClickListener;
    @Nullable
    private Listener.OnItemLongClickListener mItemLongClickListener;
    @Nullable
    private Listener.LoadMoreListener mLoadMoreListener;

    public SuperBaseAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if (isOpenLoadMore && mLoadingMoreView == null) {
            mLoadingMoreView = new DefaultLoadMoreView(parent);
        }
        VH holder = getViewHolder(parent, viewType);

        if (holder != null && RVUtil.isViewTypeFromData(viewType)) {
            bindListener(parent, holder, viewType);
        }
        return holder;
    }

    @Nullable
    private VH getViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER && mHeaderContainer != null) {
            return (VH) new NoOpViewHolder(mHeaderContainer);

        } else if (viewType == VIEW_TYPE_FOOTER && mFooterContainer != null) {
            return (VH) new NoOpViewHolder(mFooterContainer);

        } else if (viewType == VIEW_TYPE_STATUS && mStatusViewContainer != null) {
            return (VH) new NoOpViewHolder(mStatusViewContainer);

        } else if (viewType == VIEW_TYPE_LOAD_MORE && mLoadingMoreView != null) {
            //每次createViewHolder都要创建新的View，否则之后快速上拉时如果上一个此类型item的动画还没完成则item不会被放进scrap回收池，
            // 此时要复用，若到scrap池中找不到，则会重新createViewHolder,这时如果重用上次的全局变量view,则会crash,因为这个view实际上和RecyclerView还是attach状态，因为它还没有完成动画
            //Exception：Added View has RecyclerView as parent but view is not a real child
            View view = LayoutInflater.from(parent.getContext()).inflate(mLoadingMoreView.getRootViewLayout(), parent, false);
            mLoadingMoreView.resetRootView(view);
            return (VH) new NoOpViewHolder(view);
        } else {
            ItemViewDelegate delegate = mDelegateScheduler.getItemViewDelegateByItemType(viewType);
            return delegate != null ? (VH) delegate.createViewHolder(parent) : null;
        }
    }

    @Override
    public void onBindViewHolder(@Nullable VH holder, int position) {
        if (holder == null) {
            return;
        }
        //实时判断是否需要开始加载更多
        if (isRealTimeLoadMore) {
            checkStartLoadMore(position);
        }

        int itemType = holder.getItemViewType();
        ItemViewDelegate itemViewDelegate = mDelegateScheduler.getItemViewDelegateByItemType(itemType);

        if (itemType == VIEW_TYPE_HEADER || itemType == VIEW_TYPE_FOOTER || itemType == VIEW_TYPE_STATUS
                || itemType == VIEW_TYPE_LOAD_MORE || itemViewDelegate == null) {
            return;
        }

        itemViewDelegate.bindViewHolder(holder, getItemData(position));

    }

    @Override
    public int getItemCount() {
        int itemCount = mDataList == null ? 0 : mDataList.size();

        if (isHasStatusView() == 1) {
            itemCount = 1;
            if (!isHideHeaderWhenStatusView && isHasHeader() == 1) {
                itemCount++;
            }
            if (!isHideFooterWhenStatusView && isHasFooter() == 1) {
                itemCount++;
            }
        } else {
            itemCount = itemCount + isHasHeader() + isHasFooter() + isHasLoadMoreView(isOpenLoadMore);
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDelegateScheduler == null) {
            throw new NullPointerException("mDelegateScheduler can not be empty,please check if itemDelegate has been added correctly");
        }

        if (isHasStatusView() == 1) {

            return ViewTypeUtil.getItemTypeIfDataEmpty(position, !isHideHeaderWhenStatusView && isHasHeader() == 1);
        } else {

            if (isHasHeader() == 0) {//no header

                return ViewTypeUtil.getItemTypeNoHeader(position, mDataList, mDelegateScheduler.isMultiItemType(), isHasFooter() == 1);
            } else {//has header

                return ViewTypeUtil.getItemTypeHasHeader(position, mDataList, mDelegateScheduler.isMultiItemType(), isHasFooter() == 1);
            }
        }

    }

    /**
     * 添加一种itemType
     *
     * @param itemType
     * @param delegate
     */
    public void addItemType(int itemType, ItemViewDelegate delegate) {
        mDelegateScheduler.addItemViewDelegate(itemType, delegate);
    }


    //--------------对数据的操作并更新列表 start---------------------

    /**
     * 设置数据列表
     *
     * @param data
     */
    public void setDataList(@Nullable List<T> data) {

        if (mDataList == null) {
            mDataList = new ArrayList<>();
        } else {
            mDataList.clear();
        }
        if (!ListUtil.isNullOrEmpty(data)) {
            mDataList.addAll(data);
        }
        //重设数据时需要清空statusView(当data不为空时，则默认hasStatusView==false,所以不用处理)
        if (ListUtil.isNullOrEmpty(data) && mStatusViewContainer != null) {
            mStatusViewContainer.removeAllViews();
        }
        notifyDataSetChanged();
    }

    /**
     * 增量添加数据集合至列表末尾
     *
     * @param newDatas
     */
    public void addData(@Nullable List<T> newDatas) {
        if (ListUtil.isNullOrEmpty(newDatas)) {
            return;
        }
        mDataList.addAll(newDatas);
        insertItemList(mDataList.size() - newDatas.size(), newDatas.size());
        fixDataSizeChanged(newDatas.size());
    }

    /**
     * 增量添加数据集合至列表指定位置
     *
     * @param newDatas
     */
    public void addData(@Nullable List<T> newDatas, @IntRange(from = 0) int position) {
        if (ListUtil.isNullOrEmpty(newDatas)) {
            return;
        }
        mDataList.addAll(position, newDatas);
        insertItemList(position, newDatas.size());
        fixDataSizeChanged(newDatas.size());
    }

    /**
     * 增量添加单个数据至列表末尾
     */
    public void addData(@Nullable T entity) {
        if (entity == null) {
            return;
        }
        mDataList.add(entity);
        insertItem(mDataList.size());
        fixDataSizeChanged(1);
    }

    /**
     * 增量添加单个数据至列表末尾
     */
    public void addData(@Nullable T entity, @IntRange(from = 0) int position) {
        if (entity == null) {
            return;
        }
        mDataList.add(position, entity);
        insertItem(position);
        fixDataSizeChanged(1);
    }

    /**
     * 删除固定位置的一个item
     *
     * @param position
     */
    public void removeData(@IntRange(from = 0) int position) {
        if (position >= mDataList.size()) {
            catchOutOfIndexError(position);
            return;
        }
        mDataList.remove(position);
        int dataPosition = getLayoutPositionByDataPosition(position);
        notifyItemRemoved(dataPosition);
        fixDataSizeChanged(0);
        notifyItemRangeChanged(dataPosition, mDataList.size() - dataPosition);
    }

    /**
     * 修改一个item数据
     *
     * @param position
     * @return
     */
    public void changeData(@Nullable T entity, @IntRange(from = 0) int position) {
        if (entity == null) {
            return;
        }
        if (position >= mDataList.size()) {
            catchOutOfIndexError(position);
            return;
        }

        mDataList.set(position, entity);
        notifyItemChanged(getLayoutPositionByDataPosition(position));
    }


    @NonNull
    public List<T> getDataList() {
        return mDataList;
    }


    private void catchOutOfIndexError(int index) {
        String errorMessage = "outOfIndex error, ListSize:" + mDataList.size() + "  Index:" + index;
        Log.e(getClass().getName(), errorMessage);
    }

    //--------------对数据的操作并更新列表 end-------


    //--------------header start-----------------

    /**
     * 添加一个header
     *
     * @param header
     * @param index
     * @return
     */
    public int addHeaderView(@Nullable View header, int index) {
        if (header == null) {
            return -1;
        }
        if (mHeaderContainer == null) {
            mHeaderContainer = new LinearLayout(header.getContext());
            mHeaderContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderContainer.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        final int childCount = mHeaderContainer.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderContainer.addView(header, index);
        if (mHeaderContainer.getChildCount() == 1) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    public int addHeaderView(@Nullable View header) {
        return addHeaderView(header, -1);
    }

    /**
     * 替换一个header
     *
     * @param header
     * @param index
     * @return
     */
    public int replaceHeaderView(@Nullable View header, int index) {
        if (header == null) {
            return -1;
        }
        if (mHeaderContainer == null || mHeaderContainer.getChildCount() <= index) {
            return addHeaderView(header, index);
        } else {
            mHeaderContainer.removeViewAt(index);
            mHeaderContainer.addView(header, index);
            return index;
        }
    }


    /**
     * 移除某一个header
     *
     * @param header
     */
    public boolean removeHeaderView(View header) {
        if (isHasHeader() == 0) {
            return false;
        }
        mHeaderContainer.removeView(header);
        if (mHeaderContainer.getChildCount() == 0) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }

        return true;
    }

    /**
     * 移除某个index的header
     *
     * @param index
     */
    public boolean removeHeaderView(int index) {
        if (isHasHeader() == 0 || mHeaderContainer.getChildCount() <= index) {
            return false;
        }
        mHeaderContainer.removeViewAt(index);
        if (mHeaderContainer.getChildCount() == 0) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }

        return true;

    }

    /**
     * 移除所有的header
     */
    public boolean removeAllHeaderView() {
        if (isHasHeader() == 0) {
            return false;
        }
        mHeaderContainer.removeAllViews();
        int position = getHeaderViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
        return true;
    }

    /**
     * 判断某一个View是否已经被添加进header
     *
     * @param view
     * @return
     */
    public boolean isViewAddedToHeader(@Nullable View view) {
        if (mHeaderContainer == null || view == null) {
            return false;
        }
        for (int i = 0; i < mHeaderContainer.getChildCount(); i++) {
            if (mHeaderContainer.getChildAt(i) == view) {
                return true;
            }
        }

        return false;
    }

    //--------------header  end-----------------

    //--------------footer start----------------

    /**
     * 添加一个footer
     */
    public int addFooterView(@Nullable View footer, int index) {
        if (footer == null) {
            return -1;
        }
        if (mFooterContainer == null) {
            mFooterContainer = new LinearLayout(footer.getContext());
            mFooterContainer.setOrientation(LinearLayout.VERTICAL);
            mFooterContainer.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        final int childCount = mFooterContainer.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterContainer.addView(footer, index);
        if (mFooterContainer.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    public int addFooterView(@Nullable View footer) {
        return addFooterView(footer, -1);
    }


    /**
     * 替换一个footer
     *
     * @param header
     * @param index
     * @return
     */
    public int replaceFooterView(View header, int index) {
        if (mFooterContainer == null || mFooterContainer.getChildCount() <= index) {
            return addFooterView(header, index);
        } else {
            mFooterContainer.removeViewAt(index);
            mFooterContainer.addView(header, index);
            return index;
        }
    }

    /**
     * 移除一个footer
     *
     * @param footer
     */
    public boolean removeFooterView(View footer) {
        if (isHasFooter() == 0) {
            return false;
        }

        mFooterContainer.removeView(footer);
        if (mFooterContainer.getChildCount() == 0) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }

        return true;
    }

    /**
     * 移除指定index的footer
     *
     * @param index
     */
    public boolean removeFooterView(int index) {
        if (isHasFooter() == 0 || mFooterContainer.getChildCount() <= index) {
            return false;
        }
        mFooterContainer.removeViewAt(index);
        if (mFooterContainer.getChildCount() == 0) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }

        return true;
    }

    /**
     * 移除所有的Footer
     */
    public boolean removeAllFooterView() {
        if (isHasFooter() == 0) {
            return false;
        }

        mFooterContainer.removeAllViews();
        int position = getFooterViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }

        return true;
    }

    //--------------footer end----------------


    //--------------statusView(加载中、数据为空、加载失败)的展示控制 start--------

    public void showLoading() {
        showStatusView(STATUS_VIEW_LOADING);
    }

    public void showDataEmpty() {
        showStatusView(STATUS_VIEW_DATA_EMPTY);
    }

    public void showError() {
        showStatusView(STATUS_VIEW_LOAD_FAILED);
    }

    private void showStatusView(@EnumStatusView.StatusViewType int status) {
        //先把数据清空
        setDataList(null);

        View statusView = null;
        switch (status) {
            case STATUS_VIEW_LOADING:
                statusView = mLoadingView;
                break;
            case STATUS_VIEW_DATA_EMPTY:
                statusView = mDataEmptyView;
                break;
            case STATUS_VIEW_LOAD_FAILED:
                statusView = mLoadFailedView;
                break;
        }
        if (statusView == null) {
            statusView = new View(context);
        }
        boolean NeedInsertStatusView = false;
        if (mStatusViewContainer == null) {
            mStatusViewContainer = new FrameLayout(statusView.getContext());
            final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = statusView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mStatusViewContainer.setLayoutParams(layoutParams);
            NeedInsertStatusView = true;
        }
        mStatusViewContainer.removeAllViews();
        mStatusViewContainer.addView(statusView);
        if (NeedInsertStatusView) {
            if (isHasStatusView() == 1) {
                if (!isHideHeaderWhenStatusView && isHasHeader() == 1) {
                    notifyItemInserted(1);
                } else {
                    notifyItemInserted(0);
                }

            }
        }
    }


    //--------------statusView(加载中、数据为空、加载失败)的展示控制 end--------


    //--------------load more start----------------------


    /**
     * 设置底部加载更多的状态
     *
     * @param state
     */
    public void setLoadMoreState(int state) {
        if (mLoadingMoreView != null) {
            mLoadingMoreView.setState(state);
        }
    }

    /**
     * 检查是否开始加载更多
     *
     * @param position
     */
    private void checkStartLoadMore(int position) {
        if (!isOpenLoadMore || position < mDataList.size() - mPreLoadMoreNum || mLoadingMoreView == null
                || mLoadingMoreView.getCurrentState() != AbsLoadingMoreView.STATE_IDLE) {
            return;
        }
        mLoadingMoreView.setState(AbsLoadingMoreView.STATE_LOADING);
        if (mLoadMoreListener != null) {
            mLoadMoreListener.loadMore();
        }
    }

    /**
     * 一次loadMore结束
     */
    public void loadMoreFinish() {
        if (mLoadingMoreView != null) {
            mLoadingMoreView.setState(AbsLoadingMoreView.STATE_IDLE);
        }
    }

    /**
     * 无更多结果
     */
    public void hasNoMore() {
        if (mLoadingMoreView != null) {
            mLoadingMoreView.setState(AbsLoadingMoreView.STATE_END);
        }
    }

    //---------------load more end-----------------------


    //-------------可配置项 start-------------------------

    /**
     * 设置是否打开statusView
     *
     * @param isOpen
     */
    @NonNull
    public SuperBaseAdapter isOpenStatusView(boolean isOpen) {
        this.isOpenStatusView = isOpen;
        return this;
    }

    @NonNull
    public SuperBaseAdapter isOpenLoadMore(boolean isOpen) {
        if (!isOpen) {
            mLoadingMoreView = null;
        }
        this.isOpenLoadMore = isOpen;
        return this;
    }

    /**
     * 显示statusView时是否显示header
     *
     * @param isHide
     */
    @NonNull
    public SuperBaseAdapter isHideHeaderWhenStatusView(boolean isHide) {
        this.isHideHeaderWhenStatusView = isHide;
        return this;
    }

    /**
     * 显示statusView时是否显示footer
     *
     * @param isHide
     */
    @NonNull
    public SuperBaseAdapter isHideFooterWhenStatusView(boolean isHide) {
        this.isHideFooterWhenStatusView = isHide;
        return this;
    }

    /**
     * 正在加载View设置
     *
     * @param loadingView
     * @return
     */
    @NonNull
    public SuperBaseAdapter setLoadingView(@Nullable View loadingView) {
        this.mLoadingView = loadingView;
        return this;
    }

    @NonNull
    public SuperBaseAdapter setLoadingView(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
        this.mLoadingView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return this;
    }

    /**
     * Data Empty View设置
     *
     * @param dataEmptyView
     * @return
     */
    @NonNull
    public SuperBaseAdapter setDataEmptyView(@Nullable View dataEmptyView) {
        this.mDataEmptyView = dataEmptyView;
        return this;
    }

    @NonNull
    public SuperBaseAdapter setDataEmptyView(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
        this.mDataEmptyView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return this;
    }

    /**
     * 加载失败View设置
     *
     * @param loadFailedView
     * @return
     */
    @NonNull
    public SuperBaseAdapter setLoadFailedView(@Nullable View loadFailedView) {
        this.mLoadFailedView = loadFailedView;
        return this;
    }

    @NonNull
    public SuperBaseAdapter setLoadFailedView(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
        this.mLoadFailedView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return this;
    }

    /**
     * 加载更多View设置
     *
     * @param loadingMoreView
     * @return
     */
    @NonNull
    public SuperBaseAdapter setLoadingMoreView(AbsLoadingMoreView loadingMoreView) {
        this.mLoadingMoreView = loadingMoreView;
        return this;
    }

    /**
     * 设置提前几个item开始加载更多
     *
     * @param preLoadMoreNum
     */
    @NonNull
    public SuperBaseAdapter preLoadMoreNum(int preLoadMoreNum) {
        this.mPreLoadMoreNum = preLoadMoreNum;
        return this;
    }


    /**
     * 设置item点击事件
     *
     * @param listener
     */
    @NonNull
    public SuperBaseAdapter setOnItemClickListener(@Nullable Listener.OnItemClickListener listener) {
        this.mItemClickListener = listener;
        return this;
    }

    /**
     * 设置item长按事件
     *
     * @param listener
     */
    @NonNull
    public SuperBaseAdapter setOnItemLongClickListener(@Nullable Listener.OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
        return this;
    }

    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    @NonNull
    public SuperBaseAdapter setOnLoadMoreListener(@NonNull RecyclerView recyclerView, boolean isRealTimeMonitor, @Nullable Listener.LoadMoreListener listener) {
        if (mLoadMoreListener != null) {
            throw new RuntimeException("Don't repeat add loadMoreListener");
        }
        this.mLoadMoreListener = listener;
        //只需滚动状态改变时监听loadMore
        if (!isRealTimeMonitor) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (isOpenLoadMore && mLoadingMoreView != null && mLoadingMoreView.getCurrentState() == AbsLoadingMoreView.STATE_IDLE
                            && RVUtil.needLoadingMore(mPreLoadMoreNum, recyclerView, newState) && mLoadMoreListener != null) {
                        mLoadingMoreView.setState(AbsLoadingMoreView.STATE_LOADING);
                        mLoadMoreListener.loadMore();
                    }
                }

            });
        }

        return this;
    }


    //-------------可配置项 end---------------------------


    private void bindListener(ViewGroup parent, @Nullable final VH holder, final int viewType) {
        if (holder == null || ListUtil.isNullOrEmpty(mDataList)) {
            return;
        }
        if (mItemClickListener != null && holder != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int dataPosition = getDataPosition(holder);
                    mItemClickListener.onItemClick(mDataList.get(dataPosition), holder.itemView, dataPosition, viewType);
                }
            });
        }

        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int dataPosition = getDataPosition(holder);
                    return mItemLongClickListener.onItemLongClick(mDataList.get(dataPosition), holder.itemView, dataPosition, viewType);
                }
            });
        }
    }

    @Nullable
    private T getItemData(int position) {
        if (mDataList == null || mDataList.isEmpty()) {
            return null;
        }
        return mDataList.get(position - isHasHeader());
    }

    private void insertItemList(@IntRange(from = 0) int position, int itemNum) {
        notifyItemRangeInserted(getLayoutPositionByDataPosition(position), itemNum);
    }

    private void insertItem(@IntRange(from = 0) int position) {
        notifyItemInserted(getLayoutPositionByDataPosition(position));
    }

    private void fixDataSizeChanged(int size) {
        final int dataListSize = mDataList == null ? 0 : mDataList.size();
        if (dataListSize == size) {
            notifyDataSetChanged();
        }
    }


    /**
     * 获取header view的位置
     *
     * @return
     */
    public int getHeaderViewPosition() {
        if (isHasStatusView() == 1) {
            if (!isHideHeaderWhenStatusView) {
                return 0;
            }
        } else {
            return 0;
        }
        return -1;
    }

    /**
     * 获取footer view的位置
     *
     * @return
     */
    private int getFooterViewPosition() {
        if (mFooterContainer == null) {
            return -1;
        }
        if (isHasStatusView() == 1) {
            if (isHideFooterWhenStatusView) {
                return -1;
            }
            boolean hasHeader = !isHideHeaderWhenStatusView && isHasHeader() == 1;
            return hasHeader ? 2 : 1;
        } else if (ListUtil.isNullOrEmpty(mDataList)) {//数据为空但是没有设置statusView的情况
            return isHasHeader();
        } else {
            return mDataList.size() + isHasHeader();
        }
    }


    /**
     * 判断是否有header
     *
     * @return
     */
    private int isHasHeader() {
        if (mHeaderContainer == null || mHeaderContainer.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 判断是否有status view
     *
     * @return
     */
    private int isHasStatusView() {
        if (mStatusViewContainer == null || mStatusViewContainer.getChildCount() == 0) {
            return 0;
        }
        if (!isOpenStatusView) {
            return 0;
        }
        if (mDataList.size() != 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 判断是否有footer
     *
     * @return
     */
    private int isHasFooter() {
        if (mFooterContainer == null || mFooterContainer.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }


    /**
     * 判断是否有load more view
     *
     * @param isOpenLoadMore
     * @return
     */
    private int isHasLoadMoreView(boolean isOpenLoadMore) {
        return isOpenLoadMore ? 1 : 0;
    }


    /**
     * 获取某个item在不考虑header、footer情况下的position,也就是在数据集合中的位置
     *
     * @param holder
     * @return
     */
    private int getDataPosition(@NonNull RecyclerView.ViewHolder holder) {

        return holder.getLayoutPosition() - isHasHeader();
    }

    /**
     * 根据dataPosition获取显示上的position
     *
     * @param dataPosition
     * @return
     */
    private int getLayoutPositionByDataPosition(int dataPosition) {

        return dataPosition + isHasHeader();
    }


}
