package in.brainboxmedia.adpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.brainboxmedia.R;
import in.brainboxmedia.data.Attendees;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<Attendees> historyList;
    private Context mContext;

    public HistoryAdapter(Context context, List<Attendees> historyList) {
        this.historyList = historyList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_history_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Attendees data = historyList.get(position);
        holder.eventName.setText(data.getEventName());
        holder.eventDate.setText(data.getDates());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate;

        MyViewHolder(View view) {
            super(view);
            eventName = view.findViewById(R.id.event_detail_history);
            eventDate = view.findViewById(R.id.amount);
        }
    }
}