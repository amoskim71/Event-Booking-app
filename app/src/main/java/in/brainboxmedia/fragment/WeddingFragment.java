package in.brainboxmedia.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.brainboxmedia.MainActivity;
import in.brainboxmedia.R;
import in.brainboxmedia.data.WeddingImages;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class WeddingFragment extends DialogFragment {
    private ArrayList<WeddingImages> weddingList;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView top, bottom, middle;
    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private int selectedPosition = 0;

    public static WeddingFragment newInstance() {
        WeddingFragment f = new WeddingFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wedding, container, false);
        ImageView button = v.findViewById(R.id.back_button_wedding);
        viewPager = v.findViewById(R.id.viewpager_wedding);
        dotsLayout = v.findViewById(R.id.layout_dots);
        top = v.findViewById(R.id.wedding_fragement_top);
        bottom = v.findViewById(R.id.wedding_fragement_bottom);
        middle = v.findViewById(R.id.wedding_fragement_middle);
        weddingList = (ArrayList<WeddingImages>) getArguments().getSerializable("Wedding_images");
        selectedPosition = getArguments().getInt("position");
        Log.v("wedding size", "" + weddingList.size());
        myViewPagerAdapter = new WeddingFragment.MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        setCurrentItem(selectedPosition);
        addBottomDots(0);
        changeStatusBarColor();
        return v;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[weddingList.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        Log.v("length", String.valueOf(weddingList.size()));
        for (int i = 0; i < weddingList.size(); i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (weddingList.size() > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    private void displayMetaInfo(int position) {
        WeddingImages image = weddingList.get(position);

        top.setText(image.getTop());
        bottom.setText(image.getBottom());
        middle.setText(image.getMiddle_description());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.fragment_wedding_item, container, false);

            ImageView imageViewPreview = view.findViewById(R.id.wedding_fragement_image);

            WeddingImages image = weddingList.get(position);

            Glide.with(getActivity()).load(image.getUrl())
                    .thumbnail(0.5f)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return weddingList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}

