package in.brainboxmedia.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import in.brainboxmedia.CareerActivity;
import in.brainboxmedia.CategorizedGalleryActivity;
import in.brainboxmedia.EventsActivity;
import in.brainboxmedia.EventsDetailsActivity;
import in.brainboxmedia.ExhibitionActivity;
import in.brainboxmedia.GalleryActivity;
import in.brainboxmedia.MainActivity;
import in.brainboxmedia.ProfileActivity;
import in.brainboxmedia.R;
import in.brainboxmedia.ServicesActivity;
import in.brainboxmedia.adpaters.ButtonAdapter;
import in.brainboxmedia.adpaters.LatestEventAdapter;
import in.brainboxmedia.data.Buttons;
import in.brainboxmedia.data.EventsParties;
import in.brainboxmedia.data.WeddingImages;
import in.brainboxmedia.interfaces.ClickListener;
import in.brainboxmedia.listners.RecyclerTouchListener;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class HomeFragment extends Fragment {
    public View view;
    private List<Buttons> buttonsList;
    private ButtonAdapter mAdapter;
    private LatestEventAdapter adapter;
    private List<EventsParties> latestThree;
    private RecyclerView mRecyclerView, latestEventRV;
    private ProgressDialog progress;
    private List<String> key;
    private ArrayList<WeddingImages> weddingList;
    private ImageView profile;
    private ViewPager viewPager;
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        //progress dialog
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
        progress = new ProgressDialog(getContext());
        progress.setMessage("fetching the data mate :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        profile = view.findViewById(R.id.profile_go);

        //declarations
        weddingList = new ArrayList<>();
        buttonsList = new ArrayList<>();
        latestThree = new ArrayList<>();
        key = new ArrayList<>();
        buttonsList.clear();
        latestThree.clear();
        //data loading
        MyTask m = new MyTask();
        m.execute();

        adapter = new LatestEventAdapter(getContext(), latestThree);
        latestEventRV = view.findViewById(R.id.latest_events_list);
        latestEventRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        latestEventRV.setItemAnimator(new DefaultItemAnimator());
        latestEventRV.setAdapter(adapter);


        //button recycler procedure
        mAdapter = new ButtonAdapter(buttonsList);
        mRecyclerView = view.findViewById(R.id.home_button_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        prepareAlbums();
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity().getBaseContext(), EventsActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case 1:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Wedding_images", weddingList);
                        bundle.putInt("position", 0);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.setTransition(R.anim.fadein);
                        WeddingFragment newFragment = WeddingFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.setEnterTransition(R.anim.fadein);
                        newFragment.show(ft, "slideshows");
                        break;
                    case 2:
                        intent = new Intent(getActivity().getBaseContext(), ServicesActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case 3:
                        intent = new Intent(getActivity().getBaseContext(), ExhibitionActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case 4:
                        intent = new Intent(getActivity().getBaseContext(), CareerActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case 5:
                        intent = new Intent(getActivity().getBaseContext(), CategorizedGalleryActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        latestEventRV.addOnItemTouchListener(new RecyclerTouchListener(getContext(), latestEventRV, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v("key : ", key.get(position));

                Intent intent = new Intent(getContext(), EventsDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("eventId", key.get(position));
                intent.putExtras(extras);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity().getBaseContext(), ProfileActivity.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        return view;
    }

    private void prepareAlbums() {
        int[] logos = new int[]{
                R.drawable.event_button,
                R.drawable.wedding_button,
                R.drawable.services_button,
                R.drawable.exhibition_button,
                R.drawable.career_button,
                R.drawable.gallery_button,
        };

        Buttons a = new Buttons(getString(R.string.event), logos[0]);
        buttonsList.add(a);

        a = new Buttons(getString(R.string.wedding), logos[1]);
        buttonsList.add(a);

        a = new Buttons(getString(R.string.services), logos[2]);
        buttonsList.add(a);

        a = new Buttons(getString(R.string.exhibition), logos[3]);
        buttonsList.add(a);

        a = new Buttons(getString(R.string.career), logos[4]);
        buttonsList.add(a);

        a = new Buttons(getString(R.string.gallery), logos[5]);
        buttonsList.add(a);

        mAdapter.notifyDataSetChanged();
    }

    void GetEventDataFirebase() {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .orderByChild("date")
                .limitToFirst(3);
        query.keepSynced(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                key.add(dataSnapshot.getKey());
                EventsParties data = dataSnapshot.getValue(EventsParties.class);
                latestThree.add(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                GetEventDataFirebase();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                GetEventDataFirebase();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                GetEventDataFirebase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "sry please try after some time database is not connected .", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetEventDataFirebase();
            GetWeddingDataFirebase();
            return null;
        }

        public void GetWeddingDataFirebase() {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("wedding")
                    .orderByChild("id");
            query.keepSynced(true);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    WeddingImages data = dataSnapshot.getValue(WeddingImages.class);
                    weddingList.add(data);
                    progress.dismiss();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    GetWeddingDataFirebase();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    GetWeddingDataFirebase();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    GetWeddingDataFirebase();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "sry not able to connect with the database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



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
