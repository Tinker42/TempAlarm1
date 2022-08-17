package com.ata.tempalarm1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ata.tempalarm1.Data.Alarm;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Alarm> DataSet;
    public MainAdapter(List<Alarm> nextDataSet){
        this.DataSet = nextDataSet;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//new element passing into the recycler view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(v);//returning the view to be modified
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText( Integer.toString(DataSet.get(position).getTemperature()) + "℉" );
    }

    @Override
    public int getItemCount() { return DataSet.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            this.textView = itemView.findViewById(R.id.listTextView);
        }
        public TextView getTextView(){ return textView;}
    }
}

class AlarmDiffCallback extends DiffUtil.ItemCallback<Alarm>{
    @Override
    public boolean areItemsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
        return false;
    }
}
