package gameEngine.towers;

import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.LaserProjectile;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cs195n.Vec2f;


public class LaserTower extends AbstractTower {
	
	
	public LaserTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(30, 1000000, 3000000000l, 900, Constants.LASER_BLURB, vec, ref, sprite);
	}
	
	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.ORANGE);
		
	}
	
	
	
	@Override
	public boolean action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		Collection<Zombie> zs = _ref.getZombies();
		if (z != null) {
			Vec2f n = z.getCoords().minus(_vec).normalized();
			List<Zombie> toDamage = new LinkedList<Zombie>();
			for(Zombie z1 : zs) {
				float d = _vec.minus(z1.getCoords()).minus(n.smult(_vec.minus(z1.getCoords()).dot(n))).mag2();
				if(d < 80000) {
					toDamage.add(z1);
					
				}
			}
			for(Zombie z1 : toDamage) {
				_ref.dealDamage(z1, _damage);
			}
			Vec2f end = _vec.plus(z.getCoords().minus(_vec).normalized().smult(10000));
			addProjectile(new LaserProjectile(_vec.x, _vec.y, end.x, end.y));
			return true;
		}
		return false;
	}

}