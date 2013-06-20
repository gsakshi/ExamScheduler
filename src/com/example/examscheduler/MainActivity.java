package com.example.examscheduler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	Button b;
	TextView t;
	String cname = "", date = "", day = "", st = "", et = "", schedule = "",
			time = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b = (Button) findViewById(R.id.go);
		t = (TextView) findViewById(R.id.addhtml);
		b.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.go:

			Document doc = null;
			try {
				doc = Jsoup
						.connect(
								"http://172.26.142.68/examscheduler2/personal_schedule.php?rollno=12751")
						.get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			Elements tablerows = doc.select("tr");
			for (Element row : tablerows) {

				Elements tds = row.select("td");
				if (tds.get(0).text().contentEquals("COURSE"))
					continue;
				cname = tds.get(0).text();
				schedule = tds.get(1).text();
				time = tds.get(2).text();
				
				breakschedule();
				breaktime();
				ExamDatabase entry = new ExamDatabase(MainActivity.this);
				entry.open();
				try {
					entry.createEntry(cname,date,day,st,et);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				entry.close();
			}
			ExamDatabase entry = new ExamDatabase(MainActivity.this);
			entry.open();
			String data= entry.getdata();
			entry.close();
			t.setText(data);
			break;
		}

	}

	public void breakschedule() {
		date="";day="";
		int j = 0, k = 0;
		while (!((schedule.charAt(j) >= '0' && schedule.charAt(j) <= '9') || schedule.charAt(j) == 'N')) {
			j++;
		}
		if (schedule.charAt(j) == 'N')
			date = "Not Yet Set";
		else {
			for (k = j; k < j + 8; k++) {
				date = date + schedule.charAt(k);
			}
		}
		while (!(schedule.charAt(k) == '(' || schedule.charAt(k) == 'L')) {
			k++;
		}
		if (schedule.charAt(k) == '(') {
			k++;
			for (int i = 1; i <= 3; i++, k++)
				day = day + schedule.charAt(k);
		} else {
			day = "Not Yet Set";
		}

	}

	public void breaktime() {

		st="";et="";
		int p = 0;
		while (!((time.charAt(p) >= '0' && time.charAt(p) <= '9') || time.charAt(p) == 'N')) {
			p++;
		}
		if (time.charAt(p) == 'N') {
			st = "Not Yet Set";
			et = "Not Yet Set";
		} else {

			while (time.charAt(p) != '-') {
				st = st + time.charAt(p);
				p++;
			}
			while (!(time.charAt(p) >= '0' && time.charAt(p) <= '9')) {
				p++;
			}

			while (p < time.length()) {
				et = et + time.charAt(p);
				p++;
			}
		}
	}
}
