package com.example.xzh.shoppingmall.shoppingcart.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.example.xzh.shoppingmall.app.MyApplication;
import com.example.xzh.shoppingmall.home.bean.GoodsBean;
import com.example.xzh.shoppingmall.utils.CacheUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzh on 2017/10/14.
 */

public class CartStorage {
    private static final String JSON_CART = "json_cart";
    private static CartStorage instance;
    private final Context mContext;
    private SparseArray<GoodsBean> goodsBeanSparseArray;

    private CartStorage(Context context) {
        this.mContext = context;
        goodsBeanSparseArray = new SparseArray<>(100);

        listToSparseArray();
    }

    /**
     *  从本地读取数据加入到SparseArray中
     */
    private void listToSparseArray() {

        List<GoodsBean> goodsBeanList = getAllData();

        for (int i = 0; i < goodsBeanList.size(); i++) {
            GoodsBean goodsBean = goodsBeanList.get(i);
            goodsBeanSparseArray.put(Integer.parseInt(goodsBean.getProduct_id()), goodsBean);
        }

    }

    /**
     *  获取本地所有的数据
     * @return
     */
    public List<GoodsBean> getAllData() {
        List<GoodsBean> goodsBeanList = new ArrayList<>();
        String json = CacheUtils.getString(mContext, JSON_CART);
        if (!TextUtils.isEmpty(json)) {
            goodsBeanList = new Gson().fromJson(json, new TypeToken<List<GoodsBean>>(){}.getType());
        }
        return goodsBeanList;
    }

    public static CartStorage getInstance() {
        if (instance == null) {
            instance = new CartStorage(MyApplication.getContext());
        }
        return instance;
    }

    public void addData(GoodsBean goodsBean) {
        GoodsBean tempData = goodsBeanSparseArray.get(Integer.parseInt(goodsBean.getProduct_id()));
        if (tempData != null) {
            tempData.setNumber(tempData.getNumber() + 1);
        } else {
            tempData = goodsBean;
            tempData.setNumber(1);
        }

        // 同步到内存中
        goodsBeanSparseArray.put(Integer.parseInt(goodsBean.getProduct_id()), tempData);

        // 同步到本地
        saveLocal();
    }

    public void deleteData(GoodsBean goodsBean) {
        // 从内存中删除
        goodsBeanSparseArray.delete(Integer.parseInt(goodsBean.getProduct_id()));

        // 把内存中的保存到本地
        saveLocal();
    }

    public void updateData(GoodsBean goodsBean) {
        // 内存中更新
        goodsBeanSparseArray.put(Integer.parseInt(goodsBean.getProduct_id()),goodsBean);
        // 同步到本地
        saveLocal();
    }

    public void saveLocal() {
        // SparseArrayList转换成List
        List<GoodsBean> goodsBeanList = sparseToList();
        // 使用Gson把list转换成String
        String json = new Gson().toJson(goodsBeanList);
        // 将String数据保存
        CacheUtils.saveString(mContext, JSON_CART, json);

    }

    private List<GoodsBean> sparseToList() {
        List<GoodsBean> goodsBeanList = new ArrayList<>();
        for (int i = 0; i < goodsBeanSparseArray.size(); i++) {
            GoodsBean goodsBean = goodsBeanSparseArray.valueAt(i);
            goodsBeanList.add(goodsBean);
        }
        return goodsBeanList;
    }

}
