//package com.allie.pre90secs;
//
//import android.content.Context;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//
//
//public class TabPagerAdapter extends FragmentPagerAdapter {
//    private int mNumOfTabs;
//    private Context context;
//    private ImageView icon;
//    private FragmentManager fragmentManager;
//
//    public TabPagerAdapter(FragmentManager fm, Context context, int NumOfTabs) {
//        super(fm);
//        this.fragmentManager = fm;
//        this.context = context;
//        this.mNumOfTabs = NumOfTabs;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//
//        switch (position) {
//            case 0:
//                FetchWorkoutFragment fetchWorkoutFragment = FetchWorkoutFragment.newInstance();
//                return fetchWorkoutFragment;
//            case 1:
//                FilterOptionsFragment filterOptionsFragment = FilterOptionsFragment.newInstance();
//                return filterOptionsFragment;
//            default:
//                return null;
//        }
//    }
//
//    public View getTabView(int position) {
//        View view = LayoutInflater.from(this.context).inflate(R.layout.custom_tab_layout, null);
//        icon = (ImageView) view.findViewById(R.id.icon);
//
//        if(position == 0){
//            icon.setImageResource(R.drawable.selector_tab_dumbell);
////            getItem(0);
//        }
//        if(position == 1){
//            icon.setImageResource(R.drawable.selector_tab_filter);
////            getItem(1);
//        }
//
//        return view;
//    }
//
//    @Override
//    public int getCount() {
//        return mNumOfTabs;
//    }
//}

