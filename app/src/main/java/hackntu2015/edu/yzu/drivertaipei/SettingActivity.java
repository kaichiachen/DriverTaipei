package hackntu2015.edu.yzu.drivertaipei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import hackntu2015.edu.yzu.drivertaipei.utils.AutoResizeTextView;
import hackntu2015.edu.yzu.drivertaipei.utils.Utils;

/**
 * Created by andy on 8/22/15.
 */
public class SettingActivity extends Activity {

    private ListView settingList;
    private ImageButton menu;
    private Context ctx;
    private String claim = "本APP是以即時更新資料的方式運作，而一切資料均屬第三方提供，並非本網站提供，用戶應自行判斷內容之真實性。\n" +
            "對所有資料的真實性、完整性及可靠性等，本APP不負任何法律責任。\n" +
            "由於本APP受到「即時更新」運作方式所規限，故不能完全監察所有資料可靠性，若使用者發現有留言出現問題，請聯絡我們。\n" +
            "本APP保留一切法律權利。";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ctx = this;

        settingList = (ListView)findViewById(R.id.setting_list);
        menu = (ImageButton)findViewById(R.id.btn_menu);

        settingList.setAdapter(new SettingAdapter(this));
        settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent gpsOptionsIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=hackntu2015.edu.yzu.drivertaipei"));
                        startActivity(intent);
                        break;
                    case 2:
                        AlertDialog.Builder payDialog = new AlertDialog.Builder(ctx);

                        LayoutInflater inflater = getLayoutInflater();
                        View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);
                        payDialog.setView(dialoglayout);
                        final AlertDialog dialog = payDialog.show();
                        dialog.getWindow().setLayout(1000,1200);
                        AutoResizeTextView content = (AutoResizeTextView)dialoglayout.findViewById(R.id.dialog_content);
                        content.setText(claim);
                        Button closebtn = (Button)dialoglayout.findViewById(R.id.dialog_close);
                        closebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });

        final SimpleSideDrawer mMenu = new SimpleSideDrawer(this);
        mMenu.setLeftBehindContentView(R.layout.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.toggleLeftDrawer();
            }
        });
        ImageView menu_top = (ImageView)mMenu.findViewById(R.id.menu_top);
        if(Utils.isNight()) {
            menu_top.setBackgroundResource(R.mipmap.menu_topimage_night);
        } else{
            menu_top.setBackgroundResource(R.mipmap.menu_topimage_day);
        }
        ListView listView = (ListView) mMenu.findViewById(R.id.listView);
        listView.setAdapter(new MenuAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case 1:
                        mMenu.toggleLeftDrawer();
                        break;
                    case 2:
                        startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private class SettingAdapter extends BaseAdapter {

        private LayoutInflater myInflater;
        private String[] setting_title = {"改善所在位置準確度","傳送意見","條款及隱私權"};

        public SettingAdapter(Context c) {
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
            convertView = myInflater.inflate(R.layout.setting_list, null);
            TextView title = (TextView)convertView.findViewById(R.id.setting_title);
            title.setText(setting_title[position]);

            return convertView;
        }
    }
}
