package br.com.uarini.ticket.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.uarini.ticket.R;

public class TitleNavigationAdapter extends BaseAdapter {

    private ImageView imgIcon;

    private TextView txtTitle;

    private ArrayList<SpinnerNavItem> spinnerNavItem;

    private Context context;

    public TitleNavigationAdapter( Context context, ArrayList<SpinnerNavItem> spinnerNavItem ) {
        this.spinnerNavItem = spinnerNavItem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.spinnerNavItem.size();
    }

    @Override
    public Object getItem( int index ) {
        return this.spinnerNavItem.get( index );
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = mInflater.inflate( R.layout.list_item_title_navigation, null );
        }

        this.imgIcon = (ImageView) convertView.findViewById( R.id.iv_icon );
        this.txtTitle = (TextView) convertView.findViewById( R.id.tv_title );

        this.imgIcon.setImageResource( this.spinnerNavItem.get( position ).getIcon() );
        this.imgIcon.setVisibility( View.GONE );
        this.txtTitle.setText( this.spinnerNavItem.get( position ).getTitle() );
        return convertView;
    }

    @Override
    public View getDropDownView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = mInflater.inflate( R.layout.list_item_title_navigation, parent, false );
        }

        this.imgIcon = (ImageView) convertView.findViewById( R.id.iv_icon );
        this.txtTitle = (TextView) convertView.findViewById( R.id.tv_title );

        this.imgIcon.setImageResource( this.spinnerNavItem.get( position ).getIcon() );
        this.txtTitle.setText( this.spinnerNavItem.get( position ).getTitle() );
        return convertView;
    }

}
