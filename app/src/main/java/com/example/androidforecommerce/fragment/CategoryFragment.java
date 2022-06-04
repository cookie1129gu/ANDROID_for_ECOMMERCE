package com.example.androidforecommerce.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.androidforecommerce.MainActivity;
import com.example.androidforecommerce.R;
import com.example.androidforecommerce.adapter.CategoryLeftAdapter;
import com.example.androidforecommerce.adapter.CategoryRightAdapter;
import com.example.androidforecommerce.config.Constant;
import com.example.androidforecommerce.listener.OnItemClickListener;
import com.example.androidforecommerce.pojo.PageBean;
import com.example.androidforecommerce.pojo.Param;
import com.example.androidforecommerce.pojo.Product;
import com.example.androidforecommerce.pojo.ResponseCode;
import com.example.androidforecommerce.pojo.SverResponse;
import com.example.androidforecommerce.ui.DetailActivity;
import com.example.androidforecommerce.utils.JSONUtills;
import com.example.androidforecommerce.utils.SpaceItemDecoration;
import com.example.androidforecommerce.utils.Utils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {
    View rootview;

    private List<Param> leftCategoryData;//左侧分类参数
    private List<Product>  rightCategoryData;//右侧产品数据

    private RecyclerView leftRecyclerView;//左侧列表组件
    private CategoryLeftAdapter categoryLeftAdapter;//左侧适配器

    private RecyclerView rightRecyclerView;//右侧列表组件
    private CategoryRightAdapter categoryrightAdapter;//右侧适配器

    private MaterialRefreshLayout refreshLayout;

    private SverResponse<PageBean<Product>> result;//获取的产品数据
    private String typeId;//产品品种信息
    private String name;//产品名称

    private EditText search_edit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_category, container, false);

        initView();
        loadParams();
        return rootview;
    }

    private void initView() {
        leftRecyclerView=(RecyclerView)rootview.findViewById(R.id.categoty_rv);
        rightRecyclerView=(RecyclerView)rootview.findViewById(R.id.product_rv);
        refreshLayout=(MaterialRefreshLayout)rootview.findViewById(R.id.refresh_layout);

        //实例化数据容器
        leftCategoryData=new ArrayList<>();
        rightCategoryData=new ArrayList<>();

        /*设置测试数据*/
        String category_str[]={"混凝土机械","建筑起重机械","路面机械","土方机械","环卫机械","工业车辆","模型专区","特惠专区","运费"};
        String param_str[]={"混凝土缸a260","拖垒高强度直管","S管阀","超轻-混凝土垒车输送管","奔驰油滤包","AAAAA","BBBBB","CCCCC","DDDDD" };
        for(int i=0;i<category_str.length;i++){
            Param cur=new Param();
            cur.setName(category_str[i]);
            leftCategoryData.add(cur);
        }
        for(int i=0;i<param_str.length;i++){
            Product cur=new Product();
            cur.setName(param_str[i]);
            cur.setPrice(new BigDecimal(2000));
            cur.setStock(1000);
            rightCategoryData.add(cur);
        }



        categoryLeftAdapter=new CategoryLeftAdapter(getActivity(),leftCategoryData);
        categoryrightAdapter=new CategoryRightAdapter(getActivity(),rightCategoryData);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        leftRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        leftRecyclerView.setAdapter(categoryLeftAdapter);
        categoryLeftAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                typeId=leftCategoryData.get(pos).getId()+"";
                name=leftCategoryData.get(pos).getName();
                findProductByParams(typeId,1,10,name,true);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });


        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        rightRecyclerView.addItemDecoration(new SpaceItemDecoration(Utils.dp2px(getActivity(),10),Utils.dp2px(getActivity(),5)));
        rightRecyclerView.setLayoutManager(gridLayoutManager);
        rightRecyclerView.setAdapter(categoryrightAdapter);
        categoryrightAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //跳转到产品属性页
                //提取产品编号并跳转
                String id=rightCategoryData.get(pos).getId()+"";
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });


    }

    private void bindRefreshListener()
    {
        refreshLayout.setLoadMore(true);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //结束刷新
                refreshLayout.finishRefresh();
            }
            //下拉加载更多，分页
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //super.onRefreshLoadMore(materialRefreshLayout);
                if(result!=null&&result.getStatus()==ResponseCode.SUCCESS.getCode())
                {
                    PageBean pageBean=result.getData();
                    if(pageBean.getPageNum()!=pageBean.getNextPage())
                    {
                        findProductByParams(typeId,pageBean.getNextPage(),pageBean.getPageSize(),name,false);

                    }/*
                    else {
                        refreshLayout.finishRefreshLoadMore();
                        ToastUitl.showToast(getActivity(),"宸茬粡鍒板簳浜?);
                                // Toast.makeText(getActivity(),"宸茬粡鍒板簳浜?,Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        });
    }


    private void findProductByParams(String productTypeId, int pageNum, int pageSize, String name,final boolean flag)
    {
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .addParams("name","")
                .addParams("productTypeId",productTypeId)
                .addParams("partsId",0+"")
                .addParams("pageNum",pageNum+"")
                .addParams("pageSize",pageSize+"")

                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        final Type type= new TypeToken<SverResponse<PageBean<Product>>>(){}.getType();
                        result= JSONUtills.fromJson(response,type);

                        if(result.getStatus()==ResponseCode.SUCCESS.getCode()) {
                            if(null!=result.getData()) {
                                if(flag) rightCategoryData.clear();
                                rightCategoryData.addAll(result.getData().getData());
                                categoryrightAdapter.notifyDataSetChanged();
                            }
                            if(!flag)
                            {
                                 refreshLayout.finishRefreshLoadMore();
                            }
                        }
                    }
                });
        refreshLayout.finishRefreshLoadMore();
    }

    private void loadParams() {
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        System.out.println("error:"+e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        final Type type= new TypeToken<SverResponse<List<Param>>>(){}.getType();
                        SverResponse<List<Param>> result= JSONUtills.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData()==null)
                            {
                                return;
                            }


                            //leftCategoryData.clear();
                            leftCategoryData.addAll(result.getData());
                            String typeId=leftCategoryData.get(0).getId()+"";
                            leftCategoryData.get(0).setPressed(true);

                            findProductByParams(typeId,1,10,leftCategoryData.get(0).getName(),true);

                            categoryLeftAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}