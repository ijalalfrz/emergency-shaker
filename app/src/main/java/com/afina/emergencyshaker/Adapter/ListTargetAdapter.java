package com.afina.emergencyshaker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.UIActivity.AddTargetActivity;

import java.util.ArrayList;

public class ListTargetAdapter extends RecyclerView.Adapter<ListTargetAdapter.CategoryViewHolder>{

    private Context context;
    private ArrayList<Target> listTarget;
    private Target target;

    ArrayList<Target> getListTarget() {
        return listTarget;
    }

    public void setListTarget(ArrayList<Target> listTheater) {
        this.listTarget = listTheater;
    }

    public ListTargetAdapter(Context context) {
        this.context = context;
    }

    Target getTarget(){
        return target;
    }

    void setTarget(Target target){
        this.target = target;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_target, parent, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final Target mhs = getListTarget().get(position);

        holder.tvNamaTarget.setText(mhs.getNama());
//        holder.btnAddTarget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AddTargetActivity.class);
////                intent.putExtra("nama", mhs.getNama());
////                intent.putExtra("telepon", mhs.getTelepon());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return getListTarget().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaTarget;
//        Button btnAddTarget;

        CategoryViewHolder(View itemView) {
            super(itemView);
            tvNamaTarget = (TextView)itemView.findViewById(R.id.tv_nama_target);
//            btnAddTarget = (Button)itemView.findViewById(R.id.btn_add_target);
        }
    }
}

