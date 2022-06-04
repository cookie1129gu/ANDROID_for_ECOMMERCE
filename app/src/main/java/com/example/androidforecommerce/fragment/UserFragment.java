package com.example.androidforecommerce.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.androidforecommerce.R;
import com.example.androidforecommerce.config.Constant;
import com.example.androidforecommerce.pojo.ResponseCode;
import com.example.androidforecommerce.pojo.SverResponse;
import com.example.androidforecommerce.pojo.User;
import com.example.androidforecommerce.ui.LoginActivity;
import com.example.androidforecommerce.utils.JSONUtills;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;


public class UserFragment extends Fragment {

    private TextView user;

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    public UserFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localBroadcastManager= LocalBroadcastManager.getInstance(getActivity());
        intentFilter=new IntentFilter();
        intentFilter.addAction(Constant.ACTION.LOAD_CART_ACTION);
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initUserInfo();
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) initUserInfo();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void initUserInfo() {
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type=new TypeToken<SverResponse<User>>(){}.getType();
                        SverResponse<User> result= JSONUtills.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            user.setText(result.getData().getAccount());
                        }else{
                            //用户登录页
                            Intent intent=new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);

                        }

                    }
                });
    }

    private void initView(View view) {
        user=(TextView)view.findViewById(R.id.user);
        view.findViewById(R.id.btn_addr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                //打开地址详情页
                Intent intent=new Intent(getActivity(),AddressListActivity.class);
                intent.putExtra("from","UserFragment");
                startActivity(intent);
                */
            }

        });
        view.findViewById(R.id.btn_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                //打开用户订单页
                Intent intent=new Intent(getActivity(),OrderListActivity.class);
                // intent.putExtra("from","UserFragment");
                startActivity(intent);
                */
            }

        });
        view.findViewById(R.id.btn_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {

        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
/*
                        user.setText("鏈櫥褰?);

                                Intent intent=new Intent(getActivity(),LoginActivity.class);

                        startActivity(intent);
*/
                    }
                });
    }

}