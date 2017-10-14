package com.example.xzh.shoppingmall.shoppingcart.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xzh.shoppingmall.R;
import com.example.xzh.shoppingmall.base.BaseFragment;
import com.example.xzh.shoppingmall.home.bean.GoodsBean;
import com.example.xzh.shoppingmall.shoppingcart.adapter.ShoppingCartAdapter;
import com.example.xzh.shoppingmall.shoppingcart.utils.CartStorage;

import java.util.List;

/**
 * Created by xzh on 2017/10/12.
 *
 *  主页面
 *
 */

public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvShopcartEdit;
    private RecyclerView recyclerview;
    private LinearLayout llCheckAll;
    private CheckBox checkboxAll;
    private TextView tvShopcartTotal;
    private Button btnCheckOut;
    private LinearLayout llDelete;
    private CheckBox cbAll;
    private Button btnDelete;
    private Button btnCollection;
    private ImageView ivEmpty;
    private TextView tvEmptyCartToBuy;
    private LinearLayout ll_empty_shopcart;
    private ShoppingCartAdapter adapter;
    private static final int ACTION_EDIT = 1;
    private static final int ACTION_COMPLETE = 2;

    @Override
    public void onClick(View v) {
        if ( v == btnCheckOut ) {
            // Handle clicks for btnCheckOut
        } else if ( v == btnDelete ) {
            // Handle clicks for btnDelete
            adapter.deleteData();
            adapter.checkAll();
            if (adapter.getItemCount() == 0) {
                emptyShoppingCart();
            }
        } else if ( v == btnCollection ) {
            // Handle clicks for btnCollection
        }
    }


    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.fragment_shoppingcart, null);

        tvShopcartEdit = (TextView) view.findViewById( R.id.tv_shopcart_edit );
        recyclerview = (RecyclerView) view.findViewById( R.id.recyclerview );
        llCheckAll = (LinearLayout) view.findViewById( R.id.ll_check_all );
        checkboxAll = (CheckBox) view.findViewById( R.id.checkbox_all );
        tvShopcartTotal = (TextView) view.findViewById( R.id.tv_shopcart_total );
        btnCheckOut = (Button) view.findViewById( R.id.btn_check_out );
        llDelete = (LinearLayout) view.findViewById( R.id.ll_delete );
        cbAll = (CheckBox) view.findViewById( R.id.cb_all );
        btnDelete = (Button) view.findViewById( R.id.btn_delete );
        btnCollection = (Button) view.findViewById( R.id.btn_collection );
        ivEmpty = (ImageView) view.findViewById(R.id.iv_empty);
        tvEmptyCartToBuy = (TextView) view.findViewById(R.id.tv_empty_cart_tobuy);
        ll_empty_shopcart = (LinearLayout) view.findViewById(R.id.ll_empty_shopcart);

        btnCheckOut.setOnClickListener( this );
        btnDelete.setOnClickListener( this );
        btnCollection.setOnClickListener( this );

        initListener();

        return view;
    }

    private void initListener() {

        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = (int) v.getTag();
                if (action == ACTION_EDIT) {
                    // 切换为完成状态
                    showDelete();
                } else {
                    // 切换为编辑状态
                    hideDelete();
                }
            }
        });

    }

    private void hideDelete() {
        // 设置状态和文本-编辑
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setText("编辑");
        // 变为勾选
        if (adapter != null) {
            adapter.checkAll_none(true);
            adapter.checkAll();
            adapter.showTotalPrice();
        }
        // 删除视图隐藏
        llDelete.setVisibility(View.GONE);
        // 结算视图显示
        llCheckAll.setVisibility(View.VISIBLE);
    }

    private void showDelete() {
        // 设置状态和文本-完成
        tvShopcartEdit.setTag(ACTION_COMPLETE);
        tvShopcartEdit.setText("完成");
        // 变为非勾选
        if (adapter != null) {
            adapter.checkAll_none(false);
            adapter.checkAll();
        }
        // 删除视图显示
        llDelete.setVisibility(View.VISIBLE);
        // 结算视图隐藏
        llCheckAll.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();

    }

    private void showData() {

        List<GoodsBean> goodsBeanList = CartStorage.getInstance().getAllData();
        if (goodsBeanList != null && goodsBeanList.size() > 0) {
            ll_empty_shopcart.setVisibility(View.GONE);
            tvShopcartEdit.setVisibility(View.VISIBLE);
            llCheckAll.setVisibility(View.VISIBLE);
            adapter = new ShoppingCartAdapter(mContext, goodsBeanList, tvShopcartTotal, checkboxAll, cbAll);
            recyclerview.setAdapter(adapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false));

        } else {
            emptyShoppingCart();
        }

    }

    private void emptyShoppingCart() {

        ll_empty_shopcart.setVisibility(View.VISIBLE);
        tvShopcartEdit.setVisibility(View.GONE);
        llDelete.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }
}
