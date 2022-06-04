package com.example.androidforecommerce.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.androidforecommerce.R;
import com.example.androidforecommerce.adapter.AddressAdapter;
import com.example.androidforecommerce.config.Constant;
import com.example.androidforecommerce.listener.OnItemClickListener;
import com.example.androidforecommerce.pojo.Address;
import com.example.androidforecommerce.pojo.ResponseCode;
import com.example.androidforecommerce.pojo.SverResponse;
import com.example.androidforecommerce.utils.JSONUtills;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class AddressListActivity extends AppCompatActivity {

    private AddressAdapter addressAdapter;
    private List<Address> mData;
    private Toolbar toolbar;

    private static final int REQ_ADDR_CODE=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        initView();
        loadAddrLists();
    }

    //根据id删除地址
    private void deleteAddr(String id)
    {
        OkHttpUtils.get()
                .url(Constant.API.USER_ADDR_DEL_URL)
                .addParams("id",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type=new TypeToken<SverResponse>(){}.getType();
                        SverResponse result=JSONUtills.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            loadAddrLists();//重新加载数据
                        }
                        else {
                            Toast.makeText(AddressListActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void loadAddrLists()
    {
        OkHttpUtils.get()
                .url(Constant.API.USER_ADDR_LIST_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        System.out.println(response);
                        Type type=new TypeToken<SverResponse<List<Address>>>(){}.getType();
                        SverResponse<List<Address>> result=JSONUtills.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            mData.clear();
                            mData.addAll(result.getData());
                            addressAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    private void initView()
    {
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.addr_rv);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("收货地址");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mData=new ArrayList<>();
        addressAdapter=new AddressAdapter(this,mData);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addressAdapter);

        //添加地址
        findViewById(R.id.btn_add_addr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent=new Intent(AddressListActivity.this,AddAddressActivity.class);
                startActivityForResult(intent,REQ_ADDR_CODE);
                */
            }
        });

        addressAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                Intent tempIntent=getIntent();
                String from= tempIntent.getStringExtra("from");
                System.out.println("from:"+from);
                if(from.equals("UserFragment"))
                {
                    Toast.makeText(AddressListActivity.this,"鏉ヨ嚜UserFragment",Toast.LENGTH_SHORT).show();
                }
                else if(from.equals("ConfirmOrderActivity"))
                {
                    Address address=mData.get(pos);
                    Intent intent=new Intent();
                    intent.putExtra("address",address);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });

        addressAdapter.setOnAddrOptListener(new AddressAdapter.OnAddrOptListener() {
            @Override
            public void deleteItem(View v, int pos) {
                //获取id，删除地址

                String id = mData.get(pos).getId()+"";
                //System.out.println("delete:"+id);
                deleteAddr(id);
            }

            @Override
            public void changeAddrDefault(int addrId) {
                switchAddrDefaultById(addrId);
            }
        });

    }

    //加载地址数据
    private void switchAddrDefaultById(int addrId) {
        OkHttpUtils.get()
                .url(Constant.API.USER_ADDR_DEFAULT_URL)
                .addParams("id",addrId+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        System.out.println(response);
                        Type type=new TypeToken<SverResponse>(){}.getType();
                        SverResponse result= JSONUtills.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            loadAddrLists();
                        }
                        else {
                            Toast.makeText(AddressListActivity.this,result.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADDR_CODE) {
            if (resultCode == RESULT_OK) {
                loadAddrLists();
            }
        }

    }
}