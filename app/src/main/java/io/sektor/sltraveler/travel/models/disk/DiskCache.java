package io.sektor.sltraveler.travel.models.disk;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import io.reactivex.Maybe;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DiskCache<T extends Serializable> {

    private static final String LOG_TAG = "DiskCache";

    private final Context context;
    private final String cacheName;

    public DiskCache(Context context, String cacheName) {
        this.context = context;
        this.cacheName = cacheName;
    }

    public T forceCleanCacheBlocking() {
        // Invalidate anything older than a minute
        long maxAge = System.currentTimeMillis() / 1000L;

        return Maybe.create(getCleaner(maxAge))
                .doOnError(err -> Log.e(LOG_TAG, err.getMessage()))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> Log.d(LOG_TAG, "Forced, blocking cache cleaning task completed"))
                .blockingGet();
    }

    public Maybe<T> cleanCache(int maxAgeSeconds) {
        // Invalidate anything older than a minute
        long maxAge = System.currentTimeMillis() / 1000L - maxAgeSeconds;

        return Maybe.create(getCleaner(maxAge))
                .doOnError(err -> Log.e(LOG_TAG, err.getMessage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(LOG_TAG, "Cache cleaning task completed"));
    }

    private MaybeOnSubscribe<T> getCleaner(long maxAge) {
        return emitter -> {
            File file = new File(context.getCacheDir(), cacheName);
            try {
                // Just complete if we have no cache
                if (!file.isFile()) {
                    Log.d(LOG_TAG, "No disk cache found, skipping cleaning");
                    emitter.onComplete();
                    return;
                }

                // Inspect file last modified
                if (file.lastModified() / 1000L < maxAge) {
                    boolean cleaned = file.delete();
                    Log.d(LOG_TAG, "Clean disk cache success: " + cleaned);
                } else {
                    Log.d(LOG_TAG, String.format("File age of %d seconds, leaving disk cache", System.currentTimeMillis() / 1000L - file.lastModified() / 1000L));
                }

                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        };
    }

    public Maybe<T> persist(T item) {
        MaybeOnSubscribe<T> del = emitter -> {
            try {
                File file = new File(context.getCacheDir(), cacheName);
                FileOutputStream outStream = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(outStream);

                out.writeObject(item);
                out.close();
                outStream.close();

                Log.d(LOG_TAG, String.format("Persist completed: %s (%d bytes)", file.getAbsolutePath(), file.length()));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        };

        return Maybe.create(del)
                .doOnError(err -> Log.e(LOG_TAG, err.getMessage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(LOG_TAG, "Persisted item to disk: " + item));
    }

    @SuppressWarnings("unchecked")
    public Maybe<T> readCache() {
        MaybeOnSubscribe<T> del = emitter -> {
            try {
                // Just complete if we have no cache
                File file = new File(context.getCacheDir(), cacheName);
                if (!file.isFile()) {
                    emitter.onComplete();
                    return;
                }

                FileInputStream inStream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(inStream);

                T item = (T) in.readObject();

                in.close();
                emitter.onSuccess(item);
            } catch (Exception e) {
                emitter.onError(e);
            }
        };

        return Maybe.create(del)
                .doOnError(err -> Log.e(LOG_TAG, err.getMessage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(item -> Log.d(LOG_TAG, "Read item from disk: " + item));

    }

}
