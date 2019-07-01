package in.brainboxmedia.adpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.brainboxmedia.R;
import in.brainboxmedia.data.EventsParties;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class LatestEventAdapter extends RecyclerView.Adapter<LatestEventAdapter.MyViewHolder> {

    private Context mContext;
    private List<EventsParties> eventPartiesList;


    public LatestEventAdapter(Context mContext, List<EventsParties> eventPartiesList) {
        this.mContext = mContext;
        this.eventPartiesList = eventPartiesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_event_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        EventsParties ep = eventPartiesList.get(position);
        holder.title.setText(ep.getName());
        Glide.with(mContext).load(ep.getParty_poster())
                .thumbnail(0.6f).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return eventPartiesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.event_name_latest);
            thumbnail = view.findViewById(R.id.thumbnail_latest);
        }
    }


}
