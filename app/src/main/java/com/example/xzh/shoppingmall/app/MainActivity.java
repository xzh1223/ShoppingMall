package com.example.xzh.shoppingmall.app;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.xzh.shoppingmall.R;
import com.example.xzh.shoppingmall.base.BaseFragment;
import com.example.xzh.shoppingmall.community.fragment.CommunityFragment;
import com.example.xzh.shoppingmall.home.fragment.HomeFragment;
import com.example.xzh.shoppingmall.shoppingcart.fragment.ShoppingCartFragment;
import com.example.xzh.shoppingmall.type.fragment.TypeFragment;
import com.example.xzh.shoppingmall.user.fragment.UserFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {


    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.rg_main)
    RadioGroup rgMain;
    // Fragment实例集合
    private ArrayList<BaseFragment> fragments;
    private int position = 0;
    private BaseFragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Butterknife和当前Activity绑定
        ButterKnife.bind(this);

        initFragment();
        initListener();
    }

    /**
     *  设置监听器
     */
    private void initListener() {

        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        position = 0;
                        break;
                    case R.id.rb_type:
                        position = 1;
                        break;
                    case R.id.rb_community:
                        position = 2;
                        break;
                    case R.id.rb_cart:
                        position = 3;
                        break;
                    case R.id.rb_user:
                        position = 4;
                        break;
                    default:
                        position = 0;
                        break;
                }
                //根据位置取不同的Fragment
                BaseFragment baseFragment = getFragment(position);
                // 进行切换Fragment
                switchFragment(tempFragment, baseFragment);
            }
        });
        rgMain.check(R.id.rb_home);

    }

    /**
     *  初始化Fragment
     *      注意：按顺序添加
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new ShoppingCartFragment());
        fragments.add(new UserFragment());
    }

    /**
     *  获取当前位置对应的Fragment
     */
    private BaseFragment getFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            return fragments.get(position);
        }
        return null;
    }

    /**
     *  根据传递过来的上一个Fragment和下一个Fragment进行切换
     */
    private void switchFragment(Fragment fromFragment, BaseFragment toFragment) {
        if (tempFragment != toFragment) {
            tempFragment = toFragment;
            if (toFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (!toFragment.isAdded()) {
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.add(R.id.frameLayout, toFragment).commit();
                } else {
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(toFragment).commit();
                }
            }
        }
    }

}
