package malawi_music.com.entrytest.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import malawi_music.com.entrytest.R;

/**
 * Created by Cole on 12/28/2016.
 */

// class inherits from the BaseAdapter.
public class SwipeListAdapter extends BaseAdapter
{

    // declared Activity variable which can be accessed within this class.
    private Activity activity;

    // declared LayoutInflater variable which can be accessed within the class.
    private LayoutInflater inflater;

    // declared List variable with the generic type of Movie.
    private List<Movie> movieList;

    // declared String array which can be used within this class.
    private String[] bgColors;

    // class constructor which takes in two parameters/arguments of an Activity variable and a
    // List variable.
    public SwipeListAdapter(Activity activity, List<Movie> movieList)
    {
        // Assigning the private variables declared about to the passed constructor parameters/
        // arguments.
        this.activity = activity;
        this.movieList = movieList;

        // populating the String array using a String Resource found in colors.xml.
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }

    //Pre-defined Method which returns the length of the List.
    @Override
    public int getCount()
    {
        return movieList.size();
    }

    //Pre-defined method which returns a list Object at a certain index.
    @Override
    public Object getItem(int location)
    {
        return movieList.get(location);
    }

    //Pre-defined method which gets an id of an Object at a certain index.
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // If the inflater is empty, get content.
        if (inflater == null)
        {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // If the current view is empty, populate the list defined in the layout xml, which is
        // known through its id (list_row).
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        // Declaring and assigning the TextView variable to list view TextView, which has the id
        // serial.
        TextView serial = (TextView) convertView.findViewById(R.id.serial);

        // Declaring and assigning the TextView variable to list view TextView, which has the id
        // title.
        TextView title = (TextView) convertView.findViewById(R.id.title);

        // Setting the serial TextView to the position of a movieList.
        serial.setText(String.valueOf(movieList.get(position).id));

        // Setting the title to a movieList a certain position.
        title.setText(movieList.get(position).title);

        // It divides the position which is passed as a method parameter/argument which length of the
        // bgColors array,and takes the remainder (modulus) which is used as a index for the bgColors
        // which is later assigned to the String variable 'color'.
        String color = bgColors[position % bgColors.length];

        // Sets the background serial TextView to the bgColor color at position 'color'.
        serial.setBackgroundColor(Color.parseColor(color));

        // Return the complete list view
        return convertView;
    }
}
