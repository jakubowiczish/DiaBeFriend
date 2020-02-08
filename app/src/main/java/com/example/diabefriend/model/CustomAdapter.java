package com.example.diabefriend.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diabefriend.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final ArrayList<String> filteredProductNames;
    private LayoutInflater inflater;
    private List<String> productNames;
    private OnProductNameClick onProductNameClick;


    public CustomAdapter(Context context, List<String> productNames) {
        inflater = LayoutInflater.from(context);
        this.productNames = productNames;
        this.filteredProductNames = new ArrayList<>(productNames);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.productNameTextView.setText(filteredProductNames.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductNameClick.click(filteredProductNames.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredProductNames.size();
    }

    public void filter(CharSequence s) {
        filteredProductNames.clear();
        for (String productName : productNames) {
            if (productName.toLowerCase().contains(s.toString().toLowerCase())) {
                filteredProductNames.add(productName);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnProductNameClick(OnProductNameClick onProductNameClick) {
        this.onProductNameClick = onProductNameClick;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout linearLayout;
        TextView productNameTextView;

        MyViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.holderTextView);
            linearLayout = itemView.findViewById(R.id.holderLinearLayout);
        }
    }
}