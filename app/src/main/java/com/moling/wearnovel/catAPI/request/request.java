package com.moling.wearnovel.catAPI.request;

import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.MainAct;

import java.io.IOException;
import java.util.Objects;

import CatAESDecryptor.CatAESDecryptor;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class request {
    public static FormBody.Builder PostBuilder() {
        return new FormBody.Builder()
                .add("login_token", MainAct.catToken)
                .add("account",MainAct.catAccount)
                .add("app_version",MainAct.catAppVer)
                .add("device_token","ciweimao_");
    }

    public static String Post(Request request) {
        IOException exception = new IOException();
        OkHttpClient client = new OkHttpClient();
        String decrypted = null;
        int retry = 1;
        while (retry < 4) {
            Log.d("[request]", "retry:[" + retry + "]");
            try (Response response = client.newCall(request).execute()) {
                String req = response.body().string();
                Log.d("[request]", "req:[" + req + "]");
                // 解密返回数据
                decrypted = new String(CatAESDecryptor.decode(req, ""));
                Log.d("[request]", "decrypted:[" + decrypted + "]");
                break;
                //return decrypted;
            } catch (IOException e) {
                Log.d("[request]", "POST failed with error: [" + e.getMessage() + "] Retry: " + retry);
                exception = e;
                retry++;
            }
        }
        if (decrypted == null) {
            throw new RuntimeException(exception);
        }
        JSONObject return_json = JSON.parseObject(decrypted);
        Log.d("[request]", "code:[" + return_json.getInteger("code") + "]");
        if (!Objects.equals(return_json.getInteger("code"), 100000)) {
            Message msg = Message.obtain();
            msg.obj = return_json.getString("tip");
            MainAct.callToast.sendMessage(msg);
            return null;
        } else {
            return decrypted;
        }
    }
}
