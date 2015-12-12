package net.ra_project.hmfix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ra on 15.09.15.
 */


public class SmsReceiver extends BroadcastReceiver {

	public static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d(TAG, "SMS received!");
		//Toast.makeText(context, "SMS received.", Toast.LENGTH_LONG).show();

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null)
		{
			//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i=0; i<msgs.length; i++)
			{
				/*msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";*/

				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				checkSMS(context, msgs[i].getOriginatingAddress(), msgs[i].getMessageBody().toString(), msgs[i].getTimestampMillis() );
			}
			//---display the new SMS message---
			//Toast.makeText(context, str, Toast.LENGTH_LONG).show();
		}
	}

	protected void checkSMS( Context context, String address, String body, long timestamp ){
		/*
		покупка в CodeShop
		Kartka 4102-7563 uspishna operaciya 134.86UAH 14/09 22:11 CodeShop Dostupno:652.06UAH
		0      1         2        3         4         5     6     7        8

		пополнение через терминал
		Kartka 4102-7563 uspishna operaciya -13.86UAH 14/09 22:11 CodeShop Dostupno:652.06UAH
		0      1         2        3         4         5     6     7        8

		перевод на другую карту через myAlfabank
		Kartka 4102-7563 uspishna operaciya 500UAH 14/09 22:11 MY ALFABANK COM UA Dostupno:652.06UAH
		0      1         2        3         4      5     6     7  8        9   10 11

		пополнение карты через myAlfabank
		Kartka 4102-7563 popovnennya 2,000.00UAH 15.09 11:15 Dostupno:2652.06UAH
		0      1         2           3           4     5     6

    -------------------------------------
    Сейф - покупка с CodeShop="Alfa AQ UAH"
		поплнение сейфа (расход с карты)
		Kartka 4102-5497 uspishna operaciya 500UAH 13/10 14:34 Alfa AQ UAH Dostupno:2343.15

		Снятие с сейфа (доход на карту)
		Kartka 4102-5497 uspishna operaciya 500UAH 13/10 14:34 Alfa AQ UAH Dostupno:1500
		*/

		String addres_alfabank="AlfaBank";
		//String addres_alfabank="12345678";

		//Toast.makeText(context, address, Toast.LENGTH_LONG).show();

		if ( address.equals( addres_alfabank ) ){
			//Toast.makeText(context, "ok", Toast.LENGTH_LONG).show();

			String[] array_body = body.split(" ");
			if ( array_body[0].equals("Kartka") ) {
				String kard_num=array_body[1].replace("-", "*");

				String sum_currency="";
				String date="";
				String time="";
				String code_shop="";
				String balans_currency="";

				boolean is_ok=false;
				boolean is_add=false;
				if ( array_body[2].equals("uspishna") && array_body[3].equals("operaciya") ) {
					is_ok=true;

					is_add=false;
					sum_currency=array_body[4];
					date=array_body[5];
					time=array_body[6];

					int last=array_body.length - 1;
					for (int i=7; i<=last-1; i++)
					{
						if ( i>7 ) code_shop+=" ";
						code_shop+=array_body[i];
					}

					balans_currency=array_body[last];

					//проверка на операции с сейфом
					/*if ( code_shop.equals("Alfa AQ UAH") ){
						//вызвать активити, который задаст вопрос и поставит нужную смс
						//поэтому тут return
					}*/
				}

				if ( array_body[2].equals("popovnennya") ) {
					is_ok=true;

					is_add=true;
					sum_currency=array_body[3];
					date=array_body[4];
					time=array_body[5];

					int last=array_body.length - 1;
					//при пополнении кода нет
					/*for (int i=6; i<=last-1; i++)
					{
						if ( i>6 ) code_shop+=" ";
						code_shop+=array_body[i];
					}*/
					balans_currency=array_body[last];
				}

				char c=sum_currency.charAt(0);
				if ( c=='-' ){
					sum_currency=sum_currency.substring(1);
					is_add=!is_add;
				}

				if (is_ok){
					String sum=sum_currency.replaceFirst("UAH", "").replace(",", "");
					String currency="UAH";

					GregorianCalendar calendar=new GregorianCalendar();
					calendar.setTime(new Date(timestamp));
					Integer year=calendar.get(Calendar.YEAR);
					Integer second=calendar.get(Calendar.SECOND);
					String s_second=second.toString();
					if ( s_second.length()<2 ) s_second="0"+s_second;

					date=date.replace("/", ".")+"."+year.toString();
					time=time+":"+s_second;
					String balans=balans_currency.replaceFirst("Dostupno:", "").replaceFirst("UAH", "");

					SmsWriter writer=new SmsWriter();
					writer.sendPlatinumSMS( context, is_add, kard_num, date, time, code_shop, sum, currency, balans);
				}
			}
		}
	}

}