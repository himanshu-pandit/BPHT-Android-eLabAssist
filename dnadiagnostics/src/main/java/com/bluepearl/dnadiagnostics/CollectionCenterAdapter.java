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

public class CollectionCenterAdapter extends BaseAdapter
{
	Activity context;
	String names[];
	String contacts[];
	private ArrayList<CollectionCenterDetails> arraylist =  new ArrayList<CollectionCenterDetails>();
	private List<CollectionCenterDetails> userlist = null;

	public CollectionCenterAdapter(Activity context, String[] name) {
		super();
		this.context = context;
		this.names = name;
	}

	public CollectionCenterAdapter(Activity context, List<CollectionCenterDetails> userlist) {
		this.context = context;
		this.userlist = userlist;
		this.arraylist = new ArrayList<CollectionCenterDetails>();
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
			convertView = inflater.inflate(R.layout.activity_collection_center_listview, null);
			holder = new ViewHolder();
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.centerName);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtViewTitle.setText(userlist.get(position).getName());
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
			for (CollectionCenterDetails udet : arraylist)
			{
				if (udet.getName().toLowerCase().contains(charText)) 
				{
					userlist.add(udet);
				}
			}
		}
		notifyDataSetChanged();
	}
}
