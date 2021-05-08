package cn.desert.newpos.payui.setting.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.android.newpos.pay.R;

import java.util.ArrayList;
import java.util.HashMap;
import cn.desert.newpos.payui.IItem;
import cn.desert.newpos.payui.setting.ui.simple.CommunSettings;

/**
 * @author zhouqiang
 * @email wy1376359644@163.com
 */
public class SettingsFrags extends Activity implements AdapterView.OnItemClickListener{

    public static final String JUMP_KEY = "JUMP_KEY" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        GridView mGrid;
        ListView listView = null ;
        setContentView(R.layout.setting);
        mGrid = (GridView) findViewById(R.id.setting_gridview);
        listView = (ListView) findViewById(R.id.setting_listview);
        listView.setAdapter(formatAdapter2());
        listView.setOnItemClickListener(this);
        mGrid.setAdapter(formatAdapter());
        mGrid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i == 2){
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }else {
            Intent intent = new Intent();
            String text = ((TextView)view.findViewById(R.id.setting_listitem_tv)).getText().toString();
            intent.putExtra(JUMP_KEY , text);
            if (i == 0){
                intent.setClass(this, CommunSettings.class);
            }
            startActivity(intent);
        }
    }

    private static final String MAP_TV = "MAP_TV" ;
    private static final String MAP_IV = "MAP_IV" ;
    private ArrayList<HashMap<String,Object>> list ;
    private ListAdapter formatAdapter(){
        list = new ArrayList<>();
        String[] names = getResources().getStringArray(R.array.setting_items);
        for (int i = 0 ; i < names.length ; i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put(MAP_TV , names[i]);
            map.put(MAP_IV , IItem.runnerIMGS(i));
            list.add(map);
        }
        return new SimpleAdapter(this , list , R.layout.setting_item_view,
                new String[]{MAP_IV , MAP_TV},new int[]{R.id.setting_item_iv,R.id.setting_item_tv});
    }

    private ListAdapter formatAdapter2(){
        list = new ArrayList<>();
        String[] names = getResources().getStringArray(R.array.setting_items2);
        for (int i = 0 ; i < names.length ; i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put(MAP_TV , names[i]);
            map.put(MAP_IV , IItem.runnerIMGS2(i));
            list.add(map);
        }
        return new SimpleAdapter(this , list , R.layout.setting_list_item,
                new String[]{MAP_IV , MAP_TV},new int[]{R.id.setting_listitem_iv,R.id.setting_listitem_tv});
    }
}
