package net.ra_project.hmfix;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by ra on 19.09.15.
 */
public class SmsWriter {

	/**
	 *
	 * @param context
	 * @param kard_num 4714*4461
	 * @param date 14.09.2015
	 * @param time 19:30:01
	 * @param code_shop HMFIX
	 * @param is_add
	 * @param sum 666.66
	 * @param currency UAH
	 * @param balans 16.66
	 */
	public void sendPlatinumSMS(Context context, boolean is_add,  String kard_num, String date, String time, String code_shop, String sum, String currency, String balans){
		//Kartka 4714*4461 14.09.2015 19:30:01 HMFIX: -666.66 UAH pokupka tovaru. Balans 16.66 UAH. Uspishno.

		String sms_body="";
		if ( !is_add ) {
			//расход
			sms_body="Kartka "+kard_num+" "+date+" "+time+" "+code_shop+": -"+sum+" "+currency+" pokupka tovaru. Balans "+balans+" UAH. Uspishno.";
		}
		else {
			//доход
			sms_body="Kartka "+kard_num+" "+date+" "+time+". Vam zarahovano: "+sum+" "+currency;
		}


		//Toast.makeText(context, sms_body, Toast.LENGTH_LONG).show();

		//@TODO почему-то не работает в android 4.4
		ContentValues values = new ContentValues();
		values.put("address", "PLATINUM");//sender name
		values.put("body", sms_body);

		values.put("read", true);
		//values.put("date", obj.getTime());

		context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
	}

}
