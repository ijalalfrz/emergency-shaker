package com.afina.emergencyshaker.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        final Target data = getListTarget().get(position);

        holder.tvNamaTarget.setText(data.getNama());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvType.setText(Html.fromHtml("<b>Shake:</b> "+data.jumlah_shake, Html.FROM_HTML_MODE_COMPACT));
            holder.tvTelepon.setText(Html.fromHtml("<b>Telpon:</b> \"+data.telepon", Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvType.setText(Html.fromHtml("<b>Shake:</b> "+data.jumlah_shake ));
            holder.tvTelepon.setText(Html.fromHtml("<b>Telpon:</b> "+data.telepon));
        }

        if(data.yes_sms != 1){
            holder.ivSms.setVisibility(View.GONE);
        }


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
        ImageView ivSms,ivTelepon;
//        Button btnAddTarget;

        CategoryViewHolder(View itemView) {
            super(itemView);
            tvNamaTarget = (TextView)itemView.findViewById(R.id.tv_nama_target);
            tvType = (TextView) itemView.findViewById(R.id.tv_tipe);
            tvTelepon = (TextView) itemView.findViewById(R.id.tv_no_telepon);
            btnEdit = (Button) itemView.findViewById(R.id.btn_edit);
            btnHapus = (Button) itemView.findViewById(R.id.btn_hapus);
            ivSms = (ImageView) itemView.findViewById(R.id.is_sms);
            ivTelepon = (ImageView) itemView.findViewById(R.id.is_telepon);
//            btnAddTarget = (Button)itemView.findViewById(R.id.btn_add_target);
        }
    }
}

