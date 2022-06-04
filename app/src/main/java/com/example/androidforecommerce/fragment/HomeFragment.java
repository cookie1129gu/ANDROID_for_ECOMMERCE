package com.example.androidforecommerce.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.androidforecommerce.R;
import com.example.androidforecommerce.adapter.HomeActAdapter;
import com.example.androidforecommerce.adapter.HomeBannerAdapter;
import com.example.androidforecommerce.adapter.HomeHotProductAdapter;
import com.example.androidforecommerce.adapter.HomeTopBannerAndParamAdapter;
import com.example.androidforecommerce.config.Constant;
import com.example.androidforecommerce.listener.OnItemClickListener;
import com.example.androidforecommerce.pojo.Param;
import com.example.androidforecommerce.pojo.Product;
import com.example.androidforecommerce.pojo.ResponseCode;
import com.example.androidforecommerce.pojo.SverResponse;
import com.example.androidforecommerce.ui.DetailActivity;
import com.example.androidforecommerce.utils.JSONUtills;
import com.example.androidforecommerce.utils.Utils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class HomeFragment extends Fragment {
    private View rootview;

    private RecyclerView mhome_rv;
    private List<Integer> imgs;//轮播图
    private List<Param> mCategoryData;//产品类型参数
    private List<Product> mProductData;//产品信息

    private DelegateAdapter delegateAdapter;//定义代理适配器

    private HomeTopBannerAndParamAdapter homeTopBannerAndParamAdapter;
    private HomeHotProductAdapter homeHotProductAdapter;
    private HomeBannerAdapter homeBannerAdapter;


    private final int PARAM_ROLL_COL=3;//表格列数

    Context context;

    public HomeFragment(){}

    public HomeFragment(Context context){
        this.context=context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        loadParams();
        loadHotProducts();
        return rootview;
    }

    private void initView(){

        //获取RecyclerView
        mhome_rv=(RecyclerView)rootview.findViewById(R.id.home_rv);

        //数据初始化
        //初始化轮播图
        imgs=new ArrayList<>();
        imgs.add(R.drawable.lunbo01);
        imgs.add(R.drawable.lunbo02);

        //初始化数据容器
        mCategoryData=new ArrayList<>();

        mProductData=new ArrayList<>();


        /*设置测试数据*/
        String category_str[]={"混凝土机械","建筑起重机械","路面机械","土方机械","环卫机械","工业车辆","模型专区","特惠专区","运费"};
        String param_str[]={"混凝土缸a260","拖垒高强度直管","S管阀","超轻-混凝土垒车输送管","奔驰油滤包" };
        for(int i=0;i<category_str.length;i++){
            Param cur=new Param();
            cur.setName(category_str[i]);
            mCategoryData.add(cur);
        }
        for(int i=0;i<param_str.length;i++){
            Product cur=new Product();
            cur.setName(param_str[i]);
            cur.setPrice(new BigDecimal(2000));
            cur.setStock(1000);
            mProductData.add(cur);
        }

        //设置布局
        VirtualLayoutManager virtualLayoutManager=new VirtualLayoutManager(getActivity());
        mhome_rv.setLayoutManager(virtualLayoutManager);

        /*定义适配器列表*/
        List<DelegateAdapter.Adapter> adapters=new LinkedList<>();

        /*轮播图*/
        LinearLayoutHelper linearLayoutHelper=new LinearLayoutHelper();
        homeBannerAdapter=new HomeBannerAdapter(imgs,getActivity(),linearLayoutHelper);
        adapters.add(homeBannerAdapter);


        //设置表格布局
        GridLayoutHelper gridLayoutHelper=new GridLayoutHelper(PARAM_ROLL_COL);
        //实例化adapter
        homeTopBannerAndParamAdapter=new HomeTopBannerAndParamAdapter(mCategoryData,getActivity(),gridLayoutHelper);
        //把adapter放入集合，最终交给recycleview
        adapters.add(homeTopBannerAndParamAdapter);


        /*活动区*/
        LinearLayoutHelper ActLayoutHelper=new LinearLayoutHelper();
        linearLayoutHelper.setMarginBottom(Utils.dp2px(getActivity(),20));
        adapters.add(new HomeActAdapter(getActivity(),ActLayoutHelper));


        /*热销商品*/
        LinearLayoutHelper hotLayoutHelper=new LinearLayoutHelper();
        homeHotProductAdapter=new HomeHotProductAdapter(mProductData,getActivity(),hotLayoutHelper);
        homeHotProductAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //提取产品编号并跳转
                String id=mProductData.get(pos).getId()+"";
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });

        adapters.add(homeHotProductAdapter);
        /*点击热销商品，要挑战到详情页面*/


        //设置适配器
        delegateAdapter=new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.setAdapters(adapters);
        mhome_rv.setAdapter(delegateAdapter);

    }

    private void loadParams(){

    //加载产品分类
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .build()
                .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e, int id) {

                            }

                            @Override
                             public void onResponse(String response, int id) {
                                 final Type type=new TypeToken<SverResponse<List<Param>>>(){}.getType();
                                 SverResponse<List<Param>> result= JSONUtills.fromJson(response,type);
                                 if(result.getStatus()== ResponseCode.SUCCESS.getCode()){
                                     return;
                                 }

                                 if(result.getData().size()%PARAM_ROLL_COL==0){
                                     mCategoryData.addAll(result.getData());
                                 }
                                 else{
                                     int count=result.getData().size()/PARAM_ROLL_COL;
                                     mCategoryData.addAll(result.getData().subList(0,count*PARAM_ROLL_COL));
                                 }
                                 homeTopBannerAndParamAdapter.notifyDataSetChanged();
                             }
                         });

    }

    private void  loadHotProducts() {
        OkHttpUtils.get()
                .url(Constant.API.HOT_PRODUCT_URL)
                .addParams("num","10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("homeHot:"+response);
                        final Type type= new TypeToken<SverResponse<List<Product>>>(){}.getType();
                        SverResponse<List<Product>> result=JSONUtills.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData()!=null)
                            {
                                mProductData.clear();
                                mProductData.addAll(result.getData());
                                homeHotProductAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}


