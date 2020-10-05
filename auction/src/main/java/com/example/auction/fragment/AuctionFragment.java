package com.example.auction.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.activity.auction.AuctioningActivity;
import com.example.auction.activity.auction.DetailsActivity;
import com.example.auction.atapter.CommodityAdapter;
import com.example.auction.bean.Commodity;
import com.example.auction.bean.Msg;
import com.example.auction.bean.UserInfo;
import com.example.auction.database.CommodityDAO;
import com.example.auction.database.MsgDAO;
import com.example.auction.database.UserDAO;
import com.example.auction.view.UpListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AuctionFragment extends Fragment implements UpListView.ILoadLister {

    List<Commodity> data = new ArrayList<>();
    TextView tv_auction;
    UpListView commodity_list;
    CommodityAdapter adapter;
    EditText edt_search;
    Button btn_search;

    UserInfo user;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.auction_fragment, container, false);

        init();

        return view;

    }

    private void init() {
        //初始化view
        initView();
        //初始化数据
        initData();
        //初始化listener
        initListener();
    }

    private void initListener() {
        //上架拍卖品的点击事件，点击上架拍卖品
        tv_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuctioningActivity.class);
                startActivityForResult(intent, 111);
            }
        });

        //listview的item点击事件，点击跳转到详情页
        commodity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tag1", "onItemClick: " + position);
                Commodity commodity = data.get(position - 1);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("comm", commodity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //搜索的点击事件，点击发起搜索
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_search.getText().equals("搜索")) {
                    search();
                } else {
                    adapter = new CommodityAdapter(getActivity(), data);
                    commodity_list.setAdapter(adapter);
                    btn_search.setText("搜索");
                    edt_search.setText("");
                }
            }
        });

        //监听输入框的回车，输入内容时回车发起搜索
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL) {
                    search();
                }
                return true;
            }
        });

    }

    //搜索
    private void search() {
        //测试数据直接在list中搜索
        String search_content = edt_search.getText().toString();
        if (!TextUtils.isEmpty(search_content)) {
            List<Commodity> data_search = new ArrayList<>();
            Commodity commodity;
            for (int i = 0; i < data.size(); i++) {
                commodity = data.get(i);
                //                if (commodity.getName().indexOf(search_content) != -1) {
                if (commodity.getName().contains(search_content)) {
                    data_search.add(commodity);
                }
            }
            adapter = new CommodityAdapter(getActivity(), data_search);
            commodity_list.setAdapter(adapter);
            btn_search.setText("取消");
        } else {
            Toast.makeText(getActivity(), "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        //设置list的测试数据
        CommodityDAO commodityDAO = new CommodityDAO(getActivity());
        data = commodityDAO.get();
        //测试数据
        //        Commodity commodity;
        //        for (int i = 0; i < 20; i++) {
        //            commodity = new Commodity("name", "details", "icon", 10000, 555);
        //            data.add(commodity);
        //        }

        //给listview设置适配器
        adapter = new CommodityAdapter(this.getActivity(), data);
        commodity_list.setAdapter(adapter);
        commodity_list.setInterface(this);
        //给listview设置上下文菜单
        registerForContextMenu(commodity_list);
    }

    private void initView() {
        tv_auction = (TextView) view.findViewById(R.id.auction);
        commodity_list = (UpListView) view.findViewById(R.id.commodity_list);
        edt_search = (EditText) view.findViewById(R.id.search_edt);
        btn_search = (Button) view.findViewById(R.id.search_btn);
    }

    //初始化长按弹出的上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.manage, menu);
    }

    //点击弹出菜单的item
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            //点击一口价，直接一口价拍卖
            case R.id.maxPrice:
                new AlertDialog.Builder(getActivity())
                        .setTitle("购买？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                purchase(data.get(info.position - 1));
                            }
                        }).setNegativeButton("取消", null).show();
                return true;
            //点击竞拍，弹出输入框输入价格进行竞拍
            case R.id.currentPrice:
                final EditText editText = new EditText(getActivity());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(getActivity())
                        .setTitle("输入价格")
                        .setView(editText)
                        .setPositiveButton("是", new DatePickerDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double price = Double.valueOf(editText.getText().toString());
                                double currPrice = data.get(info.position).getCurrentPrice();
                                if (price > currPrice) {
                                    data.get(info.position).setCurrentPrice(price);
                                    //访问数据库，更新竞拍价
                                    auctioning(price, data.get(info.position).getId());
                                } else {
                                    Toast.makeText(getActivity(), "请输入一个更高的价格", Toast.LENGTH_SHORT).show();
                                }
                                //                                purchase(price);
                            }
                        })
                        .setNegativeButton("否", null).show();
                return true;
            default:
                return false;
        }
    }

    //购买商品
    private void purchase(Commodity commodity) {
        //得到登陆客户的信息
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        int id = sp.getInt("id", -1);
        if (id == -1) {
            return;
        }
        UserDAO userDAO = new UserDAO(getActivity());
        user = userDAO.select(id);

        if (user.getMoney() >= commodity.getMaxPrice()) {
            double money = user.getMoney() - commodity.getMaxPrice();
            user.setMoney(money);
            userDAO.update(user);

            UserInfo seller = userDAO.select(commodity.getOwnerID());
            seller.setMoney(seller.getMoney() + commodity.getMaxPrice());
            userDAO.update(seller);

            Msg msg = new Msg(id, commodity.getOwnerID(), "来自系统的消息\n" +
                    "    您成功购买了来自用户 \"" + commodity.getOwnerID() + "\" 的商品 \"" + commodity.getName()
                    + "\" ，共花费" + commodity.getMaxPrice(),
                    "", new Date(), false, false);
            MsgDAO msgDAO = new MsgDAO(getActivity());
            msgDAO.sendMsg(msg);
            Toast.makeText(getActivity(), "购买成功，花费：" + commodity.getMaxPrice(), Toast.LENGTH_SHORT).show();

            CommodityDAO commodityDAO = new CommodityDAO(getActivity());
            commodityDAO.delete(commodity.getId());

            data.remove(commodity);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "余额不足！", Toast.LENGTH_SHORT).show();
        }

    }

    private void auctioning(double price, int id) {
        //更新数据库
        CommodityDAO commodityDAO = new CommodityDAO(getActivity());
        Commodity commodity = commodityDAO.find(id);
        commodity.setCurrentPrice(price);
        commodityDAO.update(commodity);

        //更新显示的listview
        data = commodityDAO.get();
        adapter.notifyDataSetChanged();
    }


    //上拉加载更多数据
    @Override
    public void onLoad() {
        //测试用，2s后加载一次数据
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载更多数据，更新listview
                loadData();
                adapter.notifyDataSetChanged();
                //通知listview加载完毕
                commodity_list.loadComplete();
            }
        }, 2000);

    }

    //刷新数据
    @Override
    public void onReflash() {
        //测试用，2s后刷新一次数据
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载更多数据，更新listview
                reflashData();
                adapter.notifyDataSetChanged();
                //通知listview加载完毕
                commodity_list.reflashComplete();
            }
        }, 2000);
    }

    //刷新数据
    private void reflashData() {
        //测试数据
        Commodity commodity;
        for (int i = 0; i < 2; i++) {
            commodity = new Commodity("abcd", "details", "icon", 10000, 555, 1, 1);
            data.add(0, commodity);
        }
    }

    //加载数据
    private void loadData() {
        //测试数据
        Commodity commodity;
        for (int i = 0; i < 2; i++) {
            commodity = new Commodity("abcd", "details", "icon", 10000, 555, 1, 1);
            data.add(commodity);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            init();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
