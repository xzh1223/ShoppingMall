package com.example.xzh.shoppingmall.home.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.xzh.shoppingmall.R;
import com.example.xzh.shoppingmall.base.BaseFragment;
import com.example.xzh.shoppingmall.home.adapter.HomeFragmentAdapter;
import com.example.xzh.shoppingmall.home.bean.ResultBeanData;
import com.example.xzh.shoppingmall.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by xzh on 2017/10/12.
 *
 *  主页面
 *
 */

public class HomeFragment extends BaseFragment {
    private RecyclerView rvHome;
    private ImageView ib_top;
    private TextView tv_search_home;
    private TextView tv_message_home;
    private static final String TAG = "HomeFragment";
    private ResultBeanData.ResultBean resultBean;
    private HomeFragmentAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        rvHome = (RecyclerView) view.findViewById(R.id.rv_home);
        ib_top = (ImageView) view.findViewById(R.id.ib_top);
        tv_search_home = (TextView) view.findViewById(R.id.tv_search_home);
        tv_message_home = (TextView) view.findViewById(R.id.tv_message_home);

        initListener();

        return view;
    }

    private void initListener() {

        ib_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 回到顶部
                rvHome.scrollToPosition(0);
            }
        });

        tv_search_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 搜索");
            }
        });

        tv_message_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 进入消息中心");
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        String url = Constants.HOME_URL;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    /**
                     *  当请求失败的时候
                     */
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: 请求失败" + e.getMessage());
                    }

                    /**
                     *  当联网成功的时候回调
                     * @param response  请求成功的数据
                     */
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: 请求成功" + response);
                        processData(response);
                    }
                });
    }

    /**
     *  数据解析
     */
    private void processData(String response) {

        ResultBeanData resultBeanData = JSON.parseObject(response, ResultBeanData.class);
        resultBean = resultBeanData.getResult();
        if (resultBean != null) {
            // 有数据，设置适配器
            adapter = new HomeFragmentAdapter(mContext, resultBean);
            rvHome.setAdapter(adapter);
            GridLayoutManager manager = new GridLayoutManager(mContext, 1);
            // 设置跨度大小监听，实现滑动屏幕按钮显示和隐藏
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position <= 4) {
                        ib_top.setVisibility(View.GONE);
                    } else {
                        ib_top.setVisibility(View.VISIBLE);
                    }
                    return 1;
                }
            });
            rvHome.setLayoutManager(manager);
        } else {
            // 没有数据
        }

    }
}
