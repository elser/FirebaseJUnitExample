package com.firebasejunitexample.app.services;

import com.firebasejunitexample.app.testutils.FirebaseMocker;
import com.firebasejunitexample.app.testutils.SyncListener;
import com.firebasejunitexample.app.vo.Product;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.firebasejunitexample.app.testutils.FirebaseAssertions.assertFirebaseData;
import static com.firebasejunitexample.app.testutils.FirebaseAssertions.assertFirebaseNodeDoesNotExist;


/**
 * Test for ProductsService.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductsServiceTest extends TestCase {
    final FirebaseMocker firebaseMocker = new FirebaseMocker();
    final ProductsService productsService = new ProductsService(firebaseMocker.getFirebase());

    @Before
    public void clearFirebase() {
        firebaseMocker.getFirebase().child("products").removeValue();
    }

    @After
    public void flush() throws InterruptedException {
        firebaseMocker.setValueSync("finished", "ok");
    }

    @Test
    public void testCreate() throws Exception {
        Product product = new Product("phone-key", "Phone", 5);
        productsService.save(product);

        assertFirebaseData(firebaseMocker.getFirebase(), "products/phone-key", product);
    }

    @Test
    public void testRead() throws Exception {
        final String key = "tv-key";
        Product writtenProduct = new Product(key, "TV", 2);
        firebaseMocker.setValueSync("products/" + key, writtenProduct);

        final SyncListener<Product> syncListener = new SyncListener<Product>(Product.class);
        productsService.load(key, syncListener);
        syncListener.waitForLoad();

        final Product readProduct = syncListener.getValue();
        assertEquals("Written and read products should be equal", writtenProduct, readProduct);
    }

    @Test
    public void testUpdate() throws Exception {
        final String key = "radio-key";
        Product writtenProduct = new Product(key, "Old Radio", 2);
        firebaseMocker.setValueSync("products/" + key, writtenProduct);

        Product product = new Product(key, "New Radio", 5);
        productsService.save(product);

        assertFirebaseData(firebaseMocker.getFirebase(), "products/" + key, product);
    }

    @Test
    public void testDelete() throws Exception {
        final String key = "to-remove-key";
        Product obsoleteProduct = new Product(key, "Old Car", 2);
        firebaseMocker.setValueSync("products/" + key, obsoleteProduct);

        productsService.remove(key);
        assertFirebaseNodeDoesNotExist(firebaseMocker.getFirebase(), "products/" + key);
    }
}