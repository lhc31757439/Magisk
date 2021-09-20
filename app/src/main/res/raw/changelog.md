# alpha更新日志

## Magisk (558710bb-alpha-3)
- [App] 支持SharedUserId
- [App] 还原boot镜像后删除备份文件
- [App] 内置当前版本更新日志
- [App] 当无法下载stub时使用本地版本
- [App] 使用Java 8的现代时间API
- [General] 不再自动解锁设备块
- [General] Zygisk关闭时使用MagiskHide实现隐藏
- [App] 删除markdown渲染
- [General] 不执行已忽略加载模块的脚本
- [App] 修复第二次点按隐藏app按钮可能无反应
- [App] 增强修复环境功能
- [App] 支持设置PATCHVBMETAFLAG标志，以处理boot镜像中的vbmeta
- [App] 适配 Android 12L
- [MagiskInit] 重构两步启动的处理方法
- [General] 跳过镜像overlayfs分区
- [MagiskInit] 支持处理force_debuggable标记
- [App] 修复SnackBar弹出位置
- [App] 删除安装到未使用的槽位限制
- [MagiskBoot] 加快zopfli压缩
- [App] 修复某些情况下无法打开
- [App] 有root时修补镜像不再影响当前环境
- [App] 避免不断检查环境
- [App] 刷入时输出使用默认字体大小显示
- [General] 存在ramdisk时强制不安装到recovery
- [General] 更新到 NDK 23

### 如何安装？
通过Magisk应用来安装和卸载Magisk，一般情况应直接在应用内完成，第一次安装等特殊情况应修补镜像后使用fastboot/odin工具刷入。
通过自定义Recovery不是受支持的方式。

# 上游更新日志

## Magisk (92546e8a) (23016)

- Fixed a possible AVB header misalignment
- Add new env variable `PATCHVBMETAFLAG` to configure whether vbmeta flags should be patched
- Only hijack `unshare` calls with `CLONE_NEWNS` bit set
- Improved support for devices with `oplus.fstab` (OnePlus/Oppo/Realme)
- Support loading fstab from `/system/etc` (Pixel 6 Dec. Patch)
- Various Magisk app fixes

## Diffs to v23.0

- [General] MagiskHide is removed from Magisk
- [General] Support 64-bit only systems
- [General] Support Android 12
- [General] Update BusyBox to 1.34.1
- [Zygisk] Introduce new feature: Zygisk
- [Zygisk] Introduce DenyList feature to revert Magisk features in user selected processes
- [MagiskBoot] Support patching 32-bit kernel zImages
- [MagiskBoot] Support boot image header v4
- [MagiskBoot] Support patching out `skip_initramfs` from dtb bootargs
- [MagiskBoot] Add new env variable `PATCHVBMETAFLAG` to configure whether vbmeta flags should be patched
- [MagiskInit] Support loading fstab from `/system/etc` (required for Pixel 6)
- [MagiskInit] Support `/proc/bootconfig` for loading boot configurations
- [MagiskInit] Better support for some Meizu devices
- [MagiskInit] Better support for some OnePlus/Oppo/Realme devices
- [MagiskInit] Support `init.real` on some Sony devices
- [MagiskPolicy] Load `*_compat_cil_file` from system_ext
- [MagiskSU] Use isolated devpts if the kernel supports it
- [MagiskSU] Fix root shell if isolated mount namespace is set
- [resetprop] Deleted properties are now wiped from memory instead of just unlinking
- [App] Build a single APK for all ABIs
- [App] Switch to use standard bottom navigation bar
- [App] Downloading modules within the Magisk app is removed
