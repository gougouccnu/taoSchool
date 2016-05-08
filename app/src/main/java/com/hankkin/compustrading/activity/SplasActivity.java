package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Category;
import com.hankkin.compustrading.model.Product;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class SplasActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Bmob.initialize(this, "e2d5b2bea1b178fd75764aadadbba7d0");
        long size = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "compustrading" +"/app-release.apk").length();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    queryCategory();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Bmob查询分类数据显示选项卡
     * by Hankkin at:2015-11-29 19:39:45
     */
    private void queryCategory() {
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.order("createdAt");// 按照时间降序
        categoryBmobQuery.findObjects(this, new FindListener<Category>() {
            @Override
            public void onSuccess(List<Category> list) {
                if (list != null && list.size() > 0) {
                    ArrayList<Category> categories = new ArrayList<Category>();
                    categories.addAll(list);
                    Intent intent = new Intent(SplasActivity.this,MainShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("categories", (Serializable) categories);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else { //构造category初始数据
                    categoryInit();
                    productInit();
                }
            }

            @Override
            public void onError(int i, String s) {
                HankkinUtils.showToast(SplasActivity.this, s);
            }
        });


    }

    /**
     * category init,为构造初始数据用
     */
    private void categoryInit(){
        List<BmobObject> categorys = new ArrayList<BmobObject>();
        categorys.add(new Category(0, "手机数码", "手机数码", 0));
        categorys.add(new Category(1, "书籍", "书籍", 1));
        categorys.add(new Category(2, "运动", "运动", 2));
        categorys.add(new Category(3, "生活", "生活", 3));
        categorys.add(new Category(4, "其他", "其他", 4));
        new BmobObject().insertBatch(this, categorys, new SaveListener() {
            @Override
            public void onSuccess() {
                HankkinUtils.showLToast(SplasActivity.this, "category bath init ok");
            }

            @Override
            public void onFailure(int i, String s) {
                HankkinUtils.showLToast(SplasActivity.this, s);
            }
        });
    }
    /**
     * product init，为构造初始数据用
     */
    private void productInit(){
        Product productObj = new Product(0,"手机","999", "八成新小米4转让", "18926418053",0);
        productObj.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                HankkinUtils.showLToast(SplasActivity.this, "product init ok");
            }

            @Override
            public void onFailure(int i, String s) {
                HankkinUtils.showLToast(SplasActivity.this, s);
            }
        });
    }

}
