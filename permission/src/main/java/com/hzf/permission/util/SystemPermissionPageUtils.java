package com.hzf.permission.util;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

public class SystemPermissionPageUtils {
    public interface OnClickNegative {
        void click();
    }

    /**
     * 打开APP详情页面，引导用户去设置权限
     *
     * @param fragment        页面对象
     * @param permissionNames 权限名称（如是多个，使用\n分割）
     */

    public static void openAppDetails(final Fragment fragment, String permissionNames, final int requestCode, final OnClickNegative onClickNegative) {
        StringBuilder sb = new StringBuilder();
        sb.append(PermissionUtils.PermissionTip1);
        sb.append(permissionNames);
        sb.append(PermissionUtils.PermissionTip2);
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setMessage(sb.toString());
        builder.setPositiveButton(PermissionUtils.PermissionDialogPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + fragment.getContext().getPackageName()));
                fragment.startActivityForResult(intent, requestCode);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(PermissionUtils.PermissionDialogNegativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onClickNegative != null)
                    onClickNegative.click();
                dialog.cancel();
            }
        });
        builder.show();
    }
}
