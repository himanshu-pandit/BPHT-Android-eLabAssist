package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jsonparsing.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DiabetesCare extends BaseActivity {


    LineChart lineChart1;
    CheckBox  HbA1c,INSULINFASTING;
Boolean flgHbA1c,flgINSULINFASTING ;
    Button BtnPopularTest ,BtnUploadPrecription, BtnSelectTest ;


    public static  final int[] MY_COLORSLine = {
            Color.rgb(255, 140, 20),
            Color.rgb(255, 102, 125),
            Color.rgb(0, 221, 255),
            Color.rgb(120, 230, 0),
            Color.rgb(0, 0, 255),
            Color.rgb(150, 100, 55),
            Color.rgb(255, 50, 50),

    };


    DatabaseHelper database;
    RecyclerView recyclerView;
    RecycleAdapter recycler;
    List<DataModel> datamodel;
    String arrr;
    int ar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getLayoutInflater().inflate(R.layout.activity_diabetes_care,
                frameLayout);
        mDrawerList.setItemChecked(position, true);

        ColorDrawable colorDrawable = new ColorDrawable(
                Color.parseColor("#0054A6"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        BtnPopularTest = (Button) findViewById(R.id.buttonPopularTest_id);
        BtnUploadPrecription = (Button) findViewById(R.id.buttonUploadPrescription_id);
        BtnSelectTest = (Button) findViewById(R.id.buttonSelectTest_id);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DiabetesCare.this,AddMeal.class);
                        startActivity(intent);
                    }
                }
        );


        lineChart1 = (LineChart) findViewById(R.id.Linechart_id);
        HbA1c = (CheckBox) findViewById(R.id.HbA1c_id);
        INSULINFASTING = (CheckBox) findViewById(R.id.INSULINFASTING_id);

        datamodel =new ArrayList<DataModel>();
        recyclerView = (RecyclerView) findViewById(R.id.recycle);



        flgHbA1c = true;
        flgINSULINFASTING = true;

        LoadMealData(this);

        BtnPopularTest.setBackgroundResource(R.drawable.borderbtnone);
        BtnUploadPrecription.setBackgroundColor(Color.parseColor("#ffffff"));
        BtnSelectTest.setBackgroundColor(Color.parseColor("#ffffff"));

        BtnSelectTest.setTextColor(Color.parseColor("#000000"));
        BtnPopularTest.setTextColor(Color.parseColor("#000000"));
        BtnUploadPrecription.setTextColor(Color.parseColor("#000000"));

        BtnPopularTest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        BtnPopularTest.setBackgroundResource(R.drawable.borderbtnone);
                        BtnUploadPrecription.setBackgroundColor(Color.parseColor("#ffffff"));
                        BtnSelectTest.setBackgroundColor(Color.parseColor("#ffffff"));

                        BtnSelectTest.setTextColor(Color.parseColor("#000000"));
                        BtnUploadPrecription.setTextColor(Color.parseColor("#000000"));
                        BtnPopularTest.setTextColor(Color.parseColor("#000000"));

                        DrowGrapg(flgHbA1c,flgINSULINFASTING);


                    }
                }
        );


        BtnUploadPrecription.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        BtnUploadPrecription.setBackgroundResource(R.drawable.borderbtnone);
                        BtnPopularTest .setBackgroundColor(Color.parseColor("#ffffff"));
                        BtnSelectTest.setBackgroundColor(Color.parseColor("#ffffff"));



//                        BtnUploadPrecription.setBackgroundColor(Color.parseColor("#ffffff"));
//                        BtnPopularTest.setBackgroundResource(R.drawable.borderbtnone);
//                        BtnSelectTest.setBackgroundResource(R.drawable.borderbtnone);
//
                        BtnSelectTest.setTextColor(Color.parseColor("#000000"));
                        BtnUploadPrecription.setTextColor(Color.parseColor("#000000"));
                        BtnPopularTest.setTextColor(Color.parseColor("#000000"));

                        DrowGrapg(flgHbA1c,flgINSULINFASTING);



                    }
                }
        );


        BtnSelectTest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        BtnSelectTest.setBackgroundResource(R.drawable.borderbtnone);
                        BtnUploadPrecription.setBackgroundColor(Color.parseColor("#ffffff"));
                        BtnPopularTest.setBackgroundColor(Color.parseColor("#ffffff"));


//                        BtnSelectTest.setBackgroundColor(Color.parseColor("#ffffff"));
//                        BtnUploadPrecription.setBackgroundResource(R.drawable.borderbtnone);
//                        BtnPopularTest.setBackgroundResource(R.drawable.borderbtnone);

                        BtnSelectTest.setTextColor(Color.parseColor("#000000"));
                        BtnUploadPrecription.setTextColor(Color.parseColor("#000000"));
                        BtnPopularTest.setTextColor(Color.parseColor("#000000"));

                        DrowGrapg(flgHbA1c,flgINSULINFASTING);


                    }
                }
        );


        DrowGrapg(flgHbA1c,flgINSULINFASTING);

        HbA1c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    flgHbA1c = true;
                    DrowGrapg(flgHbA1c,flgINSULINFASTING);
                }else {
                    flgHbA1c = false;
                    DrowGrapg(flgHbA1c,flgINSULINFASTING);
                }

            }
        });

        INSULINFASTING.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    flgINSULINFASTING = true;
                    DrowGrapg(flgHbA1c,flgINSULINFASTING);
                }else {
                    flgINSULINFASTING = false;
                    DrowGrapg(flgHbA1c,flgINSULINFASTING);
                }

            }
        });

    }



    public void DrowGrapg( Boolean flgHbA1c, Boolean flgINSULINFASTING){

//        LineDataSet lDataSet1 = new LineDataSet(Entry[i], testStringname[i]);
//        lDataSet1.setColor(MY_COLORSLine[i]);
//        Linechart = (LineChart) findViewById(R.id.Linechart_id);
//        Linechart.setDescription("setDescription");
//        LineData chartData = new LineData(labels);
//        chartData.addDataSet(lDataSet1);
//        Linechart.setData(chartData);
//        Linechart.invalidate();
//        Linechart.fitScreen();
//        Linechart.getLegend().setWordWrapEnabled(true);
//        Linechart.animateY(2000);




        ArrayList<Entry> entrie1 = new ArrayList<>();
        entrie1.add(new Entry(4f, 0));
        entrie1.add(new Entry(8f, 1));
        entrie1.add(new Entry(6f, 2));
        entrie1.add(new Entry(10f, 3));
        entrie1.add(new Entry(18f, 4));
        entrie1.add(new Entry(9f, 5));


        ArrayList<Entry> entrie2 = new ArrayList<>();
        entrie2.add(new Entry(3f, 0));
        entrie2.add(new Entry(10f, 1));
        entrie2.add(new Entry(4f, 2));
        entrie2.add(new Entry(14f, 3));
        entrie2.add(new Entry(12f, 4));
        entrie2.add(new Entry(5f, 5));


        String[] xAxis = new String[] {"18-oct", "19-oct", "20-oct", "21-oct", "22-oct","23-oct"};

        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet> ();

        LineDataSet lDataSet1 = new LineDataSet(entrie1, "HbA1c");
        lDataSet1.setDrawFilled(true);
        lDataSet1.setColor(MY_COLORSLine[1]);
        lDataSet1.setFillAlpha(65);
        lDataSet1.setFillColor(MY_COLORSLine[1]);
        lines.add(lDataSet1);

        LineDataSet lDataSet2 = new LineDataSet(entrie2, "INSULIN FASTING");
        lDataSet2.setDrawFilled(true);
        lDataSet2.setColor(MY_COLORSLine[3]);
        lDataSet2.setFillAlpha(65);
        lDataSet2.setFillColor(MY_COLORSLine[3]);
        lines.add(lDataSet2);



        if(!flgHbA1c){
            lines.remove(lDataSet1);
        }

        if(!flgINSULINFASTING){
            lines.remove(lDataSet2);
        }


        lineChart1.invalidate();
        lineChart1.fitScreen();
        lineChart1.setData(new LineData(xAxis, lines));
        lineChart1.animateY(1000);
        //lineChart1.setData(chartData);
        lineChart1.setDescription("");
        lineChart1.getLegend().setWordWrapEnabled(true);


//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 2),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
//
//        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 3),
//                new DataPoint(1, 3),
//                new DataPoint(2, 6),
//                new DataPoint(3, 2),
//                new DataPoint(4, 5)
//        });
//        graph.addSeries(series2);

    }

    public void LoadMealData(final Context context ){

        database = new DatabaseHelper(context);
        datamodel=  database.getdata();
        recycler =new RecycleAdapter(datamodel);


        Log.i("HIteshdata",""+datamodel);
     //   RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
     //   recyclerView.setLayoutManager(reLayoutManager);
     //  recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recycler);
        recyclerView.invalidate();




        recyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener(getApplicationContext(),recyclerView,new MyRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String arrr= datamodel.get(position).getId();
             //   Toast.makeText(context, ""+arrr, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {
                arrr= datamodel.get(position).getId();
                ar = Integer.parseInt(arrr);

                final AlertDialog alertDialog =new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Delete Record");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Are you sure you want to delete this?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        boolean trm=databaseHelper.deleteitem(ar);
                        if (trm){

                            alertDialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        } }
                });
                alertDialog.show();



            }


        }));



    }

  }


