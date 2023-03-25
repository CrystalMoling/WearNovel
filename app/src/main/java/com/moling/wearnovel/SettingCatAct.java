package com.moling.wearnovel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingCatAct extends Activity {

    EditText PlainText_Account;
    EditText PlainText_Token;
    EditText PlainText_AppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_cat);

        PlainText_Account = (EditText) findViewById(R.id.plaintext_account);
        PlainText_Token = (EditText) findViewById(R.id.plaintext_token);
        PlainText_AppVersion = (EditText) findViewById(R.id.plaintext_app_version);


        PlainText_Account.setText(MainAct.catAccount);
        PlainText_Token.setText(MainAct.catToken);
        PlainText_AppVersion.setText(MainAct.catAppVer);
    }

    public void ButtonBack_onClick(View view) {
        Intent menuIntent = new Intent(this, MenuAct.class);
        startActivity(menuIntent);
        finish();
    }

    public void SaveButton_onClick(View view) {
        MainAct.preferencesEditor.putString("Account", PlainText_Account.getText().toString());
        MainAct.preferencesEditor.putString("Token", PlainText_Token.getText().toString());
        MainAct.preferencesEditor.putString("AppVer", PlainText_AppVersion.getText().toString());
        if (MainAct.preferencesEditor.commit()){
            Toast.makeText(this, "设置成功!", Toast.LENGTH_SHORT).show();
        }
    }
}
