package com.example.androidforecommerce.config;

public class Constant {
    public static class API{
        //基地址
        public static final String BASE_URL="http://localhost:8080/actionmall/";
        //产品类型参数
        public static final String CATEGORY_PARAM_URL=BASE_URL+"param/findallparams.do";
        //热销商品
        public static final String HOT_PRODUCT_URL=BASE_URL+"product/findhotproducts.do";

        public static final String CATEGORY_PRODUCT_URL=BASE_URL+"product/findproducts.do";

        //购物车
        public static final String CART_LIST_URL=BASE_URL+"cart/findallcarts.do";
        public static final String CART_DELETE_URL=BASE_URL+"cart/delcarts.do";
        //删除购物车中商品
        public static final String CART_UPDATE_URL=BASE_URL+"cart/updatecarts.do";
        //获取用户信息
        public static final String USER_INFO_URL=BASE_URL+"user/getuserinfo.do";

        //登录接口
        public static final String USER_LOGIN_URL=BASE_URL+"user/do_login.do";
        public static final String USER_LOGOUT_URL=BASE_URL+"user/do_logout.do";

        //地址列表
        public static final String USER_ADDR_LIST_URL=BASE_URL+"addr/findaddrs.do";
        //删除地址
        public static final String USER_ADDR_DEL_URL=BASE_URL+"addr/deladdr.do";
        //添加新地址
        public static final String USER_ADDR_ADD_URL=BASE_URL+"addr/saveaddr.do";
        //设置默认地址
        public static final String USER_ADDR_DEFAULT_URL=BASE_URL+"addr/setdefault.do";
        public static final String USER_REGISTER_URL=BASE_URL+"user/do_register.do";

        //商品详情数据
        public static final String PRODUCT_DETAIL_URL=BASE_URL+"product/getdetail.do";
        public static final String CART_ADD_URL=BASE_URL+"cart/savecart.do";
        //提交订单
        public static final String ORDER_CREATE_URL=BASE_URL+"order/createorder.do";
        //订单列表
        public static final String ORDER_LIST_URL=BASE_URL+"order/getlist.do";
        //订单详情
        public static final String ORDER_DETAIL_URL=BASE_URL+"order/getdetail.do";
        public static final String ORDER_CANCEL_URL=BASE_URL+"order/cancelorder.do";
    }
    public static class ACTION{
        //加载购物车列表
        public static final String LOAD_CART_ACTION="cn.techaction.mall.LOAD_CART_ACTION";
    }
}
