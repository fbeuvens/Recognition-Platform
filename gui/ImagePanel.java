package gui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;


public class ImagePanel extends JPanel implements MouseListener,  ImageObserver{
	
	/**************************
	**       VARIABLES       **
	**************************/
	    
    
    
    // Variables relatives a la souris
    private int currentX;   // valeur courante pour la coordonnee X de la zouris
	private int currentY;   // valeur courante pour la coordonnee Y de la souris
	
	
	
	private BufferedImage bi;
	private Image img;
        
	//* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *   
       
    
    /*
    * pre  -
    * post une instance de la classe ImagePanel est cree, 
    * 		et les variables en sont initialis�es  
    */
    
    ImagePanel() {
       
    	// ajout de listeners sur la souris
        addMouseListener(this);
        addMouseMotionListener(new MouseMotionAdapter()
                      
        {
                public void mouseDragged(MouseEvent me){
                    currentX = me.getX();
                    currentY = me.getY();
                   // System.out.println("IMAGE_MANIP - Dragged at x="+currentX+" y="+currentY);
                    repaint();
                }
                
                public void mouseMoved(MouseEvent me){
                    currentX = me.getX();
                    currentY = me.getY();
                   // System.out.println("IMAGE_MANIP - Moved at x="+currentX+" y="+currentY);
                    repaint();
                }
            }
        ); 
        
    }
   
	//* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *   
       

  
    // Construit l'objet BufferedImage d'un fichier JPG, en utilisant l'objet Image
    public void load(String fullPath){
	    
	    try {
	        img = getToolkit().createImage(fullPath);
	
	        MediaTracker tracker = new MediaTracker(this);
	        tracker.addImage(img, 0);
	        tracker.waitForID(0); 
	        
	        int iw = img.getWidth(this);
	        int ih = img.getHeight(this);
	        getToolkit().prepareImage(img, iw, ih, this);
	        
	        //System.out.println("Loaded image = "+fullPath+"   width = "+iw + "   height = "+ih);
	        
	        bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
	        
	        Graphics gOffscreen = bi.createGraphics();
	        gOffscreen.drawImage(img,0,0,this);
	                     
	    }
	    catch ( Exception e ) {
	        System.out.print(e.getMessage());
	    }
	
	    repaint();
   }
    
    
    
	//* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *   
       
    /*
     * pre:  -
     * post: peint l'objet Graphics
     */    
    public void paintComponent(Graphics g){
        
        g.setColor(Color.white);
        g.fillRect(0,0,getSize().width,getSize().height);
        	
        if(bi != null){
        	
        	g.drawImage(bi,0,0,getSize().width,getSize().height,this);
        
        	
        }
                      
   }
    
	//* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *   
    public void setSize(Dimension d){
    	super.setSize(new Dimension((int)d.getWidth(),(int)(d.getWidth()*(45720.0/60960.0))));
    	//super.setSize(new Dimension((int)(d.getHeight()/(45720.0/60960.0)),(int)(d.getHeight())));
    }
    
    public void setMinimumSize(Dimension d){
    	super.setMinimumSize(new Dimension((int)d.getWidth(),(int)(d.getWidth()*(45720.0/60960.0))));
    }
	
    
	/**
	 * @pre  -
	 * @post renvoie la taille pour l'image du panel pr�f�r�e
	 */
    public Dimension getPreferredSize() { return getMinimumSize(); }
	
	   
	

	//* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *   
	
	
    
	/*
	 * pre  
	 * post 
	 */ 
    public void mouseEntered(MouseEvent me){
   
    }

	/*
	 * pre  
	 * post 
	 */
    public void mouseExited(MouseEvent me){
   
    }

	/*
	 * pre  
	 * post 
	 */
    public void mouseClicked(MouseEvent me){
    
    }

	//* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *   

	/*
	 * pre  
	 * post 	
	 */
    public void mouseReleased(MouseEvent me){
        
    }

	//* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *   

	/*
	 * pre  
	 * post 	
	 */ 
    public void mousePressed(MouseEvent me){
        
    }

}

