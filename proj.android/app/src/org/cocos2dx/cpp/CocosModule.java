package org.cocos2dx.cpp;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CocosModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;

    private AppActivity mActivity;
    private HashMap<String, GraphicsElement> elements;

    CocosModule(ReactApplicationContext context, AppActivity activity) {
        super(context);
        reactContext = context;
        mActivity = activity;
        elements = new HashMap<String, GraphicsElement>();
    }

    @Override
    public String getName() {
        return "Cocos";
    }

    @ReactMethod
    public void show() {
        this.mActivity.showReact();
    }

    @ReactMethod
    public void hide() {
        this.mActivity.hideReact();
    }

    public void passProperties(String id, ReadableMap element) {
        if (element.hasKey("position")) {
            ReadableMap position = element.getMap("position");
            this.setPosition2D(id, (float) position.getDouble("x"), (float) position.getDouble("y"));
        }

        if (element.hasKey("rotation")) {
            this.setRotation2D(id, (float)element.getDouble("rotation"));
        }

        if (element.hasKey("scale")) {
            this.setScale2D(id, (float)element.getDouble("scale"));
        }
    }

    public void ensureElement(String id, ReadableMap element, String parentElement, String rootElement) {
        String type = element.getString("type");

        // Create the element if it doesn't exist.
        if (!elements.containsKey(id) || !elements.get(id).created) {
            elements.put(id, new GraphicsElement(id));
            createElement(id, type);
            elements.get(id).created = true;
            elements.get(id).rootId = rootElement;
        }

        if (elements.get(id) != null && elements.get(id).created) {
            // Update its parent.
            if (parentElement == "__ROOT__" || (elements.get(parentElement) != null && elements.get(parentElement).created)) {
                updateParent(id, parentElement);
                elements.get(id).parentId = parentElement;
            }

            // Pass properties to element.
            this.passProperties(id, element);

            // Ensure its children.
            if (element.hasKey("children")) {
                ReadableArray children = element.getArray("children");

                for (int i = 0; i < children.size(); i++) {
                    ReadableMap child = children.getMap(i);
                    this.ensureElement(child.getString("id"), child, id, rootElement);
                }
            }
        }
    }

    public void ensureElement(String id, ReadableMap element) {
        this.ensureElement(id, element, "__ROOT__", id);
    }

    @ReactMethod
    public void registerRootCocosElement(String id, ReadableMap tree, Promise promise) {
        this.ensureElement(id, tree);
        promise.resolve(null);
    }

    @ReactMethod
    public void unregisterRootCocosElement(String id, Promise promise) {
        // Destroy the root node.
        destroyElement(id);

        // Remove it and any of its descendants from the list.
        ArrayList<String> removeIds = new ArrayList<String>();

        for (GraphicsElement element : elements.values()) {
            removeIds.add(element.id);
        }

        for (String removeId : removeIds) {
            elements.remove(removeId);
        }

        promise.resolve(null);
    }

    /**
     * Native Methods
     **/
    public static native void createElement(String id, String type);
    public static native void destroyElement(String id);
    public static native void updateParent(String id, String parentId);
    public static native void setPosition2D(String id, float x, float y);
    public static native void setRotation2D(String id, float rotation);
    public static native void setScale2D(String id, float rotation);

    public static void callUpdates(float dt) {
        WritableMap params = Arguments.createMap();
        params.putDouble("deltaTime", dt);

        if (reactContext != null && reactContext.hasActiveCatalystInstance()) {
            reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("cocosFrame", params);
        }
    }
}