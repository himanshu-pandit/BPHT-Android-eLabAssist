package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdaptor extends BaseAdapter
{
	Activity context;
	String pnames[];
	String labcode[];
	String date[];

	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;

	int layoutResourceId;
	private ArrayList<MessageListDetails> arraylist =  new ArrayList<>();
	private ArrayList<MessageListDetails> flebList = null;

	static String status = "";
	String testRegistrationID = "";
	static String lab_user_id = null;
	static String pid = null;
	static String selected_labidfrompref = null;
	static String RegistrationId = "";
	static String CurrentState = "";

	static String CurrentRelesePatientState= "";
	static String CurrentReleseDoctorState = "";



	public MessageAdaptor(Activity context, ArrayList<MessageListDetails> flebotomiaList)
	{
		this.context = context;
		this.flebList = flebotomiaList;
		this.arraylist = new ArrayList<>();
		this.arraylist.addAll(flebotomiaList);
	}

	public int getCount()
	{
		// TODO Auto-generated method stub
		return flebList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		FlebotomiaHolder holder = null;

		sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		pid = sharedpreferences.getString("patientId", "");

		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

		if(row == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.message_listview_item_row, null);

			holder = new FlebotomiaHolder();

			holder.imgIcon = (ImageView)row.findViewById(R.id.list_image);
			holder.fullmsg = (TextView)row.findViewById(R.id.fullmsd_id);
			holder.msgState = (TextView)row.findViewById(R.id.msdstate_id);

			holder.RplyMesg = (TextView)row.findViewById(R.id.RplyMesg_id);



			//	holder.txtamt = (TextView)row.findViewById(R.id.Amount);
			//	holder.txtlabcode = (TextView)row.findViewById(R.id.labcode);
			//holder.txtdate = (TextView)row.findViewById(R.id.Date);
			//holder.txtOverwrite = (TextView)row.findViewById(R.id.textOverWrite_id);

			row.setTag(holder);
		}
		else
		{
			holder = (FlebotomiaHolder)row.getTag();
		}
		holder.fullmsg.setText(flebList.get(position).getfullmessage());
		holder.RplyMesg.setText(flebList.get(position).getReplyMessage());
		//holder.txtlabcode.setText(flebList.get(position).getLabCode());
		//holder.txtdate.setText(flebList.get(position).getDate());
		//holder.imgIcon.setImageResource(R.drawable.report);
		//holder.txtamt.setText(flebList.get(position).getBal_Amt());

		if(flebList.get(position).getMessageState().equalsIgnoreCase("0"))
		{
			holder.msgState.setText("New");
			holder.msgState.setTextColor(Color.parseColor("#4d94ff"));
			holder.msgState.setTypeface(null, Typeface.BOLD);
		}
		else if(flebList.get(position).getMessageState().equalsIgnoreCase("1"))
		{
			holder.msgState.setText("Replied");
			holder.msgState.setTextColor(Color.parseColor("#6cb13c"));
			holder.msgState.setTypeface(null, Typeface.BOLD);
		}
		else if(flebList.get(position).getMessageState().equalsIgnoreCase("2"))
		{
			holder.msgState.setText("Closed");
			holder.msgState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.msgState.setTypeface(null, Typeface.BOLD);
		}
		else
		{
			holder.msgState.setText("no Status");
		}
/////////////////////////////////////////////////////////////////////////

//		System.out.println("aaaa "+(Double.parseDouble(flebList.get(position).getBal_Amt())) + "  "
		//			+flebList.get(position).getState() +" "
		//			+ flebList.get(position).getDoctorRelease() + " "
		//			+flebList.get(position).getPatientRelease()) ;                  ;
/*
		if( (    (flebList.get(position).getState().equalsIgnoreCase("10")) || (flebList.get(position).getState().equalsIgnoreCase("11")))&&
				flebList.get(position).getDoctorRelease().equalsIgnoreCase("false") &&
				flebList.get(position).getPatientRelease().equalsIgnoreCase("false") &&
				(Double.parseDouble(flebList.get(position).getBal_Amt()) > 0 ))
		{
			holder.txtOverwrite.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.txtOverwrite.setVisibility(View.GONE);
		}
*/
		//	holder.txtamt.setTypeface(custom_font);
		holder.fullmsg.setTypeface(custom_font);
		holder.msgState.setTypeface(custom_font);
		holder.RplyMesg.setTypeface(custom_font);

		//holder.txtdate.setTypeface(custom_font);
		//holder.txtOverwrite.setTypeface(custom_font);


		if (position % 2 == 0)
		{
			row.setBackgroundColor(Color.parseColor("#1Affffff"));
		}else if (position % 2 == 1)
		{
			row.setBackgroundColor(Color.parseColor("#e9e9e9"));
		}

		//holder.txtOverwrite.setOnClickListener(new View.OnClickListener() {

		//	@Override
		//	public void onClick(View v) {
		// TODO Auto-generated method stub
		//	RegistrationId = (flebList.get(position).getTestRegId());
		//CurrentState = (flebList.get(position).getState());

		//	new StatusCheck().execute(new String[] { url });
		//alertBox();

		//	Intent iintent = new Intent(MessageAdaptor.this, MessageShow.class);
		//startActivity(iintent);
		//	finish();

		//	}
		//	});
		return row;
	}

	static class FlebotomiaHolder
	{
		ImageView imgIcon;
		TextView fullmsg,txtlabcode,RplyMesg,txtdate,lblLabcode,txtamt,txtOverwrite, msgState;
	}

}
