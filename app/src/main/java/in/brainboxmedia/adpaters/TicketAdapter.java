package in.brainboxmedia.adpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.brainboxmedia.BookingActivity;
import in.brainboxmedia.R;
import in.brainboxmedia.data.TypeTicket;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private Context mContext;
    private List<TypeTicket> ticketList;
    private int prices;

    public TicketAdapter(Context mContext, List<TypeTicket> ticketList) {
        this.ticketList = ticketList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_booking_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TypeTicket ticket = ticketList.get(position);
        holder.details.setText(ticket.getDetail());
        holder.headers.setText(ticket.getheader());
        if((ticket.getPrice()).equals("free")){
            holder.price.setText("free");
            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prices = Integer.parseInt(holder.number.getText().toString());
                    if (prices == 10) {
                        Toast.makeText(mContext, "sorry, maximun 10 booking for each category", Toast.LENGTH_SHORT).show();
                        ticket.setHeaderNumberAndPrice(ticket.getheader() + " (free) " + 10 + " tickets");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(true);
                    } else {
                        int p = prices += 1;
                        holder.number.setText(String.valueOf(p));
                        ticket.setHeaderNumberAndPrice(ticket.getheader() + " (free) " + p + " tickets");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(true);
                    }
                }
            });
            holder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prices = Integer.parseInt(holder.number.getText().toString());
                    if (prices == 0) {
                        holder.number.setText(String.valueOf(0));
                        ticket.setNumberOfTicket(0);
                        ticket.setHeaderNumberAndPrice(" ");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(true);
                    } else {
                        int p = prices -= 1;
                        holder.number.setText(String.valueOf(p));
                        ticket.setHeaderNumberAndPrice(ticket.getheader() + " (free) " + p + " tickets");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(true);
                    }
                }
            });
        }else {
            final int price = Integer.parseInt(ticket.getPrice());
            holder.price.setText("Rs " + price);
            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prices = Integer.parseInt(holder.number.getText().toString());
                    if (prices == 10) {
                        int p = 10 * price;
                        Toast.makeText(mContext, "sorry, maximun 10 booking for each category", Toast.LENGTH_SHORT).show();
                        ticket.setNumberOfTicket(p);
                        ticket.setHeaderNumberAndPrice(ticket.getheader() + " (Rs " + p + ") " + 10 + " tickets");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(false);
                    } else {
                        int p = prices += 1;
                        holder.number.setText(String.valueOf(p));
                        ticket.setNumberOfTicket(p * price);
                        ticket.setHeaderNumberAndPrice(ticket.getheader() + " (Rs " + p * price + ") " + p + " tickets");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(false);
                    }
                }
            });
            holder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prices = Integer.parseInt(holder.number.getText().toString());
                    if (prices == 0) {
                        holder.number.setText(String.valueOf(0));
                        ticket.setNumberOfTicket(0);
                        ticket.setHeaderNumberAndPrice(" ");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(false);
                    } else {
                        int p = prices -= 1;
                        holder.number.setText(String.valueOf(p));
                        ticket.setNumberOfTicket(p * price);
                        ticket.setHeaderNumberAndPrice(ticket.getheader() + " (Rs " + p * price + ") " + p + " tickets");
                        holder.each.setText(ticket.getHeaderNumberAndPrice());
                        calculate(false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    private void calculate(boolean free) {
        if(free){
            String selection = "";
            for (int i = 0; i < ticketList.size(); i++) {
                String a = ticketList.get(i).getHeaderNumberAndPrice();
                if (a == null) {
                    selection = selection + "";
                } else {
                    selection = selection + "\n" + a;
                }
            }
            BookingActivity.sum.setText("free");
            BookingActivity.details = selection;
        }else {
            int price = 0;
            String selection = "";
            for (int i = 0; i < ticketList.size(); i++) {
                int p = ticketList.get(i).getNumberOfTicket();
                price = price + p;
                String a = ticketList.get(i).getHeaderNumberAndPrice();
                if (a == null) {
                    selection = selection + "";
                } else {
                    selection = selection + "\n" + a;
                }
            }
            BookingActivity.sum.setText(String.format("Rs %s", String.valueOf(price)));
            BookingActivity.details = selection;
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView price, details, headers, plusButton, minusButton, number, each;

        public MyViewHolder(View view) {
            super(view);
            price = view.findViewById(R.id.ticket_price);
            details = view.findViewById(R.id.ticket_detail);
            headers = view.findViewById(R.id.ticket_header);
            plusButton = view.findViewById(R.id.add);
            each = view.findViewById(R.id.each_total);
            minusButton = view.findViewById(R.id.minus);
            number = view.findViewById(R.id.selected_number);
        }
    }
}