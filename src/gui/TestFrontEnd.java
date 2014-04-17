package gui;

import gameEngine.AbstractTower;
import gameEngine.Referee;
import gameEngine.Zombie;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collection;
import java.util.List;

import mapbuilder.Map;
import mapbuilder.MapNode;
import mapbuilder.MapWay;
import cs195n.SwingFrontEnd;
import cs195n.Vec2i;

public class TestFrontEnd extends SwingFrontEnd {

	private List<MapNode> nodes;
	private List<MapWay> ways;
	private List<MapWay> highs;
	private List<MapNode> srcs;
	
	private MainMenu _mm;
	private Console2 _console;
	private boolean _hasMain;
	private boolean _hasConsole;
	private List<AbstractTower> _towers;
	
	private MapNode base;
	
	private Vec2i _size;
	private double[] wMin = {0,0};
	private double[] wMax = {0,0};
	private Map _m;
	private Referee _ref;
	
	public TestFrontEnd(String title, boolean fullscreen) {
		super(title, fullscreen);
		super.startup();
	}
	
	public TestFrontEnd(String title, boolean fullscreen, Vec2i size) {
		super(title, fullscreen, size);
		super.setDebugMode(true);
		
		_ref = new Referee(_m);
		try {
			_m = new Map("69 Brown Street, Providence, RI", _ref);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_ref.setMap(_m);
		
		wMax = _m.getwMax();
		wMin = _m.getwMin();
		
		srcs = _m.getSources();
		///*
		_ref.startRound();
		//*/
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub
		_ref.tick(nanosSincePreviousTick);
	}

	@Override
	protected void onDraw(Graphics2D g) {
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//AffineTransform af = new AffineTransform(_size.x*(wMax[0]-wMin[0]), 0, 0, -1*_size.y*(wMax[1]-wMin[1]), -1*_size.x*wMin[0]/(wMax[0]-wMin[0]), _size.y*wMax[1]/(wMax[1]-wMin[1]));
		//g.setTransform(af);
		//_m.draw(g);
		
		for(MapWay w : _m.getWays()) {
			List<MapNode> nList = w.getNodes();
			for(int i=1; i<nList.size(); i++) {
				g.drawLine(lonToX(nList.get(i-1).lon), latToY(nList.get(i-1).lat), lonToX(nList.get(i).lon), latToY(nList.get(i).lat));
				//g.drawLine((int)nList.get(i-1).lon,(int) nList.get(i-1).lat,(int) nList.get(i).lon, (int)nList.get(i).lat);
			}
		}
		
		g.setColor(java.awt.Color.GREEN);
		for(MapWay h : _m.getHighways()) {
			List<MapNode> nList = h.getNodes();
			for(int i=1; i<nList.size(); i++) {
				g.drawLine(lonToX(nList.get(i-1).lon), latToY(nList.get(i-1).lat), lonToX(nList.get(i).lon), latToY(nList.get(i).lat));
				//g.drawLine((int)nList.get(i-1).lon,(int) nList.get(i-1).lat,(int) nList.get(i).lon, (int)nList.get(i).lat);
			}
		}
		
		g.setColor(java.awt.Color.BLUE);
		//g.setStroke(new BasicStroke(3));
		for(MapNode n : _m.getSourceList()) {
			MapNode cur = n;
			MapNode next = cur.getNext();
			while(next!=null) {
				g.drawLine(lonToX(cur.lon), latToY(cur.lat), lonToX(next.lon), latToY(next.lat));
				cur = next;
				next = next.getNext();
			}
			//System.out.println("new path\n");
		}
		
		//g.drawImage(_baseSprite, lonToX(_base.lon), latToY(_base.lat), _baseSprite.getWidth()/2, _baseSprite.getHeight()/2, null);
		g.setColor(java.awt.Color.BLUE);
		g.drawOval(lonToX(_m.getBase().lon)-2, latToY(_m.getBase().lat)-2, 3, 3);
		
		//g.setColor(java.awt.Color.ORANGE);
		for(MapNode n : _m.getSourceList()) {
			g.fillOval(lonToX(n.lon)-2, latToY(n.lat)-2, 5, 5);
		}
		
		g.setColor(java.awt.Color.RED);
		for(Zombie z : _ref.getZombies()) {
			g.drawOval(lonToX(z.getCoords().x), latToY(z.getCoords().y), 3, 3);
		}
		
		for(AbstractTower t : _ref.towers()) {
			t.draw(g);
		}
		
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {

	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
/*
		String s = Character.toString(e.getKeyChar());
		System.out.println("Typed char: " + e.getKeyChar());
		System.out.println("Typed String: " + s);
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			s = "backspace";
			System.out.println("back");
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			s = "enter";
		}
		else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			s = ""; //Don't print shift
		}
		//_mm.keyTyped(s);
		if (s.equals("p")) {
			_console.setResources(87);
*/
		if(e.getKeyCode()==39) {
			wMax[0]+=0.0005;
			wMin[0]+=0.0005;
		}
		if(e.getKeyCode()==37) {
			wMax[0]-=0.0005;
			wMin[0]-=0.0005;
		}
		if(e.getKeyCode()==38) {
			wMax[1]+=0.0005;
			wMin[1]+=0.0005;
		}
		if(e.getKeyCode()==40) {
			wMax[1]-=0.0005;
			wMin[1]-=0.0005;
		}
		if(e.getKeyCode()==81) {
			wMax[0]-=0.0005;
			wMin[0]+=0.0005;
			wMax[1]-=0.0005;
			wMin[1]+=0.0005;			
		}
		if(e.getKeyCode()==65) {
			wMax[0]+=0.0005;
			wMin[0]-=0.0005;
			wMax[1]+=0.0005;
			wMin[1]-=0.0005;			
		}
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		_console.contains(e.getX(), e.getY());

	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResize(Vec2i newSize) {
		// TODO Auto-generated method stub
		_size = newSize;
		_m.setSize(newSize);
	}
	
	public static void main(String[] args) {
		new TestFrontEnd("ZTD", false, new Vec2i(600, 500));
	}
	
	
	private int latToY(double lat) {
		return (int) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * _size.y);
	}
	
	private int lonToX(double lon) {
		return (int) ((lon - wMin[0])/(wMax[0]-wMin[0]) * _size.x);
	}
	
	public Collection<Zombie> getZombie() {
		return _ref.getZombies();
	}
	

}
