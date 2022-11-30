package com.app;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.util.Log;

public class PhoneModule extends ReactContextBaseJavaModule {
    PhoneModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "PhoneModule";
    }

    @ReactMethod
    public void sayHello() {
        // Log.d("PhoneModule", "was gud shawty");
    }

}
