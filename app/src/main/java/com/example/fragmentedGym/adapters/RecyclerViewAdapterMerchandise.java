package com.example.fragmentedGym.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentedGym.R;

import java.util.ArrayList;

public class RecyclerViewAdapterMerchandise extends RecyclerView.Adapter<RecyclerViewAdapterMerchandise.ViewHolder>{


    public interface ShopDelegate {
        void onClickShop(String merch);
    }

    ArrayList<String> gymMaterials;
    ShopDelegate shopDelegate;

    public RecyclerViewAdapterMerchandise(ArrayList<String> gymMaterials, ShopDelegate shopDelegate) {
        this.gymMaterials = gymMaterials;
        this.shopDelegate = shopDelegate;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMerchandise.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout_shop,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMerchandise.ViewHolder holder, final int position){
        holder.material.setText(gymMaterials.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopDelegate.onClickShop(gymMaterials.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return gymMaterials.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView material;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            material = itemView.findViewById(R.id.itemsTextview);
        }
    }
}
