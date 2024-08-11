package com.android.VLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity {

    //不同类型的ViewType
    int VIEW_TYPE_BANNER = 1;
    int VIEW_TYPE_APP = 2;
    int VIEW_TYPE_NEWS = 3;
    int VIEW_TYPE_GOOD_TITLE = 4;
    int VIEW_TYPE_GOOD_THUMB = 5;

    //横幅位
    String[] bannerUrls = {"https://img-blog.csdnimg.cn/03f90fd773d846de926b335276b9bdc6.gif", "https://img-blog.csdnimg.cn/03f90fd773d846de926b335276b9bdc6.gif"};

    //应用位
    String[] appTitles = {"天猫", "聚划算", "天猫国际", "外卖", "天猫超市", "充值中心", "飞猪旅行", "领金币", "拍卖", "分类"};
    int[] appIcons = {R.mipmap.ic_tian_mao, R.mipmap.ic_ju_hua_suan, R.mipmap.ic_tian_mao_guoji, R.mipmap.ic_waimai, R.mipmap.ic_chaoshi, R.mipmap.ic_voucher_center, R.mipmap.ic_travel, R.mipmap.ic_tao_gold, R.mipmap.ic_auction, R.mipmap.ic_classify};

    //新闻位
    String[] newsUrls1 = {"天猫超市最近发大活动啦，快来抢", "没有最便宜，只有更便宜！"};
    String[] newsUrls2 = {"天猫超市最近发大活动啦，快来抢", "没有最便宜，只有更便宜！"};

    //商品位
    int[] goodTitles = {R.mipmap.item1, R.mipmap.item2, R.mipmap.item3, R.mipmap.item4, R.mipmap.item5};
    int[] goodThumbs = {R.mipmap.flashsale1, R.mipmap.flashsale2, R.mipmap.flashsale3, R.mipmap.flashsale4};

    RecyclerView recyclerView;
    List<DelegateAdapter.Adapter> subAdapters = new LinkedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler);
        //1.设置总布局管理器
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //2.设置回收池大小（相同类型的View可以复用，防止多次创建ItemView）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(0, 10);
        recyclerView.setRecycledViewPool(viewPool);
        //3.逐个设置子模块适配器
        addBannerAdapter();
        addAppAdapter();
        addNewsAdapter();
        addGoodAdapter();
        //4.设置总适配器
        DelegateAdapter mainAdapter = new DelegateAdapter(layoutManager, true);
        mainAdapter.setAdapters(subAdapters);
        recyclerView.setAdapter(mainAdapter);
    }

    //添加子适配器：商品位
    private void addGoodAdapter() {
        for (int i = 0; i < goodTitles.length; i++) {
            //商品标题
            int goodTitle = goodTitles[i];
            LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
            SubAdapter titleAdapter = new SubAdapter(this, linearLayoutHelper, R.layout.vlayout_title, 1, VIEW_TYPE_GOOD_TITLE) {
                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    holder.setImageResource(R.id.iv, goodTitle);
                }
            };
            subAdapters.add(titleAdapter);
            //商品列表
            OnePlusNLayoutHelper onePlusNLayoutHelper = new OnePlusNLayoutHelper(4);
            onePlusNLayoutHelper.setBgColor(Color.WHITE);
            SubAdapter girdAdapter = new SubAdapter(this, onePlusNLayoutHelper, R.layout.vlayout_image, 4, VIEW_TYPE_GOOD_THUMB) {
                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    int item = goodThumbs[position];
                    ImageView iv = holder.getView(R.id.iv);
                    Glide.with(getApplicationContext()).load(item).into(iv);
                }
            };
            subAdapters.add(girdAdapter);
        }
    }

    //添加子适配器：新闻位
    private void addNewsAdapter() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        SubAdapter newsAdapter = new SubAdapter(this, linearLayoutHelper, R.layout.vlayout_news, 1, VIEW_TYPE_NEWS) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                MarqueeView marqueeView1 = holder.getView(R.id.marqueeView1);
                MarqueeView marqueeView2 = holder.getView(R.id.marqueeView2);
                marqueeView1.startWithList(Arrays.asList(newsUrls1));
                marqueeView2.startWithList(Arrays.asList(newsUrls2));
                marqueeView1.startWithList(Arrays.asList(newsUrls1));
                marqueeView2.startWithList(Arrays.asList(newsUrls2));
            }
        };
        subAdapters.add(newsAdapter);
    }

    //添加子适配器：应用位
    private void addAppAdapter() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
        gridLayoutHelper.setBgColor(Color.WHITE);
        SubAdapter appAdapter = new SubAdapter(this, gridLayoutHelper, R.layout.vlayout_menu, 10, VIEW_TYPE_APP) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.tv_menu_title_home, appTitles[position] + "");
                holder.setImageResource(R.id.iv_menu_home, appIcons[position]);
            }
        };
        subAdapters.add(appAdapter);
    }

    //添加子适配器：横幅位
    private void addBannerAdapter() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        SubAdapter bannerAdapter = new SubAdapter(this, linearLayoutHelper, R.layout.vlayout_banner, 1, VIEW_TYPE_BANNER) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Banner banner = holder.getView(R.id.banner);
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setImageLoader(new GlideImageLoader());
                banner.setImages(Arrays.asList(bannerUrls));
                banner.setBannerAnimation(Transformer.DepthPage);
                banner.isAutoPlay(true);
                banner.setDelayTime(5000);
                banner.setIndicatorGravity(BannerConfig.CENTER);
                banner.start();
            }
        };
        subAdapters.add(bannerAdapter);
    }
}
