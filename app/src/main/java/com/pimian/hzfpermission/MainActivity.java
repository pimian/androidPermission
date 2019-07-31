package com.pimian.hzfpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hzf.permission.HZFPermission;
import com.hzf.permission.callback.PermissionCallBack;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    HZFPermission hzfPermissionUtil;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;

    //调用照相机返回图片文件
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void readPhoneState(View view) {
        hzfPermissionUtil = new HZFPermission.Builder(this)
                .ifGoToSetting(true)
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .setPermissionCallBack(new PermissionCallBack() {
                    @Override
                    public void permissionCallBack(boolean hasPermission) {
                        if (hasPermission) {
                            Toast.makeText(MainActivity.this, "已经获取到了读取设备信息的权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
        hzfPermissionUtil.request();
    }

    /**
     * 拍照和存储权限
     *
     * @param view
     */
    public void takephoto(View view) {
        hzfPermissionUtil = new HZFPermission.Builder(this)
                .ifGoToSetting(true)
                .setPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .setPermissionCallBack(new PermissionCallBack() {
                    @Override
                    public void permissionCallBack(boolean hasPermission) {
                        if (hasPermission) {
                            Toast.makeText(MainActivity.this, "已经获取到了所有权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
        hzfPermissionUtil.request();
    }
}
