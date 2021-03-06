package com.example.androidforecommerce.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.androidforecommerce.R;
import com.example.androidforecommerce.config.Constant;
import com.example.androidforecommerce.pojo.Product;
import com.example.androidforecommerce.pojo.ResponseCode;
import com.example.androidforecommerce.pojo.SverResponse;
import com.example.androidforecommerce.utils.JSONUtills;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

public class DetailActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private ImageView icon_url;
    private TextView name;
    private TextView parts;
    private TextView price;
    private TextView stock;
    private EditText num;
    private WebView product_detail;
    private Toolbar toolbar;

    private Product product;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        //提取其他界面传递过来的产品编号
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        if(!TextUtils.isEmpty(id))
        {
            loadProductById(id);
        }
    }

    private void initView() {
        icon_url=(ImageView)findViewById(R.id.icon_url);
        name=(TextView)findViewById(R.id.name);
        parts=(TextView)findViewById(R.id.parts);
        price=(TextView)findViewById(R.id.price);
        stock=(TextView)findViewById(R.id.stock);
        num=(EditText)findViewById(R.id.edit_num);

        product_detail=(WebView)findViewById(R.id.product_detail);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("配置详情");
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_jia).setOnClickListener(this);;
        findViewById(R.id.btn_jian).setOnClickListener(this);
        findViewById(R.id.buy_btn).setOnClickListener(this);
        findViewById(R.id.addToCart).setOnClickListener(this);
        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stock=s.toString();
                if(!TextUtils.isEmpty(stock))
                {
                    int inputNum=Integer.valueOf(stock);

                    if(inputNum>product.getStock())
                    {
                        num.setText(product.getStock()+"");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {


        int inputNum=Integer.valueOf(num.getText().toString());//购买商品数量
        switch (v.getId())
        {
            case R.id.addToCart:
                addItemToCart();
                break;
            case  R.id.btn_jia:
                if(inputNum+1<=product.getStock())
                    num.setText((inputNum+1)+"");
                break;
            case  R.id.btn_jian:
                if(inputNum-1>=1)
                    num.setText((inputNum-1)+"");
                break;
            case  R.id.buy_btn:
                  createOrder();
                break;
        }
    }

    private void createOrder() {
        if(product!=null)
        {
            OkHttpUtils.post()
                    .url(Constant.API.CART_ADD_URL)
                    .addParams("productId",product.getId()+"")
                    .addParams("count",num.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            SverResponse result=JSONUtills.fromJson(response,SverResponse.class);
                            if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                            {
                                /*
                                Intent intent=new Intent(DetailActivity.this,ConfirmOrderActivity.class);
                                */
                            }
                            else {
                                Toast.makeText(DetailActivity.this,"澶辫触",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void loadProductById(final String productId)
    {
        OkHttpUtils.get()
                .url(Constant.API.PRODUCT_DETAIL_URL)
                .addParams("productId",productId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Product>>(){}.getType();
                        SverResponse<Product> result = JSONUtills.fromJson(response,type);
                        System.out.println("debug:"+response+result.getStatus());
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode()){
                            if(result.getData()==null)
                            {
                                return;
                            }
                            product=result.getData();

                            //显示配件信息
                            //System.out.println("icon_url:"+Constant.API.BASE_URL+product.getIconUrl());
                            Glide.with(DetailActivity.this).load(Constant.API.BASE_URL+product.getIconUrl()).into(icon_url);
                            name.setText(product.getName());
                            price.setText("配件类型:"+product.getPrice());
                            stock.setText("￥"+product.getStock());
                                    num.setText("1");
                            product_detail.loadDataWithBaseURL(Constant.API.BASE_URL,product.getDetail(),
                                    "text/html","utf-8",null );
                        }
                        else {
                            DetailActivity.this.finish();
                        }
                    }
                });
    }

    /*
    *加入购物车
    */
    private void addItemToCart()
    {
        if(product!=null)
        {
            OkHttpUtils.post()
                    .url(Constant.API.CART_ADD_URL)
                    .addParams("productId",product.getId()+"")
                    .addParams("count",num.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            SverResponse result= JSONUtills.fromJson(response, SverResponse.class);
                            if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                            {
                                Toast.makeText(DetailActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(DetailActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}