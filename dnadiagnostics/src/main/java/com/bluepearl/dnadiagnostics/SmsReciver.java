package com.bluepearl.dnadiagnostics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReciver extends BroadcastReceiver 
{
	SignupActivity Sms;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		final Bundle bundle = intent.getExtras();
		try
		{
			if (bundle != null)
			{
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				for (int i = 0; i < pdusObj.length; i++)
				{
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();
					String[] senderNum = phoneNumber.split("-");
					String message = currentMessage.getDisplayMessageBody();
					try
					{
						if (senderNum[1].equals("eLABDx"))
						{
							Sms = new SignupActivity();
							Sms.recivedSms(message);
							Intent in = new Intent("SmsMessage.intent.MAIN");
							context.sendBroadcast(in);
						}
					} 
					catch (Exception e) 
					{ }
				}
			}
		} 
		catch (Exception e) 
		{ }
	}
}
