package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoctorNameAdapter extends BaseAdapter
{
	Activity context;
	String names[];
	String contacts[];
	private ArrayList<DoctorNameDetails> arraylist =  new ArrayList<DoctorNameDetails>();
	private List<DoctorNameDetails> userlist = null;

	public DoctorNameAdapter(Activity context, String[] name) {
		super();
		this.context = context;
		this.names = name;
	}

	public DoctorNameAdapter(Activity context, List<DoctorNameDetails> userlist) {
		this.context = context;
		this.userlist = userlist;
		this.arraylist = new ArrayList<DoctorNameDetails>();
		this.arraylist.addAll(userlist);

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return userlist.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userlist.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class ViewHolder {
		TextView txtViewTitle;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater =  context.getLayoutInflater();

		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.doctor_details, null);
			holder = new ViewHolder();
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.doctorName);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtViewTitle.setText(userlist.get(position).getUserName());

		holder.txtViewTitle.setTypeface(custom_font);

		return convertView;
	}

	public void filter(String charText)
	{
		charText = charText.toLowerCase(Locale.getDefault());
		if(charText.equalsIgnoreCase("All"))
			charText = "";
		userlist.clear();
		if (charText.length() == 0) {
			userlist.addAll(arraylist);
		}
		else
		{
			for (DoctorNameDetails udet : arraylist)
			{
				if (udet.getUserName().toLowerCase().contains(charText)) 
				{
					userlist.add(udet);
				}
			}
		}
		notifyDataSetChanged();
	}
}