package gameEngine;

import gameEngine.towers.BasicTower;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;
import cs195n.Vec2i;
import mapbuilder.MapNode;

public class Base extends BasicTower {

	private int _health;
	private MapNode _baseNode;
	
	public Base(MapNode n, Vec2f vec, Referee ref, BufferedImage sprite) {
		super(vec, ref, sprite);
		_baseNode = n;
		_health = 100;
	}
	
	public boolean dealDamage(int damage) {
		_health-=damage;
		if(_health <= 0) {
			System.out.println("Base Destroyed!!");
			return true;
		}
		return false;
	}
	
	public MapNode getNode() {
		return _baseNode;
	}
	
	public int getHealth() {
		return _health;
	}
	
	public void setHealth(int health) {
		_health = health;
	}

	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawSimple(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
