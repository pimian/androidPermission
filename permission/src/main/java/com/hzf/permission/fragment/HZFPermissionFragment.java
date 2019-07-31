package com.hzf.permission.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.hzf.permission.HZFPermission;
import com.hzf.permission.callback.PermissionCallBack;
import com.hzf.permission.util.PermissionUtils;
import com.hzf.permission.util.SystemPermissionPageUtils;

import java.util.ArrayList;
import java.util.List;



public class HZFPermissionFragment extends Fragment {
    //找房权限请求code
    private static int HZF_REQUEST_CODE = 1101;
    private PermissionCallBack mPermissionCallBack;
    private boolean ifGoToSetting;
    private List<String> currentRequestPList = new ArrayList<>();

    public void setOnPermissionListener(PermissionCallBack permissionCallBack, boolean ifGoToSetting) {
        this.mPermissionCallBack = permissionCallBack;
        this.ifGoToSetting = ifGoToSetting;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, HZF_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != HZF_REQUEST_CODE)
            return;
        if (grantResults.length > 0) {
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.isEmpty()) {
                mPermissionCallBack.permissionCallBack(true);
            } else {
                //勾选了对话框中”Don’t ask again”的选项, 返回false
                for (String deniedPermission : deniedPermissions) {
                    boolean flag = shouldShowRequestPermissionRationale(deniedPermission);
                    if (!flag) {
                        //不再提示
                        if (ifGoToSetting) {
                            currentRequestPList = deniedPermissions;
                            SystemPermissionPageUtils.openAppDetails(this, PermissionUtils.getInstance().getPermissionNames(deniedPermissions), HZF_REQUEST_CODE, new SystemPermissionPageUtils.OnClickNegative() {
                                @Override
                                public void click() {
                                    mPermissionCallBack.permissionCallBack(false);
                                }
                            });
                        } else {
                            mPermissionCallBack.permissionCallBack(false);
                        }
                        return;
                    }
                }
                //拒绝授权
                if (ifGoToSetting) {
                    currentRequestPList = deniedPermissions;
                    SystemPermissionPageUtils.openAppDetails(HZFPermissionFragment.this, PermissionUtils.getInstance().getPermissionNames(deniedPermissions), HZF_REQUEST_CODE, new SystemPermissionPageUtils.OnClickNegative() {
                        @Override
                        public void click() {
                            mPermissionCallBack.permissionCallBack(false);
                        }
                    });
                } else {
                    mPermissionCallBack.permissionCallBack(false);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Result=fragment=", "" + requestCode + "====" + resultCode);
        if (requestCode == HZF_REQUEST_CODE) {
            mPermissionCallBack.permissionCallBack(currentRequestPList != null && HZFPermission.checkPermissionAllGranted(this.getContext(), currentRequestPList.toArray(new String[currentRequestPList.size()])));
        } else {
            mPermissionCallBack.permissionCallBack(false);
        }

    }
}
