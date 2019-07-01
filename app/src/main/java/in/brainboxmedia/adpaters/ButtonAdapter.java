package in.brainboxmedia.adpaters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.brainboxmedia.R;
import in.brainboxmedia.data.Buttons;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.MyViewHolder> {

    private List<Buttons> buttonList;

    public ButtonAdapter(List<Buttons> buttonList) {
        this.buttonList = buttonList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_button, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Buttons button = buttonList.get(position);

        holder.buttonBackground.setBackgroundResource(button.getButtonBackground());
        holder.buttonName.setText(button.getButtonName());
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView buttonName;
        LinearLayout buttonBackground;

        MyViewHolder(View view) {
            super(view);
            buttonName = view.findViewById(R.id.button_name);
            buttonBackground = view.findViewById(R.id.fragment_home_button_back);
        }
    }
}