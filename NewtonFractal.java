/*
 * PROJECT II: NewtonFractal.java
 *
 * This file contains a template for the class NewtonFractal. Not all methods
 * are implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file.
 *
 * There are a lot of functions in this class, as it deals with creating an
 * image using purely Java. I have already completed a lot of the technical
 * aspects for you, so there should not be a huge amount for you to do in this
 * class! 
 *
 * At the bottom of this class there is a section of functions which I have
 * already written and deal with the more complicated tasks. You should make
 * sure that you read through the function descriptions, but DO NOT ALTER
 * THEM! Also, remember to call the setupFractal() function from your
 * constructor!
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file! You should also test this class using the main()
 * function.
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 */

// These next lines import the relevant classes needed to output an image and
// *SHOULD NOT* be changed. You don't need to worry about their definitions
// for the most part!
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

import java.util.Arrays;
class NewtonFractal {
    /**
     * A reference to the Newton-Raphson iterator object.
     */
    private Newton iterator;
    
    /**
     * The top-left corner of the square in the complex plane to examine.
     */
    private Complex origin;
    
    /**
     * The width of the square in the complex plane to examine.
     */
    private double width;
    
    /**
     * A list of roots of the polynomial.
     */
    private ArrayList<Complex> roots;
    
    /**
     * A two dimensional array holding the colours of the plot.
     */
    private Color[][] colors;

    /**
     * A flag indicating the type of plot to generate. If true, we choose
     * darker colors if a particular root takes longer to converge.
     */
    private boolean colorIterations;

    /**
     * A standard Java object which allows us to store a simple image in
     * memory. This will be set up by setupFractal -- you do not need to worry
     * about it!
     */
    private BufferedImage fractal;
    
    /**
     * This object is another standard Java object which allows us to perform
     * basic graphical operations (drawing lines, rectangles, pixels, etc) on
     * the BufferedImage. This will be set up by setupFractal -- you do not
     * need to worry about it!
     */
    private Graphics2D g2;

    /**
     * Defines the width (in pixels) of the BufferedImage and hence the
     * resulting image.
     */
    public static final int NUMPIXELS = 400;

    //public int[][] iter;
    
    // ========================================================
    // Constructor function.
    // ========================================================
    
    /**
     * Constructor function which initialises the instance variables
     * above. IMPORTANT: Remember to call setupFractal at the end of this
     * function!!
     *
     * @param p       The polynomial to generate the fractal of.
     * @param origin  The top-left corner of the square to image.
     * @param width   The width of the square to image.
     */
    public NewtonFractal(Polynomial p, Complex origin, double width) {
        // You need to fill in this function.
        iterator = new Newton(p);           //Initialise
        this.origin = origin;       
        this.width = width;
        roots = new ArrayList<Complex>();

        //Runs iterator untill first root is found and uses this to initialise roots.
        setupRoots();
        setupFractal(); 
        
        //iter = new int[NUMPIXELS][NUMPIXELS];
    }
    
    // ========================================================
    // Basic operations.
    // ========================================================

    
    public void setupRoots(){
        for (int k = 0; k < NUMPIXELS; k++){
            for (int l = 0; l < NUMPIXELS; l++){        //Run through pixels
                //What to feed in
                //System.out.println(" Root check x: "+ k + "y: " + l);
                //printRoots();
                iterator.iterate(pixelToComplex(k,l)); // Run complex number through Newton Raphson
                if (iterator.getError() == 0){          //If converges add to roots.
                    roots.add(iterator.getRoot());
                    //System.out.println("Root added " + iterator.getRoot()); Test it is added
                    k = NUMPIXELS;                             //Stops looping.
                    l = NUMPIXELS;
                }  
            }
        }

    }

    /**
     * Print out all of the roots found so far, which are contained in the
     * roots ArrayList.
     */
    public void printRoots() {
        for (int i = 0; i<roots.size() ; i++){
            System.out.println(roots.get(i));
        }
       
    }
    
    //Test =======================================
    /*
    public void printIter(){
        for (int i = 0; i<NUMPIXELS; i++){
            for (int k = 0; k<NUMPIXELS; k++){
                System.out.print("," + iter[i][k]);
            }
            System.out.println("");
        }
    }
*/
    /**
     * Check to see if root is in the roots ArrayList (up to tolerance). If
     * the root is not found, then return -1. Otherwise return the index
     * inside this.roots where you found it.
     *
     * @param root  Root to find in this.roots.
     */
    public int findRoot(Complex root) {
        int a = -1;
        //Use k as i used later.
        for (int k = 0; k<roots.size(); k++){
            //Complex r = roots.get(k);       //Check roots[k] will be defined.
            if ((root.add((roots.get(k)).minus())).abs() <  Newton.TOL){ //Condition for equality.
                a = k;                  //Set a = k if met and break the loop
                k = roots.size();
            }
        }
        return a; //If never meets equals criteria, is added.
        }
     
    /**
     * Convert from pixel indices (i,j) to the complex number (origin.real +
     * i*dz, origin.imag - j*dz).
     *
     * @param i  x-axis co-ordinate of the pixel located at (i,j)
     * @param j  y-axis co-ordinate of the pixel located at (i,j)
     */
    public Complex pixelToComplex(int i, int j) {
        return new Complex(origin.getReal() + i*(width/NUMPIXELS), origin.getImag() - j*(width/NUMPIXELS));
    }
    // As sytart at k, l = 0 let dz = width/NUMPIXELS
    // ========================================================
    // Fractal generating function.
    // ========================================================

    /**
     * Generate the fractal image. See the colorIterations instance variable
     * for a better description of its purpose.
     */
    public void createFractal(boolean colorIterations) {
        this.colorIterations = colorIterations;
        for (int k = 0; k < NUMPIXELS; k++){
            for (int l = 0; l < NUMPIXELS; l++){
                //System.out.println("x: "+ k + "y: " + l); //Allows me to check on which pixels the program may break
                //printRoots();                             //Allows me to see if any extra roots added in a turn
                iterator.iterate(pixelToComplex(k,l));       // Run N-R.
                if (iterator.getError() == 0){              //If Netwon Raphson Converges
                    Complex z = iterator.getRoot();         //Put the root into a variable
                     //System.out.println("Counted");       //Check if counted
                    if (findRoot(z) == -1){                 //If can't find add it no plot should not impact picture
                        roots.add(z);
                        //System.out.println("x: "+ k + "y: " + l);
                        //System.out.println("Complex Num: " + pixelToComplex(k,l).toString());
                        //System.out.println("Root: " + z);
                        //System.out.println("Added above");    
                    }
                        //iter[k][l] = iterator.getNumIterations();
                        colorPixel(k, l, findRoot(z), iterator.getNumIterations()+1); //Then run color pixel.
                        //System.out.println("Plotted");  // colorPixel(int i, int j, int rootColor, int numIter) 
                    
                }
                //printRoots();
            }              

        }
    }

    // ========================================================
    // Tester function.
    // ========================================================
    
    public static void main(String[] args) {
        
        // Here is some example code which generates the two images seen in
        // figure 1 of the formulation.

       
        Complex[] coeff = new Complex[] { new Complex(-379), new Complex(), new Complex(-37), new Complex(5.0,7.0),new Complex(),
             new Complex(0.0,8.0) };    
            
        Polynomial p    = new Polynomial(coeff);
        NewtonFractal f = new NewtonFractal(p, new Complex(-4.0,4.0), 8.0);
    

    /*
            Complex[] coeff = new Complex[] { new Complex(-1.0), new Complex(), new Complex(),
                new Complex(1) };    
               
           Polynomial p    = new Polynomial(coeff);
           NewtonFractal f = new NewtonFractal(p, new Complex(-1.0,1.0), 2.0);

    */
      
        f.createFractal(false);
        f.saveFractal("fractal-light.png");
        f.createFractal(true);
        f.saveFractal("fractal-dark.png");
     

        System.out.println("Polynomial: " + p.toString());
        System.out.println("Roots:");
        f.printRoots();
        //f.printIter();
    }
    
    // ====================================================================
    // OTHER FUNCTIONS
    //
    // The rest of the functions in this class are COMPLETE (with the
    // exception of the main function) since they involve quite complex Java
    // code to deal with the graphics. This means they *do not* and *should
    // not* need to be altered! But you should read their descriptions so you
    // know how to use them.
    // ====================================================================
    
    /**
     * Sets up all the fractal image. Make sure that your constructor calls
     * this function!
     */
    private void setupFractal()
    {
        // This function is complete!
        int i, j;

        if (iterator.getF().degree() < 3 || iterator.getF().degree() > 5)
            throw new RuntimeException("Degree of polynomial must be between 3 and 5 inclusive!");

        this.colors       = new Color[5][Newton.MAXITER];
        this.colors[0][0] = Color.RED;
        this.colors[1][0] = Color.GREEN;
        this.colors[2][0] = Color.BLUE;
        this.colors[3][0] = Color.CYAN;
        this.colors[4][0] = Color.MAGENTA;
        
        for (i = 0; i < 5; i++) {
            float[] components = colors[i][0].getRGBComponents(null);
            float[] delta      = new float[3];
            
            for (j = 0; j < 3; j++)
                delta[j] = 0.8f*components[j]/Newton.MAXITER;
            
            for (j = 1; j < Newton.MAXITER; j++) {
                float[] tmp  = colors[i][j-1].getRGBComponents(null);
                colors[i][j] = new Color(tmp[0]-delta[0], tmp[1]-delta[1], 
                                         tmp[2]-delta[2]);
            }
        }
        
        fractal = new BufferedImage(NUMPIXELS, NUMPIXELS, BufferedImage.TYPE_INT_RGB);
        g2      = fractal.createGraphics();
    }
    
    /**
     * Colors a pixel in the image.
     *
     * @param i          x-axis co-ordinate of the pixel located at (i,j)
     * @param j          y-axis co-ordinate of the pixel located at (i,j)
     * @param rootColor  An integer between 0 and 4 inclusive indicating the
     *                   root number.
     * @param numIter    Number of iterations at this root.
     */
    private void colorPixel(int i, int j, int rootColor, int numIter) 
    {
        // This function is complete!
        if (colorIterations)
            g2.setColor(colors[rootColor][numIter-1]);
        else
            g2.setColor(colors[rootColor][0]);
        g2.fillRect(i,j,1,1);
    }

    /**
     * Saves the fractal image to a file.
     *
     * @param fileName  The filename to save the image as. Should end in .png.
     */
    public void saveFractal(String fileName) {
        // This function is complete!
        try {
            File outputfile = new File(fileName);
            ImageIO.write(fractal, "png", outputfile);
        } catch (IOException e) {
            System.out.println("I got an error trying to save! Maybe you're out of space?");
        }
    }
}