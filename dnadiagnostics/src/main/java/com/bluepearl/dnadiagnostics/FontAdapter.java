package com.bluepearl.dnadiagnostics;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FontAdapter extends ArrayAdapter<FontDetails> {

	String names[];
	String contacts[];

	private List<FontDetails> Flist;
	private Context context;

	public FontAdapter(List<FontDetails> Flist, Context ctx)
	{
		super(ctx, R.layout.drawer_list_item, Flist);
		this.Flist = Flist;
		this.context = ctx; }

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		// First let's verify the convertView is not null
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.text1);
		FontDetails p = Flist.get(position);
		tv.setText(p.getUserName()); 
		tv.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf"));
		return convertView;
	}

}
