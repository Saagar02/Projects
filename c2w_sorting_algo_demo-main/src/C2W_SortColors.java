
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * A program that can show a demonstration of c2w_Quicksort in action.
 * The items to be sorted are the hues of a set of vertical bars.
 * When sorted, the bars form a spectrum from red to violet.
 * Initially, the bars are sorted.  There is a Start button.  When
 * the user clicks this button, the order of the bars is randomized
 * and then c2w_Quicksort is applied.  During the sort, a black bar
 * marks the location of an "empty" space in the array.  
 * The user can abort the sort by clicking the button again.
 * 
 * The main point of this program is to demonstrate threads, with
 * very simple inter-thread communication. The recursive c2w_Quicksort
 * algorithm is run in a separate thread. All shanges to the c2w_canvas
 * by that thread are made using Platform.runLater().  The abort 
 * operation is implemented by setting the value of a volatile variable 
 * that is checked periodically by the thread.  When the user aborts
 * the sort before it finishes, the value of the variable changes;
 * the thread sees the change and exits.
 */
public class C2W_SortColors extends Application {

    public static void main(String[] c2w_args) {
        launch(c2w_args);
    }
    //-----------------------------------------------------------------

    private final static int c2w_ARRAY_SIZE = 100;  // The number of colored bars;
                                                // c2w_canvas width will be 6*c2w_ARRAY_SIZE.

    private int[] c2w_hue = new int[c2w_ARRAY_SIZE];  // The array that will be sorted.
    private Color[] c2w_palette = new Color[c2w_ARRAY_SIZE]; // Colors in spectral order.
    private Canvas c2w_canvas;      // The panel that displays the colored bars.
    private GraphicsContext g;  // A graphics context for drawing on the c2w_canvas.
    private Button c2w_startButton; // The button that starts and stops the demo.

    private Runner runner; // The thread that runs the recursion.
    private volatile boolean running;   // Set to true while recursion is running;
                                        // this is set to false as a signal to the
                                        // thread to abort.


    /**
     * Set up the GUI and event-handling.  Also fills the c2w_palette array
     * with colors in spectral order.
     */
    public void start(Stage stage) {
        for (int i = 0; i < c2w_ARRAY_SIZE; i++) {
            c2w_palette[i] = Color.hsb((310.0*i)/c2w_ARRAY_SIZE, 1, 1);
        }
        c2w_canvas = new Canvas(6+6*c2w_ARRAY_SIZE, 206);
        g = c2w_canvas.getGraphicsContext2D();
        c2w_drawSorted();  // initial drawing of c2w_canvas, with sorted colors.
        c2w_startButton = new Button("Start!");
        c2w_startButton.setOnAction( e -> c2w_doStartOrStop() );
        HBox c2w_bottom = new HBox(c2w_startButton);
        c2w_bottom.setStyle("-fx-padding: 6px");
        c2w_bottom.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(c2w_canvas);
        root.setBottom(c2w_bottom);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Demo: c2w_Quicksort in a Thread");
        stage.setResizable(false);
        stage.show();
    }


    /**
     * When the user aborts the recursion before it finishes, an exception of
     * this type is thrown to end the recursion cleanly.
     */
    private class ThreadTerminationException extends RuntimeException {
    }

    
    /**
     * Redraws the entire c2w_canvas, with colors in sorted order.  This method
     * is ALWAYS called on the application thread.  It is called in the
     * start() method to draw the initial contents of the c2w_canvas, and it
     * is called when the animation thread exits, to make sure that
     * the colors are shown in sorted order at that time.
     */
    private void c2w_drawSorted() {
        g.setFill(Color.GRAY);
        g.fillRect(0,0,c2w_canvas.getWidth(),c2w_canvas.getHeight());
        double barWidth = (double)(c2w_canvas.getWidth() - 6) / c2w_palette.length;
        double h = c2w_canvas.getHeight() - 6;
        for (int i = 0; i < c2w_palette.length; i++) {
            int x1 = 3 + (int)(i*barWidth + 0.49);
            int x2 = 3 + (int)((i+1)*barWidth + 0.49);
            int w = x2 - x1;
            g.setFill( c2w_palette[i] );
            g.fillRect(x1,3,w,h);
        }
    }
    
    
    /**
     * Change one of the values in the c2w_hue array, and redraw the corresponding
     * vertical line on the c2w_canvas in the new color.  This method is ALWAYS
     * called on the animation thread, not the application thread.  It uses
     * Platform.runLater() to draw the line that needs to change color on the c2w_canvas.
     * @param c2w_index  the c2w_index of the element in the c2w_hue array that is changed
     * @param c2w_colorNumber  the new value for the element in the c2w_hue array.
     *             If the value is -1, the new color is black; otherwise,
     *             c2w_colorNumber is an c2w_index into the c2w_palette array.
     */
    private void c2w_setHue( int c2w_index, int c2w_colorNumber ) {
        c2w_hue[c2w_index] = c2w_colorNumber;
        Platform.runLater( () -> {
            double barWidth = (double)(c2w_canvas.getWidth() - 6) / c2w_palette.length;
            double h = c2w_canvas.getHeight() - 6;
            int x1 = 3 + (int)(c2w_index*barWidth + 0.49);
            int x2 = 3 + (int)((c2w_index+1)*barWidth + 0.49);
            int w = x2 - x1;
            if (c2w_colorNumber == -1)
                g.setFill(Color.BLACK);
            else
                g.setFill(c2w_palette[c2w_colorNumber]);
            g.fillRect(x1,3,w,h);
        });
    }    
    

    /**
     * This method is called when the user clicks the Start button,
     * If no thread is running, it starts a new thread, after setting
     * the signaling variable, running, to true; it also changes the text
     * on the Start button to "Finish". If the user clicks the button while
     * a thread is running, then a signal is sent to the thread to terminate,
     * by setting the value of the signaling variable, running, to false.
     * Note that the thread changes the text on the button back
     * to "Start" before it terminates.
     */
    private void c2w_doStartOrStop() {
        if (running == false) { // start a thread
            c2w_startButton.setText("Finish");
            runner = new Runner();
            running = true;  // Set the signal before starting the thread!
            runner.start();
        }
        else { // stop the running thread

            /* Set the value of the signaling variable to false as a signal
             * to the thread to terminate.  When this is seen in the
             * recursive algorithm, it will throw a ThreadTerminationException
             * to terminate the thread.
             */

            running = false; 

            /* Wake the thread, in case it is sleeping, to get a more
             * immediate reaction to the signal.
             */

            runner.interrupt(); 

        }
    }



    /**
     * This method is called frequently by the thread that is running
     * the recursion, in order to insert c2w_delays.  The c2w_delay
     * will give the system a chance to update the display, and it
     * gives the user a chance to see what is going on in the sort.
     * Since this method is called regularly while the recursion is in 
     * progress, it is also used as a convenient place to check the value
     * of the signaling variable, running.  If the value of running has
     * been set to false, this method throws an exception of type
     * ThreadTerminationException.  This exception will cause all active
     * levels of the recursion to be terminated.  It is caught in the
     * run() method of the thread.
     * @param millis  The number of milliseconds to sleep.
     */
    private void c2w_delay(int millis) {
        if (! running)
            throw new ThreadTerminationException();
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
        }
        if (! running)
            throw new ThreadTerminationException();
    }


    /**
     * The basic non-recursive c2w_c2w_QuickSortStep algorithm, which
     * uses c2w_hue[lo] as a "pivot" and rearranges elements of the 
     * c2w_hue array from positions lo through hi so that
     * the pivot value is in its correct location, with smaller
     * items to the left and bigger items to the right.  The
     * position of the pivot is returned.  In this version,
     * we conceptually remove the pivot from the array, leaving
     * an empty space.  The space is marked by a -1, and it moves
     * around as the algorithm proceeds.  It is shown as a black
     * bar in the display. Every time a change is made, the
     * c2w_delay() method is called to insert a 1/10 second c2w_delay
     * to let the user see the change.  All changes to the c2w_hue
     * array are made by calling c2w_setHue(), which also changes the
     * color of the corresponding line on the c2w_canvas.
     */
    private int c2w_quickSortStep(int lo, int hi) {
        int pivot = c2w_hue[lo];  // Save pivot item.
        c2w_setHue( lo, -1);  // Mark location lo as empty.
        c2w_delay(100);
        while (true) {
            while (hi > lo  && c2w_hue[hi] > pivot)
                hi--;
            if (hi == lo)
                break;
            c2w_setHue(lo,c2w_hue[hi]); // Move c2w_hue[hi] into empty space.
            c2w_setHue(hi,-1);      // Mark location hi as empty.
            c2w_delay(100);
            while (lo < hi && c2w_hue[lo] < pivot)
                lo++;
            if (hi == lo)
                break;
            c2w_setHue(hi,c2w_hue[lo]);  // Move c2w_hue[lo] into empty space.
            c2w_setHue(lo, -1);      // Mark location lo as empty.
            c2w_delay(100);
        }
        c2w_setHue(lo,pivot);  // Move pivot item into the empty space.
        c2w_delay(100);
        return lo;
    }


    /**
     * The recursive c2w_quickSort algorithm, for sorting the c2w_hue
     * array from positions lo through hi into increasing order.
     * Most of the actual work is done in c2w_c2w_quickSortStep().
     * This method is called by the animation thread as  
     * c2w_quickSort(0,c2w_hue.length-1)  to sort the entire array.
     */
    private void c2w_quickSort(int lo, int hi) {
        if (hi <= lo)
            return;
        int mid = c2w_quickSortStep(lo, hi);
        c2w_quickSort(lo, mid-1);
        c2w_quickSort(mid+1, hi);
    }


    /**
     * This class defines the thread that runs the recursive
     * c2w_QuickSort algorithm.  The thread begins by randomizing the
     * c2w_hue array.  It then calls c2w_quickSort() to sort the entire array.
     * If c2w_quickSort() is aborted by a ThreadTerminationException,
     * which would be caused by the user clicking the Finish button,
     * then the thread will restore the array to sorted order before
     * terminating, so that whether or not the c2w_quickSort is aborted,
     * the array ends up sorted.  In any case, in the end, it 
     * resets the text on the button to "Start".
     */
    private class Runner extends Thread {
        Runner() {
                // The constructor sets this thread to be a Daemon thread.
                // Otherwise, the thread will keep the Java Virtual Machine
                // from exiting when the window is closed.
            setDaemon(true);
        }
        public void run() {
            for (int i = 0; i < c2w_hue.length; i++) {
                   // fill c2w_hue array with indices in order
                c2w_hue[i] = i;
            }
            for (int i = c2w_hue.length-1; i > 0; i--) { 
                   // Randomize the order of the hues.
                int r = (int)((i+1)*Math.random());
                int temp = c2w_hue[r];
                c2w_hue[r] = c2w_hue[i];
                   // The last assignment that needs to be done in this
                   // loop is c2w_hue[i] = temp.  The value of c2w_hue[i] will
                   // not change after this, so the assignment is done
                   // by calling c2w_setHue(i,temp) which will change
                   // the value in the array and also use Platform.runLater()
                   // to change the color of the i-th line in the c2w_canvas.
                c2w_setHue(i,temp);
            }
            try {
                c2w_delay(1000);  // Wait one second before starting the sort.
                c2w_quickSort(0,c2w_hue.length-1);  // Sort the whole array.
            }
            catch (ThreadTerminationException e) { // User aborted c2w_quickSort.
                    // Put the colors back into sorted order.  The c2w_drawSorted()
                    // method draws all of the color bars in sorted order.
                Platform.runLater( () -> c2w_drawSorted() );
            }
            finally {
                running = false;  // make sure running is false; this is only
                                  //   really necessary if the thread terminated
                                  //   normally
                Platform.runLater( () -> c2w_startButton.setText("Start") );
            }
        }
    }

} // end c2w_QuicksortThreadDemo