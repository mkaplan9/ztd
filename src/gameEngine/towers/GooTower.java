package gameEngine.towers;

import gameEngine.Referee;
import gameEngine.projectile.BasicProjectile;
import gameEngine.projectile.GooProjectile;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class GooTower extends AbstractTower {

	public GooTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(10, 1000000, 2000000000, 100, vec, ref, sprite);
		
	}
	
	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.BLUE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}

	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.BLUE);
	}

	@Override
	public boolean action() {
		List<Zombie> zlist = _ref.getZombiesInR(_vec, _radius);
		if (zlist.size() != 0) {
			Zombie z = null;
			float maxSpeed = 0;
			double minDist = Double.MAX_VALUE;
			for(Zombie z1 : zlist) {
				if(z1.getSpeed() > maxSpeed) {
					maxSpeed = z1.getSpeed();
					minDist = z1.getDist();
					z = z1;
				}
				else if(z1.getSpeed() == maxSpeed && z1.getDist() < minDist) {
					System.out.println("in here");
					maxSpeed = z1.getSpeed();
					minDist = z1.getDist();
					z = z1;
				}
			}
			super.addProjectile(new GooProjectile(super._vec, z));
			System.out.println("Goo Tower Firing");
			return true;
		}
		return false;
	}

	
}