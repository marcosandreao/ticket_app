package br.com.uarini.ticket.adapter;

import android.os.Bundle;

public class SpinnerNavItem {

    private String title;

    private int icon;

    private Bundle args;

    public SpinnerNavItem( String title, int icon, Bundle args ) {
        this.title = title;
        this.icon = icon;
        this.args = args;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon( int icon ) {
        this.icon = icon;
    }

    public Bundle getArgs() {
        return this.args;
    }

    public void setArgs( Bundle args ) {
        this.args = args;
    }

}
