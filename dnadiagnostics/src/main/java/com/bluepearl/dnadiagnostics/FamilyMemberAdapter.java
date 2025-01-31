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

public class FamilyMemberAdapter extends BaseAdapter{
	Activity context;
	String names[];
	private ArrayList<FamilyMemberListDetails> arraylist =  new ArrayList<FamilyMemberListDetails>();
	private List<FamilyMemberListDetails> userlist = null;

	public FamilyMemberAdapter(Activity context, String[] name) {
		super();
		this.context = context;
		this.names = name;
	}

	public FamilyMemberAdapter(Activity context, List<FamilyMemberListDetails> userlist) {
		this.context = context;
		this.userlist = userlist;
		this.arraylist = new ArrayList<FamilyMemberListDetails>();
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
			convertView = inflater.inflate(R.layout.activity_family_details, null);
			holder = new ViewHolder();
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.MemberName);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtViewTitle.setText(userlist.get(position).getPatientName());

		holder.txtViewTitle.setTypeface(custom_font);

		return convertView;
	}

}
