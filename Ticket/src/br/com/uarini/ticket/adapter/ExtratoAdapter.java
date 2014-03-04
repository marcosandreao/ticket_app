package br.com.uarini.ticket.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.uarini.ticket.R;
import br.com.uarini.ticket.json.Release;

public class ExtratoAdapter extends ArrayAdapter<Release> {

	private final LayoutInflater inflater;
	
	public ExtratoAdapter(Context context, List<Release> objects) {
		super(context, 0, 0, objects);
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if ( convertView == null ) {
			convertView = this.inflater.inflate(R.layout.item_extrato, null);
			holder = new ViewHolder();
			holder.tvDataExtrato = (TextView) convertView.findViewById(R.id.tv_extrato_data);
			holder.tvSaldoExtrato = (TextView) convertView.findViewById(R.id.tv_extrato_saldo);
			holder.tvInfoExtrato = (TextView) convertView.findViewById(R.id.tv_extrato_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Release release = this.getItem(position);
		holder.tvDataExtrato.setText(release.getDate());
		holder.tvSaldoExtrato.setText(release.getValue());
		holder.tvInfoExtrato.setText(release.getDescription());
		
		return convertView;
	}
	
	private static final class ViewHolder {
		TextView tvDataExtrato;
		TextView tvSaldoExtrato;
		TextView tvInfoExtrato;
	}

}
