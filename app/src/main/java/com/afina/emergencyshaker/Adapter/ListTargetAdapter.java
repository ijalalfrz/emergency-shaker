package com.afina.emergencyshaker.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.UIActivity.AddTargetActivity;
import com.afina.emergencyshaker.UIActivity.EditTargetActivity;
import com.afina.emergencyshaker.UIActivity.LayoutActivity;

import java.util.ArrayList;

public class ListTargetAdapter extends RecyclerView.Adapter<ListTargetAdapter.CategoryViewHolder>{

    private Context context;
    private ArrayList<Target> listTarget;
    private Target target;
    private DbEmergencyShaker db;

    ArrayList<Target> getListTarget() {
        return listTarget;
    }

    public void setListTarget(ArrayList<Target> listTheater) {
        this.listTarget = listTheater;
    }

    public ListTargetAdapter(Context context, DbEmergencyShaker db) {
        this.context = context;
        this.db = db;
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
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        final Target data = getListTarget().get(position);

        holder.tvNamaTarget.setText(data.getNama());
        holder.tvType.setText(data.jumlah_shake + " Shake");
        holder.tvTelepon.setText(data.telepon);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                Intent moveIntent = new Intent(context, EditTargetActivity.class);
                moveIntent.putExtra("EXTRA_ID", data.id);
                context.startActivity(moveIntent);
            }
        });
        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Perhatian");
                alertDialog.setMessage("Apakah anda ingin menghapus data "+data.nama+" ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YA",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteTarget(data.id);
                                listTarget.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "TIDAK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#627894"));
                        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#627894"));

                    }
                });

                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return getListTarget().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaTarget,tvType,tvTelepon;
        Button btnEdit,btnHapus;
//        Button btnAddTarget;

        CategoryViewHolder(View itemView) {
            super(itemView);
            tvNamaTarget = (TextView)itemView.findViewById(R.id.tv_nama_target);
            tvType = (TextView) itemView.findViewById(R.id.tv_tipe);
            tvTelepon = (TextView) itemView.findViewById(R.id.tv_no_telepon);
            btnEdit = (Button) itemView.findViewById(R.id.btn_edit);
            btnHapus = (Button) itemView.findViewById(R.id.btn_hapus);
//            btnAddTarget = (Button)itemView.findViewById(R.id.btn_add_target);
        }
    }
}

