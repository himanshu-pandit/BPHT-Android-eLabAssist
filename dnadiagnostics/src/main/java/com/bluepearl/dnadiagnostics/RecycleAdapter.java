package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by csa on 3/1/2017.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.Myholder> {
    List<DataModel> dataModelArrayList;
    Context context;
    DataModel dataModel;


    public RecycleAdapter(List<DataModel> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView name,city;
        ImageView delete;

        public Myholder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name1);
            city = (TextView) itemView.findViewById(R.id.city1);
            delete = (ImageView) itemView.findViewById(R.id.delete_id);


        }
    }




    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview,null);

         return new Myholder(view);

    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        dataModel=dataModelArrayList.get(position);
        holder.name.setText(dataModel.getName());
        holder.city.setText(dataModel.getCity());
        holder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showAlert1(dataModel.getId(),view);

//
//                        //   Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
//                        final android.support.v7.app.AlertDialog alertDialog =new android.support.v7.app.AlertDialog.Builder(context).create();
//                        alertDialog.setTitle("Delete Record");
//                        alertDialog.setCancelable(false);
//                        alertDialog.setMessage("Are you sure you want to delete this?");
//                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                DatabaseHelper databaseHelper = new DatabaseHelper(context);
//                                boolean trm=databaseHelper.deleteitem(Integer.parseInt(dataModel.getId()));
//                                if (trm){
//
//                                    alertDialog.dismiss();
//                                    // finish();
//                                    // startActivity(getIntent());
//                                } }
//                        });
//                        alertDialog.show();
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }


    public void showAlert1(final String idToDelete, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());

        TextView Mytitle = new TextView(view.getRootView().getContext());
        Mytitle.setText("Alert");
        Mytitle.setTextSize(20);
        Mytitle.setPadding(5, 15, 5, 5);
        Mytitle.setGravity(Gravity.CENTER);
        builder.setCustomTitle(Mytitle);

        builder.setMessage("Are you sure you want to delete this?")
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        DatabaseHelper databaseHelper = new DatabaseHelper(view.getRootView().getContext());
                        int idTodelete =Integer.parseInt(idToDelete);
                        boolean trm=databaseHelper.deleteitem(idTodelete);

                        Intent i = new Intent(view.getRootView().getContext(),DiabetesCare.class);
                        view.getRootView().getContext().startActivity(i);

                        if (trm){


                        }
                    }
                });


        AlertDialog alert = builder.create();
        alert.show();

        TextView textView = (TextView) alert.findViewById(android.R.id.message);
        Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);

    }

}
