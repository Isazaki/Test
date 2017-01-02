package malawi_music.com.entrytest.helper;

/**
 * Created by Cole on 12/28/2016.
 */


public class Movie
{
    // class properties
    public int id;
    public String title;

    // empty constructor
    public Movie()
    {

    }

    // customized constructor which takes two  parameters/arguments of a type integer and String.
    public Movie(int id, String title)
    {
        // assigning the class properties to the passed parameters/arguments.
        this.title = title;
        this.id = id;
    }

}
