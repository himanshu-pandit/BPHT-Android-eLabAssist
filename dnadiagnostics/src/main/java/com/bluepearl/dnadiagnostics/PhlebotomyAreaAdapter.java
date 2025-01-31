package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PhlebotomyAreaAdapter extends BaseAdapter
{
	Activity context;
	String pnames[];
	String labcode[];
	String date[];



	int layoutResourceId;
	private ArrayList<PhlebotomyAreaListDetails> arraylist =  new ArrayList<PhlebotomyAreaListDetails>();
	private List<PhlebotomyAreaListDetails> docList = null;

	public PhlebotomyAreaAdapter(Activity context, List<PhlebotomyAreaListDetails> doctorList)
	{
		this.context = context;
		this.docList = doctorList;
		this.arraylist = new ArrayList<PhlebotomyAreaListDetails>();
		this.arraylist.addAll(doctorList);
	}

	public int getCount() 
	{
		// TODO Auto-generated method stub
		return docList.size();
	}

	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		DoctorHolder holder = null;

		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

		if(row == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.phlebotomy_listview_item_row_online, null);

			holder = new DoctorHolder();
			holder.imgIcon = (ImageView)row.findViewById(R.id.list_image);
			holder.txtPname = (TextView)row.findViewById(R.id.PatientName_id);
			holder.lblLabcode = (TextView)row.findViewById(R.id.txtlabcode);
			holder.txtlabcode = (TextView)row.findViewById(R.id.labcode);
			holder.txtdate = (TextView)row.findViewById(R.id.Date);

			holder.txtCoolectionCenterName = (TextView)row.findViewById(R.id.CollectionCenterName_id);
			holder.txtCurrentState = (TextView)row.findViewById(R.id.CurntState_id);

			row.setTag(holder);
		}
		else
		{
			holder = (DoctorHolder)row.getTag();
		}
		holder.txtPname.setText(docList.get(position).getPatientName());
		holder.txtlabcode.setText(docList.get(position).getLabCode());
		holder.txtdate.setText(docList.get(position).getDate());
		holder.imgIcon.setImageResource(R.drawable.report);


		holder.txtCoolectionCenterName.setText(docList.get(position).getCollectionCenterName());

		if( (    (docList.get(position).getState().equalsIgnoreCase("1")) ||
				(docList.get(position).getState().equalsIgnoreCase("2")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Registration");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);

		}

		else if( ( (docList.get(position).getState().equalsIgnoreCase("3")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Sample Collection");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		else if( ( (docList.get(position).getState().equalsIgnoreCase("4")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Sample Deliver");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if ((docList.get(position).getState().equalsIgnoreCase("5")) ||
				(docList.get(position).getState().equalsIgnoreCase("6")))

		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Accession");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( ( (docList.get(position).getState().equalsIgnoreCase("7")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Result");
			holder.txtCurrentState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( ( (docList.get(position).getState().equalsIgnoreCase("8")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Technician Approve");
			holder.txtCurrentState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( ( (docList.get(position).getState().equalsIgnoreCase("9")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Pathology Approve");
			holder.txtCurrentState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( (    (docList.get(position).getState().equalsIgnoreCase("10")) ||
				(docList.get(position).getState().equalsIgnoreCase("11")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Release");
			holder.txtCurrentState.setTextColor(Color.parseColor("#6cb13c"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		else
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("No Status");
			holder.txtCurrentState.setTextColor(Color.parseColor("#6cb13c"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}


		holder.lblLabcode.setTypeface(custom_btnfont);
		holder.txtlabcode.setTypeface(custom_font);
		//holder.txtPname.setTypeface(custom_font);
		holder.txtdate.setTypeface(custom_font);

		if (position % 2 == 0) 
		{
			row.setBackgroundColor(Color.parseColor("#ffffff"));
		}else if (position % 2 == 1) 
		{
			row.setBackgroundColor(Color.parseColor("#dde3ee"));
		}
		return row;
	}

	static class DoctorHolder
	{
		ImageView imgIcon;
		TextView txtPname,txtlabcode,txtdate,lblLabcode,txtCoolectionCenterName,txtCurrentState;
	}
}
