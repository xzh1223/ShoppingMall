package com.example.xzh.shoppingmall.shoppingcart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xzh.shoppingmall.R;
import com.example.xzh.shoppingmall.home.bean.GoodsBean;
import com.example.xzh.shoppingmall.shoppingcart.utils.CartStorage;
import com.example.xzh.shoppingmall.utils.Constants;
import com.example.xzh.shoppingmall.view.AddSubView;

import java.util.List;

/**
 * Created by xzh on 2017/10/14.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private final List<GoodsBean> datas;
    private final Context mContext;
    private final TextView tvShopcartTotal;
    private final CheckBox checkboxAll;
    private final CheckBox cbAll;

    public ShoppingCartAdapter(Context mContext, List<GoodsBean> goodsBeanList, TextView tvShopcartTotal, CheckBox checkboxAll, CheckBox cbAll) {
        this.mContext = mContext;
        this.datas = goodsBeanList;
        this.tvShopcartTotal = tvShopcartTotal;
        this.checkboxAll = checkboxAll;
        this.cbAll = cbAll;
        showTotalPrice();
        setListener();
        // 检验是否全选
        checkAll();
    }

    private void setListener() {

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 根据位置找到对应的Bean对象
                GoodsBean goodsBean = datas.get(position);
                // 设置取反状态
                goodsBean.setSelected(!goodsBean.isSelected());
                // 刷新状态
                notifyItemChanged(position);
                // 校验是否全选
                checkAll();
                // 重新计算总价格
                showTotalPrice();
            }
        });

        // CheckBox的点击事件
        checkboxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到状态
                boolean isCheck = checkboxAll.isChecked();
                // 根据状态设置全选和非全选
                checkAll_none(isCheck);
                // 计算总价格
                showTotalPrice();
            }
        });

        cbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到状态
                boolean isCheck = cbAll.isChecked();
                // 根据状态设置全选和非全选
                checkAll_none(isCheck);
            }
        });

    }

    public void checkAll_none(boolean isCheck) {

        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                GoodsBean goodsBean = datas.get(i);
                goodsBean.setSelected(isCheck);
                notifyItemChanged(i);
            }

        }

    }

    public void checkAll() {

        if (datas != null && datas.size() > 0) {
            int number = 0;
            for (int i = 0; i < datas.size(); i++) {
                GoodsBean goodsBean = datas.get(i);
                if (!goodsBean.isSelected()) {
                    // 非全选
                    checkboxAll.setChecked(false);
                    cbAll.setChecked(false);
                } else {
                    // 选中的
                    number++;
                }
            }
            if (number == datas.size()) {
                // 全选
                checkboxAll.setChecked(true);
                cbAll.setChecked(true);
            }
        }else {
            checkboxAll.setChecked(false);
            cbAll.setChecked(false);
        }

    }

    public void showTotalPrice() {

        tvShopcartTotal.setText("" + getTotalPrice());

    }

    private double getTotalPrice() {
        double totalPrice = 0.0;
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                GoodsBean goodsBean = datas.get(i);
                if (goodsBean.isSelected()) {
                    totalPrice = totalPrice + Double.valueOf(goodsBean.getNumber()) * Double.valueOf(goodsBean.getCover_price());
                }
            }
        }
        return totalPrice;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_shop_cart, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final GoodsBean goodsBean = datas.get(position);
        holder.cb_gov.setChecked(goodsBean.isSelected());
        Glide.with(mContext).load(Constants.IMAGE_URL + goodsBean.getFigure()).into(holder.iv_gov);
        holder.tv_desc_gov.setText(goodsBean.getName());
        holder.tv_price_gov.setText("￥" + goodsBean.getCover_price());
        holder.addSubView.setValue(goodsBean.getNumber());
        holder.addSubView.setMinValue(1);
        holder.addSubView.setMaxValue(8);

        // 设置商品数量的变化
        holder.addSubView.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int value) {
                // 内存更新
                goodsBean.setNumber(value);
                // 本地更新
                CartStorage.getInstance().updateData(goodsBean);
                // 刷新数据
                notifyItemChanged(position);
                // 再次计算价格
                showTotalPrice();
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private CheckBox cb_gov;
        private ImageView iv_gov;
        private TextView tv_desc_gov;
        private TextView tv_price_gov;
        private AddSubView  addSubView;

        public ViewHolder(View itemView) {
            super(itemView);
            cb_gov = (CheckBox) itemView.findViewById(R.id.cb_gov);
            iv_gov = (ImageView) itemView.findViewById(R.id.iv_gov);
            tv_desc_gov = (TextView) itemView.findViewById(R.id.tv_desc_gov);
            tv_price_gov = (TextView) itemView.findViewById(R.id.tv_price_gov);
            addSubView = (AddSubView) itemView.findViewById(R.id.numberAddSubView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
        }
    }

    public void deleteData() {

        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                // 删除选中的
                GoodsBean goodsBean = datas.get(i);
                if (goodsBean.isSelected()) {
                    // 内存移除
                    datas.remove(goodsBean);
                    // 保存到本地
                    CartStorage.getInstance().deleteData(goodsBean);
                    // 刷新
                    notifyItemRemoved(i);
                    i--;
                }
            }
        }

    }

    /**
     * 点击Item监听器
     */
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
