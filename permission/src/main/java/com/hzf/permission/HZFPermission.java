package com.hzf.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hzf.permission.callback.PermissionCallBack;
import com.hzf.permission.fragment.HZFPermissionFragment;

/**
 * @author pimian
 * 版本：1.0
 * 创建日期：2019/3/14 0014 下午 18:11
 * 描述：权限调用工具类
 */
public class HZFPermission {
    final String TAG = HZFPermission.class.getSimpleName();
    private HZFPermissionFragment hzfPermissionFragment;
    private List<String> permission; //需要申请的权限列表
    private boolean ifGoToSetting = false;//是否需要强制申请权限去设置页面
    private PermissionCallBack permissionCallBack;//权限回调==》在页面不需要自主处理时回调权限获取状态


    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private HZFPermission(FragmentActivity activity) {
        hzfPermissionFragment = getSingleton(activity.getSupportFragmentManager());
    }

    private HZFPermission(Fragment fragment) {
        hzfPermissionFragment = getSingleton(fragment.getChildFragmentManager());
    }


    private HZFPermissionFragment getSingleton(@Nullable final FragmentManager fragmentManager) {
        HZFPermissionFragment hzfPermissionFragment = (HZFPermissionFragment) fragmentManager.findFragmentByTag(TAG);
        if (hzfPermissionFragment == null) {
            hzfPermissionFragment = new HZFPermissionFragment();
            fragmentManager.beginTransaction().add(hzfPermissionFragment, TAG).commitNow();
        }
        return hzfPermissionFragment;
    }

    public void request() {
        if (permission == null || permission.size() == 0) {
            Toast.makeText(hzfPermissionFragment.getContext(), "permission必须设置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (permissionCallBack == null) {
            Toast.makeText(hzfPermissionFragment.getContext(), "permissionCallBack必须设置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> pList = checkPermissionDenied(hzfPermissionFragment.getContext(), permission.toArray(new String[permission.size()]));
            if (pList.size() != 0) {
                String deniedPermissions[] = pList.toArray(new String[pList.size()]);
                hzfPermissionFragment.setOnPermissionListener(permissionCallBack, ifGoToSetting);
                hzfPermissionFragment.requestPermissions(deniedPermissions);
            } else {
                if (permissionCallBack != null)
                    permissionCallBack.permissionCallBack(true);
            }
        } else {
            //如需在6.0 以下版本做特殊处理 此处可做扩展
            if (permissionCallBack != null)
                permissionCallBack.permissionCallBack(true);
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @return 只要有一个权限没有被授予, 则直接返回 false，否则，返回true!
     */
    public static boolean checkPermissionAllGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查未允许的权限集合
     *
     * @param context     上下文
     * @param permissions 权限集合
     * @return 未允许的权限集合
     */
    public static List<String> checkPermissionDenied(Context context, String[] permissions) {
        List<String> lstPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                lstPermissions.add(permission);
            }
        }
        return lstPermissions;
    }

    public static class Builder {
        private HZFPermission hzfPermissionUtil;

        public Builder(FragmentActivity activity) {
            hzfPermissionUtil = new HZFPermission(activity);
        }

        public Builder(Fragment fragment) {
            hzfPermissionUtil = new HZFPermission(fragment);
        }

        public Builder setPermissions(String... permission) {
            hzfPermissionUtil.permission = Arrays.asList(permission);
            return this;
        }

        public Builder ifGoToSetting(boolean ifGoToSetting) {
            hzfPermissionUtil.ifGoToSetting = ifGoToSetting;
            return this;
        }

        public Builder setPermissionCallBack(PermissionCallBack permissionCallBack) {
            hzfPermissionUtil.permissionCallBack = permissionCallBack;
            return this;
        }

        public HZFPermission build() {
            return hzfPermissionUtil;
        }
    }
}
