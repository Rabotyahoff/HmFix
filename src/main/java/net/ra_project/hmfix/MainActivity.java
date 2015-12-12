package net.ra_project.hmfix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/*
отправка sms
telnet localhost 5554

текстовые адресаты не работают
sms send AlfaBank Kartka 4102-7563 uspishna operaciya 134.86UAH 14/09 22:11 CodeShop Dostupno:652.06UAH

покупка в CodeShop
sms send 12345678 Kartka 4102-7563 uspishna operaciya 134.86UAH 14/09 22:11 CodeShop Dostupno:652.06UAH

пополнение через терминал
sms send 12345678 Kartka 4102-7563 uspishna operaciya -13.86UAH 14/09 22:11 CodeShop Dostupno:652.06UAH

перевод на другую карту через myAlfabank
sms send 12345678 Kartka 4102-7563 uspishna operaciya 500UAH 14/09 22:11 MY ALFABANK COM UA Dostupno:652.06UAH

пополнение карты через myAlfabank
sms send 12345678 Kartka 4102-7563 popovnennya 2,000.00UAH 15.09 11:15 Dostupno:2652.06UAH

поплнение сейфа
sms send 12345678 Kartka 4102-5497 uspishna operaciya 500UAH 13/10 14:34 Alfa AQ UAH Dostupno:2343.15

Снятие с сейфа
sms send 12345678 Kartka 4102-5497 uspishna operaciya 500UAH 13/10 14:34 Alfa AQ UAH Dostupno:1500

 */

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*Регистрируем в манифесте, т.к. в коде он будет работать только если приложение запущено*/
		/*String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
		SmsReceiver smsReceiver = new SmsReceiver();
		registerReceiver(smsReceiver, new IntentFilter(SMS_RECEIVED));*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void clickSMS_spend( View v) {
		SmsWriter writer=new SmsWriter();
		writer.sendPlatinumSMS( getBaseContext(), false, "4102*7563", "14.09.2015", "12:01:01", "emptyShop", "666.66", "UAH", "1666.66");
	}
	public void clickSMS_addd( View v ){
		SmsWriter writer=new SmsWriter();
		writer.sendPlatinumSMS(getBaseContext(), true, "4102*7563", "14.09.2015", "12:01:01", "emptyShop", "666.66", "UAH", "1666.66");
	}


}
