package com.bluepearl.dnadiagnostics;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionCenterAdapterTwo extends BaseExpandableListAdapter
{
	private Context _context;
	private ArrayList<GroupItemColectonCenter> _groups;
	private HashMap<String, List<String>> _childs;
    //this._context = _context;

	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;

	public CollectionCenterAdapterTwo(Context context, ArrayList<GroupItemColectonCenter> groups)
	{
		this._context = context;
		this._groups = groups;
		// this._childs = childs;
	}

	@Override
	public ChildItemColectionCenter getChild(int groupPosition, int childPosititon)
	{
		ArrayList<ChildItemColectionCenter> childList = _groups.get(groupPosition).getChildItems();
		return childList.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{

		ChildItemColectionCenter child = (ChildItemColectionCenter) getChild(groupPosition, childPosition);
		//ChildItem child = _groups.get(groupPosition).getChildItems().get(childPosition);
		Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "fonts/Roboto-Bold.ttf");

		Typeface custom_fontBoldRobot = Typeface.createFromAsset(_context.getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = _context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		if (convertView == null) 
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.center_list_child,null);
		}

		TextView BlnceAmont = (TextView) convertView.findViewById(R.id.id_blnceAmount);
		TextView TotalAmount = (TextView) convertView.findViewById(R.id.id_TotalAmount);
		TextView TotalRegistrtion = (TextView) convertView.findViewById(R.id.TotalRegistration_id);
		TextView AmountPaid = (TextView) convertView.findViewById(R.id.id_AmountPaid);

        // ImageButton backButn,Mapbtn,callBtn;
        ImageButton callBtn = (ImageButton)convertView.findViewById(R.id.imgCallbtnTwo);
		ImageButton Mapbtn = (ImageButton)convertView.findViewById(R.id.imgMapLoction);

        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.ll2_id);

       // <string name="Rs">\u20B9</string>
		BlnceAmont.setText(_context.getResources().getString(R.string.Rss)+child.getSelectedCenterLongitude().toString());

		//	BlnceAmont.setText(child.getSelectedCenterLongitude().toString());
		TotalAmount.setText(_context.getResources().getString(R.string.Rss)+child.getSelectedCenterName().toString());
		AmountPaid.setText(_context.getResources().getString(R.string.Rss)+child.getSelectedCenterAdress().toString());
		TotalRegistrtion.setText(child.getSelectedTest().toString());



		//txtListChild.setTypeface(custom_font);
	//	txtListChildcenterName.setTypeface(custom_font);
		//txtListChildcenterAreaDoctorName.setTypeface(custom_fontBoldRobot);
		//txtListChildcenterAdress.setTypeface(custom_font);

		final String ColectionCenternumber = (child.getSelectedTest().toString());
		final String ColectionCenterName = (child.getSelectedCenterName().toString());

		final Double ColectionCenterLongitude = Double.valueOf((child.getSelectedCenterLongitude().toString()));
		final Double ColectionCenterLatitude = Double.valueOf((child.getSelectedCenterLatitude().toString()));

        final String ColectionCenterID = (child.getSelectedCenterID().toString());

        linearLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


					/*	SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("ColectionCenterIDFromExpndbleLV", ColectionCenterID);
						editor.commit();

                        //Toast.makeText(getApplicationContext(), "child click", Toast.LENGTH_SHORT).show();
                        Toast.makeText(_context, "You Select \"" + ColectionCenterName + "\" For Appointments", Toast.LENGTH_LONG).show();*/

                    }
                }
        );


   /*     callBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

					*//*	if(ColectionCenternumber.equals("null") ||ColectionCenternumber.equals("") ) {
							Toast.makeText(_context, "Contact Number Not Available, Please Contact to Keshayurved Main Center", Toast.LENGTH_LONG).show();
						}else {
							Intent callIntent = new Intent(Intent.ACTION_DIAL);
							callIntent.setData(Uri.parse("tel:" + ColectionCenternumber));
							_context.startActivity(callIntent);
						}*//*
                    }
                }
        );*/

/*
		Mapbtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if(ColectionCenterLongitude.equals("null") ||ColectionCenterLongitude.equals(0.0) ) {
							Toast.makeText(_context, "Loction Not Available, Please Contact to Keshayurved Main Center", Toast.LENGTH_LONG).show();
						}else {
//  18.528845, 73.874412
						String uri = "http://maps.google.com/maps?daddr=" + ColectionCenterLongitude + "," + ColectionCenterLatitude + " (" + ColectionCenterName + ")";
					//	String uri = "http://maps.google.com/maps?daddr=" + 18.528846 + "," + 73.874418 + " (" + ColectionCenterName + ")";

						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
						intent.setPackage("com.google.android.apps.maps");
						try
						{
							_context.startActivity(intent);
						}
						catch(ActivityNotFoundException ex)
						{
							try
							{
								Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
								_context.startActivity(unrestrictedIntent);
							}
							catch(ActivityNotFoundException innerEx)
							{
								Toast.makeText(_context, "Please install a maps application", Toast.LENGTH_LONG).show();

							}
						}
						}
					}
				}
		);*/
		return convertView;


	}

	@Override
	public int getChildrenCount(int groupPosition) 
	{
		ArrayList<ChildItemColectionCenter> chList = _groups.get(groupPosition).getChildItems();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return _groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() 
	{
		return this._groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		GroupItemColectonCenter group = (GroupItemColectonCenter) getGroup(groupPosition);
		Typeface custom_btnfont = Typeface.createFromAsset(_context.getAssets(), "fonts/Roboto-Bold.ttf");

		if (convertView == null) 
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.center_group_list,null);
		}

		TextView lblDate = (TextView) convertView.findViewById(R.id.lblappointmentDate);
		TextView lblAddress = (TextView) convertView.findViewById(R.id.patientname);
		/*ImageButton btndelete = (ImageButton) convertView.findViewById(R.id.btncancel);
		ImageButton btnreschedule = (ImageButton) convertView.findViewById(R.id.btnreschedule);*/
		lblDate.setTypeface(custom_btnfont);
		lblAddress.setTypeface(custom_btnfont);
		lblDate.setText(group.getAppointmentDate().toString());
		lblAddress.setText(group.getAppointmentAddress().toString());


		if (isExpanded){
			//convertView.setBackgroundColor(Color.parseColor("#000000"));
			//convertView.setDividerHeight();

		}

		else{
		//	convertView.setBackgroundColor(Color.parseColor("#000000"));

	}


		if (groupPosition % 2 == 0) 
		{
			//convertView.setBackgroundColor(Color.parseColor("#E6d9d9d9"));
		//	convertView.setBackground();
		//	android:background="@drawable/card_background_selector"
			//convertView.setBackground(_context.getDrawable(this, R.drawable.card_background_selector));
			convertView.setBackgroundResource(R.drawable.card_background_selectorc);
			convertView.setPadding(015, 015, 015, 015);


		}
		else if (groupPosition % 2 == 1) 
		{
			//convertView.setBackgroundColor(Color.parseColor("#1Ad9d9d9"));

			convertView.setBackgroundResource(R.drawable.card_background_selectorc);
			convertView.setPadding(015, 015, 015, 015);

		}
		
		/*btndelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});*/

		return convertView;
	}

	@Override
	public boolean hasStableIds() 
	{
		// return false;
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}
}
