import java.awt.Graphics;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Checkbox;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class PaintBrush extends Applet{

	private int x1;
    private int y1;
	
	private int x2;
    private int y2;
	
	private ArrayList<Shape> shapesList = new ArrayList<>();
	
	final static int IDLE 	= 0;
	final static int LINE 	= 1;
	final static int RECT 	= 2;
	final static int OVAL 	= 3;
	final static int PENCIL = 4;
	final static int ERASER = 5;
	
	private int shapeType;
	
	private Color mainColor = Color.BLACK;
	private boolean isSolid = false;
	private boolean isCleared = false;
	private boolean isDragging = false;
	
	
	public void init(){
		shapeType = IDLE;
		
		Button clearBtn = new Button("Clear");
		Button undoBtn = new Button("Undo");
		
		Font buttonFont = new Font("Arial", Font.BOLD, 14);
		
		clearBtn.setBackground(Color.LIGHT_GRAY);
		clearBtn.setForeground(Color.BLACK);
		undoBtn.setBackground(Color.LIGHT_GRAY);
		undoBtn.setForeground(Color.BLACK);
		clearBtn.setFont(buttonFont);
		undoBtn.setFont(buttonFont);
		
		add(clearBtn);
		add(undoBtn);
		
		clearBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				isCleared = true;
				repaint();
			}
		});
				
		/*  ---------- Shape Buttons  ---------- */
		
		Label shapesLabel = new Label("Paint mode: ");
		
		Button lineBtn = new Button("Line");
		Button rectBtn = new Button("Rectangle");
		Button ovalBtn = new Button("Oval");
		Button penBtn = new Button("Pencil");
		Button eraserBtn = new Button("Eraser");
		
		lineBtn.setBackground(Color.LIGHT_GRAY);
		lineBtn.setForeground(Color.BLACK);
		rectBtn.setBackground(Color.LIGHT_GRAY);
		rectBtn.setForeground(Color.BLACK);
		ovalBtn.setBackground(Color.LIGHT_GRAY);
		ovalBtn.setForeground(Color.BLACK);
		
		penBtn.setBackground(Color.LIGHT_GRAY);
		penBtn.setForeground(Color.BLACK);
		eraserBtn.setForeground(Color.BLACK);
		eraserBtn.setBackground(Color.LIGHT_GRAY);
		
	
		lineBtn.setFont(buttonFont);
		rectBtn.setFont(buttonFont);
		ovalBtn.setFont(buttonFont);
		penBtn.setFont(buttonFont);
		eraserBtn.setFont(buttonFont);

		
		setBackground(Color.WHITE);
		
		
		add(shapesLabel);
		add(lineBtn);
		add(rectBtn);
		add(ovalBtn);
		add(penBtn);
		add(eraserBtn);
		
		lineBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				shapeType = LINE;
			}
		});
		
		rectBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				shapeType = RECT;
			}
		});
		
		ovalBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				shapeType = OVAL;
			}
		});
		penBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				shapeType = PENCIL;
			}
		});
		
		eraserBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				shapeType = ERASER;
			}
		});
		
		/*  ---------- Color Buttons  ---------- */
		
		Label colorLabel = new Label("Colors: ");
		
		Button redBtn = new Button("Red");
		Button greenBtn = new Button("Green");
		Button blueBtn = new Button("Blue");
		Button blackBtn = new Button("Black");
		
		redBtn.setBackground(Color.RED);
		greenBtn.setBackground(Color.GREEN);
		blueBtn.setBackground(Color.BLUE);
		blackBtn.setBackground(Color.BLACK);
		
		add(colorLabel);
		  
		add(redBtn);
		add(greenBtn);
		add(blueBtn);
		add(blackBtn);
		
		redBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainColor = Color.RED;
			}
		});
		
		greenBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainColor = Color.GREEN;
			}
		});
		
		blueBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainColor = Color.BLUE;
			}
		});	
		
		blackBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainColor = Color.BLACK;
			}
		});
		
		/*  ---------- Checkbox added and Listener ---------- */
		
		Checkbox solidBox = new Checkbox("Solid", false);
		add(solidBox);
		solidBox.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
					int state = e.getStateChange();
					if(state == ItemEvent.SELECTED){
						isSolid = true;
					}else if(state == ItemEvent.DESELECTED){
						isSolid = false;
					}
				}
		});

		/*  ---------- Mouse Listeners added ---------- */
		MyMouseListener myMouse = new MyMouseListener();
		addMouseListener(myMouse);
		addMouseMotionListener(myMouse);
    }
	
	/*  ---------- Mouse Listeners Imp ---------- */
	
	class MyMouseListener extends MouseAdapter{
	
		public void mouseDragged(MouseEvent e){
			if(shapeType != IDLE){
				isDragging = true;
				x2 = e.getX();
				y2 = e.getY();
				
				// Pencil effect
				if(shapeType == PENCIL){
					Line l = new Line(new Point(x1, y1), new Point(x2, y2), mainColor);
					shapesList.add(l);
					// connecting between lines
					x1 = x2;
					y1 = y2;	
				}
				// ERASER effect
				else if(shapeType == ERASER){
					int upperLX = Math.min(x1, x2);
                    int upperLY = Math.min(y1, y2);

					Rectangle r = new Rectangle(new Point(upperLX, upperLY), 20, 20, Color.WHITE, true);
					
					shapesList.add(r);
					// connecting between the recangles
					x1 = x2;
					y1 = y2;
				}
				repaint();
			}

		}
		
		public void mousePressed(MouseEvent e){
			if(shapeType != IDLE){
				x1 = e.getX();
				y1 = e.getY();
			}
		}
		public void mouseReleased(MouseEvent e){
			// finish dragging then add the shape
			if(shapeType != IDLE && isDragging){
				
				Point topLeft = new Point(Math.min(x1, x2), Math.min(y1, y2));
                int width = Math.abs(x2 - x1);
                int height = Math.abs(y2 - y1);

				switch (shapeType){
					 case LINE:
                        shapesList.add(new Line(new Point(x1, y1), new Point(x2, y2), mainColor));
                        break;
                    case RECT:
                        shapesList.add(new Rectangle(topLeft, width, height, mainColor, isSolid));
                        break;
                    case OVAL:
                        shapesList.add(new Oval(topLeft, width, height, mainColor, isSolid));
                        break;
				}
				isDragging = false;
				repaint();
			}
 
      
		}

	}
		/*  ---------- end of Mouse Listeners Imp ---------- */
		
		/*  ---------- start of painting ---------- */
	public void paint(Graphics g){
		
		// clear the applet
		if(isCleared){
			//g.setColor(Color.WHITE);
			//g.fillRect(0, 0, getWidth(), getHeight());
			shapesList.clear();
			shapeType = IDLE;
			isCleared = false;
		}

		/* Painting the whole shapes list everytime */
		for(Shape s: shapesList){
			s.draw(g);
		}
		
		if (shapeType != IDLE) {
			g.setColor(mainColor);
			
			switch (shapeType) {
                case LINE:
                    g.drawLine(x1, y1, x2, y2);
                    break;
                case RECT:
					// to determine the topleft corner point despite of direction of dragging
                    int upperLX = Math.min(x1, x2);
                    int upperLY = Math.min(y1, y2);
                    int rectWidth = Math.abs(x2 - x1);
                    int rectHeight = Math.abs(y2 - y1);
					if(isSolid){
						g.fillRect(upperLX, upperLY, rectWidth, rectHeight);
					}else{
						g.drawRect(upperLX, upperLY, rectWidth, rectHeight);
					}
                    break;
                case OVAL:
					// to determine the topleft corner point despite of direction of dragging
                    int ovalX = Math.min(x1, x2);
                    int ovalY = Math.min(y1, y2);
                    int ovalWidth = Math.abs(x2 - x1);
                    int ovalHeight = Math.abs(y2 - y1);
					if(isSolid){
						g.fillOval(ovalX, ovalY, ovalWidth, ovalHeight);
					}else{
						g.drawOval(ovalX, ovalY, ovalWidth, ovalHeight);
					}
					break;

            }
		}
		

		
	}
	
		
}	

/* Classes used */

abstract class Shape{
	
	private Color color;
	private boolean isSolid;	
	
	Shape(){
		isSolid = false;     
	}
	
	
	public void setShapeColor(Color color){
		this.color = color;
	}
	
	public Color getShapeColor(){
		return color;
	}
	
	public void setSolid(boolean isSolid){
		this.isSolid = isSolid;
	}
	
	public boolean getSolid(){
		return isSolid;
	}
	
	public abstract void draw(Graphics g);
}

class Point{
	private int x;
	private int y;
	
	Point(){
		x = 0;
		y = 0;
	}
	Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getX(){
		return x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getY(){
		return y;
	}
	
}

class Line extends Shape{
	
	private Point startPoint;
	private Point endPoint;
	
	Line(){
		startPoint = new Point();
		endPoint = new Point();
		setShapeColor(Color.MAGENTA);
		setSolid(false);
	}
	Line(Point p1, Point p2, Color color){
		startPoint = p1;
		endPoint = p2;
		setShapeColor(color);
	}
	
	public void setStartP(Point p){
		startPoint = p;
	}
	
	public Point getStartP(){
		return startPoint;
	}
	
	public void setEndP(Point p){
		endPoint = p;
	}
	
	public Point getEndP(){
		return endPoint;
	}
	
	public void draw(Graphics g){
		g.setColor(getShapeColor());
		g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}
}


class Rectangle extends Shape{
	
	private Point Leftp;
	private int width;
	private int height;
	
	Rectangle(){
		Leftp = new Point();
		width = 0;
		height = 0;
		setShapeColor(Color.MAGENTA);
		setSolid(false);
	}
	
	Rectangle(Point p, int width, int height, Color color, boolean isSolid){
		Leftp = p;
		this.width = width;
		this.height = height;
		setShapeColor(color);
		setSolid(isSolid);
	}
	
	public void setLeftp(Point p){
		Leftp = p;
	}
	
	public Point getLeftp(){
		return Leftp;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public int getHeight(){
		return height;
	}
	
		
	public void draw(Graphics g){
		g.setColor(getShapeColor());
		if(getSolid()){
			g.fillRect(Leftp.getX(), Leftp.getY(), width, height);
		}else{
			g.drawRect(Leftp.getX(), Leftp.getY(), width, height);
		}
	}
	
}


class Oval extends Shape{
	
	private Point Leftp;
	private int width;
	private int height;
	
	Oval(){
		Leftp = new Point();
		width = 0;
		height = 0;
		setShapeColor(Color.MAGENTA);
		setSolid(false);
	}
	
	Oval(Point p, int width, int height, Color color, boolean isSolid){
		Leftp = p;
		this.width = width;
		this.height = height;
		setShapeColor(color);
		setSolid(isSolid);
	}
	
	public void setLeftp(Point p){
		Leftp = p;
	}
	
	public Point getLeftp(){
		return Leftp;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public int getHeight(){
		return height;
	}
	
		
	public void draw(Graphics g){
		g.setColor(getShapeColor());
		if(getSolid()){
			g.fillOval(Leftp.getX(), Leftp.getY(), width, height);
		}else{
			g.drawOval(Leftp.getX(), Leftp.getY(), width, height);
		}
	}
	
}