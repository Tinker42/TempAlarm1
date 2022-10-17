package com.ata.tempalarm1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.MainDB;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Alarm> DataSet;
    private MainDB dataBase;
    public MainAdapter(List<Alarm> nextDataSet, Context context){
        this.DataSet = nextDataSet;
        dataBase = Room.databaseBuilder(context, MainDB.class, "userAlarms")
                .allowMainThreadQueries()
                .build();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//new element passing into the recycler view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(v);//returning the view to be modified
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextViewCount().setText( Integer.toString(position + 1) + ": " );
        holder.getTextView().setText( Integer.toString(DataSet.get(position).getTemperature()) + "℉" );
        if (DataSet.get(position).getHighOrLow() == 0){
            holder.getTextViewH0C1().setText( "Hotter" );
        }else{
            holder.getTextViewH0C1().setText( "Colder" );
        }
        holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataBase.listDAO().Delete(DataSet.remove(holder.getAdapterPosition()));//getting the adapter from the viewholder which represents the position/index on the recycler view
                //then the valure is passed into the remove function that is on any list collection, the remove func passes bac the removed object, which is passed into the delete function of the listDAO
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return DataSet.size(); }

    public void addItem(Alarm alarm){ DataSet.add(alarm); notifyDataSetChanged(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView textViewCount;
        private TextView textView;
        private TextView textViewH0C1;
        //delete button stuff

        private AppCompatImageButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            this.textViewCount = itemView.findViewById(R.id.Count);
            this.textView = itemView.findViewById(R.id.listTextView);
            this.textViewH0C1 = itemView.findViewById(R.id.H0C1);
            this.deleteButton = itemView.findViewById(R.id.delButt);
        }
        public TextView getTextViewCount(){ return textViewCount;}
        public TextView getTextView(){ return textView;}
        public TextView getTextViewH0C1(){ return textViewH0C1;}
        public AppCompatImageButton getDeleteButton(){return deleteButton;}
    }
}

class AlarmDiffCallback extends DiffUtil.ItemCallback<Alarm>{
    @Override
    public boolean areItemsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
        return oldItem== newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
        return oldItem.getTemperature()== newItem.getTemperature();
    }
}
