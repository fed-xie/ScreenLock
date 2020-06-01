package com.fed.app.screenlock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    ComponentName mAdminName;
    DevicePolicyManager mDpm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdminName = new ComponentName(this, AdminMngReceiver.class);
        mDpm = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (mDpm == null) {
            Toast.makeText(this, "Can't find DevicePolicyManager", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mDpm.isAdminActive(mAdminName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.app_name));
            startActivityForResult(intent, 1);
        }
        else {
            mDpm.lockNow();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!mDpm.isAdminActive(mAdminName)) {
                Toast.makeText(this, R.string.app_err_device_admin_required, Toast.LENGTH_LONG).show();
            }
            else {
                mDpm.lockNow();
            }
            finish();
        }
    }
}
