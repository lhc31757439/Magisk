package com.topjohnwu.magisk.components;

import android.app.Notification;
import android.widget.Toast;

import com.topjohnwu.core.App;
import com.topjohnwu.core.utils.Utils;
import com.topjohnwu.magisk.R;
import com.topjohnwu.net.DownloadProgressListener;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ProgressNotification implements DownloadProgressListener {
    private NotificationManagerCompat mgr;
    private NotificationCompat.Builder builder;
    private Notification notification;
    private long prevTime;

    public ProgressNotification(String title) {
        mgr = NotificationManagerCompat.from(App.self);
        builder = Notifications.progress(title);
        prevTime = System.currentTimeMillis();
        update();
        Utils.toast(App.self.getString(R.string.downloading_toast, title), Toast.LENGTH_SHORT);
    }

    @Override
    public void onProgress(long bytesDownloaded, long totalBytes) {
        long cur = System.currentTimeMillis();
        if (cur - prevTime >= 1000) {
            prevTime = cur;
            int progress = (int) (bytesDownloaded * 100 / totalBytes);
            builder.setProgress(100, progress, false);
            builder.setContentText(progress + "%");
            update();
        }
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        return builder;
    }

    public Notification getNotification() {
        return notification;
    }

    public void update() {
        notification = builder.build();
        mgr.notify(hashCode(), notification);
    }

    private void lastUpdate() {
        notification = builder.build();
        mgr.cancel(hashCode());
        mgr.notify(notification.hashCode(), notification);
    }

    public void dlDone() {
        builder.setProgress(0, 0, false)
                .setContentText(App.self.getString(R.string.download_complete))
                .setSmallIcon(R.drawable.ic_check_circle)
                .setOngoing(false);
        lastUpdate();
    }

    public void dlFail() {
        builder.setProgress(0, 0, false)
                .setContentText(App.self.getString(R.string.download_file_error))
                .setSmallIcon(R.drawable.ic_cancel)
                .setOngoing(false);
        lastUpdate();
    }

    public void dismiss() {
        mgr.cancel(hashCode());
    }
}
