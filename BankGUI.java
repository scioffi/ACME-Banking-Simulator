/**
 *  BankGUI.java
 *
 *  @author Stephen Cioffi scc3459
 *  @author Michael Incardona mji8299
 */

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * A graphical interface for a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class BankGUI extends JFrame implements Observer {
    
    private Bank bank;
    
    public BankGUI(Bank bank) {
        
    }

    @Override
    public void update(Observable o, Object arg) {
        
    }
    
    
}
