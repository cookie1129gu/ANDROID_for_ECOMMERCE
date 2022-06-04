package com.example.androidforecommerce.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.androidforecommerce.R;
import com.example.androidforecommerce.adapter.CartAdapter;
import com.example.androidforecommerce.config.Constant;
import com.example.androidforecommerce.listener.OnItemClickListener;
import com.example.androidforecommerce.pojo.Cart;
import com.example.androidforecommerce.pojo.CartItem;
import com.example.androidforecommerce.pojo.ResponseCode;
import com.example.androidforecommerce.pojo.SverResponse;
import com.example.androidforecommerce.ui.ConfirmOrderActivity;
import com.example.androidforecommerce.ui.DetailActivity;
import com.example.androidforecommerce.ui.LoginActivity;
import com.example.androidforecommerce.utils.JSONUtills;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class CartFragment extends Fragment {

    private MaterialRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<CartItem> mData;
    private CartAdapter cartAdapter;

    private TextView totalPrice;
    private TextView btn_buy;
    private boolean isEdit=false;
    private CheckBox checkBox;

    //本地广播
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;


    public CartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        bindRefreshListener();
        return view;
    }

    private void initView(View view) {

        refreshLayout=(MaterialRefreshLayout)view.findViewById(R.id.refresh_layout);
        recyclerView=(RecyclerView)view.findViewById(R.id.cart_rv);
        totalPrice=(TextView)view.findViewById(R.id.total);
        btn_buy=(TextView)view.findViewById(R.id.btn_buy);
        checkBox=(CheckBox)view.findViewById(R.id.btn_checked);

        mData=new ArrayList<>();
        /*设置测试数据*/
        String category_str[]={"混凝土机械","建筑起重机械","路面机械","土方机械","环卫机械","工业车辆","模型专区","特惠专区","运费"};
        for(int i=0;i<category_str.length;i++){
            CartItem cur=new CartItem();
            cur.setName(category_str[i]);
            mData.add(cur);
        }


        cartAdapter=new CartAdapter(getActivity(),mData);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartAdapter);


        cartAdapter.setOnCartOptListener(new CartAdapter.OnCartOptListener() {
            @Override
            public void updateProductInfo(int productId, int count, int checked) {
                updateProduct(productId,count,checked);
            }

            @Override
            public void deleteProductFromCart(int productId) {
                deleteProductById(productId);
            }
        });

        view.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEdit)
                {
                    isEdit=false;
                    for(CartItem item:mData)
                    {
                        item.setEdit(true);
                    }
                }
                else {
                    isEdit=true;
                    for(CartItem item:mData)
                    {
                        item.setEdit(false);
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //测试用,进入登录界面
                /*
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                */

                //打开结算界面
                Intent intent=new Intent(getActivity(), ConfirmOrderActivity.class);
                startActivity(intent);
                /*
                if(!totalPrice.getText().toString().equals("鍚堣锛氾骏0"))
                {
                    Intent intent=new Intent(getActivity(), ConfirmOrderActivity.class);
                    startActivity(intent);
                }
                else
                {
                    //ToastUitl.showToast(getActivity(),"璇峰厛閫夋嫨鍟嗗搧");
                    //Toast.makeText(getActivity(),"璇峰厛閫夋嫨鍟嗗搧",Toast.LENGTH_SHORT).show();
                }
                */
            }
        });
        cartAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //点击打开产品详情
                String id=mData.get(pos).getProductId()+"";
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //注册广播
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        intentFilter=new IntentFilter();
        intentFilter.addAction(Constant.ACTION.LOAD_CART_ACTION);
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //加载购物车数据
                loadData();
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            loadData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void loadData()
    {
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("cart:"+response);
                        Type type=new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result=JSONUtills.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData().getLists()!=null)
                            {
                                mData.clear();
                                mData.addAll(result.getData().getLists());
                                cartAdapter.notifyDataSetChanged();
                            }
                            totalPrice.setText("￥"+result.getData().getTotalPrice());
                        }
                        else {
                            Intent intent=new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        refreshLayout.finishRefresh();
    }

    //删除商品
    private void deleteProductById(int prouctId) {
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .addParams("productId",prouctId+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {


                        Type type=new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result= JSONUtills.fromJson(response,type);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            mData.clear();
                            mData.addAll(result.getData().getLists());
                            cartAdapter.notifyDataSetChanged();
                        }
                        totalPrice.setText("合计：￥"+result.getData().getTotalPrice());
                    }
                });
    }

    private void updateProduct(int productId,int count,int checked) {
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .addParams("productId",productId+"")
                .addParams("count",count+"")
                .addParams("checked",checked+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result = JSONUtills.fromJson(response,type);
                        System.out.println("debug:"+response+result.getStatus());
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode()){
                            loadData();
                            //if(result.getData().getLists()!=null){
                            //  mData.clear();
                            //   mData.addAll(result.getData().getLists());
                            //  cartAdapter.notifyDataSetChanged();
                            //    }
                            //   totalPrice.setText("合计:￥"+result.getData().getTotalPrice());
                        }
                        else {
                            System.out.println("fail to update");
                        }

                    }
                });
    }
    private void bindRefreshListener()
    {
        refreshLayout.setLoadMore(true);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                // loadParams();
                loadData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                refreshLayout.finishRefreshLoadMore();

            }
        });
    }
}