package pl.mobile.kandydatpl.logic;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider;
import com.amazonaws.regions.Regions;
import pl.mobile.kandydatpl.data.AppSyncDb;
import pl.mobile.kandydatpl.data.DataProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Logic {
    public static AWSAppSyncClient appSyncClient;
    public static DataProvider dataProvider = AppSyncDb.getInstance();
    public static DateFormat defaultFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static boolean appSyncInitialized = false;

    public static void initAppSync(Context ctx) {
        if(!appSyncInitialized) {
            appSyncClient = AWSAppSyncClient.builder()
                    .context(ctx)
                    .awsConfiguration(new AWSConfiguration(ctx))
                    .region(Regions.EU_CENTRAL_1)
                    .cognitoUserPoolsAuthProvider(new CognitoUserPoolsAuthProvider() {
                        @Override
                        public String getLatestAuthToken() {
                            try {
                                return AWSMobileClient.getInstance().getTokens().getIdToken().getTokenString();
                            } catch (Exception e) {
                                Log.e("APPSYNC_ERROR", e.getLocalizedMessage());
                                return e.getLocalizedMessage();
                            }
                        }
                    })
                    .build();
            appSyncInitialized = true;
        }
    }
}
