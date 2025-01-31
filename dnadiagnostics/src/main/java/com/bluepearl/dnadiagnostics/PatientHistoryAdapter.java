package com.bluepearl.dnadiagnostics;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientHistoryAdapter extends BaseExpandableListAdapter 
{
	private Context _context;
	private ArrayList<GroupItem> _groups;
	private HashMap<String, List<String>> _childs;

	public PatientHistoryAdapter(Context context, ArrayList<GroupItem> groups)
	{
		this._context = context;
		this._groups = groups;
		// this._childs = childs;
	}

	@Override
	public ChildItem getChild(int groupPosition, int childPosititon)
	{
		ArrayList<ChildItem> childList = _groups.get(groupPosition).getChildItems();
		return childList.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent)
	{

		ChildItem child = (ChildItem) getChild(groupPosition, childPosition);
		//ChildItem child = _groups.get(groupPosition).getChildItems().get(childPosition);
		Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "fonts/Roboto-Regular.ttf");

		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.history_list_child,null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
		txtListChild.setText(child.getSelectedTest().toString());
		txtListChild.setTypeface(custom_font);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		ArrayList<ChildItem> chList = _groups.get(groupPosition).getChildItems();
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
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
	{
		GroupItem group = (GroupItem) getGroup(groupPosition);
		Typeface custom_btnfont = Typeface.createFromAsset(_context.getAssets(), "fonts/Roboto-Bold.ttf");

		if (convertView == null) 
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.history_group_list,null);
		}

		TextView lblDate = (TextView) convertView.findViewById(R.id.lblappointmentDate);
		TextView lblAddress = (TextView) convertView.findViewById(R.id.patientname);
		/*ImageButton btndelete = (ImageButton) convertView.findViewById(R.id.btncancel);
		ImageButton btnreschedule = (ImageButton) convertView.findViewById(R.id.btnreschedule);*/
		lblDate.setTypeface(custom_btnfont);
		lblAddress.setTypeface(custom_btnfont);
		lblDate.setText(group.getAppointmentDate().toString());
		lblAddress.setText(group.getAppointmentAddress().toString());

		if (groupPosition % 2 == 0) 
		{
			convertView.setBackgroundColor(Color.parseColor("#ffffff"));  
		}
		else if (groupPosition % 2 == 1) 
		{
			convertView.setBackgroundColor(Color.parseColor("#e9e9e9")); 
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
