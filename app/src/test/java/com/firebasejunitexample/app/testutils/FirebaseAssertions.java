package com.firebasejunitexample.app.testutils;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import junit.framework.Assert;

import java.util.concurrent.Semaphore;

import static junit.framework.Assert.fail;

/**
 * Assertions for Firebase contents.
 */
public class FirebaseAssertions {

    /**
     * Asserts that Firebase contains certain object under a given path
     * @param firebase Firebase reference
     * @param path path to the checked node
     * @param expectedValue expected value
     * @throws InterruptedException
     */
    public static void assertFirebaseData(Firebase firebase, final String path, final Object expectedValue) throws InterruptedException {
        final AssertionError[] collectedException = {null};
        final Semaphore semaphore = new Semaphore(0);
        firebase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Assert.assertEquals("Firebase '" + path + "' should contain", expectedValue, dataSnapshot.getValue(expectedValue.getClass()));
                } catch (AssertionError e) {
                    collectedException[0] = e;
                } catch (Throwable e) {
                    collectedException[0] = new AssertionError(e);
                } finally {
                    semaphore.release();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new UnsupportedOperationException("Not implemented");
            }
        });
        semaphore.acquire();
        if (collectedException[0] != null) {
            throw new AssertionError(collectedException[0]);
        }
    }

    /**
     * Asserts that Firebase node exists under a given path
     * @param firebase Firebase reference
     * @param path path to the checked node
     * @throws InterruptedException
     */
    public static void assertFirebaseNodeExists(Firebase firebase, String path) throws InterruptedException {
        assertNodeExistence(firebase, path, true);
    }

    /**
     * Asserts that Firebase node DOES NOT exist under a given path
     * @param firebase Firebase reference
     * @param path path to the checked node
     * @throws InterruptedException
     */
    public static void assertFirebaseNodeDoesNotExist(Firebase firebase, String path) throws InterruptedException {
        assertNodeExistence(firebase, path, false);
    }

    private static void assertNodeExistence(Firebase firebase, final String path, final boolean shouldExist) throws InterruptedException {
        final AssertionError[] collectedException = {null};
        final Semaphore semaphore = new Semaphore(0);
        firebase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    boolean exists = dataSnapshot.getValue() != null;
                    if (shouldExist) {
                        if(!exists) {
                            fail("Firebase path '" + path + "' should exist");
                        }
                    } else {
                        if(exists) {
                            fail("Firebase path '" + path + "' should not exist");
                        }

                    }
                } catch (AssertionError e) {
                    collectedException[0] = e;
                } catch (Throwable e) {
                    collectedException[0] = new AssertionError(e);
                } finally {
                    semaphore.release();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new UnsupportedOperationException("Not implemented");
            }
        });
        semaphore.acquire();
        if (collectedException[0] != null) {
            throw new AssertionError(collectedException[0]);
        }
    }
}
