package com.example.androidforecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.androidforecommerce.fragment.CartFragment;
import com.example.androidforecommerce.fragment.CategoryFragment;
import com.example.androidforecommerce.fragment.HomeFragment;
import com.example.androidforecommerce.fragment.UserFragment;

public class MainActivity extends AppCompatActivity{
    private RadioGroup main_rg;

    private HomeFragment homeFragment;
    private UserFragment userFragment;
    private CategoryFragment categoryFragment;
    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment=new HomeFragment(this);
        categoryFragment=new CategoryFragment();
        cartFragment=new CartFragment();
        userFragment=new UserFragment();
        main_rg=(RadioGroup) findViewById(R.id.main_rg);


        //设置初始fragment
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fl,homeFragment);//home
        fragmentTransaction.commit();//提交事件


        //点击切换
        main_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=(RadioButton)radioGroup.findViewById(i);
                switch(radioButton.getId()){
                    case R.id.rb1:
                        changefragement(homeFragment);//home
                        break;
                    case R.id.rb2:
                        changefragement(categoryFragment);
                        break;
                    case R.id.rb3:
                        changefragement(cartFragment);
                        break;
                    case R.id.rb4:
                        changefragement(userFragment);
                        break;
                }

                //Toast.makeText(MainActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //动态切换fragment
    private void changefragement(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fl,fragment);//创建一个将fragment替换到fglayout控件上的事件
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();//提交事件

    }
}