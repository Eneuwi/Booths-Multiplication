import java.util.Scanner;

public class Main {
    public static boolean signMc;
    public static boolean signMultip;
    public static boolean signProd;
    public static boolean signProduct;
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the multiplicand in decimal");
        int multiplicand = scan.nextInt();
        if((signMultip && !signMc) || (!signMultip && signMc))
        {
            signProduct = false;
        }
        else
        {
            signProduct = true;
        }

        if(multiplicand < 0)
        {
            signMc = false;
            System.out.println("mc negative");
        }
        else
        {
            signMc = true;
            System.out.println("mc positive");
        }
        System.out.println("Enter the multiplier");
        int multiplier = scan.nextInt();
        if(multiplier < 0)
        {
            signMultip = false;
            System.out.println("multiplier negative");
        }
        else
        {
            signMultip = true;
            System.out.println("multiplier positive");
        }
        Main.multiply(multiplicand, multiplier);
    }

    static String toBinary(int x)
    {
        int isNeg = x;
        x =  java.lang.Math.abs(x);
        String binary = "";
        String R = "0"; //used for remainder = 0
        String r = "1"; //used for remainder = 1
        while(x != 0) //stop when x == 0
        {
            if(x%2 == 1)
            {
                x = x/2;
                binary += r; // if x mod 2 = 1 then divide by 2 and add 1 to binary
            }
            else if(x%2 == 0)
            {
                x = x/2;
                binary += R; // if x mod s = 0 divide by 2 and add 0 to binary
            }
        }
        while(binary.length() < 8)
        {
            binary += "0";
        }

        String binaryOut = "";

        for (int i = binary.length() - 1; i >= 0; i--) { // reverse the string so that we get correct binary form
            binaryOut += binary.charAt(i);
        }

        if(isNeg < 0)
        {
            return negate(binaryOut);
        }
        return binaryOut; //return positive binary number
    }

    static String binaryAdd(String x, String y)
    {
        int newX = goToInt(x, signMc);
        int newY = goToInt(y, signMultip);
        return toBinary(newX + newY);
    }

    static String unsignedBinaryAdd(String x, String y)
    {
        int newX = Integer.parseInt(x, 2);
        int newY = Integer.parseInt(y, 2);
        return toBinary(newX + newY);
    }

    static int goToInt(String binary, boolean theSign) {
        if(!theSign)
        {
            int number = -1*(Integer.parseInt(negate(binary), 2));
            System.out.println(number);
            return number;
        }
        else if(theSign)
        {
            System.out.println(Integer.parseInt(binary, 2));
            return Integer.parseInt(binary, 2);
        }
        return Integer.parseInt(binary, 2);
    }

    static String negate(String binaryNum)
    {
        String bin = "";
        for(int i = 0; i < binaryNum.length(); i++)
        {
            if(binaryNum.substring(i,i+1).equals("0"))
            {
                bin += "1";
            }
            else
            {
                bin += "0";
            }
        }

        String binOut = "";

        for (int i = binaryNum.length() - 1; i >= 0; i--) { // reverse the string so that we get correct binary form
            binOut += binaryNum .charAt(i);
        }

        binOut = unsignedBinaryAdd(bin, "1");
        return binOut;
    }

    static String asr(String shift)
    {
        String keep = "";
        keep += shift.substring(0,1);//makes sure the first character is carried over for arithmetic shift
        String newShift = shift.substring(0,16);//shifts each value over 1 for the new string
        keep += newShift;
        return keep;
    }
    static String multiply(int x, int y)
    {
        // convert the decilmal inputs to binary
        String mc = toBinary(x); // multiplicand
        String multip = toBinary(y); // multiplier
        System.out.println(mc);
        System.out.println(multip);
        String prod = "00000000";// first 8 bits of product(product prime)
        signProd = true;
        String b2 = multip; //second 8 bits of product, initialize as multiplier

        String product = prod + b2; // index 0 - 15, 0-7 = first half, 8-15 = second half
        product += "0";//Add 0 to product
        System.out.println("iteration: 0 | Step: Initialization | Multiplicand: " + mc + " | product: " + product);

        for(int i = 1; i < 8; i++)
        {
            if(goToInt(prod, signProd) < 0)
            {
                signProd = false;
                System.out.println("sign prod Changed to negative");
            }
            else if(goToInt(prod, signProd) > 0)
            {
                signProd = true;
                System.out.println("sign prod Changed to positive");
            }

            //check for sign of multiplicand and multiplier
            String last = product.substring(15,17); //last two bits on the product
            if(last.equals("00"))
            {
                System.out.println("iteration: " + i + " | Step: No operation  | Multiplicand: " + mc + " | product: " + product);
                product = asr(product);//ASR>>1
                System.out.println("iteration: " + i + " | Step: ASR  | Multiplicand: " + mc + " | product: " + product);
            }
            else if(last.equals("01"))
            {   prod = toBinary((goToInt(product.substring(0,7), signProd)  + goToInt((mc), signMc)));//product prime = product prime + Multiplicand
                product = product.replace(product.substring(0,7), prod);
                System.out.println("iteration: " + i + " | Step: product prime = product prime + Multiplicand  | Multiplicand: " + mc + " | product: " + product);
                product = asr(product);
                System.out.println("iteration: " + i + " | Step: ASR  | Multiplicand: " + mc + " | product: " + product);
            }
            else if(last.equals("10"))
            {
                prod = toBinary((goToInt(product.substring(0,7), signProd)  - goToInt((mc), signMc)));//product prime = product prime - Multiplicand
                product = product.replace(product.substring(0,7), prod);
                System.out.println("iteration: " + i + " | Step: product prime = product prime - Multiplicand  | Multiplicand: " + mc + " | product: " + product);
                product = asr(product);
                System.out.println("iteration: " + i + " | Step: ASR  | Multiplicand: " + mc + " | product: " + product);
            }
            else if(last.equals("11"))//no operation
            {
                System.out.println("iteration: " + i + " | Step: No operation  | Multiplicand: " + mc + " | product: " + product);
                product = asr(product);//ASR>>1
                System.out.println("iteration: " + i + " | Step: ASR  | Multiplicand: " + mc + " | product: " + product);
            }
        }
        product = product.substring(0,16);
        System.out.println(product);
        System.out.println(goToInt(product, false));
        return product;
    }
}

