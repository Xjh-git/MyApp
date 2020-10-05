package com.example.auction.atapter;

import android.content.Context;
import android.widget.TextView;

import com.example.auction.R;
import com.example.auction.bean.Commodity;

import java.text.DecimalFormat;
import java.util.List;

public class CommodityAdapter extends ListViewAdapter<Commodity> {
    public CommodityAdapter(Context context, List data) {
        super(context, data, R.layout.commodity_item);
    }

    @Override
    public void convert(ViewHolder holder, Commodity commodity) {
        TextView tv_name = holder.getView(R.id.name);
        TextView tv_price = holder.getView(R.id.current_price);
        TextView tv_maxPrice = holder.getView(R.id.max_price);

        tv_name.setText(commodity.getName());
        DecimalFormat df = new DecimalFormat("0.00");
        tv_price.setText("竞拍价：" + df.format(commodity.getCurrentPrice()) + "元");
        tv_maxPrice.setText("一口价：" + df.format(commodity.getMaxPrice()) + "元");
    }
}
