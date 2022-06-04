package com.example.androidforecommerce.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidforecommerce.R;
import com.example.androidforecommerce.adapter.ConfirmOrderProductAdapter;
import com.example.androidforecommerce.config.Constant;
import com.example.androidforecommerce.pojo.Address;
import com.example.androidforecommerce.pojo.Cart;
import com.example.androidforecommerce.pojo.CartItem;
import com.example.androidforecommerce.pojo.Order;
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

public class ConfirmOrderActivity extends AppCompatActivity {

    //收货人信息
    private TextView name;
    private TextView mobile;
    private TextView addr_detail;

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private ConfirmOrderProductAdapter confirmOrderProductAdapter;
    private List<CartItem> cartItems;

    private static final int REQ_ADDR_CODE=0;

    private Address defaultAddr;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        initView();

        //初始化默认地址
        initDefaultAddr();
        //初始化购物车商品
        initCartProducts();
    }

    private void submitOrder()
    {
        if(defaultAddr==null)
        {
            Toast.makeText(ConfirmOrderActivity.this,"请选择收货地址",Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils.post()
                .url(Constant.API.ORDER_CREATE_URL)
                .addParams("addrId",defaultAddr.getId()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("order:"+response);
                        Type type=new TypeToken<SverResponse<Order>>(){}.getType();
                        SverResponse<Order> result=JSONUtills.fromJson(response,type);


                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            //跳转到订单详情
                            /*
                            Toast.makeText(ConfirmOrderActivity.this,"鎻愪氦璁㈠崟瀹屾垚",Toast.LENGTH_SHORT).show();
                            // ConfirmOrderActivity.this.finish();

                            Intent intent = new Intent(Constant.ACTION.LOAD_CART_ACTION);
                            LocalBroadcastManager.getInstance(ConfirmOrderActivity.this).sendBroadcast(intent);
                            ConfirmOrderActivity.this.finish();*/

                        }
                        else {
                            Toast.makeText(ConfirmOrderActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*
    初始化购物车商品
    */
    private  void initCartProducts()
    {
        OkHttpUtils.get()
                .url(Constant.API.CART_LIST_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type=new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result= JSONUtills.fromJson(response,type);
                        //System.out.println("order:"+response);
                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData().getLists()!=null)
                            {
                                cartItems.clear();
                                cartItems.addAll(result.getData().getLists());
                                confirmOrderProductAdapter.notifyDataSetChanged();
                            }
                            total.setText(result.getData().getTotalPrice()+"");
                        }

                    }
                });
    }

    //加载默认地址
    private void initDefaultAddr()
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
                        //System.out.println("addr:"+response);
                        Type type=new TypeToken<SverResponse<List<Address>>>(){}.getType();
                        SverResponse<List<Address>> result = JSONUtills.fromJson(response,type);

                        if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                        {
                            if(result.getData()!=null)
                            {
                                for(Address addr:result.getData())
                                {
                                    if(addr.getDefaultAddr()==1)
                                    {
                                        defaultAddr=addr;
                                        displayInfo();
                                        break;
                                    }
                                }
                                if(defaultAddr==null)
                                {
                                    if(result.getData().size()>0)
                                    {
                                        defaultAddr=result.getData().get(0);
                                        displayInfo();
                                    }
                                    else {
                                        displayNoneInfo();
                                    }
                                }

                            }else {
                                displayNoneInfo();
                            }
                        }
                        else {
                            displayNoneInfo();
                        }

                    }
                });
    }

    private void displayNoneInfo() {
        name.setText(" ");
        mobile.setText(" ");
        addr_detail.setText("没有可用地址");
    }

    /*显示地址信息*/
    private void displayInfo() {
        name.setText(defaultAddr.getName());
        mobile.setText(defaultAddr.getMobile());
        addr_detail.setText(defaultAddr.getAddrDetail());
    }

    private void initView() {
        name=(TextView)findViewById(R.id.name);
        mobile=(TextView)findViewById(R.id.mobile);
        total=(TextView)findViewById(R.id.total);
        addr_detail=(TextView)findViewById(R.id.addr_detail);
        recyclerView=(RecyclerView)findViewById(R.id.cart_rv);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("确认订单");
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartItems=new ArrayList<>();
        confirmOrderProductAdapter=new ConfirmOrderProductAdapter(this,cartItems);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confirmOrderProductAdapter);

        //提交订单
        findViewById(R.id.buy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });

        //选择地址
        findViewById(R.id.address_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转地址选择界面

                Intent intent=new Intent(ConfirmOrderActivity.this,AddressListActivity.class);
                intent.putExtra("from","ConfirmOrderActivity");
                startActivityForResult(intent,REQ_ADDR_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADDR_CODE) {
            if (resultCode == RESULT_OK) {
                defaultAddr = (Address) data.getSerializableExtra("address");
                displayInfo();
            }
        }

    }
}