package malawi_music.com.entrytest.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Cole on 12/28/2016.
 */

// A MyApplication class that inherits properties and methods from the Application class
public class MyApplication extends Application
{

    // declaring a static String variable, which is assigned as the current class name and can be
    // used by other classes declared in the same package.
    public static final String TAG = MyApplication.class.getSimpleName();

    // declaring a RequestQueue type variable which can only be accessed in the "SAME CLASS".
    private RequestQueue mRequestQueue;

    // declaring a MyApplication type variable which can only be accessed in the "SAME CLASS".
    private static MyApplication mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        // assigning the variable declared above to the current object.
        mInstance = this;
    }

    // a method which returns an Instance of the class.
    public static synchronized MyApplication getInstance()
    {
        return mInstance;
    }

    // a method which determines when to process a request.
    public RequestQueue getRequestQueue()
    {
        // If we don't have a request, process one, by adding a Request to the queue.
        if (mRequestQueue == null)
        {
            // adding a request to the queue.
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        // else don't do anything and return the current request queue.
        return mRequestQueue;
    }

    // a method which  processes a request, by using the passed parameters of a generic Request
    // variable and a String variable.
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // This is a ternary operator, which means if the request tag is empty set it using the "TAG",
        // which was declared as a static variable, is the request tag is not empty, set tag to the
        //'tag' variable which was passed as a method parameter/argument.
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        // adds request to the queue.
        getRequestQueue().add(req);
    }

    // This is an override method of the method above it, but it only takes in one parameter/argument.
    public <T> void addToRequestQueue(Request<T> req)
    {
        // sets request tag, using the static variable defined at the beginning of the class,
        // which is being used as a CONSTANT.
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // A method which takes in a varible of type Object, used to removed request from the queue.
    public void cancelPendingRequests(Object tag)
    {
        // if the queue has something in it.
        if (mRequestQueue != null)
        {
            // remove the queue is the queue.
            mRequestQueue.cancelAll(tag);
        }

        // else dont do anything, because its already empty.
    }
}