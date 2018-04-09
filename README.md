# InstantRecyclerView
A library that helps to implement a complex list with RecyclerView


# How to use

**Step1:** Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step2:** Add the dependency
```gradle
dependencies {
	        compile 'com.github.zhengcx:InstantRecyclerView:v1.1'
	}
```
**Step3:** Just create a adapter which extends SuperAdapter and set up some configuration. Specific can reference demo.

```java
 private MainListAdapter createAdapter() {
        mMainListAdapter = new MainListAdapter();
        mMainListAdapter.setLoadingView(mLoadingView)
                .setLoadingMoreView(new DefaultLoadMoreView(rvMain))
                .preLoadMoreNum(5)
                .setOnItemClickListener(this)
                .setOnLoadMoreListener(rvMain, false, this);
        return mMainListAdapter;
    }
```

## Why to use InstantRecyclerView
Link: [https://juejin.im/post/5abda929f265da23826e155e](https://juejin.im/post/5abda929f265da23826e155e)

- Solved a repeated global refresh.
  解决重复的全局刷新
- Improving the efficiency and performance of adding and deletions header/Footer.
  提高增删header/Footer的效率和性能
- Make the list of multiple itemType code clear.
  使多itemType的列表代码清晰解耦
- Solved the problem of overdrawing by state View.
  解决状态View导致过度绘制的问题(如loadingView、Loadfailed view、emptyData view)
- Provide functions for pullup loading more.（Two different kinds of listeners.
  集成提供两种上拉加载更多监听
- Solved the problem of repeated binding of item click events.
  解决item的点击事件重复绑定的问题
  
   [**see More**](https://juejin.im/post/5abda929f265da23826e155e)
