# androidPermission
Android 动态权限申请库

当前库只是作为 Android  api 23 及之上权限申请的一个参考  

具体的调用方式是用的构造器式的链式调用方式：
HZFPermission hzfPermissionUtil;
hzfPermissionUtil = new HZFPermission.Builder(this)
                .ifGoToSetting(true) //是否是强制提示吊起进设置页面的弹框
                .setPermissions(Manifest.permission.READ_PHONE_STATE)//申请权限的数组
                .setPermissionCallBack(new PermissionCallBack() { //权限回调 可以做一些权限申请完之后要做的事儿
                    @Override
                    public void permissionCallBack(boolean hasPermission) {
                        if (hasPermission) {
                            Toast.makeText(MainActivity.this, "已经获取到了读取设备信息的权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
        hzfPermissionUtil.request(); //发起权限请求
