package hackntu2015.edu.yzu.drivertaipei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by andy on 8/19/15.
 */
public class MenuAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private int[] icon_res = {R.mipmap.settings, R.mipmap.help_circle, R.mipmap.emoticon_cool};
    private String[] menu_title = {"設定","幫助","關於我們"};

    public MenuAdapter(Context c) {
        myInflater = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myInflater.inflate(R.layout.listview, null);
        ImageView icon = (ImageView)convertView.findViewById(R.id.menu_icon);
        TextView title = (TextView)convertView.findViewById(R.id.menu_text);
        icon.setImageResource(icon_res[position]);
        title.setText(menu_title[position]);


        return convertView;
    }
}
