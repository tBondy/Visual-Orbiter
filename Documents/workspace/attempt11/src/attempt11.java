import java.applet.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class attempt11 extends Applet implements Runnable 
{
	int widthP;
	int x;
	int y ;
	int t = 80;
	int w = 0;
	double i = 0;
	Thread bounce = null;
	boolean threadSuspended;
	private Image offScreenImage;
    private Graphics offScreenGraphics;

   public void init() 
   {
	   x = 800;
	   y = 400;
	   widthP = 250;
	   setBackground(Color.black);
   }
   public void destroy() 
   {
   }

   // Executed after the applet is created; and also whenever
   // the browser returns to the page containing the applet.
   public void start() 
   {
	   if (bounce == null) 
      {
		   bounce = new Thread(this);
		   threadSuspended = false;
		   bounce.start();
      }
      else 
      {
    	  if (threadSuspended) 
         {
    		  threadSuspended = false;
    		  synchronized(this) 
            {
    			  notify();
            }
         }
      }
   }

   // Executed whenever the browser leaves the page containing the applet.
   public void stop() 
   {
      threadSuspended = true;
   }

   public void run() 
   {
      try 
      {
         while (true) 
         {
        	w++;
        	i+=.01;
            if(i == 250)
            {
            	i = 0;
            	w = 0;
            }

            showStatus("i is " + i);

            // Now the thread checks to see if it should suspend itself
            if (threadSuspended) 
            {
               synchronized(this) 
               {
                  while (threadSuspended) 
                  {
                     wait();
                  }
               }
            }
            repaint();
            bounce.sleep(t);  
         }
      }
      catch (InterruptedException e) { }
   }

   public void paint(Graphics g) 
   {
	   if (offScreenImage == null) 
	   {
           offScreenImage = (BufferedImage) createImage(1680,1050);
       }
       offScreenGraphics = offScreenImage.getGraphics();
       //offScreenGraphics.setColor(Color.black);
       //offScreenGraphics.clearRect(0, 0, 1680, 1050);

       g.drawImage(offScreenImage, 0, 0, this);

       OrbitalPlanet front = new OrbitalPlanet(g,x, y,60, widthP, i,'b');
//       offScreenGraphics.fillOval(x+(int)((500)*Math.cos((i-1)-1))+50, y+(int)((200)*Math.sin(i-1))+50, 51, 51);
       
       drawSun(g,x,y,widthP);
       
       OrbitalPlanet back = new OrbitalPlanet(g,x, y,60, widthP, i,'f');
//       offScreenGraphics.fillOval(x+(int)((500)*Math.cos((i-1)-1))+50, y+(int)((200)*Math.sin(i-1))+50, 51, 51);
   }

   public void update(Graphics g) 
   {
       paint(g);
   }
   public static void drawSun(Graphics g,int x, int y,int width)
	{
		int R = 0;
		int G = 0;
		int B = 0;
		for(int z=1; z<127; z++)
		{
			Color colour = new Color(R, G, B);
			g.setColor(colour);
			g.fillOval(x,y,width,width);
			R +=2;
			G+=2;
			B+=2;
			width-=3;
			x++;
			y++;
		}
	}
	public static int third(int x)				{return x = x/3;}
	public static int half(int x)				{return x = x/2;}
	public static int threequarters(double x)	{int p = (int)(x*3)/4;	return p;}
	public static int quarter(int x)			{return x = x/4;}
	public static int twothirds(int x)			{return x = (2*x)/3;}
}

class OrbitalPlanet
{
	OrbitalPlanet(Graphics g,int x,int y,int width,int widthP,double i,char side)
	{
		int red = 252-97;
		int green = 190;
		int blue = 190;
		int height = width;
		int x1;
		int y1;
		
		switch(side)
		{
		case 'f':{
			for (int z = 1; z <= 20; z++)
			{
				Color c = new Color( red, green, blue);
				g.setColor(c);
				width -= 3;
				height -= 2;
				x ++;
				y ++;
				blue -=6;
				x1 = x+(int)((500)*Math.cos(i-2)+(widthP/2));
				y1 = y+(int)((200)*Math.sin(i-1))+(widthP/2);
				
				if(y1>(y+(widthP/2)))
					g.fillOval(x1, y1, width, height);
			}
				 }
		case 'b':{
			for (int z = 1; z <= 20; z++)
			{
				Color c = new Color( red, green, blue);
				g.setColor(c);
				width -= 3;
				height -= 2;
				x ++;
				y ++;
				blue -=6;
				x1 = x+(int)((500)*Math.cos(i-2)+(widthP/2));
				y1 = y+(int)((200)*Math.sin(i-1))+(widthP/2);
				
				if(y1<(y+(widthP/2)))
					g.fillOval(x1, y1, width, height);
			}
				 }
		}
	}
}