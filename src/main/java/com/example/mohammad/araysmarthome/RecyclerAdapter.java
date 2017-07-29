package com.example.mohammad.araysmarthome;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;




public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final int FOLAN = 0, BAHMAN = 1;
    Runnable button1CallBack , button2CallBack;


    //todo in cunstructor buttons call back can be passed!
    public RecyclerAdapter(Runnable button1CallBack,Runnable button2CallBack){
        this.button1CallBack =button1CallBack;
        this.button2CallBack = button2CallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FOLAN:
                view = inflater.inflate(R.layout.product_list_row4, parent, false);
                viewHolder = new ViewHolderFolan(view);
                break;
            case BAHMAN:
                view = inflater.inflate(R.layout.product_list_row2, parent, false);
                viewHolder = new ViewHolderBahman(view);
                break;
        }
        return viewHolder;
    }

    //todo getItemCount and getItemViewType works together
    @Override
    public int getItemCount() {
        //todo this means number of rows
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        //todo should implements all positions
        switch (position){
            case 0:
                return FOLAN;
            case 1:
                return BAHMAN;
        }
        return -1;//error code
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case FOLAN:
                ViewHolderFolan viewHolderFolan = (ViewHolderFolan) holder;
                configureViewHolderFolan(viewHolderFolan, position);
                break;
            case BAHMAN:
                ViewHolderBahman viewHolderBahman = (ViewHolderBahman) holder;
                configureViewHolderBahman(viewHolderBahman, position);
                break;
        }
    }

    private void configureViewHolderBahman(ViewHolderBahman viewHolderBahman,
                                           int position) {
        //todo filling rows data should be here for example
        if (position==0){
            viewHolderBahman.tv.setText("the row is "+position);
        }
        //...
    }

    private void configureViewHolderFolan(ViewHolderFolan viewHolderFolan,
                                          int position) {
        //todo filling rows data should be here
    }


    class ViewHolderFolan extends RecyclerView.ViewHolder{

        TextView tv;
        CheckBox cb1;
        CheckBox cb2;

        public ViewHolderFolan(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.text2);
            cb1= (CheckBox) itemView.findViewById(R.id.checkBox1);
            cb2= (CheckBox) itemView.findViewById(R.id.checkBox2);
            //todo set all click listeners

        }
    }

    class ViewHolderBahman extends RecyclerView.ViewHolder {
        TextView tv;
        Button b1;
        Button b2;
        public ViewHolderBahman(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text2);
            b1 = (Button) itemView.findViewById(R.id.button1);
            b2 = (Button) itemView.findViewById(R.id.button2);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Button 1","clicked and position is "+getAdapterPosition());
                    //todo here should send data
                    button1CallBack.run();
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Button 2","clicked and position is "+getAdapterPosition());
                    button2CallBack.run();
                }
            });
        }
    }
}
