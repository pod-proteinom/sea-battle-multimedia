package com.multimedia.seabattle.service.ships;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;

@Service("shipBuilder")
public class ShipBuilder implements IShipBuilder{

	private static final Logger logger = LoggerFactory.getLogger(ShipBuilder.class);

	private IBattlefieldService battlefield_service;

	@Override
	public ShipCreationResult createShip(ShipType type, Coordinates coords, Game game,
			Boolean player1)
	{
		Coordinates[] offset = type.getOffset();
		Coordinates[] ship_position = new Coordinates[offset.length];
		for (int i=0;i<offset.length;i++){
			Coordinates c = new Coordinates();
			c.setX(coords.getX() + offset[i].getX());
			c.setY(coords.getY() + offset[i].getY());
			ship_position[i] = c;
		}
		Ship ship = new Ship();
		ship.setLength(type.getValue());
		ship.setPlayer1(player1);
		ship.setGame(game);
		ShipCreationResult result = battlefield_service.deployShip(ship_position, game, player1, ship);
		if (logger.isDebugEnabled()){
			logger.debug("creating ship ["+coords+"] ["+type+"] in game ["+game.getId()+"] for player ["+player1+"] res:"+result);
		}
		return result;
	}

	@Resource(name="battlefieldService")
	public void setBattlefieldService(IBattlefieldService value){
		this.battlefield_service = value;
	}
}