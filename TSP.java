// Author: Syed Baryalay
// Student Number: 19719431

/* shortest path
38, 29, 2, 31, 40, 34, 1, 25, 33, 20, 21, 22, 14, 
7, 3, 23, 39, 27, 6, 24, 17, 18, 37, 15, 4, 35, 26,
30, 28, 32, 19, 8, 11, 5, 9, 13, 36, 10, 16, 12

angry mins = 214.77 
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;


public class TSP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Window window = new Window("TSP", 1000, 525);
                window.getComputeBtn().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {    
                        window.draw(new PathFinder(window.pullInputData()).getPath());
                    }
                });
            }
        });

    }
}