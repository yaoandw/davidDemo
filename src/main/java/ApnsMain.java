import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ApnsClientBuilder;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.proxy.Socks5ProxyHandlerFactory;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

/**
 * Created by yaoandw on 2016/12/19.
 */
public class ApnsMain {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException, ExecutionException {
        final ApnsClient apnsClient = new ApnsClientBuilder().build();
        ApnsMain demo = new ApnsMain();
//        File file = demo.getFile("APNsAuthKey_6826D38KAD.p8");
//        apnsClient.registerSigningKey(file,
//                "S4ZDAE68CG", "6826D38KAD", "com.actiz.cas");
        File file = demo.getFile("APNsAuthKey_V3NXLHPXMA.p8");
        apnsClient.registerSigningKey(file,
                "65CTY56N76", "V3NXLHPXMA", "com.actiz.castest");

        // Optional: we can listen for metrics by setting a metrics listener.
//        apnsClient.setMetricsListener(new NoopMetricsListener());
//
//        // Optional: we can set a proxy handler factory if we must use a proxy.
//        apnsClient.setProxyHandlerFactory(
//                new Socks5ProxyHandlerFactory(
//                        new InetSocketAddress("my.proxy.com", 1080), "username", "password"));

        // Once we've created a client, we can connect it to the APNs gateway.
        // Note that this process is asynchronous; we'll get a Future right
        // away, but we'll need to wait for it to complete before we can send
        // any notifications. Note that this is a Netty Future, which is an
        // extension of the Java Future interface that allows callers to add
        // listeners and adds methods for checking the status of the Future.

        final Future<Void> connectFuture = apnsClient.connect(ApnsClient.DEVELOPMENT_APNS_HOST);
        connectFuture.await();

        // Once we're connected, we can start sending push notifications.
        final SimpleApnsPushNotification pushNotification;

        {
            final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            payloadBuilder.setAlertBody("Example!");
            payloadBuilder.addCustomProperty("sc","actiz.com");
            payloadBuilder.addCustomProperty("mid","MSG-0915A48100000156Z");

            final String payload = payloadBuilder.buildWithDefaultMaximumLength();
//            final String token = "a1871ff025722553b33ecc0f149c2db58f7728abaa0e8a9d50ec945d6911fb28";// TokenUtil.sanitizeTokenString("<efc7492 bdbd8209>");
//            final String token = "20b7ac928f58553d03ba99da309e207d199cc0fa8b42b2f58c31332308d61feb";// laymond
            final String token = "94c61891abdb8ab08ffb92c2e1d9ee30f9d4f1d3d58296ad0aaf9d5b081f7f62";// yao

            pushNotification = new SimpleApnsPushNotification(token, "com.actiz.castest", payload);
        }

        // Like connecting, sending notifications is an asynchronous process.
        // We'll get a Future immediately, but will need to wait for the Future
        // to complete before we'll know whether the notification was accepted
        // or rejected by the APNs gateway.
        final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture =
                apnsClient.sendNotification(pushNotification);

        try {
            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    sendNotificationFuture.get();

            if (pushNotificationResponse.isAccepted()) {
                // Everything worked! The notification was successfully sent to
                // and accepted by the gateway.
                System.out.println("Push notification accepted by APNs gateway.");
            } else {
                // Something went wrong; this should be considered a permanent
                // failure, and we shouldn't try to send the notification again.
                System.out.println("Notification rejected by the APNs gateway: " +
                        pushNotificationResponse.getRejectionReason());

                if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                    // If we have an invalidation timestamp, we should also stop
                    // trying to send notifications to the destination token (unless
                    // it's been renewed somehow since the expiration timestamp).
                    System.out.println("\t…and the token is invalid as of " +
                            pushNotificationResponse.getTokenInvalidationTimestamp());
                }
            }
        } catch (final ExecutionException e) {
            // Something went wrong when trying to send the notification to the
            // APNs gateway. The notification never actually reached the gateway,
            // so we shouldn't consider this a permanent failure.
            System.err.println("Failed to send push notification.");
            e.printStackTrace();

            if (e.getCause() instanceof ClientNotConnectedException) {
                // If we failed to send the notification because the client isn't
                // connected, we can wait for an automatic reconnection attempt
                // to succeed before sending more notifications.
                System.out.println("Waiting for client to reconnect…");
                apnsClient.getReconnectionFuture().await();
                System.out.println("Reconnected.");
            }
        }
        // Finally, when we're done sending notifications (i.e. when our
        // application is shutting down), we should disconnect all APNs clients
        // that may be in play.
        final Future<Void> disconnectFuture = apnsClient.disconnect();
        disconnectFuture.await();
    }

    private File getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        return file;

    }

}
