package me.veryyoung.wechat.luckymoney;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SettingsFragment mSettingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }

//        IntentFilter filter = new IntentFilter();
//        filter.addAction("me.veryyoung.wechat.luckymoney.wechat_msg_received");
//        registerReceiver(msgReceiver, filter);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    private BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("me.veryyoung.wechat.luckymoney.wechat_msg_received")) {
                int msgType = intent.getIntExtra("msgType", 0);
                String content = intent.getStringExtra("content");

//                showNormalDialog(msgType, content);
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
            }
        }
    };


    private void showNormalDialog(int msgType, String content) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("消息类型："+msgType);
        normalDialog.setMessage("content");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }


    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.pref_setting);

            Preference reset = findPreference("author");
            reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse("https://github.com/veryyoung"));
                    startActivity(intent);
                    return true;
                }
            });

            Preference donateAlipay = findPreference("donate_alipay");
            donateAlipay.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    String payUrl = "https://qr.alipay.com/apbvye346u4wqkcr9b";
                    intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + payUrl));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        intent.setData(Uri.parse(payUrl));
                        startActivity(intent);
                    }
                    return true;
                }
            });

            Preference donateWechat = findPreference("donate_wechat");
            donateWechat.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    Intent intent = new Intent();
                    intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    intent.putExtra("donate", true);
                    startActivity(intent);
                    return true;
                }
            });

        }
    }
}
