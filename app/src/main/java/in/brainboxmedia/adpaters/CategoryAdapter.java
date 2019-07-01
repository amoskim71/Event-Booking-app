package in.brainboxmedia.adpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import in.brainboxmedia.R;
import in.brainboxmedia.data.EventsParties;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> keyList;
    private List<Integer> color = new ArrayList<Integer>();

    public CategoryAdapter(Context mContext, List<String> keyList) {
        this.mContext = mContext;
        this.keyList = keyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        color.add(R.color.bg_screen1);
        color.add(R.color.bg_screen2);
        color.add(R.color.bg_screen3);
        color.add(R.color.bg_screen4);
        color.add(android.R.color.holo_blue_bright);
        color.add(android.R.color.holo_orange_dark);
        color.add(android.R.color.holo_red_light);
        color.add(android.R.color.holo_blue_dark);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Random r = new Random();
        int i = r.nextInt(8);
        holder.title.setText(keyList.get(position));
        holder.title.setBackgroundColor(mContext.getResources().getColor(color.get(i)));
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.category_name);
        }
    }


}
