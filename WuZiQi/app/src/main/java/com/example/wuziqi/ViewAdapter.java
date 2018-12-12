package com.example.wuziqi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

//viewpage类
public class ViewAdapter extends PagerAdapter {
    //private List<View> datas;
    private int[] mImages;
    private Context mContext;
    public ViewAdapter(Context ctx,int[] imgs){
        this.mContext=ctx;
        this.mImages=imgs;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        /*View view=datas.get(position);
        container.addView(view);
        return view;*/
        ImageView imageView = new ImageView(mContext);
        //imageView.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(mContext,200),DensityUtil.dip2px(mContext,200)));

        imageView.setImageResource(mImages[position]);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        return imageView;
        //return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
