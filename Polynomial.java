/*
 * PROJECT II: Polynomial.java
 *
 * This file contains a template for the class Polynomial. Not all methods are
 * implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file.
 *
 * This class is designed to use Complex in order to represent polynomials
 * with complex co-efficients. It provides very basic functionality and there
 * are very few methods to implement! The project formulation contains a
 * complete description.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file! You should also test this class using the main()
 * function.
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 */

class Polynomial {
    /**
     * An array storing the complex co-efficients of the polynomial.
     */
    Complex[] coeff;

    // ========================================================
    // Constructor functions.
    // ========================================================

    /**
     * General constructor: assigns this polynomial a given set of
     * co-efficients.
     *
     * @param coeff  The co-efficients to use for this polynomial.
     */
    public Polynomial(Complex[] coeff) {  //Here adapt coeff to have no 0 leading 1s.
        int leadingzero = 1;
        int deg = coeff.length-1;
	
	    while (leadingzero == 1){
		    if (coeff[deg].abs2() == 0){        //To check if zero use abs value
			    deg--;
			    System.out.println(deg);
		    }else{
		    	leadingzero=0;
			    //System.out.println("Found lead");
		    }

    	}
        Complex[] coeff2 = new Complex[deg+1];
         for(int i=0; i<=deg;i++){          //Copy coeff into coeff2 up to the leading value.
    	    coeff2[i]=coeff[i];
        }
	    for (int k=0; k<=deg;k++){
		    //System.out.print(coeff2[k]); //Tester
	    }
	    //System.out.println();
        this.coeff = coeff2;               //Assign at end
    }
    
    /**
     * Default constructor: sets the Polynomial to the zero polynomial.
     */
    public Polynomial() {
        Complex zero = new Complex(0); //Make Zero as complex data type
        Complex[] coeff0 = {zero};     // Make matrix with this init
        this.coeff = coeff0; // Sets coeff to desired array
    }

    // ========================================================
    // Operations and functions with polynomials.
    // ========================================================

    /**
     * Create a string representation of the polynomial.
     *
     * For example: (1.0+1.0i)+(1.0+2.0i)X+(1.0+3.0i)X^2
     */
    public String toString() {
        String output;
        output = "" + coeff[0];             //Prints a_0 which always exists on new line "" tels java to make coeff[0] a string
        for (int i=1; i < coeff.length ; i++){  //again assumes coeff has no leadin 1 zeros!!!!!
        output = output + "+ (" + coeff[i] + ")" + "Z^" + i;      // prints on same line +a_i Z^i
        }
        return output;
    }

    /**
     * Returns the degree of this polynomial.
     */
    public int degree() {
        return coeff.length - 1;  //No as means coeff can have leading 1s. Edit?
    }

    /**
     * Evaluates the polynomial at a given point z.
     *
     * @param z  The point at which to evaluate the polynomial
     * @return   The complex number P(z).
     */


    public Complex evaluate(Complex z) {
        // You need to fill in this function. (Use recursion in manner in pdf)
        int degree = coeff.length -1;       //Select degree.
        Complex eval = coeff[degree];       //Set inital value to final coefficant
        for (int i=degree; i>0; i--){       //Iterate to get form as in project2 notes
        eval = (coeff[i-1].add(z.multiply(eval))); 
        //System.out.println(eval); Test loop doesn;t play up
        }
        return eval;                        //Return Final value
        }
    
    /**
     * Calculate and returns the derivative of this polynomial.
     *
     * @return The derivative of this polynomial.
     */
    public Polynomial derivative() {
        //Create new array with derivative constants and pass through evaluate.
        Complex[] Dcoeff = new Complex[coeff.length-1]; //Create array to store derivative coeffs
      
        for (int i=0; i<coeff.length-1; i++){
        Dcoeff[i] = coeff[i+1].multiply((i+1));                   //Populate this array.
            }
        return new Polynomial(Dcoeff);              //Return the array as a polynomial
	}
   // }
    
    // ========================================================
    // Tester function.
    // ========================================================

    public static void main(String[] args) {
    
    Complex c1 = new Complex(-1.0,0.0);
    Complex c2 = new Complex();
    Complex c3 = new Complex();
    Complex c4 = new Complex(1.0,0);
    Complex c = new Complex(1,0);
    //Complex c6 = new Complex(1,1);

    Complex[] coeff = {c1,c2,c3,c4};
    Polynomial P = new Polynomial(coeff);

    System.out.println("Polynomial is " + P.toString());
    System.out.println("Degree is " + P.degree());
    System.out.println("P(Z) =" + P.evaluate(c));
    System.out.println("Derivative is " + P.derivative());
    System.out.println("P'(Z) is " + (P.derivative()).evaluate(c));
    }


}
