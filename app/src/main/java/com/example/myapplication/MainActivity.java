package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;

import java.util.Calendar;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        Branch.sessionBuilder(this)
                .withCallback(branchReferralInitListener)
                .withData(getIntent() != null ? getIntent().getData() : null)
                .init();

        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle("Branch SDK - Android")
                .setContentDescription("The Advanced Features of Branch Android SDK")
                .setContentImageUrl("https://lorempixel.com/400/400")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(new ContentMetadata().addCustomMetadata("key1", "value1"));

        LinkProperties lp = new LinkProperties()
                .setChannel("copy")
                .setFeature("sharing")
                .setCampaign("branch home test")
                .setStage("new user")
                .addControlParameter("$desktop_url", "https://help.branch.io/developers-hub/docs/android-sdk-overview")
                .addControlParameter("custom", "data")
                .addControlParameter("deep_link_test", "other")
                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

        ShareSheetStyle ss = new ShareSheetStyle(MainActivity.this, "Check this out!", "This stuff is awesome: ")
                .setCopyUrlStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With");

        Branch.BranchLinkShareListener sl = new Branch.BranchLinkShareListener() {
            @Override
            public void onShareLinkDialogLaunched() {
            }
            @Override
            public void onShareLinkDialogDismissed() {
            }
            @Override
            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                if (error != null) {
                    Log.i("My App", "Share Failed: " + error.getMessage());
                } else {
                    Log.i("MyApp", sharedLink + "is shared by " + sharedChannel);
                }
                Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                intent.putExtra("link", sharedLink);
                intent.putExtra("channel", sharedChannel);
                startActivity(intent);
                finish();
            }
            @Override
            public void onChannelSelected(String channelName) {
            }
        };

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            String value = lp.getControlParams().get("deep_link_test");
            String check = "\"deep_link_test\":\"other\" exist? ";
            assert value != null;
            Log.i("MyApp", check + (value.equals("other") ? "Yes" : "No"));
            buo.showShareSheet(MainActivity.this, lp, ss, sl);
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null &&
                intent.hasExtra("branch_force_new_session") &&
                intent.getBooleanExtra("branch_force_new_session", true)) {
            Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
        }
    }
    private final Branch.BranchReferralInitListener branchReferralInitListener = (linkProperties, error) -> {
        if (error == null) {
            // apply the comment out change for reaching other activity
            String test_param = "deep_link_test"; // change to "+clicked_branch_link"
            assert linkProperties != null;
            boolean has_deep_link = linkProperties.has(test_param);
            boolean link_to_other = false;
            try {
                link_to_other = linkProperties.get(test_param).toString().equals("other"); // change to "false"
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (has_deep_link && link_to_other) {
                Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.i("MyApp", "No deep link: " + linkProperties);
            }
        } else {
            Log.i("MyApp", error.getMessage());
        }
    };
}