package com.firebasejunitexample.app.testutils;

import android.content.Context;
import com.firebase.client.Config;
import com.firebase.client.EventTarget;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Mimics Android environment to make Firebase work.
 * Also contains some useful methods for manipulating Firebase data.
 */
public class FirebaseMocker {
    @Mock
    Context context;
    @Mock
    Context appContext;
    @Mock
    private android.content.SharedPreferences sharedPreferences;

    private static AtomicBoolean initialised = new AtomicBoolean(false);

    private Firebase firebase;

    public FirebaseMocker() {
        MockitoAnnotations.initMocks(this);
        if(!initialised.get()) {
            final Config config = Firebase.getDefaultConfig();
            config.setEventTarget(new EventTarget() {
                @Override
                public void postEvent(Runnable runnable) {
                    runnable.run();
                }

                @Override
                public void shutdown() {
                    throw new UnsupportedOperationException("Not implemented");
                }

                @Override
                public void restart() {
                    throw new UnsupportedOperationException("Not implemented");
                }
            });
            Firebase.setDefaultConfig(config);
            when(context.getApplicationContext()).thenReturn(appContext);
            when(appContext.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
            Firebase.setAndroidContext(context);
            initialised.set(true);
        }
        firebase = new Firebase(getFirebaseUrl());
    }

    protected String getFirebaseUrl() {
        return "https://<your-firebase-app>.firebaseio.com/";
    }

    /**
     * Works like standard Firebase.setValue(), but waits for the update to happen.
     * @param path Path to the Firebase node we want to update
     * @param value Value that is to be written to Firebase
     * @throws InterruptedException
     */
    public void setValueSync(String path, Object value) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(0);
        firebase.child(path).setValue(value, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                semaphore.release();
            }
        });
        semaphore.acquire();
    }

    public Firebase getFirebase() {
        return firebase;
    }
}
