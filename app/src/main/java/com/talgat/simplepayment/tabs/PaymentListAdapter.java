package com.talgat.simplepayment.tabs;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.database.Payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentListAdapter extends ArrayAdapter<Payment> {

	private List<Payment> payments;
	private final Context context;
	private Map<Long, String> categoryName;

	public PaymentListAdapter(Context context, int resource,
			List<Payment> objects) {
		super(context, resource, objects);
		
		this.payments = objects;
		this.context = context;
		this.categoryName = MainActivity.categoryDataSource.getCategoryMap();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View newRow = convertView;
		if (newRow == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newRow = vi.inflate(R.layout.payment_list_item, parent, false);
        }
		
		Payment payment = payments.get(position);
		
		TextView payName = (TextView) newRow.findViewById(R.id.pay_name);
		payName.setText(categoryName.get(payment.getCategory()));
		
		TextView payComment = (TextView) newRow.findViewById(R.id.pay_comment);
		payComment.setText(payment.getComment());
		
		TextView paySum = (TextView) newRow.findViewById(R.id.pay_sum);
		paySum.setText(String.valueOf(MainActivity.decimalFormat.format(payment.getSum())));
		
		TextView payDate = (TextView) newRow.findViewById(R.id.pay_date);
		Date date = new Date(payment.getPdate());
		payDate.setText(MainActivity.simpleDateFormat.format(date));
		
	    ImageView imageView = (ImageView) newRow.findViewById(R.id.list_image);
	    if (payment.getType() == 1) {
	    	imageView.setImageResource(R.drawable.ic_content_income);
	    } else {
	    	imageView.setImageResource(R.drawable.ic_content_expense);
	    }
	    
		return newRow;
	}

	@Override
	public void addAll(Collection<? extends Payment> collection) {
		for (Payment payment : collection) {
			add(payment);
		}
	}

	public void add(Payment payment, int position) {
		payments.add(position, payment);
	}
}
