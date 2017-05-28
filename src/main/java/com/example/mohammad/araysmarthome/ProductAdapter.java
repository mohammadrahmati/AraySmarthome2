package com.example.mohammad.araysmarthome;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter {


    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    List<Product> list = new ArrayList<>();
    String base_url;
    Context context;

    public ProductAdapter(Context context, int resource){
        super(context, resource);
        this.context = context;

    }

    public void add(Product object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, @NonNull final ViewGroup parent) {
        View row;
        row = convertView;

        int a=0;
        int b=0;

        final ProductHolder productHolder;
        Product product = (Product) this.getItem(position);
        /*convertView.setTag(product);*/
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(product.getType() == 100 ) {
                row = layoutInflater.inflate(R.layout.product_list_row, parent, false);
                productHolder = new ProductHolder();
                productHolder.textView = (TextView) row.findViewById(R.id.txt1);
                productHolder.aSwitch = (Switch) row.findViewById(R.id.switch1);
                productHolder.textView.setText(product.getName());
            }
            else if(product.getType() == 200) {
                row = layoutInflater.inflate(R.layout.product_list_row2, parent, false);
                productHolder = new ProductHolder();
                productHolder.textView = (TextView) row.findViewById(R.id.txt2);
                productHolder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);
                productHolder.textView.setText(product.getName());
            }
            else if(product.getType() >200) {


                row = layoutInflater.inflate(R.layout.product_list_row, parent, false);
                productHolder = new ProductHolder();

                /*a=product.getType();
                final int finalA = a;
                productHolder.button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent zoom = new Intent(parent.getContext(), Otagh.class);
                        String sessionId1= String.valueOf(finalA);
                        zoom.putExtra("EXTRA_SESSION_ID", sessionId1);
                        parent.getContext().startActivity(zoom);
                    }
                });*/
            }
                else{
                row = layoutInflater.inflate(R.layout.product_list_row, parent, false);
                productHolder = new ProductHolder();

                b=product.getType();

//                final int finalB = b;
//                productHolder.button.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//
//                        Intent zoom = new Intent(parent.getContext(), Otagh.class);
//                        String sessionId1= String.valueOf(finalB);
//                        zoom.putExtra("EXTRA_SESSION_ID2", sessionId1);
//                        parent.getContext().startActivity(zoom);
//                    }
//                });
            }

            row.setTag(productHolder);
        }
        else {
            productHolder = (ProductHolder) row.getTag();
        }



        return row;
    }



    static class ProductHolder{
        Switch aSwitch;
        TextView textView;
        CheckBox checkBox;
         Button button;
    }
}